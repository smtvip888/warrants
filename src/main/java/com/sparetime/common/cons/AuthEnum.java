package com.sparetime.common.cons;

/**
 * Created by muye on 17/7/17.
 */
public enum AuthEnum {

    SUP_ADMIN("超级管理员"), ADMIN("普通管理员");

    AuthEnum(String authName){
        this.authName = authName;
    }

    private String authName;

    public String getRoleName() {
        return authName;
    }
}
