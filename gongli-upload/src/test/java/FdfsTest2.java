import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
public class FdfsTest2 {

    @Autowired
    FastFileStorageClient fastFileStorageClient;

    @Autowired
    ThumbImageConfig thumbImageConfig;

    @Test
    public void test() throws Exception {
        File file = new File("d://set//1.png");

        StorePath storePath = fastFileStorageClient.uploadFile(new FileInputStream(file), file.length(), "png", null);

        System.out.println("带分组"+storePath.getFullPath());
        System.out.println("不带分组"+storePath.getPath());


    }

    @Test
    public void test2() throws Exception {
        File file = new File("d://set//1.png");

        StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(new FileInputStream(file), file.length(), "png", null);

        System.out.println("带分组"+storePath.getFullPath());
        System.out.println("不带分组"+storePath.getPath());
        String imagePath = thumbImageConfig.getThumbImagePath(storePath.getPath());

        System.out.println("不带分组的缩略图路径"+imagePath);


    }

}
