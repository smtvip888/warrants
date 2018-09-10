package com.sparetime.domian;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by muye on 17/8/28.
 */
@Data
public class SysConfig {

    @NotEmpty(message = "key cant be null")
    @Length(max = 20)
    private String key;
    @NotEmpty(message = "value cant be null")
    //@Length(max = 50)
    private String value;

    @Length(max = 50)
    private String remark;
}
