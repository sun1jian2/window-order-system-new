package com.window.system.controller;

import com.window.system.common.Result;
import com.window.system.config.MinioConfig;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfig minioConfig;

    private boolean bucketChecked = false;

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件为空");
        }
        try {
            // Check if bucket exists (only once)
            if (!bucketChecked) {
                boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build());
                if (!found) {
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
                    // Set public read policy
                    String policy = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" + minioConfig.getBucketName() + "/*\"]}]}";
                    minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(minioConfig.getBucketName()).config(policy).build());
                }
                bucketChecked = true;
            }

            String original = file.getOriginalFilename();
            String ext = "";
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf("."));
            } else {
                String ct = file.getContentType();
                if (ct != null) {
                    if (ct.startsWith("image/jpeg")) ext = ".jpg";
                    else if (ct.startsWith("image/png")) ext = ".png";
                    else if (ct.startsWith("image/gif")) ext = ".gif";
                    else if (ct.startsWith("application/pdf")) ext = ".pdf";
                    else if (ct.startsWith("application/vnd.ms-excel")) ext = ".xls";
                    else if (ct.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) ext = ".xlsx";
                    else if (ct.startsWith("application/msword")) ext = ".doc";
                    else if (ct.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) ext = ".docx";
                }
            }

            String dateDir = LocalDate.now().toString();
            String filename = UUID.randomUUID().toString().replace("-", "") + ext;
            String objectName = dateDir + "/" + filename;

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            String url = minioConfig.getEndpoint() + "/" + minioConfig.getBucketName() + "/" + objectName;
            return Result.success(url);
        } catch (Exception e) {
            log.error("File upload failed", e);
            return Result.error("上传失败: " + e.getMessage());
        }
    }

    @GetMapping("/{date}/{filename}")
    public ResponseEntity<byte[]> get(@PathVariable("date") String date, @PathVariable("filename") String filename) {
        try {
            String objectName = date + "/" + filename;
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .build());
            
            byte[] bytes = stream.readAllBytes();
            stream.close();

            String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            String lower = filename.toLowerCase();
            if (lower.endsWith(".png")) contentType = MediaType.IMAGE_PNG_VALUE;
            else if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) contentType = MediaType.IMAGE_JPEG_VALUE;
            else if (lower.endsWith(".gif")) contentType = MediaType.IMAGE_GIF_VALUE;
            else if (lower.endsWith(".pdf")) contentType = MediaType.APPLICATION_PDF_VALUE;

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(bytes);
        } catch (Exception e) {
            log.error("File download failed: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
