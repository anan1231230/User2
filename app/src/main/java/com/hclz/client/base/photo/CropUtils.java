/*
 * Copyright 2014 CommonQ
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hclz.client.base.photo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;

public class CropUtils {

    public static final String TAG = "CropUtils";

    public static final int REQUEST_SELECT = 100;
    public static final int REQUEST_CAMERA = 101;
    public static final int REQUEST_CROP = 102;

    public static final String CROP_CACHE_FILE_NAME = "cache_file.jpg";
    public static final String CROP_CACHE_CAMERA_NAME = "camera_file.jpg";

    public static Uri buildUri() {
        return Uri.fromFile(Environment.getExternalStorageDirectory())
                .buildUpon().appendPath(CROP_CACHE_FILE_NAME).build();
    }

    public static Uri buildCameraUri() {
        return Uri.fromFile(Environment.getExternalStorageDirectory())
                .buildUpon().appendPath(CROP_CACHE_CAMERA_NAME).build();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void handleResult(CropInterface handler, int requestCode,
                                    int resultCode, Intent data) {
        if (handler == null)
            return;

        if (resultCode == Activity.RESULT_CANCELED) {
            handler.onImageCanceled();
        } else if (resultCode == Activity.RESULT_OK) {
            CropConfig cropConfig = handler.getCropConfig();
            if (cropConfig == null) {
                handler.onImageFailed("CropUtils' params MUST NOT be null!");
                return;
            }
            Intent intent;
            Activity context = handler.getContext();
            Fragment fragment = handler.getFragment();
            switch (requestCode) {
                case REQUEST_SELECT:
                    Uri uri = data.getData();
                    uri = Uri.fromFile(new File(JianrongUtil.getPath(context,uri)));

                    intent = buildCropPickIntent(uri,
                            "com.android.camera.action.CROP",
                            handler.getCropConfig());
                    if (fragment != null) {
                        fragment.startActivityForResult(intent, REQUEST_CROP);
                    } else if (context != null) {
                        context.startActivityForResult(intent, REQUEST_CROP);
                    } else {
                        handler.onImageFailed("CropHandler's context MUST NOT be null!");
                    }
                    break;
                case REQUEST_CAMERA:
                    intent = buildCropCameraIntent(
                            "com.android.camera.action.CROP",
                            handler.getCropConfig());
                    context = handler.getContext();
                    fragment = handler.getFragment();
                    if (context != null) {
                        context.startActivityForResult(intent, REQUEST_CROP);
                    } else if (fragment != null) {
                        fragment.startActivityForResult(intent, REQUEST_CROP);
                    } else {
                        handler.onImageFailed("CropHandler's context MUST NOT be null!");
                    }
                    break;

                case REQUEST_CROP:
                    Log.d(TAG, "Photo cropped successfully!");
                    handler.onImageCropped(buildCameraUri());
                    break;
            }
        }
    }

    public static boolean clearCachedCropFile(Uri uri) {
        if (uri == null)
            return false;

        File file = new File(uri.getPath());
        if (file.exists()) {
            boolean result = file.delete();
            if (result)
                Log.i(TAG, "Cached crop file cleared.");
            else
                Log.e(TAG, "Failed to clear cached crop file.");
            return result;
        } else {
            Log.w(TAG,
                    "Trying to clear cached crop file but it does not exist.");
        }
        return false;
    }

    public static Intent buildGetFromGalleryIntent(Uri uri) {
//        return new Intent(Intent.ACTION_GET_CONTENT, null)
//                .setDataAndType(uri, CropConfig.CROP_TYPE);
        Intent intent;
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
        } else {
            intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
        }
            return intent;
        }

    public static Intent buildCaptureIntent(Uri uri) {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                MediaStore.EXTRA_OUTPUT, uri);
    }

    public static Intent buildCropPickIntent(Uri uri, String action, CropConfig params) {
        return new Intent(action, null)
                .setDataAndType(uri, params.type)
                // .setType(params.type)
                .putExtra("crop", params.crop).putExtra("scale", params.scale)
                .putExtra("aspectX", params.aspectX)
                .putExtra("aspectY", params.aspectY)
                .putExtra("outputX", params.outputX)
                .putExtra("outputY", params.outputY)
                .putExtra("return-data", params.returnData)
                .putExtra("outputFormat", params.outputFormat)
                .putExtra("noFaceDetection", params.noFaceDetection)
                .putExtra("scaleUpIfNeeded", params.scaleUpIfNeeded)
                .putExtra(MediaStore.EXTRA_OUTPUT, buildCameraUri());

    }

    public static Intent buildCropCameraIntent(String action, CropConfig params) {
        return new Intent(action, null)
                .setDataAndType(buildUri(), params.type)
                // .setType(params.type)
                .putExtra("crop", params.crop).putExtra("scale", params.scale)
                .putExtra("aspectX", params.aspectX)
                .putExtra("aspectY", params.aspectY)
                .putExtra("outputX", params.outputX)
                .putExtra("outputY", params.outputY)
                .putExtra("return-data", params.returnData)
                .putExtra("outputFormat", params.outputFormat)
                .putExtra("noFaceDetection", params.noFaceDetection)
                .putExtra("scaleUpIfNeeded", params.scaleUpIfNeeded)
                .putExtra(MediaStore.EXTRA_OUTPUT, buildCameraUri());

    }

    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        if (context == null || uri == null)
            return null;

        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
