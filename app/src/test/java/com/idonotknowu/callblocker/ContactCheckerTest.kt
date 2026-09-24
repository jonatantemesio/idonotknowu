package com.idonotknowu.callblocker

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Robolectric

@RunWith(RobolectricTestRunner::class)
class ContactCheckerTest {

    class FakeContacts : ContentProvider() {
        override fun onCreate() = true
        override fun query(
            uri: Uri, projection: Array<String>?, selection: String?,
            selectionArgs: Array<String>?, sortOrder: String?,
        ): Cursor {
            val cursor = MatrixCursor(arrayOf(ContactsContract.PhoneLookup._ID))
            if (uri.lastPathSegment == "+55 11 99999-0000") cursor.addRow(arrayOf(1L))
            return cursor
        }
        override fun getType(uri: Uri): String? = null
        override fun insert(uri: Uri, values: ContentValues?): Uri? = null
        override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?) = 0
        override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?) = 0
    }

    @Before fun registerProvider() {
        Robolectric.setupContentProvider(FakeContacts::class.java, ContactsContract.AUTHORITY)
    }

    private val context get() = ApplicationProvider.getApplicationContext<android.content.Context>()

    @Test fun trueWhenNumberMatchesContact() =
        assertTrue(ContactChecker.isInContacts(context, "+55 11 99999-0000"))

    @Test fun falseWhenNoMatch() =
        assertFalse(ContactChecker.isInContacts(context, "+55 11 88888-0000"))
}
