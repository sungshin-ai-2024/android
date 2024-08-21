package com.example.savewith_android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GuardianViewModel : ViewModel() {
    private val _guardians = MutableLiveData<List<Guardian>>()
    val guardians: LiveData<List<Guardian>> get() = _guardians

    fun setGuardians(guardianList: List<Guardian>) {
        _guardians.value = guardianList
    }
}
