package com.sparetime.domian.extend;

import com.sparetime.domian.User;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by muye on 17/10/16.
 */
@Data
public class UserGuide extends User {

    private String productName;

    private BigDecimal num;
}
