//预览相机的着色器，顶点着色器不变
attribute vec4 vPosition;

void main(){
    gl_Position = vPosition;
}