package com.example.savewith_android

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8000/"

    fun createApiService(context: Context): ApiService {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(Interceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()

                // Add the token to requests that are not for signup or login
                if (original.url.encodedPath != "/api/signup/" && original.url.encodedPath != "/api/login/") {
                    val token = SharedPrefManager.getToken(context)
                    if (token != null) {
                        requestBuilder.header("Authorization", "Token $token")
                    }
                }

                val request = requestBuilder.build()
                chain.proceed(request)
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor { getAuthToken() })
        .build()

//    val apiService: ApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(ApiService::class.java)
//    }

//    val apiService: ApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient)
//            .build()
//            .create(ApiService::class.java)
//    }
//
    val guardianService: GuardianApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(GuardianApiService::class.java)
    }

//    val userService: UserApiService by lazy {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        retrofit.create(UserApiService::class.java)
//    }

    private fun getAuthToken(): String? {
        // Retrieve the token from SharedPreferences or other secure storage
        val sharedPreferences = MyApplication.appContext.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }
}

class AuthInterceptor(private val tokenProvider: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val token = tokenProvider()
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}

//object ApiClient {
//    private const val BASE_URL = "http://10.0.2.2:8000/"
//
//    fun createApiService(context: Context): ApiService {
//        val logging = HttpLoggingInterceptor()
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
//
//        val client = OkHttpClient.Builder()
//            .addInterceptor(logging)
//            .addInterceptor(Interceptor { chain ->
//                val original = chain.request()
//                val requestBuilder = original.newBuilder()
//
//                // Add the token to requests that are not for signup or login
//                if (original.url.encodedPath != "/api/signup/" && original.url.encodedPath != "/api/login/") {
//                    val token = SharedPrefManager.getToken(context)
//                    if (token != null) {
//                        requestBuilder.header("Authorization", "Token $token")
//                    }
//                }
//
//                val request = requestBuilder.build()
//                chain.proceed(request)
//            })
//            .build()
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(client)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        return retrofit.create(ApiService::class.java)
//    }
//
//    fun isNetworkAvailable(context: Context): Boolean {
//        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
//        return activeNetwork?.isConnected == true
//    }
//}

object SharedPrefManager {
    private const val PREF_NAME = "my_shared_preferences"
    private const val KEY_TOKEN = "token"

    fun saveToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_TOKEN, token)
        editor.apply()
    }

    fun getToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_TOKEN, null)
    }
    fun clearToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(KEY_TOKEN)
        editor.apply()
    }
}