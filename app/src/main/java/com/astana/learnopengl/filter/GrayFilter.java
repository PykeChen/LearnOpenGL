/*
 *
 * GrayFilter.java
 * 
 * Created by Wuwang on 2016/12/14
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.astana.learnopengl.filter;


import android.content.res.Resources;
import com.astana.learnopengl.AppApplication;
import com.astana.learnopengl.utils.CommonUtils;

/**
 * 灰色滤镜
 */
public class GrayFilter extends BaseFilter {

    @Override
    protected void onCreate() {
        //加载编译程序
        Resources resources = AppApplication.getApplication().getResources();
        String fragmentStrRes = CommonUtils.readContentFromAssetsPath(resources, "shader/color/gray_fragment.glsl");
        String vertexStrRes = CommonUtils.readContentFromAssetsPath(resources, "shader/base_vertex.glsl");
        createProgram(vertexStrRes, fragmentStrRes);

    }

    @Override
    protected void onSizeChanged(int width, int height) {

    }
}
