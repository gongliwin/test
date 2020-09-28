package com.gongli.item.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gongli.enums.ExceptionEnum;
import com.gongli.exception.myException;
import com.gongli.item.controller.pojo.Brand;
import com.gongli.item.controller.pojo.Category;
import com.gongli.item.mapper.BrandMapper;
import com.gongli.page.PageResult;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class BrandService {

    @Autowired
    BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page,rows);
        //过滤
        Example example=new Example(Brand.class);

        if(StringUtils.isNotBlank(key)){
            example.createCriteria().orLike("name","%"+key+"%").orEqualTo("letter",key.toUpperCase());
        }
        //排序
        if (StringUtils.isNotBlank(sortBy)){
            String orderByClause=sortBy+(desc?" DESC":" ASC");
            example.setOrderByClause(orderByClause);
        }

        List<Brand> list = brandMapper.selectByExample(example);


        if (CollectionUtils.isEmpty(list)){
            throw new myException(ExceptionEnum.brand_cannot_null);
        }
        PageInfo<Brand> pageInfo=new PageInfo<>(list);

        return new PageResult<Brand>(pageInfo.getTotal(), (long) pageInfo.getPages(),list);
    }

    public void insertBrandBycids(Brand brand,List<Long> cids) {
        int count = brandMapper.insertSelective(brand);
        if(count!=1){
            throw new myException(ExceptionEnum.price_cannot_null);
        }
        System.out.println("龚力"+brand.getId());
        for(Long cid:cids){

            brandMapper.insertBrandByCids(cid,brand.getId());
        }


    }

    public List<Category> findCategoryByBid(Long bid) {

        return brandMapper.findCategoryByBid(bid);
    }

    public List<Brand> findBrandByCid(Long cid) {
        return brandMapper.findBrandByCid(cid);

    }

    public Brand findBrandByBid(Long bid) {
        return brandMapper.selectByPrimaryKey(bid);
    }

    public List<Brand> findBrandsByBids(List<Long> bids){
        return brandMapper.selectByIdList(bids);
    }
}
