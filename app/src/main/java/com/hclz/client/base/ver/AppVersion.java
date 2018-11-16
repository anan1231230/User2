package com.hclz.client.base.ver;

/**
 * 软件版本表
 */
public class AppVersion {
    /**
     * 版本号
     */
    private String ver;
    /**
     * 程序类型
     */
    private String appTp;
    /**
     * 安装文件路径
     */
    private String appPath;
    /**
     * 发布时间
     */
    private String regTm;
    /**
     * 升级内容说明
     */
    private String ctt;
    /**
     * 是否最新
     */
    private Boolean isLt;

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getAppTp() {
        return appTp;
    }

    public void setAppTp(String appTp) {
        this.appTp = appTp;
    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public String getRegTm() {
        return regTm;
    }

    public void setRegTm(String regTm) {
        this.regTm = regTm;
    }

    public String getCtt() {
        return ctt;
    }

    public void setCtt(String ctt) {
        this.ctt = ctt;
    }

    public Boolean getIsLt() {
        return isLt;
    }

    public void setIsLt(Boolean isLt) {
        this.isLt = isLt;
    }

}
