package com.idonotknowu.callblocker.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PrefsTest {
    private val context get() = ApplicationProvider.getApplicationContext<Context>()

    @Test fun disabledByDefault() = assertFalse(Prefs.isEnabled(context))

    @Test fun persistsEnabledFlag() {
        Prefs.setEnabled(context, true)
        assertTrue(Prefs.isEnabled(context))
        Prefs.setEnabled(context, false)
        assertFalse(Prefs.isEnabled(context))
    }
}
