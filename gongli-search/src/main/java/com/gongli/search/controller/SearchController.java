package com.gongli.search.controller;


import com.gongli.page.PageResult;
import com.gongli.search.bean.Goods;
import com.gongli.search.bean.SearchRequest;
import com.gongli.search.bean.SearchResult;
import com.gongli.search.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("搜索服务接口")
@RequestMapping("search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @ApiOperation(notes = "分页搜索数据",value = "PageResult<Goods>")
    @ApiImplicitParam(name = "request",type = "SearchRequest",required = true,value = "SearchRequest")
    @PostMapping("page")
    ResponseEntity<SearchResult> search(@RequestBody SearchRequest request){
        String key = request.getKey();
        SearchResult pageResult = searchService.search(request);
        if(pageResult==null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(pageResult);
    }
}
