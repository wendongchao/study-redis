package com.example.redis.service;

import com.example.redis.mbg.model.PmsBrand;

import java.util.List;

/**
 * PmsBrandService
 * Created by macro on 2019/4/19.
 */
public interface PmsBrandService {

    int create(PmsBrand brand);

    int update(Long id, PmsBrand brand);

    int delete(Long id);

    PmsBrand getItem(Long id);

    List<PmsBrand> list(Integer pageNum, Integer pageSize);

    List<PmsBrand> ListAll();
}
