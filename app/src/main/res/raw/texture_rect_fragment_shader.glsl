precision mediump float;

uniform sampler2D vTexture;

//从顶点着色器传递过来的纹理坐标值
varying vec2 aCoordinate;

void main(){
    gl_FragColor = texture2D(vTexture, aCoordinate);
}