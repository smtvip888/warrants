package com.sparetime.domian;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by muye on 17/7/18.
 */
@Data
public class Manager extends BaseDomain {


    @NotEmpty(message = "name cant be null")
    private String name;

    @NotEmpty(message = "password cant be null")
    private String password;
}
