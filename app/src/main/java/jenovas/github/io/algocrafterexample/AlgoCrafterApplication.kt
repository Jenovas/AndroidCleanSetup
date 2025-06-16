package jenovas.github.io.algocrafterexample

import android.app.Application
import jenovas.github.io.algocrafterexample.di.domainModule
import jenovas.github.io.algocrafterexample.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class AlgoCrafterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AlgoCrafterApplication)
            modules(
                domainModule,
                viewModelModule,
            )
        }
    }
} 