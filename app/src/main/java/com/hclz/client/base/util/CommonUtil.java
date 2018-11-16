package com.hclz.client.base.util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;

import com.hclz.client.base.constant.ProjectConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Closeable;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/***
 * 共通工具类
 *
 * @author Administrator
 */
public class CommonUtil {
    private final static String TAG = CommonUtil.class.getSimpleName();

    /*
     * 测试网络连接
     */
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

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return true如果该字符串为null或者"",否则false
     */
    public static final boolean isNull(String str) {
        return str == null || "".equals(str.trim());
    }

    /**
     * 获取屏幕密度规格
     *
     * @param context
     * @return
     */
    public static final String getDpi(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        String dpi = null;
        if (density == 0.75f)
            dpi = "ldpi";
        else if (density == 1.0f)
            dpi = "mdpi";
        else if (density == 1.5f)
            dpi = "hdpi";
        else if (density == 2f)
            dpi = "xhdpi";
        else if (density == 3f)
            dpi = "xxhdpi";
        return dpi;
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground(Context context) {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) context
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取APP版本
     *
     * @param context 环境
     * @return String
     */
    public static final int getAppVersionCode(Context context)
            throws NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
        return pi.versionCode;
    }

    /**
     * 获取APP版本
     *
     * @param context 环境
     * @return String
     */
    public static final String getAppVersionName(Context context)
            throws NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
        return pi.versionName;
    }

    /**
     * 用当前时间给文件命名
     *
     * @return String yyyyMMdd_HHmmss
     */
    public static final String getFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault());
        return dateFormat.format(date);// + ".jpg";
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
            moneyFormatted = new DecimalFormat("￥#0.00").format(moneyDouble);
        }

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
     * 格式化金额。
     *
     * @param money 金额
     * @return 格式化的金额
     */
    public static String formatMoneyTwo(double money) {

        String moneyFormatted = new DecimalFormat("#0.00").format(money);
        return moneyFormatted;
    }

    /**
     * 格式化money以分为单位的int值
     */
    public static String getMoney(long money) {
        String unit = (money % 10) + "";
        long tmp1 = money / 10;
        long ten = tmp1 % 10;
        long hundred = tmp1 / 10;
        StringBuilder sb = new StringBuilder();
        sb.append(hundred);
        sb.append(".");
        sb.append(ten);
        sb.append(unit);
        return sb.toString();
    }

    /**
     * 格式化money以分为单位的int值
     */
    public static String getMoneyForCommitOrder(long money) {
        long moneyAbs = Math.abs(money);
        String unit = (moneyAbs % 10) + "";
        long tmp1 = moneyAbs / 10;
        long ten = tmp1 % 10;
        long hundred = tmp1 / 10;
        StringBuilder sb = new StringBuilder();
        if (money >= 0) {
            sb.append("+¥");
        } else {
            sb.append("-¥");
        }
        sb.append(hundred);
        sb.append(".");
        sb.append(ten);
        sb.append(unit);
        return sb.toString();
    }

    /**
     * 格式化money以分为单位的int值
     */
    public static String getDistance(int distance) {
        if (distance <= 0) return "";
        DecimalFormat df = new DecimalFormat("######0.00");
        double newdistance = distance / 1000.00;
        return df.format(newdistance) + "km";
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
     * 将字符串解析成数量。
     *
     * @param countStr 数量字符串
     * @return 数量
     */
    public static int formatCountString(String countStr) {

        int count = Integer.parseInt(countStr.replaceAll(",", ""));
        return count;
    }

    /**
     * 转换时间显示形式
     *
     * @param time   时间字符串yyyy-MM-dd HH:mm:ss
     * @param format 格式
     * @return String
     */
    public static String transTime(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        try {
            Date date1 = sdf.parse(time);
            SimpleDateFormat dateFormat = new SimpleDateFormat(format,
                    Locale.getDefault());// "yyyy年MM月dd HH:mm"
            return dateFormat.format(date1);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取系统当前时间
     *
     * @param format 时间格式yyyy-MM-dd HH:mm:ss
     * @return String
     */
    public static String getCurrentTime(String format) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(format,
                Locale.getDefault());
        return dateFormat.format(date);
    }

    /**
     * 转换时间显示形式(与当前系统时间比较),在发表话题、帖子和评论时使用
     *
     * @param time 时间字符串
     * @return String
     */
    public static String transTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        String current = getCurrentTime("yyyy-MM-dd HH:mm:ss");
        String dian24 = transTime(current, "yyyy-MM-dd") + " 24:00:00";
        String dian00 = transTime(current, "yyyy-MM-dd") + " 00:00:00";
        Date now = null;
        Date date = null;
        Date d24 = null;
        Date d00 = null;
        try {
            now = sdf.parse(current); // 将当前时间转化为日期
            date = sdf.parse(time); // 将传入的时间参数转化为日期
            d24 = sdf.parse(dian24);
            d00 = sdf.parse(dian00);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = now.getTime() - date.getTime(); // 获取二者之间的时间差值
        long min = diff / (60 * 1000);
        if (min <= 5)
            return "刚刚";
        if (min < 60)
            return min + "分钟前";

        if (now.getTime() <= d24.getTime() && date.getTime() >= d00.getTime())
            return "今天" + transTime(time, "HH:mm");

        int sendYear = Integer.valueOf(transTime(time, "yyyy"));
        int nowYear = Integer.valueOf(transTime(current, "yyyy"));
        if (sendYear < nowYear)
            return transTime(time, "yyyy-MM-dd HH:mm");
        else
            return transTime(time, "MM-dd HH:mm");
    }

    /**
     * 转换时间显示形式(与当前系统时间比较),在显示即时聊天的时间时使用
     *
     * @param time 时间字符串
     * @return String
     */
    public static String transTimeChat(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault());
            String current = getCurrentTime("yyyy-MM-dd HH:mm:ss");
            String dian24 = transTime(current, "yyyy-MM-dd") + " 24:00:00";
            String dian00 = transTime(current, "yyyy-MM-dd") + " 00:00:00";
            Date now = null;
            Date date = null;
            Date d24 = null;
            Date d00 = null;
            try {
                now = sdf.parse(current); // 将当前时间转化为日期
                date = sdf.parse(time); // 将传入的时间参数转化为日期
                d24 = sdf.parse(dian24);
                d00 = sdf.parse(dian00);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long diff = now.getTime() - date.getTime(); // 获取二者之间的时间差值
            long min = diff / (60 * 1000);
            if (min <= 5)
                return "刚刚";
            if (min < 60)
                return min + "分钟前";

            if (now.getTime() <= d24.getTime()
                    && date.getTime() >= d00.getTime())
                return "今天" + transTime(time, "HH:mm");

            int sendYear = Integer.valueOf(transTime(time, "yyyy"));
            int nowYear = Integer.valueOf(transTime(current, "yyyy"));
            if (sendYear < nowYear)
                return transTime(time, "yyyy-MM-dd HH:mm");
            else
                return transTime(time, "MM-dd HH:mm");
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 隐藏手机号和邮箱显示
     *
     * @param old     需要隐藏的手机号或邮箱
     * @param keytype 1手机2邮箱
     * @return
     */
    public static String hide(String old, String keytype) {
        try {
            if ("1".equals(keytype))
                return old.substring(0, 3) + "****" + old.substring(7, 11);
            else {
                StringBuilder sb = new StringBuilder();
                String[] s = old.split("@");
                int l = s[0].length();
                int z = l / 3;
                sb.append(s[0].substring(0, z));
                int y = l % 3;
                for (int i = 0; i < z + y; i++)
                    sb.append("*");
                sb.append(s[0].substring(z * 2 + y, l));
                sb.append("@");
                if (s[1] == null) {

                }
                sb.append(s[1]);
                return sb.toString();
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 方法说明：获取资源图片
     *
     * @param context 资源
     * @param id      资源图片id
     * @return
     */
    public static Drawable getResImage(Activity context, int id) {
        Drawable image = context.getResources().getDrawable(id);
        return image;
    }

    /**
     * 方法说明：打电话
     *
     * @param phone
     * @param context
     */
    public static void makePhone(String phone, Context context) {
        Intent intent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ToastUtil.showToast(context, "打电话权限为开启");
            return;
        }
        context.startActivity(intent);
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

    public static JSONObject getDeviceObj(Activity context) {
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        JSONObject devide = new JSONObject();
        try {
            devide.put(ProjectConstant.PLATFORM, "android");//'platform': 'ios/android/webapp',
//            devide.put("device_id", tm.getDeviceId());//'device_id': '', //设备唯一id，用于标识一个设备信息//'device_token': '', //设备访问token，通常用于给用户推送push弹出信息; //android参考gcm服务文档，ios参考苹果apn服务接口;
//            JSONArray location = new JSONArray();
//            location.put(SharedPreferencesUtil.get(context, ProjectConstant.APP_START_LONGITUDE));
//            location.put(SharedPreferencesUtil.get(context, ProjectConstant.APP_START_LATITUDE));
//            devide.put("location", location);//'location': []
//            devide.put("os_version", android.os.Build.VERSION.RELEASE);//'os_version': '', //操作系统版本
//            devide.put("timezone", TimeZone.getDefault().getOffset(System.currentTimeMillis()));//'timezone': '', //时区信息，偏离0时区的秒数，如东八区为28800=8*3600
//            Locale locale = context.getResources().getConfiguration().locale;
//            devide.put("language", locale.getCountry());//'locale': '', //地区信息，如zh_CN
//            devide.put("language", locale.getLanguage());//'language': '', //语言信息, 如cn
            if (TextUtils.isEmpty(SharedPreferencesUtil.get(context, "device_id"))) {
                UUID uuid = UUID.randomUUID();
                SharedPreferencesUtil.save(context, "device_id", uuid.toString());
            }
            devide.put("device_id", SharedPreferencesUtil.get(context, "device_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return devide;
    }

    public static String hmac(Mac mac, String key, String src) throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), mac.getAlgorithm());
        mac.init(signingKey);
        byte[] data = mac.doFinal(src.getBytes("utf-8"));
        String result = toHexString(data);
        return result;
    }

    /**
     * 关闭closeable， 忽略任何检查的异常。 <br />
     * <p/>
     * 不能用于 Android 中关闭Cursor、 CursorWrapper，虽然实现了closeable接口。具体原因不明 <br />
     *
     * @param closeables
     */
    public static void closeQuitely(Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable c : closeables) {
            if (c != null) {
                try {
                    c.close();
                } catch (RuntimeException rethrown) {
                    throw rethrown;
                } catch (Exception e) {
                    // 忽略
                }
            }
        }
    }

    /**
     * 比较App版本
     *
     * @return int
     */
    public static boolean isVersionBigger(String oldVersion, String newVersion) {
        String tmpOldStr = "";
        String tmpNewStr = "";
        if (oldVersion != null && oldVersion.length() > 0) {
            for (int i = 0; i < oldVersion.length(); i++) {
                String tmp = "" + oldVersion.charAt(i);
                if ((tmp).matches("[0-9]")) {
                    tmpOldStr += tmp;
                }
            }
        }
        if (newVersion != null && newVersion.length() > 0) {
            for (int i = 0; i < newVersion.length(); i++) {
                String tmp = "" + newVersion.charAt(i);
                if ((tmp).matches("[0-9]")) {
                    tmpNewStr += tmp;
                }
            }
        }
        if (TextUtils.isEmpty(tmpOldStr)) {
            tmpOldStr = "1000";
        }
        if (TextUtils.isEmpty(tmpNewStr)) {
            tmpNewStr = "1000";
        }

        int oldInt = Integer.parseInt(tmpOldStr);
        int newInt = Integer.parseInt(tmpNewStr);

        if (0 < oldInt && oldInt < 100) {
            oldInt = oldInt * 100;
        } else if (oldInt < 1000) {
            oldInt = oldInt * 10;
        }
        if (0 < newInt && newInt < 100) {
            newInt = newInt * 100;
        } else if (newInt < 1000) {
            newInt = newInt * 10;
        }

        if (newInt > oldInt) {
            return true;
        } else {
            return false;
        }

    }

    public static String formatDuring(int second) {
        long days = second / (60 * 60 * 24);
        long hours = (second % (60 * 60 * 24)) / (60 * 60);
        long minutes = (second % (60 * 60)) / (60);
        long seconds = (second % (60));

        StringBuilder sb = new StringBuilder();
        if (days != 0) {
            sb.append(days + "天");
        }
        if (days != 0 || hours != 0) {
            sb.append(hours + "时");
        }
        if (days != 0 || hours != 0 || minutes != 0) {
            sb.append(minutes + "分");
        }
        sb.append(seconds + "秒");
        return sb.toString();
    }

    /**
     * 比较App版本
     *
     * @return int
     */
    public static boolean isVersionBiggerOrEqual(String oldVersion, String minVersion) {
        String tmpOldStr = "";
        String tmpMinStr = "";
        if (oldVersion != null && oldVersion.length() > 0) {
            for (int i = 0; i < oldVersion.length(); i++) {
                String tmp = "" + oldVersion.charAt(i);
                if ((tmp).matches("[0-9]")) {
                    tmpOldStr += tmp;
                }
            }
        }

        if (minVersion != null && minVersion.length() > 0) {
            for (int i = 0; i < minVersion.length(); i++) {
                String tmp = "" + minVersion.charAt(i);
                if ((tmp).matches("[0-9]")) {
                    tmpMinStr += tmp;
                }
            }
        }
        if (TextUtils.isEmpty(tmpOldStr)) {
            tmpOldStr = "1000";
        }
        if (TextUtils.isEmpty(tmpMinStr)) {
            tmpMinStr = "1000";
        }
        int oldInt = Integer.parseInt(tmpOldStr);
        int minInt = Integer.parseInt(tmpMinStr);

        if (0 < oldInt && oldInt < 100) {
            oldInt = oldInt * 100;
        } else if (oldInt < 1000) {
            oldInt = oldInt * 10;
        }
        if (0 < minInt && minInt < 100) {
            minInt = minInt * 100;
        } else if (minInt < 1000) {
            minInt = minInt * 10;
        }

        if (oldInt >= minInt) {
            return true;
        } else {
            return false;
        }

    }

    //=========================距离计算公式 start==========================================
    //半径
    private static final double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;//圆周率π
    }

    //sqrt:求平方根   pow:计算幂   asin:角度的反正弦   sin:三角正弦值的角度    cos:角的三角余弦
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;//四舍五入
        return s;
    }
    //=========================距离计算公式 end=============================================


    //===============dp转px===================================================
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    //===============px转dp===================================================
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    //===============去掉换行符================================================
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
