package com.mailian.generator.config;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/23
 * @Description: 数据源配置
 */
public class DataSourceConfig {
    private String driver = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://192.168.1.199:3306/ml4?useSSL=false&characterEncoding=UTF-8";
    private String userName = "mailian";
    private String password = "123456";

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
