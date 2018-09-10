package com.sparetime.domian;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by muye on 17/8/8.
 */
@Data
public class SharesConfig {

    @NotNull(message = "cant be null")
    private BigDecimal DTFTBL;

    @NotNull(message = "cant be null")
    private BigDecimal DTTXBL;

    @NotNull(message = "cant be null")
    private BigDecimal DTLYJFBL;

    @NotNull(message = "cant be null")
    private BigDecimal JTSXFBL;

    @NotNull(message = "cant be null")
    private BigDecimal JTTXBL;

    @NotNull(message = "cant be null")
    private BigDecimal JTFTBL;

    @NotNull(message = "cant be null")
    private BigDecimal JTSCJFBL;

    @NotNull(message = "cant be null")
    private BigDecimal JDJBL;

    @NotNull(message = "cant be null")
    private BigDecimal initialPrice;

    @NotNull(message = "cant be null")
    private BigDecimal splitPrice;

    @NotNull(message = "cant be null")
    private BigDecimal unitPrice;

    @NotNull(message = "cant be null")
    private BigDecimal unitShares;

    @NotNull(message = "cant be null")
    private BigDecimal maxBuyPrice;

    @NotNull(message = "cant be null")
    private BigDecimal ZXTXJE;

    @NotNull(message = "cant be null")
    private BigDecimal MRJG;

    @NotNull(message = "cant be null")
    private BigDecimal MCJG;

    @NotNull(message = "cant be null")
    private BigDecimal DPJZFDBS;

    private long HYSJQX;

    @NotNull(message = "cant be null")
    private BigDecimal TXSXF;

    private Integer HKQRSJ;

    private Integer SKQRSJ;

    private int enabledReg;
    private int enabledLogin = 1;
    private int enabledGetCash;

    @Min(value = 0, message = "不能为负数")
    @NotNull(message = "不能为空")
    private Integer enabledTrade;
}
