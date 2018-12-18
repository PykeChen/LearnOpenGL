package com.astana.learnopengl.fbo;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import com.astana.learnopengl.filter.BaseFilter;
import com.astana.learnopengl.filter.GrayFilter;
import com.astana.learnopengl.utils.GLCommonUtils;
import com.astana.learnopengl.utils.MatrixUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;

/**
 * FrameBufferRender的学习使用
 * FBO 实际上是由颜色附件、深度附件、模板附件组成的，作为着色器各方面（一般包括颜色、深度、深度值）绘制结果存储的逻辑对象。
 *
 * @author cpy
 * @Description:
 * @version:
 * @date: 2018/12/18
 */
public class FBORender implements GLSurfaceView.Renderer {

    private Bitmap mBitmap;
    private BaseFilter mFilter;

    private ByteBuffer mBuffer;

    private int[] mFrame = new int[1];
    private int[] fTexture = new int[2];


    private Callback mCallback;

    public FBORender(){
        mFilter = new GrayFilter();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //初始化滤镜以及GL相关环境
        mFilter.create();
        //为什么绘制出来是倒的呢???
        mFilter.setMatrix(MatrixUtils.flip(MatrixUtils.getOriginalMatrix(),false,true));

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //开始绘制
        if (mBitmap != null && !mBitmap.isRecycled()) {
            createFBOEnvi();
            //切换到FBO上进行渲染
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrame[0]);
            //为FrameBuffer挂载Texture[1]来存储颜色,绑定颜色附件
            GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, fTexture[1], 0);

            //设置视图
            GLES20.glViewport(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
            //设置绘制的纹理id为绑定image的纹理0
            mFilter.setTextureId(fTexture[0]);
            //绘制
            mFilter.draw();
            //从fbo中读出bitmap的对应RGB的buffer
            GLES20.glReadPixels(0, 0, mBitmap.getWidth(), mBitmap.getHeight(), GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE, mBuffer);
            if (mCallback != null) {
                mCallback.onCall(mBuffer);
            }
            deleteEnvi();
            mBitmap.recycle();
        }

    }

    private void createFBOEnvi() {
        //创建FBO
        GLES20.glGenFramebuffers(1, mFrame, 0);
        //生成两个纹理id, 一个纹理直接绑定bitmap,另外一个纹理进行存储bitmap的rgba的值
        GLES20.glGenTextures(2, fTexture, 0);
        for (int i = 0; i < 2; i++) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture[i]);
            if (i == 0) {
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, mBitmap, 0);
            } else {
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, mBitmap.getWidth(), mBitmap.getHeight(),
                        0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
            }
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        }
        //创建存储bitmap对应RGB的buffer值
        mBuffer = ByteBuffer.allocate(mBitmap.getWidth() * mBitmap.getHeight() * 4);
    }

    private void deleteEnvi() {
        GLES20.glDeleteTextures(2, fTexture, 0);
        GLES20.glDeleteFramebuffers(1, mFrame, 0);
    }


    public void setBitmap(Bitmap bitmap){
        this.mBitmap=bitmap;
    }

    public void setCallback(Callback callback){
        this.mCallback=callback;
    }

    interface Callback{
        void onCall(ByteBuffer data);
    }
}
