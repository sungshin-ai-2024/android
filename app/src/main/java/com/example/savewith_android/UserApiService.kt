package com.example.savewith_android

import retrofit2.http.GET

interface UserApiService {
    @GET("api/users/")
    suspend fun getUsers(): List<UserData>
}