package com.devdcrq.usastateparks.api

import com.devdcrq.usastateparks.model.ParksResponse
import com.devdcrq.usastateparks.util.Constants.API_KEY
import com.devdcrq.usastateparks.util.Constants.PARKS_RESULT_LIMIT_PARAM
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ParksApi {

    @GET("parks")
    suspend fun getAllParks(
        @Query("stateCode") stateCode: String = "",
        @Query("api_key") apiKey: String = API_KEY
    ) : Response<ParksResponse>

    @GET("parks")
    suspend fun searchParks(
        @Query("q") searchQuery: String,
        @Query("limit") limit: Int = PARKS_RESULT_LIMIT_PARAM,
        @Query("api_key") apiKey: String = API_KEY
    ) : Response<ParksResponse>
}