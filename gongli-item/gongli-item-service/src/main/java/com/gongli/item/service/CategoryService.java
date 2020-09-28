package com.gongli.item.service;


import com.gongli.enums.ExceptionEnum;
import com.gongli.exception.myException;
import com.gongli.item.controller.pojo.Category;
import com.gongli.item.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    public List<Category> queryCategoryListByParentId(Long pid) {
        Category category=new Category();
        category.setParentId(pid);
        List<Category> list = categoryMapper.select(category);
        if (CollectionUtils.isEmpty(list)){
            throw new myException(ExceptionEnum.price_cannot_null);
        }
        return list;
    }

    public List<String> queryCategoryNameByCids(List<Long> cids) {
        return categoryMapper.selectByIdList(cids).stream().map(Category::getName).collect(Collectors.toList());
    }

    public List<Category> queryAllByCid3(Long cid){
        Category c3 = categoryMapper.selectByPrimaryKey(cid);
        Category c2 = categoryMapper.selectByPrimaryKey(c3.getParentId());
        Category c1 = categoryMapper.selectByPrimaryKey(c2.getParentId());
//        List<Category> list=new ArrayList<>();
//        list.add(c1);
//        list.add(c2);
//        list.add(c3);

        return Arrays.asList(c1,c2,c3);
    }


}
