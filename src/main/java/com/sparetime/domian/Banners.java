package com.sparetime.domian;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class Banners extends BaseDomain{

    private String imgurl;

    @NotEmpty(message = "链接地址不能为空")
    private String url;

    @NotEmpty(message = "描述不能为空")
    private String desc;

    private Integer sort;
}
