package com.example.savewith_android

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    // API 메소드 정의
    @GET("api/users/")
    suspend fun getUsers(): List<UserData>

    @GET("api/users")
    suspend fun getUserData(@Header("Authorization") token: String): Response<UserData> // Response<List<UserData>>
    @GET("api/users")
    suspend fun getUserData(): Response<UserData> // Response<List<UserData>>

    @GET("path/to/guardian/data") // 적절한 API 경로로 대체
    suspend fun getGuardianData(): Response<List<ItemGuardian>>

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("profile")
    fun getUserProfile(@Header("Authorization") token: String): Response<UserProfileResponse>

    @GET("guardian")
    fun getGuardianInfo(@Header("Authorization") token: String): Call<GuardianResponse>

    @PUT("profile")
    fun updateUserProfile(@Header("Authorization") token: String, @Body updateRequest: UserProfileUpdateRequest): Response<Void>

    @PUT("guardian")
    fun updateGuardianInfo(@Header("Authorization") token: String, @Body updateRequest: GuardianUpdateRequest): Response<Void>

//    @POST("send_verification_code/")
//    suspend fun sendVerificationCode(@Body phoneNumber: String): Response<Void>
//
//    @POST("verify_code/")
//    suspend fun verifyCode(@Header("Authorization") authHeader: String, @Body verificationCode: String): Response<Void>
//
//    @POST("update_phone_number/")
//    suspend fun updatePhoneNumber(@Header("Authorization") authHeader: String, @Body phoneNumber: String): Response<Void>
    @POST("send_verification_code/")
    suspend fun sendVerificationCode(@Body phoneNumberRequest: PhoneNumberRequest): Response<Void>

    @POST("verify_code/")
    suspend fun verifyCode(@Header("Authorization") authHeader: String, @Body verificationCodeRequest: VerificationCodeRequest): Response<Void>

    @POST("update_phone_number/")
    suspend fun updatePhoneNumber(@Header("Authorization") authHeader: String, @Body phoneNumberRequest: PhoneNumberRequest): Response<Void>

    @PUT("user/change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body passwordChangeRequest: PasswordChangeRequest
    ): Response<ApiResponse>

    @DELETE("user")
    suspend fun deleteUser(@Header("Authorization") token: String): Response<ApiResponse>
}

//data class PhoneNumberRequest(val phoneNumber: String)
//data class VerificationCodeRequest(val verificationCode: String)