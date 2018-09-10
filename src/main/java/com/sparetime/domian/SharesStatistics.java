package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by muye on 17/9/10.
 */
@Data
public class SharesStatistics {

    private BigDecimal totalProfit;
    private BigDecimal DTBonus;
    private BigDecimal DTFT;
    private BigDecimal DTLY;
    private BigDecimal buyShares;
    private BigDecimal userSellShares;
    private BigDecimal sysSellShares;
    private BigDecimal buyAmount;
    private BigDecimal userSellAmount;
    private BigDecimal sysSellAmount;
    private BigDecimal JTBonus;
    private BigDecimal JTFT;
    private BigDecimal JTSC;
    private Date cdate;
}
