package com.astana.learnopengl.texture;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.astana.learnopengl.R;
import com.astana.learnopengl.utils.BitmapUtils;
import com.astana.learnopengl.utils.CommonUtils;
import com.astana.learnopengl.utils.GLCommonUtils;
import com.astana.learnopengl.utils.GlUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * 绘制带有纹理的矩形
 *
 * @author cpy
 * @Description:
 * @version:
 * @date: 2018/11/23
 */
public class DrawTextureRect extends AppCompatActivity implements GLSurfaceView.Renderer {

    private static final int SIZEOF_FLOAT = 4;

    float mSqureCoords[] = {
            -1.0f, 1.0f, 0f, // top left
            -1.0f, -1.0f, 0f,// bottom left
            1.0f, -1.0f, 0f,  // bottom right
            1.0f, 1.0f, 0f  // bottom top
    };

    //纹理顶点的值,对应坐标值----具体坐标值和纹理的坐标值怎么对应请搜索信息
    //顶点坐标在中心,有-1, 而纹理坐标,左上角为0,0点,右边和下边分别为x, y轴的正值
    float sCoord[] = {
            0.0f, 0.0f, // top left
            0.0f, 1.0f, // bottom left
            1.0f, 1.0f, //bottom right
            1.0f, 0.0f  // bottom top
    };


    //绘制顶点的索引-逆时针索引
    short mIndices[] = {
            0, 2, 1, // 左下角三角形
            0, 2, 3 //右上角三角形
    };

    //顶点坐标的缓存
    private FloatBuffer mVertexBuffer;
    //纹理坐标的缓存
    private FloatBuffer mTexCoordBuffer;
    private ShortBuffer mIndicesBuffer;
    private int mShaderProgram = -1;

    float[] mProjectMatrix = new float[16];
    float[] mViewMatrix = new float[16];
    float[] mMVPMatrix = new float[16];

    private Bitmap mTextureBitmap;
    private int mTexture2D;

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
        mTextureBitmap = BitmapUtils.loadBitmapFromAssetFile(this, "cike.jpg", 1024, 1024);
        mTexture2D = GLCommonUtils.createTexture(mTextureBitmap);
        GlUtil.checkGlError("createTexture");

        //做一些初始化的事情
        //createBuffer,创 建FBO
        mVertexBuffer = GLCommonUtils.createBuffer(mSqureCoords);
        mTexCoordBuffer = GLCommonUtils.createBuffer(sCoord);

        mIndicesBuffer = GLCommonUtils.createBuffer(mIndices);

        //加载编译程序
        String fragmentStrRes = CommonUtils.readShaderFromResource(this, R.raw.texture_rect_fragment_shader);
        String vertexStrRes = CommonUtils.readShaderFromResource(this, R.raw.texture_rect_vertex_shader);
        int vertexShader = GLCommonUtils.loadShader(GLES20.GL_VERTEX_SHADER, vertexStrRes);
        int fragmentShader = GLCommonUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentStrRes);
        mShaderProgram = GLCommonUtils.linkProgram(vertexShader, fragmentShader);
    }



    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置窗口
        GLES20.glViewport(0, 0, width, height);

        int w = mTextureBitmap.getWidth();
        int h = mTextureBitmap.getHeight();
        float sWH = w / (float) h;
        float sWidthHeight = width / (float) height;
        if (width > height) {
            if (sWH > sWidthHeight) {
                Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight * sWH, sWidthHeight * sWH, -1, 1, 3, 7);
            } else {
                Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight / sWH, sWidthHeight / sWH, -1, 1, 3, 7);
            }
        } else {
            if (sWH > sWidthHeight) {
                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1 / sWidthHeight * sWH, 1 / sWidthHeight * sWH, 3, 7);
            } else {
                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -sWH / sWidthHeight, sWH / sWidthHeight, 3, 7);
            }
        }

        //设置相机置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0f, 0, 0, 0, 0, 1, 0);
        //计算得到变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //重绘背景色,如果不设置这个BUFFER_BIT的话,上面onSurfaceCreate#glClearColor下次不生效
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //获取顶点着色器的vMatrix成员句柄
        int matrixHandler = GLES20.glGetUniformLocation(mShaderProgram, "vMatrix");
        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(matrixHandler, 1, false, mMVPMatrix, 0);

        //获取顶点着色器的vPosition成员句柄
        int positionHandler = GLES20.glGetAttribLocation(mShaderProgram, "vPosition");
        //启用正方形的顶点的句柄
        GLES20.glEnableVertexAttribArray(positionHandler);
        //准备正方形的坐标数据
        GLES20.glVertexAttribPointer(positionHandler, 3, GLES20.GL_FLOAT, false, 3 * SIZEOF_FLOAT, mVertexBuffer);

        //获取顶点着色器的vCoordinate成员句柄
        int aCoordinateHandler = GLES20.glGetAttribLocation(mShaderProgram, "vCoordinate");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(aCoordinateHandler);
        //准备纹理坐标数据
        GLES20.glVertexAttribPointer(aCoordinateHandler, 2, GLES20.GL_FLOAT, false, 2 * SIZEOF_FLOAT, mTexCoordBuffer);

        int vTextureHandler = GLES20.glGetUniformLocation(mShaderProgram,"vTexture");
        GLES20.glUniform1i(vTextureHandler, 0);

        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
//        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndices.length, GLES20.GL_UNSIGNED_SHORT, mIndicesBuffer);

//        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(positionHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BitmapUtils.release(mTextureBitmap);
    }
}
