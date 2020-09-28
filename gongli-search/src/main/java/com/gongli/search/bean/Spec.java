package com.gongli.search.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Spec {
    String group;
    List<Param> params;
    Boolean empty;

}
