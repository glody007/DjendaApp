package com.jjenda.dagger

import com.jjenda.reseau.Repository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(target: Repository)
}