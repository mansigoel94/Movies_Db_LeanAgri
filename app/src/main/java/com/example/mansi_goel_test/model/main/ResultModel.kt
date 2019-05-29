package com.example.mansi_goel_test.model.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResultModel {
    @SerializedName("vote_count")
    @Expose
    public var voteCount: Integer? = null
    @SerializedName("id")
    @Expose
    public var id: Integer? = null
    @SerializedName("video")
    @Expose
    public var video: Boolean? = null
    @SerializedName("vote_average")
    @Expose
    public var voteAverage: Double? = null
    @SerializedName("title")
    @Expose
    public var title: String? = null
    @SerializedName("popularity")
    @Expose
    public var popularity: Double? = null
    @SerializedName("poster_path")
    @Expose
    public var posterPath: String? = null
    @SerializedName("original_language")
    @Expose
    public var originalLanguage: String? = null
    @SerializedName("original_title")
    @Expose
    public var originalTitle: String? = null
    @SerializedName("genre_ids")
    @Expose
    public var genreIds: List<Integer>? = null
    @SerializedName("backdrop_path")
    @Expose
    public var backdropPath: String? = null
    @SerializedName("adult")
    @Expose
    public var adult: Boolean? = null
    @SerializedName("overview")
    @Expose
    public var overview: String? = null
    @SerializedName("release_date")
    @Expose
    public var releaseDate: String? = null
}