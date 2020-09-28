package com.gongli.search.api;

import com.gongli.search.bean.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodsRepository extends ElasticsearchRepository<Goods,Long>{


}
