package com.example.savewith_android

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