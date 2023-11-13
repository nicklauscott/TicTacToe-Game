package com.example.daggerhilttemplate.page.settings

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daggerhilttemplate.Repository
import com.example.daggerhilttemplate.constant.ServerAddressPreference
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
class SettingsViewModel @Inject constructor(private val client: HttpClient, dataStore: DataStore<Preferences>): ViewModel() {

    val repository = Repository(dataStore)

    fun updateLocalAddress(serverAddressPreference: ServerAddressPreference,
                      onFailure: (String) -> Unit,
                      onSuccess: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = client.request {
                    url {
                        protocol = URLProtocol.HTTP
                        host = serverAddressPreference.host
                        port = serverAddressPreference.port.toInt()
                        encodedPath = "/${serverAddressPreference.path}"
                    }
                    method = HttpMethod.Get
                }
                if (response.bodyAsText() == "active") {
                    withContext(Dispatchers.Main) {
                        withContext(Dispatchers.Main) {
                            onSuccess(
                                "${serverAddressPreference.host}:${serverAddressPreference.port}/${serverAddressPreference.path}"
                            )
                        }
                        repository.setServerAddress(
                            isLocalHost = true,
                            host = serverAddressPreference.host,
                            port = serverAddressPreference.port,
                            path = serverAddressPreference.path
                        )
                    }
                }else {
                    withContext(Dispatchers.Main) {
                        onFailure("Couldn't connect to the right server")
                    }
                }
            }catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure(ex.message.toString())
                }
            }
        }
    }


    fun updateWebAddress(serverAddressPreference: ServerAddressPreference,
                           onFailure: (String) -> Unit,
                           onSuccess: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = client.get(serverAddressPreference.webHost)
                if (response.bodyAsText() == "active") {
                    //Log.d("KtorRealtimeMessagingClient", "response if: ${response.bodyAsText()}")
                    withContext(Dispatchers.Main) {
                        withContext(Dispatchers.Main) {
                            onSuccess(serverAddressPreference.webHost)
                        }
                        repository.setServerAddress(
                            isLocalHost = false,
                            webHost = serverAddressPreference.webHost
                        )
                    }
                }else {
                    withContext(Dispatchers.Main) {
                        onFailure("Couldn't connect to the right server")
                    }
                }
            }catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure(ex.message.toString())
                }
            }
        }
    }

}