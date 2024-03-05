package com.goudurixx.pokedex.core.utils


fun String.trimIntFromUrl(): Int{
    return this.substringBeforeLast("/").substringAfterLast("/").toInt()
}