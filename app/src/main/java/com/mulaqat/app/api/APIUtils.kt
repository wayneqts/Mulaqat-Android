package com.mulaqat.app.api

object APIUtils {
    const val API_endpoint = "https://mulaqat.app/apis/"

    fun getAPIService(): AllNameAPI {
        return RetrofitClient.getClient(API_endpoint).create(AllNameAPI::class.java)
    }
}