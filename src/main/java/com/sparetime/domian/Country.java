package com.sparetime.domian;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class Country extends BaseDomain {

    private BigDecimal countryId;

    @NotEmpty(message = "国家名称不能为空")
    private String countryName;

    @NotEmpty(message = "国家代码不能为空")
    private String countryCode;

    @NotEmpty(message = "币种名称不能为空")
    private String currencyName;

    @NotNull(message = "汇率不能为空")
    @DecimalMax(value = "99999", message = "不能超过99999")
    @DecimalMin(value = "0.01", message = "不能小于0.01")
    private BigDecimal exchangeRate;

    @NotNull(message = "不能为空")
    private Integer isEnable;

    @NotNull(message = "排序号不能为空")
    private Integer sort;
}
