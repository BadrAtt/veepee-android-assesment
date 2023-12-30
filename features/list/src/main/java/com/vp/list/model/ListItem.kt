package com.vp.list.model

import com.google.gson.annotations.SerializedName

data class ListItem(
    @SerializedName("Title")
    val title: String,
    @SerializedName("Year")
    val year: String,
    @SerializedName("Poster")
    val poster: String,
    val imdbID: String,
)