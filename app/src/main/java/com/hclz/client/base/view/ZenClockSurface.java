package com.hclz.client.base.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hclz.client.R;

public class ZenClockSurface extends SurfaceView implements
        SurfaceHolder.Callback {

    private DrawClock drawClock;

    public ZenClockSurface(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
    }

    public ZenClockSurface(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public ZenClockSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawClock = new DrawClock(getHolder(), getResources());
        drawClock.setRunning(true);
        drawClock.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawClock.setRunning(false);
        while (retry) {
            try {
                drawClock.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }

    }

    class DrawClock extends Thread {
        private boolean runFlag = false;
        private SurfaceHolder surfaceHolder;
        private Bitmap ic_cha;
        private Bitmap ic_dao;
        private Matrix matrix_cha;
        private Matrix matrix_dao;
        private Paint painter;
        private boolean isZheng = true;

        public DrawClock(SurfaceHolder surfaceHolder, Resources resources) {
            this.surfaceHolder = surfaceHolder;
            ic_cha = BitmapFactory.decodeResource(resources,
                    R.mipmap.ic_cha);
            ic_dao = BitmapFactory.decodeResource(resources, R.mipmap.ic_dao);
            matrix_cha = new Matrix();
            matrix_dao = new Matrix();
            this.painter = new Paint();
            this.painter.setStyle(Paint.Style.FILL);
            this.painter.setAntiAlias(true);
            this.painter.setFilterBitmap(true);
        }

        public void setRunning(boolean run) {
            runFlag = run;
        }


        @Override
        public void run() {
            Canvas canvas;
            while (runFlag) {
                matrix_cha.setTranslate(0, 0);
                matrix_dao.setTranslate(100, 0);
                if (isZheng) {
                    matrix_cha.preRotate(15, ic_cha.getWidth() * 4 / 5,
                            ic_cha.getHeight() * 4 / 5);
                    matrix_dao.preRotate(-15, ic_dao.getWidth() * 4 / 5,
                            ic_dao.getHeight() * 4 / 5);
                    isZheng = false;
                } else {
                    matrix_cha.preRotate(0, ic_cha.getWidth() * 4 / 5,
                            ic_cha.getHeight() * 4 / 5);
                    matrix_dao.preRotate(0, ic_dao.getWidth() * 4 / 5,
                            ic_dao.getHeight() * 4 / 5);
                    isZheng = true;
                }
                canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
//                        float[] f = new float[9];
//                        matrix.getValues(f);
//                        float y = f[Matrix.MTRANS_Y];
//                        matrix.postTranslate(0, 10);
                        canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
                        canvas.drawBitmap(ic_cha, matrix_cha, this.painter);
                        canvas.drawBitmap(ic_dao, matrix_dao, this.painter);
                        sleep(1000);
//                        float[] f1 = new float[9];
//                        matrix.getValues(f1);
//                        float y1 = f1[Matrix.MTRANS_Y];
//                        if (y1 >= 800) {
//                            matrix.setTranslate(0, 0);
//                        }
                    }
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }

            }
        }
    }
}