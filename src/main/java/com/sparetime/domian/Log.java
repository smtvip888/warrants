package com.sparetime.domian;

import lombok.Data;

import java.util.Date;

/**
 * Created by muye on 17/10/13.
 */
@Data
public class Log extends BaseDomain{

    private Integer logType;

    private Integer userType;

    private String userName;

    private String remark;

    private String ip;

    private Date cdate;
}
