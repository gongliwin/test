package com.gongli.itemdetail.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongli.item.controller.pojo.*;
import com.gongli.itemdetail.api.BrandClient;
import com.gongli.itemdetail.api.CategoryClient;
import com.gongli.itemdetail.api.GoodsClient;
import com.gongli.itemdetail.api.SpecClient;
import com.gongli.itemdetail.bean.Param;
import com.gongli.itemdetail.bean.Spec;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsDetailService {
    @Autowired
    GoodsClient goodsClient;
    @Autowired
    BrandClient brandClient;
    @Autowired
    CategoryClient categoryClient;
    @Autowired
    SpecClient specClient;

    private static final Logger logger = LoggerFactory.getLogger(GoodsDetailService.class);
    ObjectMapper mapper=new ObjectMapper();

    public Map<String,Object> LoadModelData(Long id){
        Spu spu = goodsClient.querySpuById(id);

        SpuDetail spuDetail = goodsClient.querySpuDetailBySpuId(id);

        List<Sku> skus = goodsClient.querySkuBySpuId(id);

        List<Brand> brands = brandClient.findBrandsByBids(Arrays.asList(spu.getBrandId()));

        List<Category> categories = getCategories(spu);

        Map<String, Object> paramMap=getParams(spuDetail);

        Map<String, Object> map = new HashMap<>();
        map.put("spu", spu);
        map.put("spuDetail", spuDetail);
        map.put("skus", skus);
        map.put("brand", brands.get(0));
        map.put("categories", categories);
        //map.put("groups", specGroups);
        map.put("params", paramMap);
        return map;
    }

    private Map<String, Object> getParams(SpuDetail spuDetail){
        try {
            List<Spec> genericSpecs = mapper.readValue(spuDetail.getSpecifications(), new TypeReference<List<Spec>>() {
            });

            Map<String, Object> specialSpecs = mapper.readValue(spuDetail.getSpecTemplate(), new TypeReference<Map<String, Object>>() {
            });

            Map<String, Object> specMap = new HashMap<>();

            genericSpecs.forEach(spec -> {
                List<Param> params = spec.getParams();
                params.forEach(p -> {
                    if (p.getGlobal()) {
                        specMap.put(p.getK(), StringUtils.isBlank(p.getV()) ? "其它" : String.valueOf(p.getV()));
                    } else {
                        specMap.put(p.getK(), StringUtils.isBlank(p.getV()) ? "其它" : String.valueOf(specialSpecs.get(p.getV())));
                    }
                });
            });
            return specMap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Category> getCategories(Spu spu) {
        try {
            List<String> names = this.categoryClient.queryCategoryNameByCids(
                    Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            Category c1 = new Category();
            c1.setName(names.get(0));
            c1.setId(spu.getCid1());

            Category c2 = new Category();
            c2.setName(names.get(1));
            c2.setId(spu.getCid2());

            Category c3 = new Category();
            c3.setName(names.get(2));
            c3.setId(spu.getCid3());

            return Arrays.asList(c1, c2, c3);
        } catch (Exception e) {
            logger.error("查询商品分类出错，spuId：{}", spu.getId(), e);
        }
        return null;
    }



}
