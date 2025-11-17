package com.example.edgeviewer.gl

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.opengles.GL10
import javax.microedition.khronos.egl.EGLConfig

class GLRenderer : GLSurfaceView.Renderer {
    private var textureId = -1
    private var pendingBitmap: Bitmap? = null
    @Volatile var fps: Float = 0f

    fun updateBitmap(bmp: Bitmap) {
        pendingBitmap = bmp
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f,0f,0f,1f)
        // init shaders, vbo, generate texture id
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0,0,width,height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        val bmp = pendingBitmap
        if (bmp != null) {
            // upload to texture (TexImage2D or TexSubImage2D) and draw quad
            // NOTE: use GLUtils.texImage2D if using Bitmap
            pendingBitmap = null
        }
        // draw call
    }
}
