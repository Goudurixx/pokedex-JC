package com.goudurixx.pokedex.core.routing.utils

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

fun NavController.navigateSafely(
    route: String,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    try {
        this.navigate(route, navOptions, navigatorExtras)
    } catch (e: Exception) {
        Log.e(
            "NavController",
            "NavController.navigateSafely() => Exception raised : ${e.localizedMessage}",
            e
        )
    }
}
