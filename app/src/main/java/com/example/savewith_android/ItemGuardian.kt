package com.example.savewith_android

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class ItemGuardian(
    val grgName: String,
    val relation: String,
    val phone: String,
    val photoUrl: String? // 이미지 URL
)

@RequiresApi(Build.VERSION_CODES.O)
class ItemGuardianModel(application: Application) : AndroidViewModel(application) {
    private val _guardians = MutableLiveData<List<ItemGuardian>>()
    val guardians: LiveData<List<ItemGuardian>> get() = _guardians

    fun setGuardians(guardianList: List<ItemGuardian>) {
        _guardians.value = guardianList
    }

    fun fetchGuardians() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.guardianService.getGuardians()
                _guardians.postValue(response)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}