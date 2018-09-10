package com.sparetime.domian;

import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by muye on 17/8/15.
 */
@Data
public class SharesPrice extends BaseDomain {

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.2", message = "价格不能小于0.2")
    @DecimalMax(value = "1", message = "价格不能大于1")
    private BigDecimal price;

    private BigDecimal shares;

    private BigDecimal succesShares;

    private BigDecimal userShares;

    private String ip;

    private String adminUserName;

    private Date cdate;
}
