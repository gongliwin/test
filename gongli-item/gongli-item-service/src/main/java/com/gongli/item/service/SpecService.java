package com.gongli.item.service;


import com.gongli.item.controller.pojo.specification;
import com.gongli.item.mapper.SpecMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class SpecService {

    @Autowired
    SpecMapper specMapper;

    public specification findSpecByBid(Long bid) {

        //LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();


        specification specification = specMapper.selectByPrimaryKey(bid);

        return specification;
    }
}
