package com.astana.learnopengl.filter;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import com.astana.learnopengl.AppApplication;
import com.astana.learnopengl.R;
import com.astana.learnopengl.utils.CommonUtils;
import com.astana.learnopengl.utils.GlUtil;

/**
 * @author cpy
 * @Description:
 * @version:
 * @date: 2018/11/27
 */
public class OESFilter extends BaseFilter {


    @Override
    protected void onCreate() {
        //加载编译程序
        String fragmentStrRes = CommonUtils.readShaderFromResource(AppApplication.getApplication(), R.raw.camera_preview_fragment_shader);
        String vertexStrRes = CommonUtils.readShaderFromResource(AppApplication.getApplication(), R.raw.camera_preview_vertext_shader);
        createProgram(vertexStrRes, fragmentStrRes);

    }

    @Override
    protected void onSizeChanged(int width, int height) {

    }

    @Override
    protected void onBindTexture() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + getTextureType());
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, getTextureId());
        GLES20.glUniform1i(mHTexture, getTextureType());
        GlUtil.checkGlError("oes filter bindTexture");
    }
}
