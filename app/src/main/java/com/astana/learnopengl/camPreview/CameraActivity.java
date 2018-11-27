package com.astana.learnopengl.camPreview;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.astana.learnopengl.BaseActivity;
import com.astana.learnopengl.R;
import com.astana.learnopengl.camPreview.view.CameraView;
import com.astana.learnopengl.utils.PermissionUtils;

/**
 * 使用GLSurfaceView将相机预览呈现到界面上
 *
 * @author cpy
 * @Description:
 * @version:
 * @date: 2018/11/27
 */
public class CameraActivity extends BaseActivity {

    private CameraView mCameraView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_camera);

        PermissionUtils.askPermission(this, new String[]{Manifest.permission.CAMERA, Manifest
                .permission.WRITE_EXTERNAL_STORAGE}, 10, initViewRunnable);
    }


    private Runnable initViewRunnable = new Runnable() {
        @Override
        public void run() {
            setContentView(R.layout.activity_camera);
            mCameraView = findViewById(R.id.camera_preview);
        }
    };
}
