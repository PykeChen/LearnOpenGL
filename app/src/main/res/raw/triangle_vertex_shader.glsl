attribute vec4 vPosition;
//为了给顶点进行坐标换算,加上一个矩阵-要变换的
uniform mat4 vMatrix;
//从外界传入顶点的颜色
attribute vec4 aColor;
//传给片元着色器-varying一般用于从顶点着色器传入到片元着色器的量
varying vec4 vColor;

void main(){
    //将顶点进行坐标换算,以为opengl的坐标是列向量,所以要matrix放前 m * position
    gl_Position = vMatrix * vPosition;
    //将顶点颜色赋值给vColor,以便传给片元
    vColor = aColor;
}