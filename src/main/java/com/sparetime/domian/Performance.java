package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by muye on 17/8/23.
 */
@Data
public class Performance extends BaseDomain {

    private BigDecimal userId;

    private String productName;

    private BigDecimal amount;
}
