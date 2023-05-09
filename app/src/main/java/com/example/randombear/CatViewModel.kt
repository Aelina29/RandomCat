package com.example.randombear

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel

class CatViewModel(application: Application) : AndroidViewModel(application) {
    var catImage : Bitmap? = null
}