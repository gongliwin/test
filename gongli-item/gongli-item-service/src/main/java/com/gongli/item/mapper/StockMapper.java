package com.gongli.item.mapper;

import com.gongli.item.controller.pojo.Stock;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface StockMapper extends Mapper<Stock> {
}
