package com.gongli.search.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Param {

    String k;
    Boolean searchable;
    Boolean global;
    String[] options;
    Boolean numerical;
    String unit;
    String v;
}
