attribute vec4 vPosition;
//为了给顶点进行坐标换算,加上一个矩阵-要变换的
uniform mat4 vMatrix;

//外部赋值过来的纹理坐标值
attribute vec2 vCoordinate;
//外部赋值过来的纹理坐标值, 传递给片元着色器
varying vec2 aCoordinate;

void main(){
    //将顶点进行坐标换算,以为opengl的坐标是列向量,所以要matrix放前 m * position
    //将顶点进行坐标换算,以为opengl的坐标是列向量,所以要matrix放前 m * position
    gl_Position = vMatrix * vPosition;
    //将顶点颜色赋值给vColor,以便传给片元
    aCoordinate = vCoordinate;
}