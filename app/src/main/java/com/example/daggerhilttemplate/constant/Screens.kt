package com.example.daggerhilttemplate.constant

enum class Screens(val route: String) {
    SplashScreen("splash"),
    HomeScreen("home"),
    SettingsScreen("settings");

    fun withArg(vararg args: String): String{
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}