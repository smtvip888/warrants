package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by muye on 17/8/21.
 */
@Data
public class SharesSellRefundLog extends BaseDomain {

    private BigDecimal userId;

    private BigDecimal sellId;

    private BigDecimal shares;

    private BigDecimal price;

    private BigDecimal amount;

    private Date cdate;
}
