package com.astana.learnopengl.drawTriangle;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.astana.learnopengl.R;
import com.astana.learnopengl.utils.CommonUtils;
import com.astana.learnopengl.utils.GLCommonUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.FloatBuffer;

/**
 * @author cpy
 * @Description:
 * @version:
 * @date: 2018/11/20
 */
public class GLDrawTriangle extends AppCompatActivity implements GLSurfaceView.Renderer{

    private static final int SIZEOF_FLOAT = 4;

    float mTriangleCoords[] = {
            0.0f, 1.0f, // top
            -1.0f, -1.0f, // bottom left
            1.0f, -1.0f  // bottom right
    };
    private FloatBuffer mVertexBuffer;
    private int mShaderProgram = -1;

    float color[] = {1.0f, 0.0f, 0.0f, 1.0f}; //白色

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        //设置2.0的环境
        glSurfaceView.setEGLContextClientVersion(2);

        setContentView(glSurfaceView);

        glSurfaceView.setRenderer(this);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //设置背景色（r,g,b,a）
        GLES20.glClearColor(1.0f, 0.0f, 1.0f, 1.0f);

        //做一些初始化的事情
        //createBuffer,创建FBO
        mVertexBuffer = GLCommonUtils.createBuffer(mTriangleCoords);

        //加载编译程序
        String fragmentStrRes = CommonUtils.readShaderFromResource(this, R.raw.triangle_fragment_shader);
        String vertexStrRes = CommonUtils.readShaderFromResource(this, R.raw.triangle_vertex_shader);
        int vertexShader = GLCommonUtils.loadShader(GLES20.GL_VERTEX_SHADER, vertexStrRes);
        int fragmentShader = GLCommonUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentStrRes);
        mShaderProgram = GLCommonUtils.linkProgram(vertexShader, fragmentShader);
    }



    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置窗口
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //重绘背景色,如果不设置这个BUFFER_BIT的话,上面onSurfaceCreate#glClearColor下次不生效
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
//        GLES20.glClearColor(1.0f, 1.0f, 0.0f, 1.0f);
        //获取顶点着色器的vPosition成员句柄
        int positionHandler = GLES20.glGetAttribLocation(mShaderProgram, "vPosition");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(positionHandler);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(positionHandler, 2, GLES20.GL_FLOAT, false, 2 * SIZEOF_FLOAT, mVertexBuffer);

        //获取片元着色器的vColor成员的句柄
        int colorHandler = GLES20.glGetUniformLocation(mShaderProgram, "vColor");
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(colorHandler, 1, color, 0);

        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

//        //禁止顶点数组的句柄
//        GLES20.glDisableVertexAttribArray(positionHandler);
    }
}