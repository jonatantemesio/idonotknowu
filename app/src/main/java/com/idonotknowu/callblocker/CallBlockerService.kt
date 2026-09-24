package com.idonotknowu.callblocker

import android.Manifest
import android.content.pm.PackageManager
import android.telecom.Call
import android.telecom.CallScreeningService
import com.idonotknowu.callblocker.data.AppDatabase
import com.idonotknowu.callblocker.data.BlockedCall
import com.idonotknowu.callblocker.data.Prefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CallBlockerService : CallScreeningService() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onScreenCall(details: Call.Details) {
        if (details.callDirection != Call.Details.DIRECTION_INCOMING) {
            respondToCall(details, CallResponse.Builder().build())
            return
        }

        val number = details.handle?.schemeSpecificPart?.takeIf { it.isNotBlank() }
        val canReadContacts =
            checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED

        // Fail-open: inactive or unable to read contacts -> let the call through.
        val shouldBlock = Prefs.isEnabled(this) && canReadContacts &&
            (number == null || !ContactChecker.isInContacts(this, number))

        if (!shouldBlock) {
            respondToCall(details, CallResponse.Builder().build())
            return
        }

        respondToCall(
            details,
            CallResponse.Builder()
                .setDisallowCall(true)
                .setRejectCall(true)
                .setSkipNotification(true)
                .setSkipCallLog(true)
                .build(),
        )

        val db = AppDatabase.get(applicationContext)
        scope.launch {
            db.blockedCallDao().insert(
                BlockedCall(number = number ?: "Desconhecido", timestamp = System.currentTimeMillis()),
            )
        }
    }
}
