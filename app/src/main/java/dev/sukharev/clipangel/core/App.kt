package dev.sukharev.clipangel.core

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.sukharev.clipangel.data.local.database.ClipAngelDatabase
import dev.sukharev.clipangel.data.local.repository.ChannelRepository
import dev.sukharev.clipangel.data.local.repository.ChannelRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.definition.BeanDefinition
import org.koin.dsl.module


class App : Application() {

    companion object {
        lateinit var app: Application
        private set
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        startKoin {
            androidContext(this@App)
            modules(listOf(repositories))
        }
    }

    val repositories = module {
        this.single { ChannelRepositoryImpl() } as ChannelRepository
    }

    val database = module {
         single { Room.databaseBuilder(applicationContext,
                ClipAngelDatabase::class.java, "clip_angel.db").build()
        }

        single {  }

        single { }
    }

}