package com.sparetime.domian;

import lombok.Data;

import java.util.Date;

/**
 * Created by muye on 17/11/7.
 */
@Data
public class MessageImage extends BaseDomain{

    private Long mid;

    private String imgPath;

    private Date cdate;
}
