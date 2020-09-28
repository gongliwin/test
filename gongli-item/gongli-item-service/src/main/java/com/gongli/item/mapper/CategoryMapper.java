package com.gongli.item.mapper;

import com.gongli.item.controller.pojo.Category;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface CategoryMapper extends Mapper<Category>,SelectByIdListMapper<Category,Long>{

}
