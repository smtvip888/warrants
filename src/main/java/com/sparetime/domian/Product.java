package com.sparetime.domian;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by muye on 17/7/25.
 */
@Data
public class Product implements Serializable {

    private BigDecimal productId;

    @NotEmpty(message = "level cant be null")
    private String level;

    @NotEmpty(message = "name cant be null")
    private String name;

    @NotNull(message = "isShow cant be null")
    private int isShow;

    @NotNull(message = "cant be null")
    private BigDecimal invested;

    @NotNull(message = "cant be null")
    private BigDecimal TJJ;

    @NotNull(message = "cant be null")
    private BigDecimal DPJ;

    @NotNull(message = "cant be null")
    private BigDecimal FDJ1;

    @NotNull(message = "cant be null")
    private BigDecimal FDJ2;

    @NotNull(message = "cant be null")
    private BigDecimal FDJ3;

    @NotNull(message = "cant be null")
    private BigDecimal FDJ4;

    @NotNull(message = "cant be null")
    private BigDecimal FDJ5;

    @NotNull(message = "cant be null")
    private BigDecimal JDJCS;

    @NotNull(message = "cant be null")
    private BigDecimal CJBS;

    @NotNull(message = "cant be null")
    private BigDecimal KCGS;

    @NotNull(message = "cant be null")
    private BigDecimal KMGS;

    @NotNull(message = "cant be null")
    private BigDecimal PGBL;
}
