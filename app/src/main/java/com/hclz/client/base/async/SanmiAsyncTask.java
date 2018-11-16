package com.hclz.client.base.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.hclz.client.R;
import com.hclz.client.base.bean.BaseResponseBean;
import com.hclz.client.base.config.SanmiConfig;
import com.hclz.client.base.constant.MessageConstant;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.exception.HttpException;
import com.hclz.client.base.handler.WeakHandler;
import com.hclz.client.base.http.HttpUtil;
import com.hclz.client.base.log.LogUploadUtil;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.base.ver.VersionUtils;
import com.hclz.client.base.view.TextViewDialog;
import com.hclz.client.base.view.WaitingDialogControll;

import java.util.HashMap;
import java.util.Map;

/***
 * @author Administrator
 *         异步请求基类
 */
public class SanmiAsyncTask {
    // 基类
    private final Context context;
    // 服务端返回的错误状态值
    private final String STATUS_SUCCESS = "200";
    private final String STATUS_CLIENT_ERROR = "400";
    private final String STATUS_SERVER_ERROR = "500";

    private boolean isShowDialog = true;

    private WeakHandler mHandler;

    /***
     * 构造函数
     *
     * @param context
     */
    public SanmiAsyncTask(Context context) {
        this.context = context;
    }


    public void setIsShowDialog(boolean isShowDialog) {
        this.isShowDialog = isShowDialog;
    }

    public WeakHandler getHandler() {
        return mHandler;
    }


    public void setHandler(WeakHandler handler) {
        this.mHandler = handler;
    }


    /***
     * 不带参数的请求
     *
     * @param method  方法名
     * @param handler 回调
     */
    public void excutePosetRequest(final String method,
                                   final ResultHandler handler) {
        excutePosetRequest(method, null, handler);
    }

    /***
     * 不带参数的请求
     *
     * @param method  方法名
     * @param handler 回调
     */
    public void excutePosetRequest(final String method,
                                   final HashMap<String, String> map, final ResultHandler handler) {
        excutePosetRequest(method, map, null, handler);
    }

