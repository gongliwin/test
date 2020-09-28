package com.gongli.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gongli.enums.ExceptionEnum;
import com.gongli.exception.myException;
import com.gongli.item.controller.pojo.*;
import com.gongli.item.mapper.*;
import com.gongli.page.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoosService {

    @Autowired
    StockMapper stockMapper;
    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    BrandMapper brandMapper;
    @Autowired
    SpuMapper spuMapper;
    @Autowired
    SpuDetailMapper spuDetailMapper;
    @Autowired
    SkuMapper skuMapper;
    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendMsg(Long id,String type){
        try {
            amqpTemplate.convertAndSend("item."+type,id);
        } catch (AmqpException e) {
            e.printStackTrace();
        }

    }


    public PageResult<SpuBo> querySpuByPageAndFilter(Integer page, Integer rows, String key, Boolean salable) {
        PageHelper.startPage(page,Math.min(rows,100));

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        if (salable!=null){
            criteria.orEqualTo("saleable",salable);
        }

        if (StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }

        List<Spu> lists = goodsMapper.selectByExample(example);

        if(CollectionUtils.isEmpty(lists)){
            throw new myException(ExceptionEnum.price_cannot_null);
        }

        PageInfo<Spu> pageInfo=new PageInfo<>(lists);

        List<SpuBo> spuBos = pageInfo.getList().stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);

            List<String> list = categoryMapper.selectByIdList(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());

            spuBo.setCname(StringUtils.join(list, "/"));

            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());

            spuBo.setBname(brand.getName());

            return spuBo;
        }).collect(Collectors.toList());

        return new PageResult<>(pageInfo.getTotal(),spuBos);


    }

    @Transactional
    public void save(SpuBo spu) {
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spuMapper.insert(spu);

        Long id = spu.getId();
        spu.getSpuDetail().setSpu_id(id);
        spuDetailMapper.insert(spu.getSpuDetail());

        List<Sku> skuList = spu.getSkus();

        for (Sku sku:skuList){

            if (!sku.getEnable()){
                continue;
            }
            sku.setSpuId(id);
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            skuMapper.insert(sku);

            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());

            stockMapper.insert(stock);
        }

    }

    public SpuDetail querySpuDetailBySpuId(Long spuId) {

        return spuDetailMapper.selectByPrimaryKey(spuId);
    }

    public List<Sku> querySkuBySpuId(Long spuId) {
        Sku param = new Sku();
        param.setSpuId(spuId);
        List<Sku> skus = skuMapper.select(param);
        for (Sku sku:skus) {
            Stock stock = stockMapper.selectByPrimaryKey(sku.getId());
            if (stock!=null){
                sku.setStock(stock.getStock());
            }
        }

        return skus;
    }

    public Spu querySpuById(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }
}
