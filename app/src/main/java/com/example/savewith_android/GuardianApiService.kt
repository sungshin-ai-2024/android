package com.example.savewith_android
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface GuardianApiService {
    @GET("api/guardians/")
    suspend fun getGuardians(): List<ItemGuardian>

    @GET("path/to/guardian/data") // 적절한 API 경로로 대체
    suspend fun getGuardianData(@Header("Authorization") token: String): Response<List<ItemGuardian>>
}

class GuardianRepository {
    private val service = ApiClient.guardianService

    suspend fun fetchGuardians(): List<ItemGuardian> {
        return try {
            val response = service.getGuardians()
            if (response.isEmpty()) {
                // 빈 배열에 대한 처리 (예: 기본값 제공, 오류 메시지 표시 등)
                emptyList()
            } else {
                response
            }
        } catch (e: Exception) {
            // 오류 처리 (예: 로그 남기기, 사용자에게 오류 메시지 표시 등)
            emptyList()
        }
    }
}