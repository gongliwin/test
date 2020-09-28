package com.gongli.item.mapper;

import com.gongli.item.controller.pojo.Brand;
import com.gongli.item.controller.pojo.Category;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


@Repository
public interface BrandMapper extends Mapper<Brand>,SelectByIdListMapper<Brand,Long>{


   @Insert("insert into tb_category_brand (category_id,brand_id) values(#{c_id},#{b_id})")
   void insertBrandByCids(@Param("c_id") Long c_id, @Param("b_id") Long b_id);

   @Select("select * from tb_category where id in(select category_id from tb_category_brand where brand_id = #{b_id})")
   List<Category> findCategoryByBid(@Param("b_id") Long b_id);

   @Select("select * from tb_brand b left join tb_category_brand cb on b.id = cb.brand_id where cb.category_id = #{c_id}")
   List<Brand> findBrandByCid(@Param("c_id") Long cid);
}
