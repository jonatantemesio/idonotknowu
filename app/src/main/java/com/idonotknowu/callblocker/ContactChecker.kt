package com.idonotknowu.callblocker

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract

object ContactChecker {
    /** True if [number] matches a saved contact. PhoneLookup handles format/DDI normalization. */
    fun isInContacts(context: Context, number: String): Boolean {
        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(number),
        )
        return context.contentResolver.query(
            uri, arrayOf(ContactsContract.PhoneLookup._ID), null, null, null,
        )?.use { it.moveToFirst() } ?: false
    }
}
