package com.astana.learnopengl

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.astana.learnopengl.camPreview.CameraActivity
import com.astana.learnopengl.drawCube.GLDrawCube
import com.astana.learnopengl.drawCube.GLDrawSquare
import com.astana.learnopengl.drawTriangle.GLDrawTriangle
import com.astana.learnopengl.fbo.FBOActivity
import com.astana.learnopengl.texture.DrawTextureRect
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        sample_text.text = stringFromJNI()
    }


    fun gotoGLTriangleDraw(view: View) {
        val intent = Intent(this, GLDrawTriangle::class.java)
        startActivity(intent)
    }

    fun gotoGLSquareDraw(view: View) {
        val intent = Intent(this, GLDrawSquare::class.java)
        startActivity(intent)
    }

    fun gotoGLCubeDraw(view: View) {
        val intent = Intent(this, GLDrawCube::class.java)
        startActivity(intent)
    }

    fun gotoGLTextureDraw(view: View) {
        val intent = Intent(this, DrawTextureRect::class.java)
        startActivity(intent)
    }

    fun gotoNormalCameraPreview(view: View) {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }

    fun gotoFBORender(view: View) {
        val intent = Intent(this, FBOActivity::class.java)
        startActivity(intent)
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
