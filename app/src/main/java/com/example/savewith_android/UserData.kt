package com.example.savewith_android

import okhttp3.Address

data class DeleteResponse(
    val success: Boolean,
    val message: String
)
data class Guardian(
    val name: String,
    val phone: String,
    val relation: String,
    val photoUrl: String
)

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
    val user: Profile,
    val token: String)

data class ProfileResponse(
    val signup_id: String,
    val profile: Profile
)

data class GuardianResponse(
    val name: String,
    val phone: String,
    val relation: String,
    val photoUrl: String? // 이미지 URL
)
data class UserProfileUpdateRequest(
    val name: String,
    val id: String,
    val phone: String,
    val birth: String,
    val gender: String,
    val address1: String,
    val address2: String,
    val address3: String,
    val photoUrl: String? // 이미지 URL
)

data class GuardianUpdateRequest(
    val name: String,
    val phone: String,
    val relation: String,
    val photoUrl: String? // 이미지 URL
)
data class PhoneNumberRequest(val phoneNumber: String)
data class VerificationCodeRequest(val verificationCode: String)
// PasswordChangeRequest.kt
data class PasswordChangeRequest(
    val currentPassword: String,
    val newPassword: String
)
// ApiResponse.kt
data class ApiResponse(
    val success: Boolean,
    val message: String
)

//data class User( // 규원
//    val id: Int,
//    val signup_id: String,
//    val signup_name: String,
//    val phone_number: String,
//    val birth_date: String,
//    val sex: String,
//    val address: String,
//    val detailed_address: String
//) // 유저 structure