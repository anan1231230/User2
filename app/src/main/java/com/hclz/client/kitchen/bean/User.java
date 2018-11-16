package com.hclz.client.kitchen.bean;

import java.util.List;

/**
 * Created by handsome on 16/1/17.
 */
public class User {

    /**
     * 73 : {"address":"潮合汇 哈哈","addressid":"73","detail":"潮合汇 哈哈","location":[117.043647,36.708541],"name":"还打电话","phone":"18764126675"}
     * 74 : {"address":"立新给池 黄家驹","addressid":"74","detail":"立新给池 黄家驹","location":[117.07736,37.268687],"name":"要好好","phone":"18764126675"}
     * 75 : {"address":"烟台经济技术开发区 哈哈哈","addressid":"75","detail":"烟台经济技术开发区 哈哈哈","location":[121.260374,37.569871],"name":"干活","phone":"15632365235"}
     * 76 : {"address":"烟台经济技术开发区 刚刚给","addressid":"76","detail":"烟台经济技术开发区 刚刚给","location":[121.260374,37.569871],"name":"GG胡","phone":"18632365262"}
     * 77 : {"address":"烟台经济技术开发区   刚刚给","addressid":"77","detail":"烟台经济技术开发区   刚刚给","location":[121.260374,37.569871],"name":"GG胡","phone":"18632365262"}
     * 78 : {"address":"烟台经济技术开发区管委会 哈哈","addressid":"78","detail":"烟台经济技术开发区管委会 哈哈","location":[121.260284,37.569571],"name":"QQ","phone":"18764312364"}
     * 79 : {"address":"烟台经济技术开发区  好久没见","addressid":"79","detail":"烟台经济技术开发区  好久没见","location":[121.260374,37.569871],"name":"GGv哈哈","phone":"18764126625"}
     */

    private List<AddressEntity> addresses;
    /**
     * basic : {"points":{"amount":100,"expire_utcms":null,"mid":"15","name":"points","unlimited":false}}
     * coupons_count : 0
     */

    private AssetsEntity assets;
    /**
     * address : 好生活不动产    很多很多说说
     * addressid : 16
     * detail : 好生活不动产    很多很多说说
     * location : [117.028843,36.716338]
     * name : 挂的空
     * phone : 18766509750
     */

    private DefaultAddressEntity default_address;
    /**
     * at : 1453193092611
     * avatar : http://wx.qlogo.cn/mmopen/QnM5bMcic4Z3RpFRicT1Fe94XBpHKEiaicxwLIWXST2Okb31uENib2qkAnibZLgwjEJlF5pcXLKj78Xa7v1U0H3XibC6A/0
     * ct : 1441696903789
     * default_addreddid : 16
     * default_addressid : 16
     * language : zh_CN
     * latest_cid : 05310002
     * mid : 15
     * nickname : 听音乐
     * sessionid : kALUShHP6tpi400jlqyqikb3qj4xs411l2bw7737
     * sex : 1
     * ut : 1452601113435
     */

    private MainAccountEntity main_account;
    /**
     * city : 烟台
     * country : 中国
     * headimgurl : http://wx.qlogo.cn/mmopen/QnM5bMcic4Z3RpFRicT1Fe94XBpHKEiaicxwLIWXST2Okb31uENib2qkAnibZLgwjEJlF5pcXLKj78Xa7v1U0H3XibC6A/0
     * language : zh_CN
     * mid : 15
     * nickname : 华少
     * privilege : []
     * province : 山东
     * sex : 1
     * sid : o1nGUt857761-dOdSo7TpGzYWTAs
     * type : weichat
     */

    private List<SubAccountsEntity> sub_accounts;

    public AssetsEntity getAssets() {
        return assets;
    }

    public void setAssets(AssetsEntity assets) {
        this.assets = assets;
    }

    public DefaultAddressEntity getDefault_address() {
        return default_address;
    }

    public void setDefault_address(DefaultAddressEntity default_address) {
        this.default_address = default_address;
    }

    public MainAccountEntity getMain_account() {
        return main_account;
    }

    public void setMain_account(MainAccountEntity main_account) {
        this.main_account = main_account;
    }

    public List<SubAccountsEntity> getSub_accounts() {
        return sub_accounts;
    }

