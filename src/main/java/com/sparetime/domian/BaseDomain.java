package com.sparetime.domian;

import lombok.Data;

import java.util.Date;

/**
 * Created by muye on 17/7/18.
 */
@Data
public class BaseDomain {

    private Long id;

    private Date createTime;

    private Date modifyTime;

    private Date startTime;

    private Date endTime;
}
