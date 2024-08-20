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

data class SignUpRequest(
    val signup_id: String,
    val password: String,
    val profile: Profile
)

data class SignUpResponse(
    val profile: Profile,
    val token: String)

data class ProfileResponse(
    val signup_id: String,
    val profile: Profile
)
data class PasswordChangeRequest(
    val current_password: String,
    val new_password: String
)

interface ApiService {
    @POST("api/signup/")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>
    @POST("api/login/")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
    @GET("api/profile/")
    fun getUserProfile(): Call<ProfileResponse>
    @PATCH("api/profile/")
    fun updateUserProfile(@Body profileUpdateRequest: ProfileUpdateRequest): Call<ProfileResponse>
    @PUT("api/change-password/")
    fun changePassword(
        @Header("Authorization") token: String,
        @Body passwordChangeRequest: PasswordChangeRequest
    ): Call<Void>
}
