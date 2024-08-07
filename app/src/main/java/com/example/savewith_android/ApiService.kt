package com.example.savewith_android

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.PATCH





data class LoginRequest(
    val signup_id: String,
    val password: String)
data class LoginResponse(
    val token: String,
    val userName: String,
    val userPhone: String
)
data class SignUpResponse(
    val user: User,
    val token: String)

data class ProfileResponse(
    val signup_id: String,
    val profile: Profile
)

interface ApiService {
    @POST("api/signup/")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>
    @POST("api/login/")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
    @GET("api/profile/")
    fun getUserProfile(@Header("Authorization") token: String): Call<ProfileResponse>

}
