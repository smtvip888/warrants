package com.sparetime.common.cons;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by muye on 17/7/26.
 */
public enum TradeTypeEnum {

    动态(1), 静态(2);

    TradeTypeEnum(int code){
        this.code = code;
    }

    private int code;

    public static TradeTypeEnum getTradeTypeByCode(int code){

        Optional<TradeTypeEnum> optional = Arrays.asList(TradeTypeEnum.values()).stream().filter(tradeTypeEnum -> tradeTypeEnum.code == code).findFirst();
        if (optional.isPresent())
            return optional.get();
        return null;
    }

    public int getCode() {
        return code;
    }
}
