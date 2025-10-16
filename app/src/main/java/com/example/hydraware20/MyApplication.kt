package com.example.hydraware20

import android.app.Application

class MyApplication : Application() {
    /**
     * Se crea una instancia única del repositorio usando 'lazy'.
     * Esto asegura que el repositorio solo se crea una vez, la primera vez que se necesita,
     * y vivirá mientras la aplicación esté activa.
     */
    val userRepository by lazy {
        UserRepository(applicationContext)
    }
}
