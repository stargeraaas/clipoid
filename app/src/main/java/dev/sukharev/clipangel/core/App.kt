package dev.sukharev.clipangel.core

import android.app.Application
import android.preference.PreferenceManager
import androidx.room.Room
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dev.sukharev.clipangel.data.local.database.ClipAngelDatabase
import dev.sukharev.clipangel.data.local.repository.channel.ChannelRepository
import dev.sukharev.clipangel.data.local.repository.channel.ChannelRepositoryImpl
import dev.sukharev.clipangel.data.remote.repository.channel.ChannelRemoteRepository
import dev.sukharev.clipangel.data.remote.repository.channel.ChannelRemoteRepositoryImpl
import dev.sukharev.clipangel.domain.channel.ChannelInteractor
import dev.sukharev.clipangel.domain.channel.ChannelInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.bind
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
            modules(listOf(repositories, database, useCases))
        }
    }

    val repositories = module {
        single { ChannelRepositoryImpl(get()) } bind ChannelRepository::class
        single { ChannelRemoteRepositoryImpl(get()) } bind ChannelRemoteRepository::class
    }

    val useCases = module {
        factory { ChannelInteractorImpl(get(), get()) } bind ChannelInteractor::class
    }

    val database = module {
         single { Room.databaseBuilder(applicationContext,
                ClipAngelDatabase::class.java, "clip_angel.db").build()
        }

        single { get<ClipAngelDatabase>().getChannelDao() }

        single { Firebase.database }

        single {  }
    }

}