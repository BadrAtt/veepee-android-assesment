package com.vp.list.model

import com.google.gson.annotations.SerializedName

open class SearchResponse {

    @SerializedName("Search")
    val search: List<ListItem> = arrayListOf()

    @SerializedName("Response")
    val response: Boolean = false

    val totalResults = 0
    fun hasResponse(): Boolean {
        return POSITIVE_RESPONSE == response
    }

    companion object {
        private const val POSITIVE_RESPONSE = true
    }
}