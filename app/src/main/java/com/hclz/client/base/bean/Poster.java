package com.hclz.client.base.bean;

import java.util.Map;

public class Poster {

//    private String img;
//    private String webview_url;
//
//    public String getImg() {
//        return img;
//    }
//
//    public void setImg(String img) {
//        this.img = img;
//    }
//
//    public String getWebview_url() {
//        return webview_url;
//    }
//
//    public void setWebview_url(String webview_url) {
//        this.webview_url = webview_url;
//    }

    /**
     * img : http://hclzdatadev.oss-cn-hangzhou.aliyuncs.com/posters/geichanggelaidian_poster.png
     * pageroute : {"pageid":"laidian_main_page","pagename":"给ta来点","pagetype":"native","pageparams":{"target_mid":"11701706"}}
     */

    private String img;
    /**
     * pageid : laidian_main_page
     * pagename : 给ta来点
     * pagetype : native
     * pageparams : {"target_mid":"11701706"}
     */

    private PagerouteEntity pageroute;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public PagerouteEntity getPageroute() {
        return pageroute;
    }

    public void setPageroute(PagerouteEntity pageroute) {
        this.pageroute = pageroute;
    }

    public static class PagerouteEntity {
        private String pageid;
        private String pagename;
        private String pagetype;
        private String url;
        /**
         * target_mid : 11701706
         */

        private Map<String, String> pageparams;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPageid() {
            return pageid;
        }

        public void setPageid(String pageid) {
            this.pageid = pageid;
        }

        public String getPagename() {
            return pagename;
        }

        public void setPagename(String pagename) {
            this.pagename = pagename;
        }

        public String getPagetype() {
            return pagetype;
        }

        public void setPagetype(String pagetype) {
            this.pagetype = pagetype;
        }

        public Map<String, String> getPageparams() {
            return pageparams;
        }

        public void setPageparams(Map<String, String> pageparams) {
            this.pageparams = pageparams;
        }

    }

}
