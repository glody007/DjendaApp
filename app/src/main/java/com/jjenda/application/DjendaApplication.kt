package com.jjenda.application

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.jjenda.dagger.AppComponent
import com.jjenda.dagger.AppModule
import com.jjenda.dagger.DaggerAppComponent
import com.segment.analytics.Analytics
import com.segment.analytics.Traits
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

        // Create an analytics client with the given context and Segment write key.
        val analytics = Analytics.Builder(this, "GNSKac5PW8zjQd4WtRMhK0qlIgb7LeaA")
                .trackApplicationLifecycleEvents() // Enable this to record certain application events automatically!
                .recordScreenViews() // Enable this to record screen views automatically!
                .build()

        // Set the initialized instance as a globally accessible instance.
        Analytics.setSingletonInstance(analytics)

        Analytics.with(this).track("Application Started");
    }

    private fun initDagger(app: DjendaApplication): AppComponent =
            DaggerAppComponent.builder()
                    .appModule(AppModule(app))
                    .build()
}