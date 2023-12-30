package com.vp.core_models

import com.google.gson.annotations.SerializedName
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class MovieDetail : RealmObject {
    @SerializedName("Title")
    var title: String = ""

    @SerializedName("Year")
    var year: String = ""

    @SerializedName("Runtime")
    var runtime: String = ""

    @SerializedName("Director")
    var director: String = ""

    @SerializedName("Plot")
    var plot: String = ""

    @SerializedName("Poster")
    var poster: String = ""

    @PrimaryKey
    var movieId: String = ""

}