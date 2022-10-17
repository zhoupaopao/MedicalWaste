package com.arch.demo.core.model;

import java.io.Serializable;

/**
 * Created by zcx on 2018/6/9.
 */

public class TokenBean implements Serializable {

    /**
     * access_token : 21578aa5-c1bf-44bc-934e-e90a3d5f2829
     * token_type : bearer
     * expires_in : 42703
     * scope : read
     */

    private String access_token;
    private String token_type;
    private int expires_in;
    private String scope;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
