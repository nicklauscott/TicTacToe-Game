package com.example.daggerhilttemplate.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.daggerhilttemplate.Repository
import com.example.daggerhilttemplate.data.KtorRealtimeMessagingClient
import com.example.daggerhilttemplate.data.RealtimeMessagingClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import java.util.prefs.Preferences
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideDatastore(@ApplicationContext context: Context)
    : DataStore<androidx.datastore.preferences.core.Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("server_address")
        }
    }

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient{
        return HttpClient(CIO) {
            install(WebSockets)
        }
    }

    @Singleton
    @Provides
    fun provideRealTimeMessagingClient(httpClient: HttpClient, dataStore: DataStore<androidx.datastore.preferences.core.Preferences>)
    : RealtimeMessagingClient{
        return KtorRealtimeMessagingClient(httpClient, dataStore)
    }

}