package com.gongli.upload;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNullApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api("上传服务接口")
public class UploadController {

    @Autowired
    UploadService uploadService;


    @PostMapping(value = "upload",headers = "content-type=multipart/form-data")
    @ApiOperation(value = "返回文件存储路径",notes = "上传文件")
    ResponseEntity<String> uploadImage(@RequestParam(value = "file") MultipartFile file) {

        String url = uploadService.uploadImage(file);
        if (StringUtils.isBlank(url)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(url);

    }


}
