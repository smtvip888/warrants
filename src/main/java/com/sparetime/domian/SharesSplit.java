package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by muye on 17/8/23.
 */
@Data
public class SharesSplit extends BaseDomain {

    private BigDecimal splitPrice;

    private BigDecimal afterSplitPrice;

    private BigDecimal lastNetUserId;

    private Long userCount;

    private BigDecimal largessShares;

    private BigDecimal largessSShares;

    private BigDecimal largessDShares;

    private Date cdate;
}
