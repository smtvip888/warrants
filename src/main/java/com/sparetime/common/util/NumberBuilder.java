package com.sparetime.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by muye on 17/8/15.
 */
public class NumberBuilder {

    public static String build(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = sdf.format(new Date());

        Random random = new Random();
        for (int i = 0; i < 4; i++){
            s = s + random.nextInt(10);
        }
        return s;
    }
}
