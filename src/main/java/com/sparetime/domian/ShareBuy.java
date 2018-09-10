package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by muye on 17/7/24.
 */
@Data
public class ShareBuy extends BaseDomain {

    private BigDecimal buyId;

    private String tradeSN;

    private BigDecimal userId;

    private BigDecimal netUserId;

    private Integer fundType;

    private BigDecimal price;

    private BigDecimal buyPrice;

    private BigDecimal succesPrice;

    private BigDecimal shares;

    private BigDecimal succesShares;

    private Integer status;

    private String ip;

    private Date cdate;
}
