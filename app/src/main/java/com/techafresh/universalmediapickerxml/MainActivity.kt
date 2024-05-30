package com.techafresh.universalmediapickerxml

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.compose.rememberImagePainter
import coil.decode.VideoFrameDecoder
import coil.load
import coil.request.videoFrameMillis
import coil.request.videoFramePercent
import com.robertlevonyan.components.picker.ItemModel
import com.robertlevonyan.components.picker.ItemType
import com.robertlevonyan.components.picker.MimeType
import com.robertlevonyan.components.picker.PickerDialog
import com.robertlevonyan.components.picker.ShapeType
import com.robertlevonyan.components.picker.*
import com.robertlevonyan.components.picker.pickerDialog
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btnPicker).setOnClickListener {
            pickerDialog {
            setTitle(R.string.app_name)
            setTitleTextBold(true)
            setTitleTextSize(22f)
            setTitleGravity(Gravity.END)
            setItems(
             setOf(
               ItemModel(ItemType.Camera, backgroundType = ShapeType.TYPE_SQUARE),
               ItemModel(ItemType.Video),
               ItemModel(ItemType.ImageGallery(MimeType.Image.Png)),
               ItemModel(ItemType.VideoGallery()),
               ItemModel(ItemType.AudioGallery(MimeType.Audio.Mp3)),
               ItemModel(ItemType.Files()),
             )
            )
            setListType(PickerDialog.ListType.TYPE_GRID)
            }.setPickerCloseListener { type, uris ->
            val ivPreview = findViewById<ImageView>(R.id.ivPreview)
            when (type) {
             ItemType.Camera -> ivPreview.load(uris.first())
              ItemType.Video -> {
               ivPreview.load(uris.first()) {
//                   videoFrameMillis(1000)
//                   videoFramePercent(0.5)
                   decoderFactory { result, options, _ -> VideoFrameDecoder(result.source, options)}
//                   fetcher(VideoFrameUriFetcher(this@MainActivity))
               }
               ivPreview.setOnClickListener{
                 Intent(Intent.ACTION_VIEW)
                   .apply {
                     setDataAndType(uris.first(), "video/mp4")
                     addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                   .let { startActivity(it) }
               }
             }
             is ItemType.ImageGallery -> {
                println(uris.toTypedArray().contentToString())
               ivPreview.load(uris.first())
             }
             is ItemType.AudioGallery -> {
               println(uris.toTypedArray().contentToString())
             }
             is ItemType.VideoGallery -> {
               println(uris.toTypedArray().contentToString())
               ivPreview.load(uris.first()){
//                   videoFrameMillis(1000)
//                   videoFramePercent(0.5)
                   decoderFactory { result, options, _ -> VideoFrameDecoder(result.source, options)}
//                   VideoFrameUriFetcher(this@MainActivity)
//                   fetcher(VideoFrameUriFetcher(this@MainActivity))
               }
               ivPreview.setOnClickListener {
                 Intent(Intent.ACTION_VIEW)
                   .apply {
//                     val mimeTypes = type.mimeTypes
//                     if (mimeTypes.size == 1) {
                       setDataAndType(uris.first(), "*/*")
//                     } else {
//                       setType("*/*")
//                       putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes.map { it.type }.toTypedArray())
//                     }
                     addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                   }
                    .let { startActivity(it) }
               }
             }
             is ItemType.Files -> println(uris.toTypedArray().contentToString())
            }
            }.show()
   }

    }
}