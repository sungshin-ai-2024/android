package com.example.savewith_android

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class GuardianInfo(
    val name: String,
    val phoneNumber: String,
)

class ThreatContactViewModel (application: Application) : AndroidViewModel(application) {

    //private val db = Room.databaseBuilder(application, AppDatabase::class.java, "contacts-db").build()
    private val _guardianList = MutableLiveData<List<GuardianInfo>>().apply {
        value = listOf(
            GuardianInfo("John", "010-1234-5678") // Example data
        ) // 초기값 설정
    }
    val guardianList: LiveData<List<GuardianInfo>> get() = _guardianList

    fun addGuardian(guardian: GuardianInfo) {
        val updatedList = _guardianList.value?.toMutableList() ?: mutableListOf()

        // 새로운 guardian을 리스트에 추가
        updatedList.add(guardian)

        // 업데이트된 리스트를 MutableLiveData에 반영
        _guardianList.value = updatedList
    }


    fun getGuardians(): LiveData<List<GuardianInfo>> = guardianList
}

    private fun loadContacts() {
        /*Executors.newSingleThreadExecutor().execute {
            val contactList = db.contactDao().getAllContacts()
            _contacts.postValue(contactList)
        }*/
    }
