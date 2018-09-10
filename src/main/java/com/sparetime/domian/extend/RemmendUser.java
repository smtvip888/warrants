package com.sparetime.domian.extend;

import com.sparetime.domian.User;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by muye on 17/8/24.
 */
@Data
public class RemmendUser extends User {

    private String level;

    private BigDecimal netPrice;

    private String regTime;

}
