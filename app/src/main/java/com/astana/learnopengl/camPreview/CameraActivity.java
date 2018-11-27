package com.astana.learnopengl.camPreview;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;
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

    private static final int REQUEST_CODE = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_camera);

        PermissionUtils.askPermission(this, new String[]{Manifest.permission.CAMERA, Manifest
                .permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE, initViewRunnable);
    }


    private Runnable initViewRunnable = new Runnable() {
        @Override
        public void run() {
            setContentView(R.layout.activity_camera);
            mCameraView = findViewById(R.id.camera_preview);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(requestCode == REQUEST_CODE, grantResults, initViewRunnable,
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CameraActivity.this, "没有获得相机和存储权限", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraView.release();
    }
}
