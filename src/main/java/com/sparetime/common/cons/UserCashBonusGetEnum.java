package com.sparetime.common.cons;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by muye on 17/7/25.
 */
public enum  UserCashBonusGetEnum {

    待确认(1), 已确认(2);

    UserCashBonusGetEnum(int code){
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }

    public static UserCashBonusGetEnum getByCode(int code){

        Optional<UserCashBonusGetEnum> optional = Arrays.asList(UserCashBonusGetEnum.values()).stream()
                .filter(userCashBonusGetEnum -> userCashBonusGetEnum.code == code).findFirst();
        if (optional.isPresent())
            return optional.get();
        return UserCashBonusGetEnum.待确认;
    }
}
