package com.astana.learnopengl.utils;

import android.graphics.Bitmap;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.*;

/**
 * GL相关的工具类
 *
 * @author cpy
 * @Description:
 * @version:
 * @date: 2018/11/20
 */
public class GLCommonUtils {

    private static String TAG = "GLUtils";

    public static FloatBuffer createBuffer(float[] vertexData) {
        FloatBuffer buffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        buffer.put(vertexData, 0, vertexData.length).position(0);
        return buffer;
    }

    public static ShortBuffer createBuffer(short[] vertexData) {
        ShortBuffer buffer = ByteBuffer.allocateDirect(vertexData.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();
        buffer.put(vertexData, 0, vertexData.length).position(0);
        return buffer;
    }

    /**
     * 根据类型和string, 加载纹理
     *
     * @param type
     * @param shaderSource
     * @return
     */
    public static int loadShader(int type, String shaderSource) {
        int shader = glCreateShader(type);
        if (shader == 0) {
            throw new RuntimeException("Create Shader Failed!" + glGetError());
        }
        glShaderSource(shader, shaderSource);
        glCompileShader(shader);
        return shader;
    }

    /**
     * 将顶点着色str和片段着色str进行链接,生成program
     *
     * @param verShader
     * @param fragShader
     * @return
     */
    public static int linkProgram(int verShader, int fragShader) {
        int program = glCreateProgram();
        if (program == 0) {
            throw new RuntimeException("Create Program Failed!" + glGetError());
        }
        glAttachShader(program, verShader);
        glAttachShader(program, fragShader);
        glLinkProgram(program);

        glUseProgram(program);
        return program;
    }

    /**
     * Checks to see if a GLES error has been raised.
     */
    public static void checkGlError(String op) {
        int error = GLES20.glGetError();
        if (error != GLES20.GL_NO_ERROR) {
            String msg = op + ": glError 0x" + Integer.toHexString(error);
            Log.e(TAG, msg);
            throw new RuntimeException(msg);
        }
    }

    /**
     * 创建纹理对象,返回生成的纹理id
     *
     * @return 纹理id
     */
    public static int createTexture() {
        int[] texture = new int[1];
        //生成纹理
        GLES20.glGenBuffers(1, texture, 0);
        //绑定到2D纹理上
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
        //设置缩小过滤器为 使用纹理中坐标最接近的一个像素的颜色 作为需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        //设置放大过滤器为 使用纹理中坐标最接近的若干个颜色, 通过加权平均算法得到需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        //设置环绕方向S, 截取纹理坐标到[1/2n, 1-1/2n].将导致永远不会与border融合, 我觉得这个就是设置截取时候的处理,类似imageView_crop效果
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        //设置环绕方向T, 截取纹理坐标到[1/2n, 1-1/2n].将导致永远不会与border融合, 我觉得这个就是设置截取时候的处理,类似imageView_crop效果
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        return texture[0];
    }

    /**
     * 创建绑定外部纹理的id
     *
     * Android的Camera及Camera2都允许使用SurfaceTexture作为预览载体，但是它们所使用的SurfaceTexture传入的OpenGL texture object
     * name必须为GLES11Ext.GL_TEXTURE_EXTERNAL_OES
     * @return
     */
    public static int createExternalTexture(){
        int[] texture = new int[1];
        //创建的是扩展纹理，所以绑定的时候我们也需要绑定到扩展纹理上才可以正常使用
        GLES20.glGenBuffers(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        return texture[0];
    }

    /**
     * 创建指定bitmap的纹理对象,返回生成的纹理id
     *
     * @return 纹理id
     */
    public static int createTexture(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            int texture2D = createTexture();
            //绑定纹理到bitmap上
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            return texture2D;
        }
        return 0;
    }
}
