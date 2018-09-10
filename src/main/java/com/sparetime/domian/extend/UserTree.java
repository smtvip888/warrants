package com.sparetime.domian.extend;

import com.sparetime.domian.User;
import com.sparetime.domian.UserAsset;
import lombok.Data;

/**
 * Created by muye on 17/8/15.
 */
@Data
public class UserTree {

    private User user;

    private UserAsset userAsset;

    private UserTree left;

    private UserTree right;
}
