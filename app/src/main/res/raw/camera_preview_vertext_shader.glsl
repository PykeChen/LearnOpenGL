//预览相机的着色器，顶点着色器不变
attribute vec4 vPosition;
attribute vec2 vCoord;
uniform mat4 vMatrix;
varying vec2 textureCoordinate;

void main(){
    gl_Position = vMatrix * vPosition;
    textureCoordinate = vCoord;
}