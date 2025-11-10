package com.example.asn8

import kotlinx.coroutines.flow.Flow

class ContactRepository(private val contactDao: ContactDao) {

    suspend fun insert(contact: Contact) = contactDao.insert(contact)

    suspend fun delete(contact: Contact) = contactDao.delete(contact)

    fun getAllContacts(): Flow<List<Contact>> =
        contactDao.getAllContacts()

    fun findContacts(searchQuery: String): Flow<List<Contact>> =
        contactDao.findContacts(searchQuery)

    fun getContactsSortedByNameDesc(): Flow<List<Contact>> =
        contactDao.getContactsSortedByNameDesc()

    fun getContactsSortedByNameAsc(): Flow<List<Contact>> =
        contactDao.getContactsSortedByNameAsc()
}
