package com.hclz.client.faxian;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.handler.WeakHandler;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.BitmapUtil;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.base.util.WindowSizeUtil;
import com.hclz.client.base.ver.VersionUtils;
import com.hclz.client.base.view.ShareDialog;
import com.hclz.client.base.view.WaitingDialogControll;
import com.hclz.client.base.wxapi.Util;
import com.hclz.client.faxian.adapter.SubProductAdapter;
import com.hclz.client.faxian.bean.Product;
import com.hclz.client.faxian.bean.ProductBean;
import com.hclz.client.shouye.newcart.DiandiCart;
import com.hclz.client.shouye.newcart.DiandiCartItem;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by handsome on 16/4/29.
 */
public class ProductDetailActivity extends BaseActivity {

    private ArrayList<String> mProductPics;
    private String productTitle, mProductPid;
    private WebView wv_product_detail;

    private ImageView ivCart;// 购物车
    private ImageView ivShare, ivPicInvisible;
    private int num = 0;
    private ProductBean.ProductsBean laidianProduct;
    private LinearLayout ll_showorder_layout;
    //TODO 会有一个类似的按钮，注意搜索即可
//    private ImageView addCartIv;
    private String url, title, content, picUrl;
    private TextView mTitleTv;
    private ImageView mBackIv;
    private ImageView mDorIv;
    private TextView tv_price;
    private TextView tv_add_cart;
    private TextView tv_cart;

    //sub产品
    private RecyclerView rv_sub_products;
    private LinearLayoutManager mManager;
    private SubProductAdapter mAdapter;

    private WeakHandler zujiHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String productsString = SharedPreferencesUtil.get(mContext, ProjectConstant.APP_ZUJIS);
            ArrayList<ProductBean.ProductsBean> products;
            if (TextUtils.isEmpty(productsString)) {
                products = new ArrayList<>();
            } else {
                products = JsonUtility.fromJson(JsonUtility.parseArray(productsString),new TypeToken<ArrayList<ProductBean.ProductsBean>>(){});
            }
            for(int i=0;i<products.size();i++){
                if(laidianProduct.pid.equals(products.get(i).pid)){
                    products.remove(i);
                }
            }
            products.add(0,laidianProduct);
//
//            if (!products.contains(laidianProduct)) {
//                products.add(0,laidianProduct);
//            } else {
//                products.remove(laidianProduct);
//                products.add(0,laidianProduct);
//            }
            if (products.size() > 50){
                products.remove(products.get(products.size() - 1));
            }

