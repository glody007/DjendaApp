package com.example.djenda.application

import android.app.Application
import com.example.djenda.dagger.AppComponent
import com.example.djenda.dagger.AppModule
import com.example.djenda.dagger.DaggerAppComponent
import com.facebook.drawee.backends.pipeline.Fresco
import javax.inject.Singleton

@Singleton
class DjendaApplication : Application() {

    lateinit var djendaComponent: AppComponent

    companion object {
       lateinit var appComponent : AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        djendaComponent = initDagger(this)
        appComponent = djendaComponent

    }

    private fun initDagger(app: DjendaApplication): AppComponent =
            DaggerAppComponent.builder()
                    .appModule(AppModule(app))
                    .build()
}