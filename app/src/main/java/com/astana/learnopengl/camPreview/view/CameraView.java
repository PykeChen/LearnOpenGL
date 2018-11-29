package com.astana.learnopengl.camPreview.view;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import com.astana.learnopengl.camPreview.CameraSurfaceTextureRender;
import com.astana.learnopengl.camPreview.camera.KitkatCamera;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 展示Camera预览层的view
 *
 * @author cpy
 * @Description:
 * @version:
 * @date: 2018/11/27
 */
public class CameraView extends GLSurfaceView implements GLSurfaceView.Renderer, CameraSurfaceTextureRender.RenderCallback {

    private KitkatCamera mCamera;
    //前置摄像头cameraId
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private CameraSurfaceTextureRender mRender;

    public CameraView(Context context) {
        super(context);
        init();
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        mCamera = new KitkatCamera();
        mRender = new CameraSurfaceTextureRender();
        mRender.init(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //初始化gl相关
        mRender.onSurfaceCreated();
        //开启相机
        mCamera.open(cameraId);
        mRender.setIsFontCamera(cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT);
        Point point = mCamera.getPreviewSize();
        mRender.setPreviewPicSize(point.x, point.y);
        mCamera.setPreviewTexture(mRender.getSurfaceTexture());
        mCamera.preview();
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mRender.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mRender.onDrawFrame();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCamera.close();
    }

    public void release(){
        mCamera.close();
    }


    @Override
    public void onFrameAvailable() {
        requestRender();
    }
}
