package com.goudurixx.pokedex.core.utils

import java.util.Locale


fun String.trimIntFromUrl(): Int{
    return this.substringBeforeLast("/").substringAfterLast("/").toInt()
}

fun String.snakeCaseToLabel(): String {
    return this.split("_").joinToString(" ") { it.capitalize(Locale.ROOT) }
}