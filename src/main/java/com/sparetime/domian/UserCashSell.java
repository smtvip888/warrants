package com.sparetime.domian;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserCashSell extends BaseDomain {

    private BigDecimal sellId;

    private String tradeSN;

    private BigDecimal userId;

    @DecimalMax(value = "1500", message = "数量不能超过1500")
    @DecimalMin(value = "50", message = "数量不能小于50")
    @NotNull(message = "数量不能为空")
    private BigDecimal amount;

    private BigDecimal brokerage;

    @NotEmpty(message = "币种不能为空")
    private String currencyName;

    @NotNull(message = "汇率不能为空")
    private BigDecimal exchangeRate;

    @NotEmpty(message = "银行名称不能为空")
    private String bankName;

    private String bankAddress;

    @NotEmpty(message = "账户名称不能为空")
    private String accountName;

    @NotEmpty(message = "银行账号不能为空")
    private String accountNumber;

    @NotEmpty(message = "手机号码不能为空")
    private String mobile;

    private BigDecimal buyId;

    private BigDecimal buyUserId;

    private String ip;

    private Integer status;

    private Date cdate;

    private String confirmIp;

    private Date confirmDate;
}
