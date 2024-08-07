package com.example.savewith_android

import okhttp3.Address

data class UserData(
    val token: String,
    val name: String,
    val id: String,
    val phone: String,
    val birth: String,
    // 회원가입 할때, 생년월일을 String "MM/dd/yyyy" 형식으로 받아옴
    // 생년월일을 LocalDate로 선언하는 것은 굳이..
    val gender: String,
    val address1: String, // 수정 필요할 수도..? 'val address1: Address'
    val address2: String, // 수정 필요할 수도..?
    val address3: String, // 수정 필요할 수도..?
    val photoUrl: String? // 이미지 URL
)

data class LoginResponse(
    val token: String,
    val userId: String,
    val username: String
)

data class LoginRequest(
    val userId: String,
    val password: String
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

//data class LoginRequest(
//    val signup_id: String,
//    val password: String)
//
//data class LoginResponse(
//    val token: String,
//    val userName: String,
//    val userPhone: String
//)

//data class SignUpResponse(
//    val user: User,
//    val token: String)

//data class ProfileResponse(
//    val signup_id: String,
//    val profile: Profile
//)

data class Profile(
    val signup_name: String,
    val phone_number: String,
    val birth_date: String,
    val sex: String,
    val zipcode: String,
    val address: String,
    val detailed_address: String
)

data class User(
    val id: Int,
    val signup_id: String,
    val signup_name: String,
    val phone_number: String,
    val birth_date: String,
    val sex: String,
    val address: String,
    val detailed_address: String
) // 유저 structure