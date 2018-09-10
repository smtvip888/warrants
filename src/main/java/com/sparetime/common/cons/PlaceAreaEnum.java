package com.sparetime.common.cons;

import java.util.Arrays;
import java.util.Optional;

public enum PlaceAreaEnum {

    左(0), 右(1);

    PlaceAreaEnum(Integer code){
        this.code = code;
        this.name = this.name();
    }

    private Integer code;

    private String name;

    public static PlaceAreaEnum getPlaceAreaEnumByCode(Integer code){
        Optional<PlaceAreaEnum> optional = Arrays.asList(PlaceAreaEnum.values()).stream().filter(placeArea -> placeArea.code == code).findFirst();
        if (optional.isPresent())
            return optional.get();

        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
