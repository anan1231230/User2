package com.hclz.client.base.util;
  
import android.app.Activity;  
import android.graphics.Bitmap;  
import android.os.AsyncTask;  
import android.os.Environment;  
import android.widget.ImageView;  
import android.widget.Toast;

import com.hclz.client.R;

import java.io.File;
import java.io.FileOutputStream;  
  
/** 
 * Created by y on 2016/1/14. 
 */  
public class SaveImageUtils extends AsyncTask<Bitmap, Void, String> {  
    Activity mActivity;  
    ImageView mImageView;
    String mName;
  
    public SaveImageUtils(Activity activity, ImageView imageView,String name) {
        this.mImageView = imageView;  
        this.mActivity = activity;
        this.mName = name;
    }  
  
    @Override  
    protected String doInBackground(Bitmap... params) {  
        String result = mActivity.getResources().getString(R.string.save_picture_failed);
        try {  
            String sdcard = Environment.getExternalStorageDirectory().toString();  
            File file = new File(sdcard + "/hclz");
            if (!file.exists()) {  
                file.mkdirs();  
            }  
            File imageFile = new File(file.getAbsolutePath(), mName +".jpg");
            FileOutputStream outStream = null;  
            outStream = new FileOutputStream(imageFile);  
            Bitmap image = params[0];  
            image.compress(Bitmap.CompressFormat.JPEG, 100, outStream);  
            outStream.flush();  
            outStream.close();  
            result = mActivity.getResources().getString(R.string.save_picture_success, file.getAbsolutePath());
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return result;  
    }  
  
    @Override  
    protected void onPostExecute(String result) {  
        Toast.makeText(mActivity, result, Toast.LENGTH_LONG).show();
        mImageView.setDrawingCacheEnabled(false);  
    }  
}  