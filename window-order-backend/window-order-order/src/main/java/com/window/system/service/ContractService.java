package com.window.system.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.window.system.mapper.OrderContractMapper;
import com.window.system.mapper.WindowOrderMapper;
import com.window.system.model.entity.OrderContract;
import com.window.system.model.entity.WindowOrder;
import com.window.system.model.req.ContractSignCallbackReq;
import com.window.system.model.req.OrderContractCreateReq;
import com.window.system.common.Result;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 合同服务类
 */
@Slf4j
@Service
public class ContractService {

    @Autowired
    private OrderContractMapper contractMapper;

    @Autowired
    private WindowOrderMapper windowOrderMapper;

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name:window-order}")
    private String bucketName;
    
    @Value("${minio.endpoint:http://localhost:9000}")
    private String minioEndpoint;

    /**
     * 生成订单合同
     */
    public Result<OrderContract> generateContract(OrderContractCreateReq req, Long userId) {
        WindowOrder order = windowOrderMapper.getById(req.getOrderId());
        if (order == null) {
            return Result.error("订单不存在");
        }

        try {
            // 1. 生成PDF合同
            byte[] pdfBytes = createPdfContract(order);

            // 2. 上传到MinIO
            String fileName = "contracts/" + order.getOrderNo() + "_contract_" + System.currentTimeMillis() + ".pdf";
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(new ByteArrayInputStream(pdfBytes), pdfBytes.length, -1)
                            .contentType("application/pdf")
                            .build()
            );
            
            String pdfUrl = minioEndpoint + "/" + bucketName + "/" + fileName;

            // 3. 模拟电子签章平台的签约初始化（法大大/上机签等）
            String contractNo = "C" + System.currentTimeMillis();
            String mockSignUrl = "http://localhost:8000/mock-sign?contractNo=" + contractNo;
            String mockThirdPartyId = UUID.randomUUID().toString();

            // 4. 保存合同记录
            OrderContract contract = new OrderContract();
            contract.setContractNo(contractNo);
            contract.setOrderId(order.getId());
            contract.setCustomerId(order.getCustomerId());
            contract.setPdfUrl(pdfUrl);
            contract.setSignStatus("PENDING");
            contract.setSignUrl(mockSignUrl);
            contract.setThirdPartyId(mockThirdPartyId);
            contract.setRemark(req.getRemark());
            contract.setCreateBy(userId);

            contractMapper.insert(contract);

            return Result.success(contract);
        } catch (Exception e) {
            log.error("生成合同失败", e);
            return Result.error("生成合同失败: " + e.getMessage());
        }
    }

    /**
     * 简单模拟生成PDF合同内容
     */
    private byte[] createPdfContract(WindowOrder order) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        // 尝试加载中文字体，如果系统中没有会抛异常，这里为了演示使用内置或者简单支持
        BaseFont bfChinese;
        try {
            bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UTF16-H", BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            // 兜底英文字体
            bfChinese = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        }
        Font titleFont = new Font(bfChinese, 18, Font.BOLD);
        Font contentFont = new Font(bfChinese, 12, Font.NORMAL);

        document.add(new Paragraph("门窗定制销售合同", titleFont));
        document.add(new Paragraph(" ", contentFont));
        document.add(new Paragraph("订单编号: " + order.getOrderNo(), contentFont));
        document.add(new Paragraph("客户姓名: " + (order.getCustomerName() != null ? order.getCustomerName() : ""), contentFont));
        document.add(new Paragraph("联系电话: " + (order.getCustomerPhone() != null ? order.getCustomerPhone() : ""), contentFont));
        document.add(new Paragraph("安装地址: " + (order.getAddress() != null ? order.getAddress() : ""), contentFont));
        document.add(new Paragraph("合同总金额: ￥" + (order.getPrice() != null ? order.getPrice() : "0.00"), contentFont));
        document.add(new Paragraph(" ", contentFont));
        document.add(new Paragraph("产品信息:", titleFont));
        document.add(new Paragraph("品牌: " + (order.getBrand() != null ? order.getBrand() : ""), contentFont));
        document.add(new Paragraph("窗型: " + (order.getWindowType() != null ? order.getWindowType() : ""), contentFont));
        document.add(new Paragraph("颜色: " + (order.getColor() != null ? order.getColor() : ""), contentFont));
        document.add(new Paragraph("玻璃规格: " + (order.getGlassSpec() != null ? order.getGlassSpec() : ""), contentFont));
        document.add(new Paragraph(" ", contentFont));
        document.add(new Paragraph("双方权利与义务: ", titleFont));
        document.add(new Paragraph("1. 甲方应按时支付合同款项。", contentFont));
        document.add(new Paragraph("2. 乙方应按期完成门窗的生产与安装。", contentFont));
        document.add(new Paragraph("3. 本合同一经电子签署即生效。", contentFont));
        document.add(new Paragraph(" ", contentFont));
        document.add(new Paragraph("日期: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), contentFont));

        document.close();
        return out.toByteArray();
    }

    /**
     * 获取订单的合同列表
     */
    public Result<List<OrderContract>> getContractsByOrderId(Long orderId) {
        return Result.success(contractMapper.getByOrderId(orderId));
    }

    /**
     * 处理电子签章平台的回调
     */
    public Result<String> handleSignCallback(ContractSignCallbackReq req) {
        OrderContract contract = contractMapper.getByContractNo(req.getContractNo());
        if (contract == null) {
            return Result.error("合同不存在");
        }

        contract.setSignStatus(req.getStatus());
        contractMapper.update(contract);
        return Result.success("处理成功");
    }
}
