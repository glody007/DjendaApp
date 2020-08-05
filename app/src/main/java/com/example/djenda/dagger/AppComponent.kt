package com.example.djenda.dagger

import com.example.djenda.reseau.Repository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(target: Repository)
}