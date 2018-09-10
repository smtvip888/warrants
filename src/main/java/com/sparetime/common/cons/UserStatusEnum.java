package com.sparetime.common.cons;

import java.util.Arrays;
import java.util.Optional;

public enum  UserStatusEnum {

    正常("1"), 冻结("-1");

    UserStatusEnum(String code){
        this.code = code;
    }

    private String code;

    public static UserStatusEnum getStatusByCode(String code){

        Optional<UserStatusEnum> optional = Arrays.asList(UserStatusEnum.values()).stream().filter(status -> status.code.equals(code)).findFirst();
        if (optional.isPresent())
            return optional.get();
        return UserStatusEnum.正常;
    }

    public String getCode() {
        return code;
    }

}
