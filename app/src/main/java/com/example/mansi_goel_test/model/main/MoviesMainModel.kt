package com.example.mansi_goel_test.model.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class MoviesMainModel {
    @Expose
    public var page: Int? = null
    @SerializedName("total_results")
    @Expose
    public var totalResults: Int? = null
    @SerializedName("total_pages")
    @Expose
    public var totalPages: Int? = null
    @SerializedName("results")
    @Expose
    public var results: List<ResultModel>? = null


}