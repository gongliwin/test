package com.gongli.itemdetail.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Param {

    String k;
    Boolean searchable;
    Boolean global;
    String[] options;
    Boolean numerical;
    String unit;
    String v;
}
