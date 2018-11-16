package com.hclz.client.base.config;

/**
 * 配置
 */
public class SanmiConfig {

    /**
     * 订单产品详情最大显示数量
     */
    public static final int ORDER_MAX_SHOW_NUM = 5;
    /**
     * 录音时长限制(秒)
     */
    public static final int TIME_RECORD = 90;
    /**
     * 图片压缩的最大宽度
     */
    public static final int IMAGE_WIDTH = 640;
    /**
     * 图片压缩的最大高度
     */
    public static final int IMAGE_HEIGHT = 3000;
    /**
     * 图片压缩的失真率
     */
    public static final int IMAGE_QUALITY = 100;
    /**
     * 是否打印信息
     */
    public static final boolean LOG = true;
    /**
     * 网络请求尝试次数
     */
    public static int TRYTIMES_HTTP = 3;
    /**
     * 图片请求尝试次数
     */
    public static int TRYTIMES_IMAGE = 3;
    /**
     * 网络请求编码方式
     */
    public static final String ENCODING = "UTF-8";
    /**
     * 网络请求连接超时时限(单位:毫秒)
     */
    public static final int TIMEOUT_CONNECT_HTTP = 10000;
    /**
     * 网络请求读取超时时限(单位:毫秒)
     */
    public static final int TIMEOUT_READ_HTTP = 10000;
    /**
     * 图片缓存（外部缓存）的最大数量
     */
    public static int IMAGES_EXTERNAL = 200;
    /**
     * 图片缓存（内部缓存）的最大数量
     */
    public static int IMAGES_INTERNAL = 100;
    /**
     * 图片请求连接超时时限(单位:毫秒)
     */
    public static int TIMEOUT_CONNECT_IMAGE = 10000;

    /**
     * 聊天录音时长控制
     */
    /**
     * 图片请求读取超时时限(单位:毫秒)
     */
    public static int TIMEOUT_READ_IMAGE = 10000;
    /**
     * 文件下载请求连接超时时限(单位:毫秒)
     */
    public static int TIMEOUT_CONNECT_FILE = 10000;
    /**
     * 文件下载读取超时时限(单位:毫秒)
     */
    public static final int TIMEOUT_READ_FILE = 1000000;
    /**
     * 是否只在WIFI下下载图片
     */
    public static boolean IMAGELOAD_ONLYWIFI = false;
    /**
     * 判定是否loadingDialog
     */
    public static boolean isShowingLoadingDialog = false;

//    /**
//     * 判定当前界面是否有dialog显示，如果有，则不能弹出第二次
//     */
//    public static boolean isDialogShowing = false;

    /**
     * 判定是否首次加载
     */
    public static boolean isFirstLoad = true;


    /**
     * 是否下拉状态
     */
    public static boolean isPulling = false;

    //是否需要刷新产品价格
    public static boolean isHaiwaiNeedRefresh = false;
    public static boolean isMallNeedRefresh = false;

    public static boolean isOrderNeedRefresh = true;
    public static boolean isPaihangNeedRefresh = false;

    public static boolean isJiajuNeedRedresh = false;

    //===============屏幕宽度/屏幕高度
    public static int screen_width;
    public static int screen_height;

    //==============判断是好吃懒做还是海外精品
    public static boolean isHaiwai = false;

}
