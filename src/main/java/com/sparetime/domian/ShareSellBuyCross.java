package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by muye on 17/7/24.
 */
@Data
public class ShareSellBuyCross extends BaseDomain{

    private BigDecimal buyId;

    private BigDecimal sellId;

    private BigDecimal shares;

    private BigDecimal price;

    private BigDecimal amount;

    private Date cdate;
}
