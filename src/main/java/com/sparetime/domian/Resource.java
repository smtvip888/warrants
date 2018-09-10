package com.sparetime.domian;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by muye on 17/7/19.
 */
@Data
public class Resource extends BaseDomain{

    @NotNull(message = "pid cant be null")
    private Long pid;

    private String uri;

    private String icon;

    @NotEmpty(message = "name cant be null")
    private String name;

    private String description;

    private List<Resource> children;
}
