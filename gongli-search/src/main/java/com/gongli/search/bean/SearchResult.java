package com.gongli.search.bean;

import com.gongli.item.controller.pojo.Brand;
import com.gongli.item.controller.pojo.Category;
import com.gongli.page.PageResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResult extends PageResult<Goods> {

    private List<Map<String,Object>> specs;

    private List<Category> categories;

    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Map<String, Object>> specs, List<Category> categories, List<Brand> brands) {
        super(total, totalPage, items);
        this.specs = specs;
        this.categories = categories;
        this.brands = brands;
    }

    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }

    private List<Brand> brands;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public SearchResult() {
    }

    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Category> categories, List<Brand> brands) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
    }
}
