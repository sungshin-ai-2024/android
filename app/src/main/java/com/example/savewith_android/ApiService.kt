package com.example.savewith_android

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.PATCH
import retrofit2.http.Path

data class Profile(
    val signup_id: String,
    val signup_pw: String,
    val signup_name: String,
    val phone_number: String,
    val birth_date: String,
    val sex: String,
    val zipcode: String,
    val address: String,
    val detailed_address: String
)
data class ProfileUpdateRequest(
    val profile: Profile
)
data class LoginRequest(
    val signup_id: String,
    val password: String
)

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
data class Guardian(
    val id: Int?,  // null 가능성을 유지
    val name: String,
    val phone_number: String,
    val relationship: String,
    val photoUrl: String? = null
)
data class ItemGuardian(
    val name: String,
    val phone_number: String,
    val relationship: String
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

    @GET("api/guardians/")
    fun getGuardians(@Header("Authorization") token: String): Call<List<Guardian>>

    @POST("api/guardians/")
    fun addGuardian(@Header("Authorization") token: String, @Body guardian: Guardian): Call<Guardian>


    @DELETE("api/guardians/{id}/")
    fun deleteGuardian(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<Void>

    @GET("api/guardians/{guardianId}/")
    fun getGuardian(@Header("Authorization") token: String, @Path("guardianId") guardianId: Int): Call<Guardian>

    @PATCH("api/guardians/{guardianId}/")
    fun updateGuardian(@Header("Authorization") token: String, @Path("guardianId") guardianId: Int, @Body guardian: Guardian): Call<Guardian>
}