            Gson gson = new Gson();
            String  zujiString = gson.toJson(products);
            SharedPreferencesUtil.save(mContext,ProjectConstant.APP_ZUJIS,zujiString);
            return true;
        }
    });


    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from, ProductBean.ProductsBean product) {
        Intent intent = new Intent(from, ProductDetailActivity.class);
        intent.putExtra("product", product);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_product_detail);
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        zujiHandler.sendEmptyMessageDelayed(1000,20 * 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showNumAndPrice();
        WaitingDialogControll.dismissWaitingDialog();
    }

    @Override
    protected void onPause() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            wv_product_detail.onPause(); // 暂停网页中正在播放的视频
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            wv_product_detail.onPause(); // 暂停网页中正在播放的视频
        }
        super.onStop();
        zujiHandler.removeMessages(1000);
    }

    @Override
    protected void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            wv_product_detail.onPause(); // 暂停网页中正在播放的视频
        }
        super.onDestroy();
        zujiHandler.removeMessages(1000);
    }

    @Override
    protected void initView() {

        mTitleTv = (TextView) findViewById(R.id.tv_title);
        mBackIv = (ImageView) findViewById(R.id.iv_back);

        wv_product_detail = (WebView) findViewById(R.id.wv_product_detail);
        wv_product_detail.getSettings().setJavaScriptEnabled(true);

        mDorIv = (ImageView) findViewById(R.id.notification_dot_iv);

        ivCart = (ImageView) findViewById(R.id.iv_cart);

//        addCartIv = (ImageView) findViewById(R.id.centerImage);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        ivPicInvisible = (ImageView) findViewById(R.id.pic_invisible);

        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_add_cart = (TextView) findViewById(R.id.tv_add_cart);

        tv_cart = (TextView) findViewById(R.id.tv_cart);

        ll_showorder_layout = (LinearLayout) findViewById(R.id.ll_showorder_layout);

        rv_sub_products = (RecyclerView) findViewById(R.id.rv_sub_products);
    }

    /**
     * 购物袋显示有货物
     */
    private void showNumAndPrice() {

        if (mDorIv == null) {
            return;
        }
        if (!DiandiCart.getInstance().isEmpty()) {
            mDorIv.setVisibility(View.VISIBLE);
        } else {
            mDorIv.setVisibility(View.GONE);
        }

        if (DiandiCart.getInstance().contains(mProductPid)) {
            num = DiandiCart.getInstance().get(mProductPid).num;
        } else {
            num = 0;
        }
        if (num >= laidianProduct.inventory && laidianProduct.is_bookable == 0) {
            ToastUtil.showToastCenter(mContext, "产品已被抢购一空,请选择其他商品继续抢购");
//            addCartIv.setClickable(false);
        } else {
//            addCartIv.setClickable(true);
        }
    }

    @Override
    protected void initInstance() {
        configMap = HclzApplication.getData();
        mManager = new LinearLayoutManager(mContext);
        mManager.setAutoMeasureEnabled(true);
        mAdapter = new SubProductAdapter(mContext, new SubProductAdapter.SubproductListener() {
            @Override
            public void onAddCart(int pos, ProductBean.ProductsBean item) {
                showNumAndPrice();
            }
        });
        rv_sub_products.setLayoutManager(mManager);
        rv_sub_products.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        if (mIntent != null) {
            laidianProduct = (ProductBean.ProductsBean) mIntent.getSerializableExtra("product");
            mProductPics = (ArrayList<String>) laidianProduct.albums;
            if (mProductPics == null) {
                mProductPics = new ArrayList<>();
            }

            if (laidianProduct.virtual_goods != null && laidianProduct.virtual_goods.real_goods != null && laidianProduct.virtual_goods.real_goods.size() > 0) {
                mAdapter.setData((ArrayList<ProductBean.ProductsBean>) laidianProduct.virtual_goods.real_goods);
            }

            if (laidianProduct.virtual_goods != null) {
                ll_showorder_layout.setVisibility(View.GONE);
            } else {
                ll_showorder_layout.setVisibility(View.VISIBLE);
            }

            productTitle = laidianProduct.name;
            title = "好吃懒做";
            mProductPid = laidianProduct.pid;
            picUrl = laidianProduct.album_thumbnail;
            content = laidianProduct.name + "\n惊爆价:" + CommonUtil.getMoney(laidianProduct.normal_detail.price) + "\n下载App,立即购买";
            ImageUtility.getInstance(mContext).showImage(picUrl, ivPicInvisible);
            url = configMap.get("share_webview") + mProductPid;
            showContent();
            showNumAndPrice();
        }
    }

    private void showContent() {
        mTitleTv.setText(productTitle);
        StringBuilder sb = new StringBuilder();
        if (laidianProduct.properties == null || laidianProduct.properties.size() == 0) {
            sb.append("<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><meta content=\"width=device-width,height=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\" name=\"viewport\"></head><body> <style>*{margin:0;padding:0;}img{display:block;}</style>\n");
            String[] picFormats = {"jpeg", "png", "gif", "jpg"};
            for (String pic : mProductPics) {
                String picTrim = pic.trim().toLowerCase();
                boolean isPic = false;
                for (String picFormat : picFormats) {
                    if (picTrim.endsWith(picFormat)) {
                        isPic = true;
                        break;
                    }
                }
                if (isPic) {
                    sb.append("<img width=100% src=\"" + pic + "\" frameborder=0 allowfullscreen />\n");
                } else {
                    sb.append("<div><iframe height=\"" + WindowSizeUtil.getDpWidth(this) * 5 / 9 + "\" width=\"" + WindowSizeUtil.getDpWidth(this) + "\" src=\"" + pic + "\"frameborder=0 allowfullscreen></iframe></div>\n");
//                    sb.append("<iframe frameborder=\"0\" width=\"320\" height=\"249\" src=\"https://v.qq.com/iframe/player.html?vid=g0358e1tqi0&tiny=0&auto=0\" allowfullscreen></iframe>");
                }
            }
            sb.append("</body></html>");
        } else {
            sb.append("<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><meta content=\"width=device-width,height=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\" name=\"viewport\"></head><body> <style>*{margin:0;padding:0;}img{display:block;}</style>\n");
            String[] picFormats = {"jpeg", "png", "gif", "jpg"};
            for (String pic : mProductPics) {
                String picTrim = pic.trim().toLowerCase();
                boolean isPic = false;
                for (String picFormat : picFormats) {
                    if (picTrim.endsWith(picFormat)) {
                        isPic = true;
                        break;
                    }
                }
                if (isPic) {
                    sb.append("<img width=100% src=\"" + pic + "\" frameborder=0 allowfullscreen />\n");
                } else {
                    sb.append("<div><iframe height=\"" + WindowSizeUtil.getDpWidth(this) * 5 / 9 + "\" width=\"" + WindowSizeUtil.getDpWidth(this) + "\" src=\"" + pic + "\"frameborder=0 allowfullscreen></iframe></div>\n");
                }
            }
            sb.append("<br><div align=\"left\"><table border=\"0\" align=\"left\" cellspacing=\"15\" cellpadding=\"15\"><tr>");
            for (List<String> item : laidianProduct.properties) {
                sb.append("<th align=\"left\"> <font color=\"#8B6969\">");
                sb.append(item.get(0) + ":");
                sb.append("</font></th><th align=\"left\"><font color=\"#8B6969\">");
                sb.append(item.get(1));
                sb.append("</font></th></tr>");
            }
            sb.append("</table></div><br><br></body></html>");
        }
        wv_product_detail.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
        wv_product_detail.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        tv_price.setText("¥" + CommonUtil.getMoney(laidianProduct.price));
    }

    @Override
    protected void initListener() {
        wv_product_detail.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.reload();
                return true;
            }
        });
