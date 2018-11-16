package com.hclz.client.base.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hjm on 16/7/12.
 */

public class KitchenUser implements Serializable {

    private String cid;
    private String code;
    /**
     * at : 1468293665953
     * at_ui : 2016-07-12 11:21:05
     * mid : 21001356
     * nickname : nimini
     */

    private MainAccountBean main_account;
    private String user_type;
    /**
     * mid : 21001356
     * sid : 13356698059
     * type : phone
     */

    private List<SubAccountsBean> sub_accounts;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MainAccountBean getMain_account() {
        return main_account;
    }

    public void setMain_account(MainAccountBean main_account) {
        this.main_account = main_account;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public List<SubAccountsBean> getSub_accounts() {
        return sub_accounts;
    }

    public void setSub_accounts(List<SubAccountsBean> sub_accounts) {
        this.sub_accounts = sub_accounts;
    }

    public static class MainAccountBean {
        private String at;
        private String at_ui;
        private String mid;
        private String nickname;

        public String getAt() {
            return at;
        }

        public void setAt(String at) {
            this.at = at;
        }

        public String getAt_ui() {
            return at_ui;
        }

        public void setAt_ui(String at_ui) {
            this.at_ui = at_ui;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }

    public static class SubAccountsBean {
        private String mid;
        private String sid;
        private String type;

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
