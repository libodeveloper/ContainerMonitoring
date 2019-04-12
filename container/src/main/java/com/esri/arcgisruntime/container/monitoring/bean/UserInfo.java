package com.esri.arcgisruntime.container.monitoring.bean;

/**
 * Created by libo on 2019/4/12.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public class UserInfo {

    /**
     * account : test123
     * passwordOld : $2a$10$J6KTSSCv2aevAgL3oOwOr.EX9YYKhY0pFp3TAhPT8rHKWPEmTen5a
     * passwordNew :
     * roleName : 站点技术人员
     */

    private String account;
    private String passwordOld;
    private String passwordNew;
    private String roleName;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPasswordOld() {
        return passwordOld;
    }

    public void setPasswordOld(String passwordOld) {
        this.passwordOld = passwordOld;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
