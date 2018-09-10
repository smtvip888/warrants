package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by muye on 17/7/24.
 */
@Data
public class ShareSell extends BaseDomain{

    private BigDecimal sellId;

    private String tradeSN;

    private BigDecimal userId;

    private BigDecimal netUserId;

    private int shareType;

    private int isOriginalStake;

    private BigDecimal shares;

    private BigDecimal price;

    private BigDecimal succesShares;

    private BigDecimal splitCount;

    private String tradeIds;

    private Integer status;

    private String ip;

    private Date cdate;
}
