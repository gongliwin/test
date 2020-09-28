package com.gongli.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongli.GLSearchService;
import com.gongli.item.controller.pojo.*;
import com.gongli.page.PageResult;
import com.gongli.search.api.*;
import com.gongli.search.bean.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.jetbrains.annotations.TestOnly;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class SearchService {

    @Autowired
    CategoryClient categoryClient;

    @Autowired
    GoodsClient goodsClient;

    @Autowired
    SpecificationClient specificationClient;

    @Autowired
    BrandClient brandClient;

    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    ObjectMapper mapper=new ObjectMapper();

    private static final Logger logger= LoggerFactory.getLogger(SearchService.class);

    public Goods buildsGoods(Spu spu) throws IOException {

        Goods goods = new Goods();

//        PageResult<SpuBo> result = goodsClient.querySpuByPageAndFilter(1,100,null,true);
//
//        List<SpuBo> spus = result.getItems();
//
//        SpuBo spu=spus.get(0);

        List<String> nameList = categoryClient.queryCategoryNameByCids(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));



        List<Sku> skus = goodsClient.querySkuBySpuId(spu.getId());

        System.out.println("****************spuId****************"+spu.getId());
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

        Map<String, Object> specialSpecs = mapper.readValue(spuDetail.getSpecTemplate(), new TypeReference<Map<String, Object>>() {
        });

        Map<String, Object> specMap = new HashMap<>();

        genericSpecs.forEach(spec -> {
            List<Param> params = spec.getParams();
            params.forEach(p -> {
                if (p.getSearchable()) {
                    if (p.getGlobal()) {
                        specMap.put(p.getK(), StringUtils.isBlank(p.getV()) ? "其它" : String.valueOf(p.getV()));
                    } else {
                        specMap.put(p.getK(), StringUtils.isBlank(p.getV()) ? "其它" : String.valueOf(specialSpecs.get(p.getV())));
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
        return goods;

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


    public SearchResult search(SearchRequest request) {
        String key=request.getKey();
        if(StringUtils.isBlank(key)){
            return null;
        }
        NativeSearchQueryBuilder sq = new NativeSearchQueryBuilder();
//        MatchQueryBuilder qb = QueryBuilders.matchQuery("all", key).operator(Operator.AND);
//
//        sq.withQuery(qb);
        //根据前端参数过滤
        QueryBuilder qb = getSepcSelectedFilter(request);
        sq.withQuery(qb);

        //过滤
        sq.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"},null));

        //分页
        int page=request.getPage();
        int size=request.getSize();
        sq.withPageable(PageRequest.of(page-1,size));

        //排序
        Boolean descending = request.getDescending();
        String sortBy = request.getSortBy();
        if(StringUtils.isNotBlank(sortBy)){
            sq.withSort(SortBuilders.fieldSort(sortBy).order(descending ? SortOrder.ASC : SortOrder.DESC));
        }

        //聚合
        String categoryAggName="category";
        String brandAggName="brand";
        sq.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        sq.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        AggregatedPage<Goods> pages = (AggregatedPage<Goods>) goodsRepository.search(sq.build());

        Aggregation agg = pages.getAggregation(categoryAggName);
        List<Category> categorys = parseCategoryResult(agg);
        List<Map<String,Object>> spec=new ArrayList<>();
        if(categorys.size()==1){
             spec = getSpec(categorys.get(0).getId(), qb);
        }
        List<Brand> brands = parseBrandResult(pages.getAggregation(brandAggName));

        double totalPages = Math.ceil((double) pages.getTotalElements() / (double) size);
        return new SearchResult(pages.getTotalElements(), (long)totalPages ,pages.getContent(),spec,categorys,brands);

    }

    private QueryBuilder getSepcSelectedFilter(SearchRequest request) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND));

        BoolQueryBuilder boolQuery1 = QueryBuilders.boolQuery();

        Map<String, String> filter = request.getFilter();

        filter.forEach((k,v)->{
            if (k != "cid3" && k != "brandId") {
                k = "specs." + k + ".keyword";
            }
            boolQuery1.must(QueryBuilders.termQuery(k,v));
        });

        boolQuery.filter(boolQuery1);
        return boolQuery;
    }

    private List<Map<String,Object>> getSpec(Long id, QueryBuilder qb){

        try {
            NativeSearchQueryBuilder sq = new NativeSearchQueryBuilder();
            sq.withQuery(qb);

            String specJson = specificationClient.findSpecByCid(id);
            //SpuDetail spuDetail = goodsClient.querySpuDetailBySpuId(spu.getId());
            List<Spec>  specs = mapper.readValue(specJson, new TypeReference<List<Spec>>() {
            });

            List<Map<String,Object>> result = new ArrayList<>();

            specs.forEach(spec->{
                List<Param> params = spec.getParams();
                params.forEach(p->{
                    if (p.getSearchable()) {
                        String k = p.getK();
                        sq.addAggregation(AggregationBuilders.terms(k).field("specs."+k+".keyword"));
                    }

                });
            });


            AggregatedPage<Goods> goods = elasticsearchTemplate.queryForPage(sq.build(), Goods.class);
//            Map<String, Aggregation> aggs = elasticsearchTemplate.query(sq.build(),
//                    SearchResponse::getAggregations).asMap();

            Aggregations aggs = goods.getAggregations();

            specs.forEach(spec->{
                List<Param> params = spec.getParams();
                params.forEach(p->{
                    if (p.getSearchable()) {
                        Map<String,Object> map1=new HashMap<>();
                        String k = p.getK();
                        map1.put("key",k);
                        StringTerms aggregation = (StringTerms) aggs.get(k);
                        map1.put("options",aggregation.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString).collect(Collectors.toList()));
                        result.add(map1);
                    }
                });
            });
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    List<Category> parseCategoryResult(Aggregation aggregation){
        try {
            LongTerms longTerms= (LongTerms) aggregation;
            List<Category> categories = new ArrayList<>();
            List<Long> cids = new ArrayList<>();
            longTerms.getBuckets().forEach(gongli->{
                cids.add(gongli.getKeyAsNumber().longValue());
            });

            List<String> names = categoryClient.queryCategoryNameByCids(cids);
            for(int i=0;i<names.size();i++){
                Category category = new Category();
                category.setName(names.get(i));
                category.setId(cids.get(i));
                categories.add(category);
            }
            return categories;
        } catch (Exception e) {
            logger.error("种类聚合出现异常：", e);
            return null;
        }
    }

     List<Brand> parseBrandResult(Aggregation aggregation) {
        try {
            LongTerms brandAgg = (LongTerms) aggregation;
            List<Long> bids = new ArrayList<>();
            for (LongTerms.Bucket bucket : brandAgg.getBuckets()) {
                bids.add(bucket.getKeyAsNumber().longValue());
            }
            // 根据id查询品牌
            return brandClient.findBrandsByBids(bids);
        } catch (Exception e){
            logger.error("品牌聚合出现异常：", e);
            return null;
        }
    }

    public void createIndex(Long id) throws IOException {
        Spu spu = goodsClient.querySpuById(id);
        Goods goods = buildsGoods(spu);
        goodsRepository.save(goods);

    }

    public void createDelete(Long id) {
        goodsRepository.deleteById(id);
    }
}
