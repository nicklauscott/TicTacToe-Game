package com.example.daggerhilttemplate

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.daggerhilttemplate.constant.ServerAddressPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class Repository @Inject constructor(private val dataStore: DataStore<Preferences>) {

    var serverPreference: ServerAddressPreference = ServerAddressPreference()

    init {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            dataStore.data.collect { data ->
                val preference = data.asMap()
                if (preference.isNotEmpty()) {
                    var isLocalHost = true
                    try {
                        isLocalHost = preference[LOCAL_HOST] as Boolean
                    }catch (_: Exception) { }
                    serverPreference = ServerAddressPreference(
                        isLocalHost = isLocalHost,
                        host = preference[SERVER_HOST] as String,
                        port = preference[SERVER_PORT] as String,
                        path = preference[SERVER_PATH] as String,
                        webHost = preference[WEB_HOST] as String
                    )
                }
            }
        }
    }

    suspend fun setServerAddress(isLocalHost: Boolean,
                                 host: String? = null, port: String? = null, path: String? = null,
                                 webHost: String? = null) {
        Log.d("KtorRealtimeMessagingClient", "response fun call: $isLocalHost $host $port $path $webHost")
        dataStore.edit {
                it[LOCAL_HOST] = isLocalHost
                it[SERVER_HOST] = host ?: ""
                it[SERVER_PORT] = port ?: ""
                it[SERVER_PATH] = path ?: ""
                it[WEB_HOST] = webHost ?: ""
        }
        dataStore.data.collect{
            Log.d("KtorRealtimeMessagingClient", "response repo: $it")
        }
    }

    companion object {
        private val LOCAL_HOST = booleanPreferencesKey("server_host_type")
        private val SERVER_HOST = stringPreferencesKey("server_host")
        private val SERVER_PORT = stringPreferencesKey("server_port")
        private val SERVER_PATH = stringPreferencesKey("server_path")
        private val WEB_HOST = stringPreferencesKey("server_web_host")
    }
}