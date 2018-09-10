package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by muye on 17/8/16.
 */
@Data
public class TodayPrice {

    private String name;

    private BigDecimal price;

    private int sort;
}
