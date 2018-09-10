package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by muye on 17/7/25.
 */
@Data
public class NetUser {

    private BigDecimal netUserId;

    private BigDecimal userId;

    private BigDecimal recommendUserId;

    private BigDecimal parentUserId;

    private Integer placeArea;

    private BigDecimal productId;

    private BigDecimal price;

    private BigDecimal totalProfit;

    private BigDecimal lastSalePrice;

    private Integer isOut;

    private Date cdate;
}
