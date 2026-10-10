package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide

import android.app.Application
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.di.AppContainer

class NewsApplication : Application() {
    val container = AppContainer()
}
