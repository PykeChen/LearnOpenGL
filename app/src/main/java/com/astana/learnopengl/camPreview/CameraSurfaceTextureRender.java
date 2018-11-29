package com.astana.learnopengl.camPreview;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import com.astana.learnopengl.filter.BaseFilter;
import com.astana.learnopengl.filter.OESFilter;
import com.astana.learnopengl.utils.GLCommonUtils;
import com.astana.learnopengl.utils.MatrixUtils;

/**
 * Camera预览使用surfaceTexture渲染所使用的渲染
 *
 * @author cpy
 * @Description:
 * @version:
 * @date: 2018/11/29
 */
public class CameraSurfaceTextureRender {
    private SurfaceTexture mSurfaceTexture;
    private BaseFilter mFilter;
    private RenderCallback mRenderCallback;
    private int mViewWidth, mViewHeight;
    private int mPreviewWidth, mPreviewHeight;
    private float[] mMatrix = new float[16];
    private boolean mIsFontCamera;

    public void init(RenderCallback callback) {
        mFilter = new OESFilter();
        mRenderCallback = callback;
    }

    public void onSurfaceCreated() {
        int texture = GLCommonUtils.createExternalTexture();
        //Construct a new SurfaceTexture to stream images to a given OpenGL texture.
        // 构造一个surfaceTexture将surface的图像流传递给指定的openGL纹理
        mSurfaceTexture = new SurfaceTexture(texture);
        mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                mRenderCallback.onFrameAvailable();
            }
        });
        //创建Program
        mFilter.create();
        mFilter.setTextureId(texture);
    }

    public void onSurfaceChanged(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        setWindowViewSize(width, height);
    }

    public void onDrawFrame(){
        if (mSurfaceTexture != null) {
            mSurfaceTexture.updateTexImage();
        }
        mFilter.draw();
    }

    public void setPreviewPicSize(int width, int height) {
        mPreviewWidth = width;
        mPreviewHeight = height;
    }

    private void setWindowViewSize(int width, int height) {
        mViewWidth = width;
        mViewHeight = height;
        calculateMatrix();
    }

    private void calculateMatrix() {
        if (mPreviewWidth > 0 && mPreviewHeight > 0 && mViewWidth > 0 && mViewHeight > 0) {
            MatrixUtils.getShowMatrix(mMatrix, mPreviewWidth, mPreviewHeight, mViewWidth, mViewHeight);
            if (mIsFontCamera) {
                MatrixUtils.flip(mMatrix, true, false);
                MatrixUtils.rotate(mMatrix, 90);
            } else {
                MatrixUtils.rotate(mMatrix, 270);
            }
            mFilter.setMatrix(mMatrix);
        }
    }

    public SurfaceTexture getSurfaceTexture(){
        return mSurfaceTexture;
    }

    public void setIsFontCamera(boolean isBackCamera) {
        mIsFontCamera = isBackCamera;
    }

    public interface RenderCallback {

        void onFrameAvailable();

    }
}
