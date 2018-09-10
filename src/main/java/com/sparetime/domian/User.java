package com.sparetime.domian;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

@Data
public class User extends BaseDomain implements Serializable{

    private BigDecimal userId;

    private BigDecimal subId;

    private BigDecimal recommendUserId;

    private BigDecimal parentUserId;

    private Integer placeArea;

    private String userSN;

    //@NotEmpty(message = "userName cant be null")
    @Length(min = 6, max = 20, message = "用户名必须在6-20位字符之间")
    private String userName;

    private String regCountry;

    //@NotEmpty(message = "password cant be null")
    @Length(min = 6, max = 20, message = "登录密码必须在6-20位字符之间")
    private String password;

    //@NotEmpty(message = "password1 cant be null")
    @Length(min = 6, max = 20, message = "安全密码必须在6-20位字符之间")
    private String password1;

    @Email(message = "email格式不正确")
    private String email;

    //@Pattern(regexp = "^\\s{0}|1[3|5|7|8][0-9]{9}$", message = "phone format is wrong")
    private String mobile;

    private String status;

    private int upgraded;

    private Date regDate;

    private String ip;

    private Integer isService;

    public static String builderUserSN(){
        //String userSN = "SN" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String userSN = "";
        Random random = new Random();
        for (int i = 0; i < 8; i++){
            userSN = userSN + random.nextInt(10);
        }
        return userSN;
    }
}
