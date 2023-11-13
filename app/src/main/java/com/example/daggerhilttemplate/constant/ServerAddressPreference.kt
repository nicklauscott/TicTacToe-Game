package com.example.daggerhilttemplate.constant

data class ServerAddressPreference(
    val isLocalHost: Boolean = true,
    val host: String = "127.0.0.1",
    val port: String = "8080",
    val path: String = "ping",
    val webHost: String = ""
)
