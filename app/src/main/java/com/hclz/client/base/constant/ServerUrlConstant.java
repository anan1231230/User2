package com.hclz.client.base.constant;

import com.google.gson.reflect.TypeToken;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.util.JsonUtility;

import java.util.Map;

public enum ServerUrlConstant {
    //webview测试根路径
//	WEBVIEWROOTTEST("http://test.jabomall.com/hclzwebview","webview根路径"),

    PRODUCTS_APPRAISE("/product/product_comment.html", "评价"),
    GET_COUPON("/coupon/distribute_coupons.html", "领取卡券"),
    COUPON_MY("/coupon/user_coupons.html", "我的卡券"),
    PRODUCT_DETAIL("/product/product_detail.html", "产品详情"),

    // ///////// 服务器根路径 ///////////
//	BASEROOT("http://121.40.249.75:8080/hclz", "好吃懒做项目根路径"),
    // ///////// 接口名字 ///////////
    CSHOP_INFO_FOR_ERWEIMA("/cshop/fromcode", "通过二维码获取cshop信息"),
    CSHOP_RECOMMEND("/cshop/search/recommend", "该接口以基于地理位置的搜索为主，同时会给出最近使用、最多使用等推荐"),
    CSHOP_ALL("/cshop/all", "返回所有的合伙人列表"),
    CSHOP_USER_BIND("/cshop/user/bind", "绑定合伙人"),
    CSHOP_USER_BIND_QUERY("/cshop/user/bind_query", "查询是否已绑定"),
    CSHOP_SEARCH_QUERY("/cshop/search/query", "关键词搜索为主,搜索商店"),


    CSHOP_ORDER_CHECK_UNDELIVERED("/cshop/order/check/undelivered", "城代未发货"),
    CSHOP_ORDER_CHECK_UNRECEIVED("/cshop/order/check/unreceived", "未接单"),
    CSHOP_ORDER_CHECK_UNCONFIRMED("/cshop/order/check/unconfirmed", "未接货"),
    CSHOP_ORDER_CHECK_CONFIRMED("/cshop/order/check/confirmed", "已接货"),
    CSHOP_ORDER_CHECK_HISTORY("/cshop/order/check/finished", "历史订单"),

    CSHOP_ORDER_OP_CONFIRM("/cshop/order/op/confirm", "确认接单"),
    CSHOP_ORDER_OP_SENDOUT("/cshop/order/op/sendout", "发货"),
    CSHOP_ORDER_OP_REACHED("/cshop/order/op/reached", "送达"),
    CSHOP_ORDER_OP_TODSHOP("/cshop/order/op/todshop", "缺货"),
    CSHOP_INVENTORY_QUERY("/cshop/inventory/query2", "库存查询"),
    CSHOP_INVENTORY_ADJUST("/cshop/inventory/adjust2", "库存调整"),

    DSHOPMALL_ALL("/dshopmall/product/query/all", "查询城代库存接口"),
    DSHOPMALL_V1_ALL("/dshopmall/product/v1/query/all", "查询库存接口"),
    CSHOPMALL_ALL("/cshopmall/product/query/all", "查询社区厨房库存接口"),
    DSHOPMALL_PERMLINK("/dshopmall/permlinks", "获取dshop permlink的接口"),
    GET_CITYS("/dshopmall/cities", "获取城市列表"),

    ORDER_APPLY("/order/orderid/apply", "订单号申请接口"),
    ORDER_APPLY2("/order/apply_order", "订单号申请接口"),
    ORDER_REFRESH2("/order/refresh", "订单刷新"),
    ORDER_PLACE2("/order/place", "下单"),
    ORDER_OFFLINEPAY2("/order/offline_pay", "线下支付"),
    PRODUCT_TYPES_QUERY2("/product/types/query", "获取分类促销国家等信息"),
    ORDER_ANOTHER("/products/query/pids","再来一单产品查询"),


    PRODUCT_TYPES("/products/category", "最新的获取type的接口"),
    PRODUCT_TYPES_B2B("/products/category/b2b", "最新的获取type的接口-B2B"),
    PRODUCT_LIST("/products/query", "最新的获取产品列表接口"),
    PRODUCTS_RECOMMENDS("/products/recommends", "猜你喜欢接口"),
    ORDER_RECORD("/products/purchased", "买过的商品列表"),


    HOT_SEARCH_KEY("/init/hot_search_keys","获取热搜词汇"),

    INIT_ARTICLES("/init/articles", "文章列表接口"),


    BRAND_LIST("/products/franchisee/list", "品牌加盟列表接口"),
    BRAND_GOODS("/products/franchisee/goods", "加盟商上架的商品"),




    ORDER_CSHOP_COMMIT("/order/submit/cshopmall", "从厨房下单（即堂食和带回家做）"),
    ORDER_DSHOP_COMMIT("/order/submit/dshopmall", "从城市代理下单（即网上商城）"),
    ORDER_ALL("/order/query", "订单查询接口（最近一个月的订单）"),
    ORDER_LIST("/order/list", "订单查询接口（最近一个月的订单新）"),


