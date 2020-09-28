package com.gongli.upload;



import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {

    @Autowired
    FastFileStorageClient fastFileStorageClient;
    @Autowired
    ThumbImageConfig thumbImageConfig;

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    private List<String> suffixes= Arrays.asList("image/png","image/ipeg");

    public String uploadImage(MultipartFile file) {
        String type = file.getContentType();
        if (!suffixes.contains(type)){
            logger.info("失败:{}",type);
            return null;
        }

        try {
            BufferedImage read = ImageIO.read(file.getInputStream());
            if (read==null){
                logger.info("文件内容");
                return null;
            }

//            File dir = new File("d://gongli//upload");
//            if (dir.exists()){
//                dir.mkdir();
//            }
//
//            file.transferTo(new File(dir,file.getOriginalFilename()));

            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");

            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            return "http://www.gongliwin.top/"+storePath.getFullPath();

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("啥异常");
            return null;
        }
    }
}