    /***
     * 不带参数的请求
     *
     * @param method  方法名
     * @param handler 回调
     * @throws HttpException
     */
    public void excutePosetRequest(final String method,
                                   final HashMap<String, String> map, final HashMap<String, String> filemap,
                                   final ResultHandler handler) {

        // 检查网络
        if (!CommonUtil.isInternetAvailable(context)) {
            ToastUtil.showToast(
                    context,
                    MessageConstant.NETWORK_DISCONNECT_EXCEPTION.getMsgcontent());
            return;
        }
        if (isShowDialog && !SanmiConfig.isShowingLoadingDialog) {
            // 显示等待对话框
            WaitingDialogControll.showWaitingDialog(context);
        }
        // 开启异步线程，请求数据
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result;
                try {
                    // 不包含文件
                    if (filemap == null) {
                        result = HttpUtil.sendPOSTForString(method, map, SanmiConfig.ENCODING);
                    } else {

                        // 包含文件
                        result = HttpUtil
                                .sendPOSTWithFilesForString(
                                        // 请求总路径
                                        method,
                                        // 请求文件参数
                                        filemap,
                                        // 请求参数
                                        map,
                                        // 请求编码
                                        SanmiConfig.ENCODING);


                    }
                } catch (HttpException e) {
                    return null;
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                // 关闭对话框
                WaitingDialogControll.dismissWaitingDialog();
                // 对服务端返回的结果进行判断处理，区分服务器连接错误以及业务错误
                // ①：服务器连接错误
                if (result == null) {
                    ToastUtil.showToast(context,
                            MessageConstant.IO_EXCEPTION.getMsgcontent() + "\n当前版本号:" + VersionUtils.getVerName(context));
                    LogUploadUtil.upload("request:" + (method == null ? "" : method.toString()) + (filemap == null ? "" : filemap.toString()) + (map == null ? "" : map.toString()) + "\nresult:" + result);
                    handler.callBackForServerFailed(result);
                    // ②：业务错误
                } else {
                    try {
                        BaseResponseBean rep = JsonUtility.fromJson(JsonUtility.getElement(result, "meta"), BaseResponseBean.class);
                        if (!rep.getCode().equals(STATUS_SUCCESS)) {
                            //400  UserNotFound、SessionError 清空本地用户数据缓存
                            if (rep.getCode().equals(STATUS_CLIENT_ERROR)) {
                                if (rep.getMessage().equals("UserNotFound")) {
                                    SharedPreferencesUtil.save(context, ProjectConstant.APP_USER_MID, "");
                                    SharedPreferencesUtil.save(context, ProjectConstant.APP_USER_SESSIONID, "");
                                    if (mHandler != null) {
                                        TextViewDialog mDialog = new TextViewDialog(context, "用户不存在，是否注册？", mHandler);
                                        mDialog.setGotoActivity("RegisterActivity");
                                        mDialog.showDialog();
                                    } else {
                                        ToastUtil.showToast(context, context.getString(R.string.login_error) + getVersion());
                                    }
                                } else if (rep.getMessage().equals("SessionError")) {
                                    SharedPreferencesUtil.save(context, ProjectConstant.APP_USER_MID, "");
                                    SharedPreferencesUtil.save(context, ProjectConstant.APP_USER_SESSIONID, "");
                                    if (mHandler != null) {
                                        //如果页面没进行处理
                                        if (handler.callBackSessionError() == false) {
                                            TextViewDialog mDialog = new TextViewDialog(context, "用户信息过期，是否重新登录？", mHandler);
                                            mDialog.setGotoActivity("LoginActivity");
                                            mDialog.showDialog();
                                        }
                                    } else {
                                        ToastUtil.showToast(context, context.getString(R.string.login_error) + getVersion());
                                    }
                                } else if (rep.getMessage().equals("InventoryLackError")) {
                                    handler.callBackLackError(rep.getLacks());
                                } else if (rep.getMessage().equals("VerifyCodeError")) {
                                    ToastUtil.showToast(context, context.getString(R.string.verifycode_not_correct) + getVersion());
                                } else if (rep.getMessage().equals("PasswdError")) {
                                    ToastUtil.showToast(context, context.getString(R.string.pass_error) + getVersion());
                                }else if (rep.getMessage().equals("PasswordError")) {
                                    ToastUtil.showToast(context, context.getString(R.string.pass_error) + getVersion());
                                }
                                else if (rep.getMessage().equals("RequestTooFrequently")) {
                                    ToastUtil.showToast(context, context.getString(R.string.request_too_frequently) + getVersion());
                                } else if (rep.getMessage().equals("OrderIdExpireError")) {
                                    ToastUtil.showToast(context, context.getString(R.string.order_id_expire_error) + getVersion());
                                } else if (rep.getMessage().equals("CodeNotExistException")) {
                                    ToastUtil.showToast(context, context.getString(R.string.bind_code_error) + getVersion());
                                } else if (rep.getMessage().equals("BadRequest")) {
                                    if (rep.getDescription().indexOf("parameter phone %s invalid") != -1) {
                                        ToastUtil.showToast(context, context.getString(R.string.phone_invalid) + getVersion());
                                    } else if (rep.getDescription().indexOf("user not cshop") != -1) {
                                        ToastUtil.showLongToast(context, "此二维码不是正常的合伙人二维码,请手动输入正确的合伙人推荐码" + getVersion());
                                        handler.callBackNotHehuoren();
                                    } else {
                                        ToastUtil.showToast(context, rep.getDescription());
                                    }
                                } else if (rep.getMessage().equals("NotServicedArea")) {
                                    ToastUtil.showToast(context, "您的地址不在服务区域" + getVersion());
                                    handler.callBackForNotServicedArea();
                                } else if (rep.getMessage().equals("HasUnpaidOrder")) {
                                    ToastUtil.showToast(context, "您有未支付的订单,请先取消或者支付再下新的订单!");
                                } else if (rep.getMessage().equals("AddressIllegal")) {//提交订单时收获地址不存在或格式不正确
                                    handler.callBackAddressError();
                                } else if(rep.getMessage().equals("StaffError")){
                                    ToastUtil.showToast(context, rep.getDescription());
                                    handler.callBackStaffError();
                                }else {
                                    ToastUtil.showToast(context, rep.getDescription());
                                }


                            }
                            if (rep.getCode().equals(STATUS_SERVER_ERROR)) {
                                ToastUtil.showToast(context, context.getString(R.string.server_error) + getVersion());
                            }
//							ToastUtil.showToast(context, rep.getMessage());
                            handler.callBackForGetDataFailed(result);
                        } else {
                            String data = JsonUtility.getElement(result, "data") != null ?
                                    JsonUtility.getElement(result, "data").toString() : "";
                            handler.callBackForServerSuccess(data);
                        }
                    } catch (Exception e) {
                        LogUploadUtil.upload("request:" + (method == null ? "" : method.toString()) + (filemap == null ? "" : filemap.toString()) + (map == null ? "" : map.toString()) + "\nresult:" + result);
                        ToastUtil.showToast(
                                context,
                                MessageConstant.DATAPARSE_EXCEPTION.getMsgcontent() + getVersion());
                    }
                }
            }
//        }.execute();
        }.executeOnExecutor(HttpUtil.mPool);
    }
    public String getVersion(){
        return "\n当前版本号:" + VersionUtils.getVerName(context);
    }

    /**
     * TODO:请求回调接口
     * 该接口可根据后台业务返回code进行定义相应的处理方法
     */
    public static abstract class ResultHandler {
        /**
         * 服务器处理成功：获得数据成功
         */
        public abstract void callBackForServerSuccess(String result);

        /**
         * 服务器处理成功：获得数据失败
         */
        protected void callBackForGetDataFailed(String result) {

        }


        /**
         * 服务器处理失败
         */
        public void callBackForServerFailed(String result) {
        }

        ;

        /**
         * 不在服务区域
         */
        public void callBackForNotServicedArea() {
        }

        /**
         * 处理sessionerror 的
         */

        public boolean callBackSessionError() {
            return false;
        }

        /**
         * 处理库存缺少 的
         */

        public void callBackLackError(Map<String, Integer> lacks) {
        }

        /**
         * 处理二维码不是合伙人二维码的情况
         */

        public void callBackNotHehuoren() {
        }

        /**
         * 收获地址不存在或格式不正确
         */
        public void callBackAddressError() {
        }
        /**
         * 添加员工时不存在该用户
         */
        public void callBackStaffError() {
        }

    }
}
