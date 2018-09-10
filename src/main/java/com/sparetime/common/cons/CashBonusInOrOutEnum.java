package com.sparetime.common.cons;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by muye on 17/7/26.
 */
public enum CashBonusInOrOutEnum {

    进账(1), 支出(2);


    CashBonusInOrOutEnum(int code){
        this.code = code;
    }

    private int code;

    public static CashBonusInOrOutEnum getByCode(int code){

        Optional<CashBonusInOrOutEnum> optional = Arrays.asList(CashBonusInOrOutEnum.values()).stream().
                filter(cashBonusInOrOutEnum -> cashBonusInOrOutEnum.code == code).findFirst();

        if (optional.isPresent())
            return optional.get();
        return null;
    }

    public int getCode() {
        return code;
    }
}
