package com.hclz.client.forcshop.qrcode;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hclz.client.base.util.SharedPreferencesUtil;
import com.google.gson.JsonObject;
import com.hclz.client.R;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.SaveImageUtils;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

/**
 * Created by handsome on 16/7/11.
 */
public class MyQrCodeActivity extends BaseActivity {

    TextView tv_cshop_title ,tv_tuijianma;
    ImageView iv_cshop;
    String cshop_title,cshop_code;

    /**
     * 页面跳转,不需要穿参数
     * @param from
     */
    public static void startMe(Context from){
        Intent intent = new Intent(from, MyQrCodeActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_qrcode);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        tv_cshop_title = (TextView) findViewById(R.id.tv_cshop_title);
        tv_tuijianma = (TextView) findViewById(R.id.tv_tuijianma);
        iv_cshop = (ImageView) findViewById(R.id.iv_cshop);
        setCommonTitle("推荐新用户");
    }

    @Override
    protected void initInstance() {

    }

    @Override
    protected void initData() {
        String cshop = SharedPreferencesUtil.get(mContext,"cshop");
        if (!TextUtils.isEmpty(cshop)){
            JsonObject obj = JsonUtility.parse(cshop);
            cshop_title = JsonUtility.fromJson(
                    obj.get("title"),
                    String.class);
            cshop_code = JsonUtility.fromJson(obj.get("code"),String.class);
        }
        showContent();
    }

    private void showContent(){

        tv_tuijianma.setText(cshop_code);
        tv_cshop_title.setText(TextUtils.isEmpty(cshop_title)?"":cshop_title);

//        Bitmap cshopLogo = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_cshop_qr);
        Bitmap cshopBitmap = EncodingUtils.createQRCode(cshop_code,200,200,null);
        if (cshopBitmap == null){
            iv_cshop.setImageResource(R.mipmap.perm_pic);
        } else {
            iv_cshop.setImageBitmap(cshopBitmap);
        }
    }

    @Override
    protected void initListener() {
        iv_cshop.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setItems(new String[]{getResources().getString(R.string.save_picture)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        iv_cshop.setDrawingCacheEnabled(true);
                        Bitmap imageBitmap = iv_cshop.getDrawingCache();
                        if (imageBitmap != null) {
                            new SaveImageUtils(mContext, iv_cshop,cshop_title).execute(imageBitmap);
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
    }
}
