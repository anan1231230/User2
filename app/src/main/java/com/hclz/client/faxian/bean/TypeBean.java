package com.hclz.client.faxian.bean;

import java.util.List;

/**
 * Created by hjm on 16/9/21.
 */

public class TypeBean {
    /**
     * cateid : 6
     * icon : http://hclzimages.oss-cn-hangzhou.aliyuncs.com/%E7%B1%BB%E5%88%AB/%E8%82%89%E7%B1%BB%E7%A6%BD%E7%B1%BB.png
     * level : 1
     * name : 肉类禽类
     * subs : []
     * superid : 0
     * type1 : diancan
     */

    private List<SubsBean> subs;
    public List<SubsBean> getSubs() {
        return subs;
    }

    public void setSubs(List<SubsBean> subs) {
        this.subs = subs;
    }

    public static class SubsBean {
        private String cateid;
        private String icon;
        private int level;
        private String name;
        private String superid;
        private int size;
        private String type1;
        private List<SubsBean> subs;
        public boolean isSelected;

        public int getSize(){return subs.size();}
        public String getCateid() {
            return cateid;
        }

        public void setCateid(String cateid) {
            this.cateid = cateid;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSuperid() {
            return superid;
        }

        public void setSuperid(String superid) {
            this.superid = superid;
        }

        public String getType1() {
            return type1;
        }

        public void setType1(String type1) {
            this.type1 = type1;
        }

        public List<SubsBean> getSubs() {
            return subs;
        }

        public void setSubs(List<SubsBean> subs) {
            this.subs = subs;
        }

        public boolean isSelected(){
            return isSelected;
        }
        public void setSelected(boolean isSelected){
            this.isSelected = isSelected;
        }
    }

}
