package dev.sukharev.clipangel.core

import android.app.Application
import androidx.room.Room
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dev.sukharev.clipangel.data.local.database.ClipAngelDatabase
import dev.sukharev.clipangel.data.local.repository.channel.ChannelRepository
import dev.sukharev.clipangel.data.local.repository.channel.ChannelRepositoryImpl
import dev.sukharev.clipangel.data.local.repository.clip.ClipRepository
import dev.sukharev.clipangel.data.local.repository.clip.ClipRepositoryImpl
import dev.sukharev.clipangel.data.local.repository.credentials.Credentials
import dev.sukharev.clipangel.data.local.repository.credentials.CredentialsClipAngel
import dev.sukharev.clipangel.data.remote.repository.channel.ChannelRemoteRepository
import dev.sukharev.clipangel.data.remote.repository.channel.ChannelRemoteRepositoryImpl
import dev.sukharev.clipangel.data.remote.repository.clip.ClipRemoteRepository
import dev.sukharev.clipangel.data.remote.repository.clip.ClipRemoteRepositoryImpl
import dev.sukharev.clipangel.domain.channel.ChannelInteractor
import dev.sukharev.clipangel.domain.channel.ChannelInteractorImpl
import dev.sukharev.clipangel.domain.clip.create.CreateClipInteractor
import dev.sukharev.clipangel.domain.clip.create.CreateClipInteractorImpl
import dev.sukharev.clipangel.presentation.fragments.cliplist.ClipListViewModel
import dev.sukharev.clipangel.presentation.viewmodels.channellist.ChannelListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
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
        FirebaseDatabase.getInstance().setPersistenceEnabled(false)
        DatabaseReference.goOnline()
        startKoin {
            androidContext(this@App)
            modules(listOf(repositories, database, useCases, viewModels))
        }

    }

    val viewModels = module {
        viewModel { ChannelListViewModel(get()) }
        viewModel { ClipListViewModel(get(), get()) }
    }

    val repositories = module {
        single { ChannelRepositoryImpl(get()) } bind ChannelRepository::class
        single { ChannelRemoteRepositoryImpl(get(), get()) } bind ChannelRemoteRepository::class
        single { CredentialsClipAngel(get()) } bind Credentials::class
        single { ClipRemoteRepositoryImpl(get(), get()) } bind ClipRemoteRepository::class
        single { ClipRepositoryImpl(get(), get()) } bind ClipRepository::class
    }

    val useCases = module {
        factory { ChannelInteractorImpl(get(), get(), get()) } bind ChannelInteractor::class
        factory { CreateClipInteractorImpl(get(), get()) } bind CreateClipInteractor::class
    }

    val database = module {
        single {
            Room.databaseBuilder(applicationContext,
                    ClipAngelDatabase::class.java, "clip_angel.db").build()
        }

        single { get<ClipAngelDatabase>().getChannelDao() }

        single { Firebase.database.apply {
            setPersistenceEnabled(false)
        } } bind FirebaseDatabase::class

        single { get<ClipAngelDatabase>().getClipDao() }

    }

}