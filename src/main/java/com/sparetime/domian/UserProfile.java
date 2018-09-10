package com.sparetime.domian;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserProfile extends BaseDomain {

    private BigDecimal userId;

    private String idCard;

    private String postCode;

    @NotEmpty(message = "地址不能为空")
    private String address;

    @NotEmpty(message = "省不能为空")
    private String province;

    @NotEmpty(message = "市不能为空")
    private String city;

    private Date birthday;

    private String bankCountry;

    @NotEmpty(message = "银行名称不能为空")
    private String bankName;

    private String bankCode;

    @NotEmpty(message = "账户名不能为空")
    private String bankAccountName;

    @NotEmpty(message = "卡号不能为空")
    private String bankAccountNumber;
}
