package com.example.daggerhilttemplate.page.splash

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daggerhilttemplate.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.request
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(private val client: HttpClient,
                                                dataStore: DataStore<Preferences>) : ViewModel()  {

    private val repository = Repository(dataStore)

    suspend fun pingServer(onFailure: () -> Unit, onSuccess: () -> Unit) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    if (repository.serverPreference.isLocalHost) {
                        val response = client.request {
                            url {
                                protocol = URLProtocol.HTTP
                                host = repository.serverPreference.host
                                port = repository.serverPreference.port.toInt()
                                encodedPath = "/${repository.serverPreference.path}"
                            }
                            method = HttpMethod.Get
                        }
                        if (response.bodyAsText() == "active") {
                            withContext(Dispatchers.Main) {
                                onSuccess()
                            }
                        }else {
                            withContext(Dispatchers.Main) {
                                onFailure()
                            }
                        }
                    }else {
                        val response = client.get(repository.serverPreference.webHost)
                        if (response.bodyAsText() == "active") {
                            withContext(Dispatchers.Main) {
                                onSuccess()
                            }
                        }else {
                            withContext(Dispatchers.Main) {
                                onFailure()
                            }
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        onFailure()
                    }
                }
            }
        }
}