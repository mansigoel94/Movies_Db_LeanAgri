package com.example.mansi_goel_test.model.detail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DetailMainModel {
    @SerializedName("id")
    @Expose
    val id: Integer? = null
    @SerializedName("overview")
    @Expose
    var overview: String? = null
    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null
    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null
    @SerializedName("runtime")
    @Expose
    var runtime: Integer? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("vote_average")
    @Expose
    var voteAverage: Double? = null
}