package com.gongli.item.controller.pojo;


import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

@Data
public class SpuBo extends Spu {
    @Transient
    String cname;
    @Transient
    String bname;
    @Transient
    List<Sku> skus;
    @Transient
    SpuDetail spuDetail;
}
