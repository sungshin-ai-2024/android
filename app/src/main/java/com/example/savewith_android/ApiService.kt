package com.example.savewith_android

import android.provider.ContactsContract
import com.google.firebase.firestore.auth.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT

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
    @GET("api/guardians/")
    fun getGuardians(@Header("Authorization") token: String): Call<List<Guardian>>
    @DELETE("api/user/delete/") // 데이터 삭제
    fun deleteUser(@Header("Authorization") token: String): Call<DeleteResponse>

    // API 메소드 정의
    @GET("api/guardian/data") // 보호자 정보 가져오기 v1
    suspend fun getGuardianData(): Response<List<ItemGuardian>>

    @GET("api/guardian") // 보호자정보 가져오기 v2
    fun getGuardianInfo(@Header("Authorization") token: String): Call<GuardianResponse>

    @PUT("api/profile/") //
    fun updateUserProfile(@Header("Authorization") token: String, @Body updateRequest: UserProfileUpdateRequest): Response<Void>

    @PUT("api/guardian/")
    fun updateGuardianInfo(@Header("Authorization") token: String, @Body updateRequest: GuardianUpdateRequest): Response<Void>

    @POST("send_verification_code/")
    suspend fun sendVerificationCode(@Body phoneNumberRequest: PhoneNumberRequest): Response<Void>

    @POST("verify_code/")
    suspend fun verifyCode(@Header("Authorization") authHeader: String, @Body verificationCodeRequest: VerificationCodeRequest): Response<Void>

    @POST("update_phone_number/")
    suspend fun updatePhoneNumber(@Header("Authorization") authHeader: String, @Body phoneNumberRequest: PhoneNumberRequest): Response<Void>
}

//data class PhoneNumberRequest(val phoneNumber: String)
//data class VerificationCodeRequest(val verificationCode: String)