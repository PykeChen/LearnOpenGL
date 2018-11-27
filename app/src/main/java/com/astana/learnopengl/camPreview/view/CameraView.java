package com.astana.learnopengl.camPreview.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import com.astana.learnopengl.camPreview.camera.KitkatCamera;
import com.astana.learnopengl.filter.BaseFilter;
import com.astana.learnopengl.filter.OESFilter;
import com.astana.learnopengl.utils.GLCommonUtils;

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
public class CameraView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private KitkatCamera mCamera;
    //后置摄像头cameraId
    private int cameraId = 1;

    private SurfaceTexture mSurfaceTexture;
    private BaseFilter mFilter;

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
        mFilter = new OESFilter();
    }

    private void initGLSurfaceView() {
        int texture = GLCommonUtils.createExternalTexture();
        //Construct a new SurfaceTexture to stream images to a given OpenGL texture.
        // 构造一个surfaceTexture将surface的图像流传递给指定的openGL纹理
        mSurfaceTexture = new SurfaceTexture(texture);
        mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                requestRender();
            }
        });
        //创建Program
        mFilter.create();
        mFilter.setTextureId(texture);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //初始化gl相关
        initGLSurfaceView();
        //开启相机
        mCamera.open(cameraId);
        mCamera.setPreviewTexture(mSurfaceTexture);
        mCamera.preview();
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (mSurfaceTexture != null) {
            mSurfaceTexture.updateTexImage();
        }
        mFilter.draw();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCamera.close();
    }

    public void release(){
        mCamera.close();
    }


}
