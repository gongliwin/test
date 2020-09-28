package com.gongli.item.controller.pojo;


import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_specification")
@Data
public class specification {

    @Id
    Long category_id;
    String specifications;


}
