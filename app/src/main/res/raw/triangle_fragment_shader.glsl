precision mediump float;

//uniform vec4 vColor; 之前的color是通过vec向量的值来关联赋值
//从顶点着色器传递过来的值
varying vec4 vColor;

void main(){
    gl_FragColor = vColor;
}