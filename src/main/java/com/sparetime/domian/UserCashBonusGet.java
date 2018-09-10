package com.sparetime.domian;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by muye on 17/7/24.
 */
@Data
public class UserCashBonusGet extends BaseDomain {

    private BigDecimal userId;

    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    @NotEmpty(message = "银行名称不能为空")
    private String bankName;

    private String bankCode;

    @NotEmpty(message = "银行地址不能为空")
    private String bankAddress;

    @NotEmpty(message = "账户名不能为空")
    private String accountName;

    @NotEmpty(message = "账号不能为空")
    private String accountNumber;

    private Integer status;

    private String remark;

    private Long adminUserId;

    private Date payDate;

    private Date cdate;
}
