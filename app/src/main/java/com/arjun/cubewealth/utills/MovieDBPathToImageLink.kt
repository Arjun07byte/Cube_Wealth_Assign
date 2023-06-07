package com.arjun.cubewealth.utills

object MovieDBPathToImageLink {
    fun convertPathToImage(givenPath: String?) : String = "https://image.tmdb.org/t/p/original$givenPath"
}