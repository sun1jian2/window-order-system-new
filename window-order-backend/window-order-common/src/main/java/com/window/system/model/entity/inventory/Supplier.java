package com.window.system.model.entity.inventory;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
/**
 * Supplier 实体/请求/响应类
 */
public class Supplier implements Serializable {
    private Long id;
    private String name;
    private String contactPerson;
    private String phone;
    private String address;
    private BigDecimal accountBalance;
    private String remark;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    private Integer isDeleted;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