    ORDER_CONFIRM("/order/confirm", "确认收货接口， 修改user_status为200"),
    ORDER_CANCEL("/order/cancel", "取消订单"),
    ORDER_HIDE("/order/hide", "删除订单"),
    ORDER_STATUS_OP("/order/status_op", "删除订单"),
    ORDER_GET("/order/get", "获取某个单个订单"),
    CSHOP_GET("/cshop/get", "获取CSHOP信息"),




    ASSETS_COUPON_LIST("/assets/order/coupon/list", "下订单时可用卡券查询"),
    ASSETS_USER("/assets/user/basic/list", "用户查询自己一对一资产（当前只有积分）"),
    ASSETS_USER_CARDS_QUERY("/assets/user/cards/query", "查询用户钱包,充值卡等"),
    ASSETS_USER_BILLS_BUYCARD("/assets/user/bills/buycard", "资产购买订单提交"),
    ASSETS_USER_BILLS_RECHARGE("/assets/user/bills/recharge", "充值任意金额接口"),
    ASSETS_USER_BILLS_QUERY("/assets/user/bills/query", "账单查询（包含充值及消费记录)"),
    ASSETS_USER_VERIFYCODE_SEND("/assets/user/zhifu/verifycode/send", "钱包密码设置验证码发送"),
    ASSETS_USER_ZHIFUPWD_SET("/assets/user/zhifu/zhifupasswd/set", "支付密码设定"),
    ASSETS_USER_ZHIFU("/assets/user/zhifu/pay", "钱包支付"),

    JIAJU_ALL_PRODUCT("/product/jiaju", "获取家居产品信息"),

    USER_CHANGE_INTRODUCER("/user/introducer/reset", "修改推荐人手机号接口"),
    USER_SELT("/user/query/self", "获取自己用户信息接口"),
    USER_BIND_WX("/user/bind/oauth/weichat", "绑定账号到微信"),
    USER_FEEDBACK("/user/advise/feedback", "提交意见接口"),
    USER_LOGIN("/user/login", "手机号登录接口"),
    USER_REGIASTER("/user/signup", "手机号注册"),
    USER_FORGET_PWD("/user/passwd/reset", "重置密码"),
    USER_VERIFYCODE("/user/verifycode/sms/send", "短信验证码发送"),
    USER_UPDATE("/user/update", "更新用户信息接口"),
    USER_ADDRESS_LIST("/user/address/fetch", "查询地址信息"),
    USER_UPLOAD_ADDRESS("/user/address/add_or_update", "创建/更新地址信息接口"),

    USER_UPLOAD_STAFF("/staff/add_or_update", "创建/更新员工信息接口"),
    USER_STAFF_LIST("/staff/fetch", "查询员工列表"),
    USER_DELETE_STAFF("/staff/delete", "删除员工"),

    HCLZ_IM_KEFU("/im/kefu", "客服列表"),
    SHOUYE_INFO("/init/index", "获取首页的展示信息");


    // 请求完整路径
    StringBuilder sb;
    private String urlDomains = HclzApplication.getData().get("url_domains");
    private Map<String, String> urls = JsonUtility.fromJson(urlDomains,
            new TypeToken<Map<String, String>>() {
            });
    // 接口名字
    private String method;
    // 接口描述
    private String methodDescription;

    // 定义信息
    private ServerUrlConstant(String method, String methodDescription) {
        this.method = method;
        this.methodDescription = methodDescription;
    }

//	public String getMethod() {
//		// 拼接成完整请求路径
//		sb = new StringBuilder(urls.get("user"));
//		sb.append(method);
//		return sb.toString();
//	}

    public String getUserMethod() {
        // 拼接成完整请求路径
        sb = new StringBuilder(urls.get("user"));
        sb.append(method);
        return sb.toString();
    }

    public String getShopMethod() {
        // 拼接成完整请求路径
        sb = new StringBuilder(urls.get("shop"));
        sb.append(method);
        return sb.toString();
    }

    public String getOrderMethod() {
        // 拼接成完整请求路径
        sb = new StringBuilder(urls.get("order"));
        sb.append(method);
        return sb.toString();
    }

    public String getAssetsMethod() {
        // 拼接成完整请求路径
        sb = new StringBuilder(urls.get("assets"));
        sb.append(method);
        return sb.toString();
    }

    public String getSocityMethod() {
        // 拼接成完整请求路径
        sb = new StringBuilder(urls.get("socity"));
        sb.append(method);
        return sb.toString();
    }

    public String getProductMethod() {
        // 拼接成完整请求路径
        sb = new StringBuilder(urls.get("product"));
        sb.append(method);
        return sb.toString();
    }

    public String getWebViewMethod() {
        // 拼接成完整请求路径
        sb = new StringBuilder(urls.get("webview"));
        sb.append(method);
        return sb.toString();
    }

    public String getMethodDescription() {
        return methodDescription;
    }
}
