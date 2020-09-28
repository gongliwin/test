package com.gongli.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Locale;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageResult<T> {

    //总条数
    private Long total;
    //总页数
    private Long totalPage;
    //当前页数据
    private List<T> items;

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }
}
