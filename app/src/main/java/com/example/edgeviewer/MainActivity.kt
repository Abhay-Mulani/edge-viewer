package com.example.edgeviewer

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.provider.MediaStore
import android.view.TextureView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity(), TextureView.SurfaceTextureListener {

    private lateinit var textureView: TextureView
    private lateinit var fpsText: TextView
    private lateinit var toggleBtn: Button
    private lateinit var saveBtn: Button
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
        saveBtn = findViewById(R.id.saveBtn)

        textureView.surfaceTextureListener = this

        // toggle between RAW / Edge processed
        toggleBtn.setOnClickListener {
            showEdges = !showEdges
            setShowEdges(showEdges)
            toggleBtn.text = if (showEdges) "Show: Edges" else "Show: Raw"
        }

        // save the currently processed frame from TextureView
        saveBtn.setOnClickListener {
            val bmp = textureView.bitmap
            if (bmp != null) {
                saveBitmapToPictures(bmp, "edge_frame_${System.currentTimeMillis()}.png")
            } else {
                Toast.makeText(this, "No frame available", Toast.LENGTH_SHORT).show()
            }
        }

        // camera permission check
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                1
            )
        }
    }

    // TextureView callbacks
    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        Camera2Helper.openCamera(this, textureView)
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        Camera2Helper.closeCamera()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
        val bitmap = textureView.bitmap ?: return
        sendBitmapToNative(bitmap)
    }

    private external fun sendBitmapToNative(bitmap: Bitmap)
    private external fun setShowEdges(enabled: Boolean)

    // Save processed frame to Pictures/EdgeViewer/
    private fun saveBitmapToPictures(bitmap: Bitmap, filename: String) {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/EdgeViewer")
        }

        val resolver = contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        if (uri != null) {
            resolver.openOutputStream(uri).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            Toast.makeText(this, "Saved to Pictures/EdgeViewer", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }
}