    public void setSub_accounts(List<SubAccountsEntity> sub_accounts) {
        this.sub_accounts = sub_accounts;
    }

    public static class AddressEntity {
        /**
         * address : 潮合汇 哈哈
         * addressid : 73
         * detail : 潮合汇 哈哈
         * location : [117.043647,36.708541]
         * name : 还打电话
         * phone : 18764126675
         */

        private String address;
        private String addressid;
        private String detail;
        private String name;
        private String phone;
        private List<Double> location;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddressid() {
            return addressid;
        }

        public void setAddressid(String addressid) {
            this.addressid = addressid;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public List<Double> getLocation() {
            return location;
        }

        public void setLocation(List<Double> location) {
            this.location = location;
        }

    }

    public static class AssetsEntity {
        /**
         * points : {"amount":100,"expire_utcms":null,"mid":"15","name":"points","unlimited":false}
         */

        private BasicEntity basic;
        private AssetsettingEntity assetsettings;
        private int coupons_count;

        public AssetsettingEntity getAssetsettings() {
            return assetsettings;
        }

        public void setAssetsettings(AssetsettingEntity assetsettings) {
            this.assetsettings = assetsettings;
        }

        public BasicEntity getBasic() {
            return basic;
        }

        public void setBasic(BasicEntity basic) {
            this.basic = basic;
        }

        public int getCoupons_count() {
            return coupons_count;
        }

        public void setCoupons_count(int coupons_count) {
            this.coupons_count = coupons_count;
        }

        public static class AssetsettingEntity {

//            'is_freeze': True/False, //账户是否被冻结，默认False
//                    'freeze_dt': '2016-05-13 12:12:43', 上次冻结时间
//            'is_zhifu_passwd_set': True/False, 是否设置过支付密码，默认False
//            'passwd_error_count': 2,  //错误次数
//                    'passwd_error_count_remain': 3,
//                    'verifycode_error_count': 3,
//                    'verifycode_error_count_remain': 2

            private boolean is_freeze;
            private String freeze_dt;
            private boolean is_zhifu_passwd_set;
            private int passwd_error_count;
            private int passwd_error_count_remain;
            private int verifycode_error_count;
            private int verifycode_error_count_remain;

            public String getFreeze_dt() {
                return freeze_dt;
            }

            public void setFreeze_dt(String freeze_dt) {
                this.freeze_dt = freeze_dt;
            }

            public boolean is_freeze() {
                return is_freeze;
            }

            public void setIs_freeze(boolean is_freeze) {
                this.is_freeze = is_freeze;
            }

            public boolean getIs_zhifu_passwd_set() {
                return is_zhifu_passwd_set;
            }

            public void setIs_zhifu_passwd_set(boolean is_zhifu_passwd_set) {
                this.is_zhifu_passwd_set = is_zhifu_passwd_set;
            }

            public int getPasswd_error_count() {
                return passwd_error_count;
            }

            public void setPasswd_error_count(int passwd_error_count) {
                this.passwd_error_count = passwd_error_count;
            }

            public int getPasswd_error_count_remain() {
                return passwd_error_count_remain;
            }

            public void setPasswd_error_count_remain(int passwd_error_count_remain) {
                this.passwd_error_count_remain = passwd_error_count_remain;
            }

            public int getVerifycode_error_count() {
                return verifycode_error_count;
            }

            public void setVerifycode_error_count(int verifycode_error_count) {
                this.verifycode_error_count = verifycode_error_count;
            }

            public int getVerifycode_error_count_remain() {
                return verifycode_error_count_remain;
            }

            public void setVerifycode_error_count_remain(int verifycode_error_count_remain) {
                this.verifycode_error_count_remain = verifycode_error_count_remain;
            }


        }

        public static class BasicEntity {
            /**
             * amount : 100
             * expire_utcms : null
             * mid : 15
             * name : points
             * unlimited : false
             */

            private PointsEntity points;
            private BalanceEntity balance;

            public BalanceEntity getBalance() {
                return balance;
            }

            public void setBalance(BalanceEntity balance) {
                this.balance = balance;
            }

            public PointsEntity getPoints() {
                return points;
            }

