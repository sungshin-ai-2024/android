package com.example.savewith_android

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ThreatContactViewModel (application: Application) : AndroidViewModel(application) {

    //private val db = Room.databaseBuilder(application, AppDatabase::class.java, "contacts-db").build()
    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> get() = _contacts

    init {
        loadContacts()
    }

    private fun loadContacts() {
        /*Executors.newSingleThreadExecutor().execute {
            val contactList = db.contactDao().getAllContacts()
            _contacts.postValue(contactList)
        }*/
    }
}