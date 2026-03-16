package com.window.system.service;

import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.Brand;
import com.window.system.model.req.brand.BrandListReq;
import com.window.system.model.req.brand.BrandSaveReq;
import com.window.system.mapper.BrandMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import com.window.system.security.AuthUser;

import java.util.Collections;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public Result<PageResponse<Brand>> list(BrandListReq req) {
        long count = brandMapper.countList(req);
        if (count == 0) {
            return Result.success(PageResponse.of(Collections.emptyList(), 0L));
        }
        List<Brand> list = brandMapper.selectList(req);
        return Result.success(PageResponse.of(list, count));
    }
    
    @Cacheable(value = "brand_list", key = "'all'")
    public Result<List<Brand>> listAll() {
        return Result.success(brandMapper.selectAll());
    }

    @CacheEvict(value = "brand_list", allEntries = true)
    public Result<String> save(BrandSaveReq req) {
        Brand brand = new Brand();
        BeanUtils.copyProperties(req, brand);
        if (req.getId() == null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication() != null 
                ? SecurityContextHolder.getContext().getAuthentication().getPrincipal() : null;
            if (principal instanceof AuthUser au) {
                brand.setCreateBy(au.getId());
            }
            brandMapper.insert(brand);
        } else {
            Object principal = SecurityContextHolder.getContext().getAuthentication() != null 
                ? SecurityContextHolder.getContext().getAuthentication().getPrincipal() : null;
            if (principal instanceof AuthUser au) {
                brand.setUpdateBy(au.getId());
            }
            brandMapper.update(brand);
        }
        return Result.success("Saved successfully");
    }

    @CacheEvict(value = "brand_list", allEntries = true)
    public Result<String> delete(Long id) {
        brandMapper.delete(id);
        return Result.success("Deleted successfully");
    }
}
