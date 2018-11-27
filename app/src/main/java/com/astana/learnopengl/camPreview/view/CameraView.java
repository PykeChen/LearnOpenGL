package com.astana.learnopengl.camPreview.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * 展示Camera预览层的view
 *
 * @author cpy
 * @Description:
 * @version:
 * @date: 2018/11/27
 */
public class CameraView extends GLSurfaceView {

    public CameraView(Context context) {
        super(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
