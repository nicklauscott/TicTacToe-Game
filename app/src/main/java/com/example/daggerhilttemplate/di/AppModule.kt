package com.example.daggerhilttemplate.di

import com.example.daggerhilttemplate.data.KtorRealtimeMessagingClient
import com.example.daggerhilttemplate.data.RealtimeMessagingClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient{
        return HttpClient(CIO) {
            install(WebSockets)
        }
    }

    @Singleton
    @Provides
    fun provideRealTimeMessagingClient(httpClient: HttpClient)
    : RealtimeMessagingClient{
        return KtorRealtimeMessagingClient(httpClient)
    }
}