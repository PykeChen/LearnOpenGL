package com.astana.learnopengl.fbo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.astana.learnopengl.BaseActivity;
import com.astana.learnopengl.R;

import java.nio.ByteBuffer;

/**
 *
 * FBO学习使用承载的activity
 *
 * @author cpy
 * @Description:
 * @version:
 * @date: 2018/12/18
 */
public class FBOActivity extends BaseActivity implements FBORender.Callback {

    private FBORender mRender;
    private GLSurfaceView mGlSurfaceView;
    private ImageView mImgShow;

    private int mBmpWidth,mBmpHeight;
    private String mImgPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fbo);

        //使用GLSurfaceView作为渲染的视图
        mGlSurfaceView = findViewById(R.id.glView_show);
        //设置2.0的环境
        mGlSurfaceView.setEGLContextClientVersion(2);

        //使用fboRender作为渲染回调类
        mRender = new FBORender();
        mRender.setCallback(this);
        mGlSurfaceView.setRenderer(mRender);
        mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        mImgShow = findViewById(R.id.img_show);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            mImgPath = c.getString(columnIndex);
            c.close();
            Log.e("cpy", "img->" + mImgPath);
            Bitmap bmp=BitmapFactory.decodeFile(mImgPath);
            mBmpWidth=bmp.getWidth();
            mBmpHeight=bmp.getHeight();
            mRender.setBitmap(bmp);
            //需不需要调用呢
            mGlSurfaceView.requestRender();
        }
    }

    public void onClick(View view){
        //调用相册
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onCall(final ByteBuffer data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("wuwang","callback success");
                final Bitmap bitmap = Bitmap.createBitmap(mBmpWidth, mBmpHeight, Bitmap.Config.ARGB_8888);
                bitmap.copyPixelsFromBuffer(data);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mImgShow.setImageBitmap(bitmap);
                    }
                });
                data.clear();
            }
        }).start();
    }
}
