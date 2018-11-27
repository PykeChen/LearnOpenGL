package com.astana.learnopengl.drawCube;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.astana.learnopengl.BaseActivity;
import com.astana.learnopengl.R;
import com.astana.learnopengl.utils.CommonUtils;
import com.astana.learnopengl.utils.GLCommonUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * 绘制正方形, 编写过程:
 * 1.绘制普通的正方形
 * 2.绘制立方体
 *
 * @author cpy
 * @Description:
 * @version:
 * @date: 2018/11/20
 */
public class GLDrawSquare extends BaseActivity implements GLSurfaceView.Renderer{

    private static final int SIZEOF_FLOAT = 4;

    float mTriangleCoords[] = {
            -1.0f, 1.0f, 0f, // top left
            -1.0f, -1.0f, 0f,// bottom left
            1.0f, -1.0f, 0f,  // bottom right
            1.0f, 1.0f, 0f  // bottom top
    };

    //各个顶点的颜色值
    float mColorCoords[] = {
            1.0f, 0.0f, 0.0f, 1.0f, // top left
            0.0f, 1.0f, 0f, 1.0f,// bottom left
            0.0f, 0.0f, 1.0f, 1.0f,   // bottom right
            0.0f, 1.0f, 1.0f, 1.0f  // bottom top
    };


    //绘制顶点的索引-逆时针索引
    short mIndices[] = {
            0, 2, 1, // 左下角三角形
            0, 2, 3 //右上角三角形
    };

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;
    private ShortBuffer mIndicesBuffer;
    private int mShaderProgram = -1;

    float[] mProjectMatrix = new float[16];
    float[] mViewMatrix = new float[16];
    float[] mMVPMatrix = new float[16];

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
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        //-------------演示顺时针还是逆时针以及正面背面剔除---
//        GLES20.glFrontFace(GLES20.GL_CCW);
//        GLES20.glEnable(GLES20.GL_CULL_FACE);
//        GLES20.glCullFace(GLES20.GL_FRONT);
        //-------------演示顺时针还是逆时针以及正面背面剔除---

        //做一些初始化的事情
        //createBuffer,创建FBO
        mVertexBuffer = GLCommonUtils.createBuffer(mTriangleCoords);
        mColorBuffer = GLCommonUtils.createBuffer(mColorCoords);
        mIndicesBuffer = GLCommonUtils.createBuffer(mIndices);

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
        //设置宽高比
        float radio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -radio, radio, -1, 1, 3, 5);
        //设置相机置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 5.0f, 0, 0, 0, 0, 1, 0);
        //计算得到变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //重绘背景色,如果不设置这个BUFFER_BIT的话,上面onSurfaceCreate#glClearColor下次不生效
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
//        GLES20.glClearColor(1.0f, 1.0f, 0.0f, 1.0f);
        //获取顶点着色器的vMatrix成员句柄
        int matrixHandler = GLES20.glGetUniformLocation(mShaderProgram, "vMatrix");
        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(matrixHandler, 1, false, mMVPMatrix, 0);

        //获取顶点着色器的aColor句柄
        int aColorHandler = GLES20.glGetAttribLocation(mShaderProgram, "aColor");
        //启动三角形顶点的颜色句柄-不启动则没有效果
        GLES20.glEnableVertexAttribArray(aColorHandler);
        //指定aColor的值
        GLES20.glVertexAttribPointer(aColorHandler, 4, GLES20.GL_FLOAT, false, 4 * SIZEOF_FLOAT, mColorBuffer);


        //获取顶点着色器的vPosition成员句柄
        int positionHandler = GLES20.glGetAttribLocation(mShaderProgram, "vPosition");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(positionHandler);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(positionHandler, 3, GLES20.GL_FLOAT, false, 3 * SIZEOF_FLOAT, mVertexBuffer);

        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
//        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndices.length, GLES20.GL_UNSIGNED_SHORT, mIndicesBuffer);

//        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(positionHandler);
        //禁止顶点数组的句柄--用来测试
        GLES20.glDisableVertexAttribArray(aColorHandler);
    }
}
