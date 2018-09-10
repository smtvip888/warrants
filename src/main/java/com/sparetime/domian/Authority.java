package com.sparetime.domian;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;

/**
 * Created by muye on 17/7/19.
 */
@Data
public class Authority extends BaseDomain implements GrantedAuthority {

    private Long id;

    @NotEmpty(message = "authName cant be null")
    private String authName;

    @NotEmpty(message = "authCode cant be null")
    private String authCode;

    private String description;

    @Override
    public String getAuthority() {
        return authCode;
    }
}
