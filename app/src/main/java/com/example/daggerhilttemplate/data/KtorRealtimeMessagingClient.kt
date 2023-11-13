package com.example.daggerhilttemplate.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.daggerhilttemplate.Repository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class KtorRealtimeMessagingClient (
    private val client: HttpClient,
    dataStore: DataStore<Preferences>
): RealtimeMessagingClient {

    private val repository = Repository(dataStore).serverPreference
    private var session: WebSocketSession? = null

    init {
        Log.d("KtorRealtimeMessagingClient", "$repository")
    }
    override fun getGameStateStream(): Flow<GameState> {
        if (repository.isLocalHost) {
            Log.d("KtorRealtimeMessagingClient", "WS = LOCAL: $repository")
            return flow {
                session = client.webSocketSession(
                    method = HttpMethod.Get,
                    host = repository.host,
                    port = repository.port.toInt(),
                    path = "/play"
                )
                val gameState = session!!
                    .incoming
                    .consumeAsFlow()
                    .filterIsInstance<Frame.Text>()
                    .mapNotNull { Json.decodeFromString<GameState>(it.readText()) }
                emitAll(gameState)
            }
        }else {
            Log.d("KtorRealtimeMessagingClient", "WS = WEB: ${repository.webHost.split("/")[0]}/play")
            return flow {
                session = client.webSocketSession(
                    method = HttpMethod.Get,
                    host = repository.webHost.split("/")[0],
                    path = "/play"
                )
                val gameState = session!!
                    .incoming
                    .consumeAsFlow()
                    .filterIsInstance<Frame.Text>()
                    .mapNotNull { Json.decodeFromString<GameState>(it.readText()) }
                emitAll(gameState)
            }
        }
    }

    override suspend fun sendAction(action: MakeTurn) {
        session?.outgoing?.send(
            Frame.Text("make_turn#${Json.encodeToString(action)}")
        )
    }

    override suspend fun close() {
        session?.close()
        session = null
    }
}