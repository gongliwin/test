package com.gongli.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongli.GLSearchService;
import com.gongli.item.controller.pojo.Sku;
import com.gongli.item.controller.pojo.Spu;
import com.gongli.item.controller.pojo.SpuBo;
import com.gongli.item.controller.pojo.SpuDetail;
import com.gongli.page.PageResult;
import com.gongli.search.api.CategoryClient;
import com.gongli.search.api.GoodsClient;
import com.gongli.search.api.SpecificationClient;
import com.gongli.search.bean.Goods;
import com.gongli.search.bean.Param;
import com.gongli.search.bean.Spec;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;


@SpringBootTest(classes = GLSearchService.class)
@RunWith(SpringRunner.class)
public class GoodsServiceTest {


    @Autowired
    CategoryClient categoryClient;

    @Autowired
    GoodsClient goodsClient;

    @Autowired
    SpecificationClient specificationClient;

    ObjectMapper mapper=new ObjectMapper();


    @Test
    public void buildsGoods() throws IOException {

        Goods goods = new Goods();

        PageResult<SpuBo> result = goodsClient.querySpuByPageAndFilter(1,100,null,true);

        List<SpuBo> spus = result.getItems();

        SpuBo spu=spus.get(0);

        List<String> nameList = categoryClient.queryCategoryNameByCids(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));



        List<Sku> skus = goodsClient.querySkuBySpuId(spu.getId());

        SpuDetail spuDetail = goodsClient.querySpuDetailBySpuId(spu.getId());

        List<Long> prices = new ArrayList<>();
        List<Map<String, Object>> skuList = new ArrayList<>();
        skus.forEach(sku -> {
            prices.add(sku.getPrice());
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id", sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("price", sku.getPrice());
            skuMap.put("image", StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            skuList.add(skuMap);
        });



        List<Spec> genericSpecs = mapper.readValue(spuDetail.getSpecifications(), new TypeReference<List<Spec>>() {
        });

        Map<String, String> specialSpecs = mapper.readValue(spuDetail.getSpecTemplate(), new TypeReference<Map<String, String>>() {
        });

        Map<String, Object> specMap = new HashMap<>();

        genericSpecs.forEach(spec -> {
            List<Param> params = spec.getParams();
            params.forEach(p -> {
                if (p.getSearchable()) {
                    if (p.getGlobal()) {
                        String value = p.getV();
                        if(p.getNumerical()!=null&&p.getNumerical()){
                            value = chooseSegment(value, p);
                        }
                        specMap.put(p.getK(), StringUtils.isBlank(value) ? "其它" : value);
                    } else {
                        specMap.put(p.getK(), specialSpecs.get(p.getV()));
                    }
                }
            });

        });

        goods.setId(spu.getId());
        goods.setSubTitle(spu.getSubTitle());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setAll(spu.getTitle() + " " + StringUtils.join(nameList, " "));
        goods.setPrice(prices);
        goods.setSkus(mapper.writeValueAsString(skuList));
        goods.setSpecs(specMap);

        System.out.println(goods.getSpecs().toString());




    }

    private String chooseSegment(String value, Param p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getUnit().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = val + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = val + p.getUnit() + "以下";
                }else{
                    result = val + p.getUnit();
                }
                break;
            }
        }
        return result;
    }




    }
