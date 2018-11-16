package com.hclz.client.base.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * 共通工具类
 *
 * @author jia-changyu
 */
public class Utility {

    public static boolean isInternetAvailable(Context context) {
        try {
            ConnectivityManager manger = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manger.getActiveNetworkInfo();
            return (info != null && info.isConnected());
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * 此方法是用来检测电话号码是否合法 如果合法，返回true 如果不合法，返回false
     */
    public static boolean isPhoneNumber(String PhoneNumber) {

        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(PhoneNumber);
        return m.matches();
    }

    /**
     * MD5加密。
     *
     * @param str 字符串
     * @return MD5加密后字符串
     */
    public static String encryptMd5(String str) {
        try {
            // 加密
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            md5Digest.update(str.getBytes("utf-8"));
            byte[] bytes = md5Digest.digest();
            // 转换成16进制
            String hexString = toHexString(bytes);
            return hexString;
        } catch (Exception e) {
            return null;
        }
    }

    /***
     * 打电话
     *
     * @param phone
     */
    public static void makePhone(Context context, String phone) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ToastUtil.showToast(context,"电话权限已被关闭,请手动打开以后再拨打电话");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    /**
     * 格式化金额。
     *
     * @param money 金额
     * @return 格式化的金额
     */
    public static String formatMoneyString(String money) {

        String moneyFormatted = money;

        if (money != null && !"".equals(money.trim())) {
            Double moneyDouble = Double.parseDouble(money);
            moneyFormatted = new DecimalFormat("¥#0.00").format(moneyDouble);
        }

        return moneyFormatted;
    }

    /**
     * 获取资源图片
     *
     * @param id 资源图片id
     * @return
     */
    public static Drawable getResImage(Context context, int id) {
        Drawable image = context.getResources().getDrawable(id);
        return image;
    }

    /**
     * 设置可用属性。
     *
     * @param v       View
     * @param enabled 可用属性
     */
    public static void setEnabled(View v, boolean enabled) {

        if (v.isEnabled() != enabled) {
            v.setEnabled(enabled);
        }
    }

    /**
     * 格式化日期。
     *
     * @param format 格式
     * @param date   日期
     * @return 显示用格式的日期
     */
    public static String formatDate(String format, Date date) {
        String formattedDate = new SimpleDateFormat(format).format(date);
        return formattedDate;
    }

    /**
     * 格式化时间
     *
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     * @return
     */
    public static String formatDateTimeDisp(int year, int month, int date,
                                            int hour, int minute) {

        String formattedDateTime = formatString("%04d-%02d-%02d %02d:%02d",
                year, month, date, hour, minute);
        return formattedDateTime;
    }

    /**
     * 格式化字符串
     *
     * @param format 格式
     * @param args   内容
     * @return 格式化的字符串
     */
    private static String formatString(String format, Object... args) {

        String formattedString = String.format(format, args);
        return formattedString;
    }

    /**
     * 解析日期。
     *
     * @param format  格式
     * @param dateStr 日期字符串
     * @return 日期
     */
    public static Date parseDate(String format, String dateStr) {

        Date date;
        try {
            date = new SimpleDateFormat(format).parse(dateStr);
        } catch (ParseException e) {
            date = null;
        }

        return date;
    }

    /**
     * 格式化数量。
     *
     * @param count 数量
     * @return 格式化的数量
     */
    public static String formatCount(int count) {

        String countFormatted = new DecimalFormat("#,##0").format(count);
        return countFormatted;
    }

    /**
     * 解析数量。
     *
     * @param countStr 数量字符串
     * @return 数量
     */
    public static int parseCount(String countStr) {

        int count = Integer.parseInt(countStr.replaceAll(",", ""));
        return count;
    }

    /**
     * 格式化金额。
     *
     * @param money 金额
     * @return 格式化的金额
     */
    public static String formatMoney(BigDecimal money) {

        String moneyFormatted = new DecimalFormat("￥#0.00").format(money);
        return moneyFormatted;
    }

    /**
     * 格式化金额。
     *
     * @param money 金额
     * @return 格式化的金额
     */
    public static String formatMoney(double money) {

        String moneyFormatted = new DecimalFormat("￥#0.00").format(money);
        return moneyFormatted;
    }

    /**
     * 解析金额。
     *
     * @param moneyStr 金额字符串
     * @return 金额
     */
    public static double parseMoney(String moneyStr) {

        double money = Double.parseDouble(moneyStr.substring(1));
        return money;
    }

    /**
     * 格式化信息。
     *
     * @param format
     *            格式
     * @param items
     *            填充内容
     * @return 格式化的信息
     */
//	public static String formatMessage(String format, String... items) {
//
//		String messageFormatted = MessageFormat.format(format, items);
//		return messageFormatted;
//	}

    /**
     * 判断是否为手机号。 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
     *
     * @param phoneNO 手机号
     * @return 是否为手机号
     */
    public static boolean isPhoneNO(String phoneNO) {

        boolean isPhoneNO = phoneNO
//                .matches("^1[3|4|5|7|8][0-9]\\d{8}$");
                .matches("^\\d{11}$");
        return isPhoneNO;
    }

    /**
     * 将字符串转换成16进制字符串。
     *
     * @param bytes 加密后bytes数组
     * @return 16进制字符串
     */
    private static String toHexString(byte[] bytes) {

        StringBuilder hexBuilder = new StringBuilder();
        for (byte b : bytes) {
            // 转换为16进制
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hexBuilder.append("0");
            }
            hexBuilder.append(hex);
        }

        // 转换为大写
        String hexString = hexBuilder.toString().toUpperCase();
        return hexString;
    }

    /**
     * 校验密码安全等级。
     *
     * @return 0：密码为空，1：密码长度小于6,2：输入了不符合规则的字符,3：密码安全等级为低,4:正常密码
     */
    public static int checkPassLevel(String pass) {

        // 空
        if (pass.isEmpty()) {
            return 0;
        }

        // 不足6位
        if (pass.length() < 6) {
            return 1;
        }

        // 格式
        String reg = "[\\da-zA-Z@_]+";
        if (!pass.matches(reg)) {
            return 2;
        }

        // 只包含一种
        String regLowNumber = "[\\d]+";
        String regLowChar = "[a-zA-Z]+";
        String regLow = "[@_]+";
        if (pass.matches(regLowNumber) || pass.matches(regLowChar)
                || pass.matches(regLow)) {
            return 3;
        }

        return 4;
    }

    /**
     * 验证车牌号码是否正确
     *
     * @return 1：输入为空 2:车牌号必须为6位 3：车牌首位必须是大写字母 4：第2～6位必须是大写英数字 0：正常值
     */
    public static int checkCarNumber(String carNumber) {
        // 车牌号码可包含的字符
        String regChar = "[A-Z]+";
        String regRight = "[\\dA-Z]+";

        if (carNumber == null || "".equals(carNumber)) {
            return 1;
        }
        if (6 != carNumber.length()) {
            return 2;
        }
        if (!carNumber.substring(0, 1).matches(regChar)) {
            return 3;
        }
        if (!carNumber.substring(1, 6).matches(regRight)) {
            return 4;
        }

        return 0;
    }

    /***
     * 获取GridView 每一项宽度
     *
     * @param context  关联
     * @param margId   左右间距
     * @param margItem 项目间距
     * @param Column   列数
     * @return 列宽
     */
    public static int getItemWidth(Context context, int margId, int margItem,
                                   int Column) {
        // 获取左右边距
        int marg;
        if (margId == -1) {
            marg = 0;
        } else {
            marg = context.getResources().getDimensionPixelSize(margId);
        }
        int margm;
        // 获取项目间距离
        if (margItem == -1) {
            margm = 0;
        } else {
            margm = context.getResources().getDimensionPixelSize(margItem);
        }
        // 获取屏幕宽度
        int wWidth = WindowSizeUtil.getWidth(context);
        // 获取项目总宽度
        int aWidth = wWidth - marg * 2 - margm * (Column - 1);
        // 获取每一项宽度
        int cWidth = aWidth / Column;
        return cWidth;

    }

    /***
     * 获取屏幕中的大小
     *
     * @param context 关联
     * @param sizeId  大小的id
     * @return 大小
     */
    public static int getPixelSize(Context context, int sizeId) {
        int size = 0;
        size = context.getResources().getDimensionPixelSize(sizeId);
        return size;
    }

    /***
     * 将sp转换为像素
     *
     * @param context 关联
     * @param spId    spid
     * @return
     */
    public static int getSpAsPx(Context context, int spId) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (context.getResources().getDimension(spId) / fontScale + 0.5f);
    }

    /***
     * 将dp转换为px
     *
     * @param context 关联
     * @param dpId    dpid
     * @return
     */
    public static int getDpAsPx(Context context, int dpId) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (context.getResources().getDimension(dpId) * scale + 0.5f);
    }

    /***
     * 生成设备唯一标识码
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /***
     * 给TextView 设置下划线
     *
     * @param textView
     */
    public static void setTextViewUnderline(TextView textView) {
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
        textView.getPaint().setAntiAlias(true);// 抗锯齿
    }

    /***
     * 设置EditText最大字符数量
     *
     * @param edInput 输入框
     * @param num     输入数目
     */
    public static void setInputCount(EditText edInput, int num) {
        edInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(num)});
    }
}
