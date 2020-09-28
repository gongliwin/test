import com.gongli.GLSearchService;
import com.gongli.item.controller.pojo.SpuBo;
import com.gongli.page.PageResult;
import com.gongli.search.api.CategoryClient;
import com.gongli.search.api.GoodsClient;
import com.gongli.search.api.GoodsRepository;
import com.gongli.search.bean.Goods;
import com.gongli.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = GLSearchService.class)
@RunWith(SpringRunner.class)
public class test {
    @Autowired
    CategoryClient categoryClient;

    @Autowired
    GoodsClient goodsClient;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    SearchService searchService;

    @Autowired
    GoodsRepository goodsRepository;

    @Test
    public void createIndex(){
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);
    }

    @Test
    public void LoadData() throws Exception {
        int page = 1;
        int rows = 100;
        int size = 0;

        do {
            System.out.println("**************进来一次***************"+page);
            PageResult<SpuBo> result = goodsClient.querySpuByPageAndFilter(page, rows, null, true);

            List<SpuBo> items = result.getItems();
            size = items.size();
            List<Goods> goodsList = new ArrayList<Goods>();

            items.forEach(spu -> {
                try {
                    Goods goods = searchService.buildsGoods(spu);
                    goodsList.add(goods);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            goodsRepository.saveAll(goodsList);
            page++;
        } while (size == 100);


    }
}

