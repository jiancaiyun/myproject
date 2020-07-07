package com.imooc.seckill.vo;

import javax.validation.constraints.NotNull;
import com.imooc.seckill.util.IsMobile;
import org.hibernate.validator.constraints.Length;

public class LoginVo {
    private String mobile;
    private String password;

    @NotNull
    @IsMobile
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    @NotNull
    @Length(min=32)
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


}