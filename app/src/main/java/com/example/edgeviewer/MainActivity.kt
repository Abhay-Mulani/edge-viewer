package com.example.edgeviewer

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.view.TextureView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity(), TextureView.SurfaceTextureListener {

    private lateinit var textureView: TextureView
    private lateinit var fpsText: TextView
    private lateinit var toggleBtn: Button
    private var showEdges = true

    companion object {
        init { System.loadLibrary("native-lib") }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textureView = findViewById(R.id.textureView)
        fpsText = findViewById(R.id.fpsText)
        toggleBtn = findViewById(R.id.toggleBtn)

        textureView.surfaceTextureListener = this

        toggleBtn.setOnClickListener {
            showEdges = !showEdges
            setShowEdges(showEdges)
            toggleBtn.text = if (showEdges) "Show: Edges" else "Show: Raw"
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }
    }

    // Texture listener stubs
    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        Camera2Helper.openCamera(this, textureView)
        // pass surface to GL renderer if needed
    }
    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}
    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        Camera2Helper.closeCamera()
        return true
    }
    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
        // get frame bitmap and send to native or use SurfaceTexture -> GL pipeline
        val bitmap = textureView.bitmap ?: return
        // Convert and pass to native (we'll implement a fast path later)
        sendBitmapToNative(bitmap)
    }

    private external fun sendBitmapToNative(bitmap: android.graphics.Bitmap)
    private external fun setShowEdges(enabled: Boolean)

    // Optional: update FPS from native via a callback or a polling function
}
