package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by muye on 17/8/22.
 */
@Data
public class SharesBuyLog extends BaseDomain {

    private BigDecimal buyId;

    private BigDecimal userId;

    private BigDecimal price;

    private BigDecimal amount;

    private BigDecimal shares;

    private Date cdate;
}
