package com.example.redis.service.impl;

import com.example.redis.config.RedisConfig;
import com.example.redis.mbg.mapper.PmsBrandMapper;
import com.example.redis.mbg.model.PmsBrand;
import com.example.redis.mbg.model.PmsBrandExample;
import com.example.redis.service.PmsBrandService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PmsBrandService实现类
 * Created by macro on 2019/4/19.
 */
@Service
public class PmsBrandServiceImpl implements PmsBrandService {
    @Autowired
    private PmsBrandMapper brandMapper;

    @Override
    public int create(PmsBrand brand) {
        return brandMapper.insertSelective(brand);
    }

    @CacheEvict(value = RedisConfig.REDIS_KEY_DATABASE, key = "'pms:brand:'+#id")
    @Override
    public int update(Long id, PmsBrand brand) {
        brand.setId(id);
        return brandMapper.updateByPrimaryKeySelective(brand);
    }

    @CacheEvict(value = RedisConfig.REDIS_KEY_DATABASE, key = "'pms:brand:'+#id")
    @Override
    public int delete(Long id) {
        return brandMapper.deleteByPrimaryKey(id);
    }

    @Cacheable(value = RedisConfig.REDIS_KEY_DATABASE, key = "'pms:brand:'+#id", unless = "#result==null")
    @Override
    public PmsBrand getItem(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<PmsBrand> list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return brandMapper.selectByExample(new PmsBrandExample());
    }

    @Override
    public List<PmsBrand> ListAll() {
        return brandMapper.selectByExample(new PmsBrandExample());
    }


}
