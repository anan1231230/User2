package com.hclz.client.base.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by hjm on 16/7/28.
 */

public class TxtToSpeackUtil implements TextToSpeech.OnInitListener {
    private TextToSpeech mTextToSpeech;//TTS对象
    private final ConcurrentLinkedQueue<String> mBufferedMessages = new ConcurrentLinkedQueue();//消息队列
    private Context mContext;
    private boolean mIsReady;//标识符
    private static TxtToSpeackUtil textToSpeechDemo = new TxtToSpeackUtil();
    private boolean isInit = false;
    private MediaPlayer mMediaPlayer;
    private AssetManager mAssetManager;

    private TxtToSpeackUtil() {
    }

    public static TxtToSpeackUtil getInstence() {
        return textToSpeechDemo;
    }

    public void init(Context context) {
        if (isInit) {
            return;
        }
        isInit = true;
        this.mContext = context;//获取上下文
        this.mTextToSpeech = new TextToSpeech(this.mContext, this);//实例化TTS
    }

    //初始化TTS引擎
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = this.mTextToSpeech.setLanguage(Locale.UK);//设置识别语音为中文
            synchronized (this) {
                this.mIsReady = true;//设置标识符为true
                for (String bufferedMessage : this.mBufferedMessages) {
                    speakText(bufferedMessage);//读语音
                }
                this.mBufferedMessages.clear();//读完后清空队列
            }
        }
    }

    //释放资源
    public void release() {
        synchronized (this) {
            this.mTextToSpeech.shutdown();
            this.mIsReady = false;
        }
    }

    //更新消息队列，或者读语音
    public void notifyNewMessage(String lanaugh) {
        String message = lanaugh;
        synchronized (this) {
            if (this.mIsReady) {
                speakText(message);
            } else {
                this.mBufferedMessages.add(message);
            }
        }
    }

    //读语音处理
    private void speakText(String message) {
        HashMap params = new HashMap();
        params.put(TextToSpeech.Engine.KEY_PARAM_STREAM, "STREAM_NOTIFICATION");//设置播放类型（音频流类型）
        this.mTextToSpeech.speak(message, TextToSpeech.QUEUE_ADD, params);//将这个发音任务添加当前任务之后
        this.mTextToSpeech.playSilence(100, TextToSpeech.QUEUE_ADD, params);//间隔多长时间
    }

    public void loadMedia(Context context) {
        mMediaPlayer = new MediaPlayer();
        mAssetManager = context.getAssets();
        try {
            AssetFileDescriptor fileDescriptor = mAssetManager.openFd("notification.wav");
            mMediaPlayer
                    .setDataSource(fileDescriptor.getFileDescriptor(),
                            fileDescriptor.getStartOffset(),
                            fileDescriptor.getLength());
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playNotification() {
        mMediaPlayer.start();
    }
}