//        addCartIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                add(v);
//            }
//        });
//        ivShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            /*
//             * 图片分享 it.setType("image/png"); 　//添加图片 File f = new
//             * File(Environment.getExternalStorageDirectory()+"/name.png");
//             *
//             * Uri uri = Uri.fromFile(f); intent.putExtra(Intent.EXTRA_STREAM,
//             * uri); 　
//             */
//                ivPicInvisible.buildDrawingCache();
//                Bitmap bitmap = ivPicInvisible.getDrawingCache();
//                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
//                intent.putExtra(Intent.EXTRA_TEXT, title + "  " + content.replaceAll("\\n", " ") + url);
//                intent.putExtra(Intent.EXTRA_STREAM, uri);
//                intent.putExtra("Kdescription", title + "  " + content.replaceAll("\\n", " ") + url);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mContext.startActivity(Intent.createChooser(intent, getTitle()));
//            }
//        });
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartActivity.startMe(mContext);
            }
        });
        tv_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(v);
            }
        });
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartActivity.startMe(mContext);
            }
        });
    }

    /**
     * 分享方法实现
     */
    private void wxShare(final int reqType, final String url, final String title, final String content, final String picUrl) {
        //检测是否有微信并且微信是否支持该版本
        boolean sIsWXAppInstalledAndSupported = api.isWXAppInstalled() && api.isWXAppSupportAPI();
        if (!sIsWXAppInstalledAndSupported) {
            ToastUtil.showToastCenter(mContext, "您的手机尚未安装微信或微信版本不支持分享功能");
            return;
        }
//        showShareWaitingDialog();
        ImageUtility.getInstance(mContext).showImage(picUrl, ivPicInvisible);

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = content;

//        Bitmap thumbBmp = ((BitmapDrawable) ivPicInvisible.getDrawable()).getBitmap();
        ivPicInvisible.buildDrawingCache();
        Bitmap bitmap = ivPicInvisible.getDrawingCache();

//        Bitmap bitmap = BitmapUtil.comp(thumbBmp);

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        if (bitmap != null && baos.toByteArray().length / 1024 <= 10) {
//            msg.thumbData = Util.bmpToByteArray(bitmap, true);
//        } else {
//            ToastUtil.showLongToast(mContext, "图片过大,分享失败");
//        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        if (reqType == 0) {
            req.scene = SendMessageToWX.Req.WXSceneSession;//分享到好友
        } else {
            msg.title = title + "  " + content.replaceAll("\\n", " ");
            req.scene = SendMessageToWX.Req.WXSceneTimeline;//分享到朋友圈
        }
        req.message = msg;
        boolean isSend = api.sendReq(req);
        if (!isSend) {
            ToastUtil.showToastCenter(mContext, "打开微信失败,原因:分享过于频繁或者您的微信不是最新版本" + VersionUtils.getVerName(mContext));
            WaitingDialogControll.dismissWaitingDialog();
        }

    }

    private void showShareWaitingDialog() {
        WaitingDialogControll.showWaitingDialog(mContext);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private void add(View v) {
        Integer num = 0;
        if (DiandiCart.getInstance().contains(mProductPid)) {
            DiandiCartItem cartItem = DiandiCart.getInstance().get(mProductPid);
            num = cartItem.num;
        }
        int previous_num = num;
        if (num < laidianProduct.minimal_quantity) {
            num = laidianProduct.minimal_quantity;
        } else {
            num = num + laidianProduct.minimal_plus;
        }

        if (num > laidianProduct.inventory && laidianProduct.is_bookable == 0) {
            ToastUtil.showToastCenter(mContext, "商品已被抢购一空,请选购其他商品");
            return;
        }
        DiandiCartItem newItem = new DiandiCartItem(laidianProduct, num);
        DiandiCart.getInstance().put(newItem, mContext);
        showNumAndPrice();
        ToastUtil.showToastCenter(mContext, "已加入购物袋");
    }
    private void showShare(){
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("分享自好吃懒做");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(productTitle);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(picUrl);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);

// 启动分享GUI
        oks.show(this);
    }
}
