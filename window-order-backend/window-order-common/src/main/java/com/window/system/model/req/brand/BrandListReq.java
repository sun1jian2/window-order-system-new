package com.window.system.model.req.brand;

import cn.hutool.json.JSONUtil;
import com.window.system.model.req.BasePageReq;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class BrandListReq extends BasePageReq implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }


}