            public void setPoints(PointsEntity points) {
                this.points = points;
            }

            public static class PointsEntity {
                private int amount;
                private Object expire_utcms;
                private String mid;
                private String name;
                private boolean unlimited;

                public int getAmount() {
                    return amount;
                }

                public void setAmount(int amount) {
                    this.amount = amount;
                }

                public Object getExpire_utcms() {
                    return expire_utcms;
                }

                public void setExpire_utcms(Object expire_utcms) {
                    this.expire_utcms = expire_utcms;
                }

                public String getMid() {
                    return mid;
                }

                public void setMid(String mid) {
                    this.mid = mid;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public boolean isUnlimited() {
                    return unlimited;
                }

                public void setUnlimited(boolean unlimited) {
                    this.unlimited = unlimited;
                }
            }

            public static class BalanceEntity {
                private Long amount;

                public Long getAmount() {
                    return amount;
                }

                public void setAmount(Long amount) {
                    this.amount = amount;
                }
            }
        }
    }

    public static class DefaultAddressEntity {
        private String address;
        private String addressid;
        private String detail;
        private String name;
        private String phone;
        private List<Double> location;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddressid() {
            return addressid;
        }

        public void setAddressid(String addressid) {
            this.addressid = addressid;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public List<Double> getLocation() {
            return location;
        }

        public void setLocation(List<Double> location) {
            this.location = location;
        }
    }

    public static class MainAccountEntity {
        private String at;
        private String avatar;
        private String ct;
        private String default_addreddid;
        private String default_addressid;
        private String language;
        private String latest_cid;
        private String mid;
        private String nickname;
        private String sessionid;
        private String sex;
        private String ut;
        private String age;
        private String sign;
        private String coverimg;
        private String name;
        private String daxue;

        private String daxueJson;

        private String xiaoqu;
        private String xueyuan;
        private String ruxue_year;
        private String health_point;

        public String getDaxueJson() {
            return daxueJson;
        }

        public void setDaxueJson(String daxueJson) {
            this.daxueJson = daxueJson;
        }

        public String getAt() {
            return at;
        }

        public void setAt(String at) {
            this.at = at;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCt() {
            return ct;
        }

        public void setCt(String ct) {
            this.ct = ct;
        }

        public String getDefault_addreddid() {
            return default_addreddid;
        }

        public void setDefault_addreddid(String default_addreddid) {
            this.default_addreddid = default_addreddid;
        }

        public String getDefault_addressid() {
            return default_addressid;
        }

        public void setDefault_addressid(String default_addressid) {
            this.default_addressid = default_addressid;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getLatest_cid() {
            return latest_cid;
        }

        public void setLatest_cid(String latest_cid) {
            this.latest_cid = latest_cid;
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

        public String getSessionid() {
            return sessionid;
        }

        public void setSessionid(String sessionid) {
            this.sessionid = sessionid;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getUt() {
            return ut;
        }

        public void setUt(String ut) {
            this.ut = ut;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getCoverimg() {
            return coverimg;
        }

        public void setCoverimg(String coverimg) {
            this.coverimg = coverimg;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDaxue() {
            return daxue;
        }

        public void setDaxue(String daxue) {
            this.daxue = daxue;
        }

        public String getXiaoqu() {
            return xiaoqu;
        }

        public void setXiaoqu(String xiaoqu) {
            this.xiaoqu = xiaoqu;
        }

        public String getXueyuan() {
            return xueyuan;
        }

        public void setXueyuan(String xueyuan) {
            this.xueyuan = xueyuan;
        }

        public String getRuxue_year() {
            return ruxue_year;
        }

        public void setRuxue_year(String ruxue_year) {
            this.ruxue_year = ruxue_year;
        }

        public String getHealth_point() {
            return health_point;
        }

        public void setHealth_point(String health_point) {
            this.health_point = health_point;
        }

    }

    public static class SubAccountsEntity {
        private String city;
        private String country;
        private String headimgurl;
        private String language;
        private String mid;
        private String nickname;
        private String privilege;
        private String province;
        private String sex;
        private String sid;
        private String type;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
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

        public String getPrivilege() {
            return privilege;
        }

        public void setPrivilege(String privilege) {
            this.privilege = privilege;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
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
