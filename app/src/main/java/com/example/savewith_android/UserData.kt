package com.example.savewith_android

import okhttp3.Address

/*data class UserData( // 규원이의 User와 동일한 역할
    val token: String,
    val Id: Int,
    val name: String,
    val id: String,
    val phone: String,
    val birth: String,
    val gender: String,
    val address: String,
    val detailed_address: String
//    val photoUrl: String? // 이미지 URL
)*/
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
    val token: String,
    val id: String,
    val signup_name: String,
    val phone_number: String,
    val birth_date: String,
    val gender: String,
    val zipcode: String,
    val address: String,
    val detailed_address: String
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


data class UserProfileResponse(
    val name: String,
    val id: String,
    val phone: String,
    val birth: String,
    val gender: String,
    val address: Address,
    val photoUrl: String? // 이미지 URL
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
data class Address(
    val address1: String,
    val address2: String,
    val address3: String
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