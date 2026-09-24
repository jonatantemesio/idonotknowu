package com.idonotknowu.callblocker

import android.Manifest
import android.app.role.RoleManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
import com.idonotknowu.callblocker.data.AppDatabase
import com.idonotknowu.callblocker.data.Prefs
import com.idonotknowu.callblocker.ui.BlockedListScreen
import com.idonotknowu.callblocker.ui.HomeScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = AppDatabase.get(this).blockedCallDao()

        setContent {
            MaterialTheme {
                var showList by remember { mutableStateOf(false) }
                var active by remember { mutableStateOf(isActive(this)) }
                val calls by dao.observeAll().collectAsState(initial = emptyList())

                val roleLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.StartActivityForResult(),
                ) {
                    Prefs.setEnabled(this, hasRole(this))
                    active = isActive(this)
                }
                val requestRole = {
                    if (hasRole(this)) {
                        Prefs.setEnabled(this, true)
                        active = true
                    } else {
                        val rm = getSystemService(RoleManager::class.java)
                        roleLauncher.launch(rm.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING))
                    }
                }
                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission(),
                ) { granted -> if (granted) requestRole() }

                if (showList) {
                    BlockedListScreen(calls = calls, onBack = { showList = false })
                } else {
                    HomeScreen(
                        active = active,
                        onToggle = {
                            if (active) {
                                Prefs.setEnabled(this, false)
                                active = false
                            } else if (hasContactsPermission(this)) {
                                requestRole()
                            } else {
                                permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                            }
                        },
                        onShowBlocked = { showList = true },
                    )
                }
            }
        }
    }

    private fun hasRole(context: Context) =
        context.getSystemService(RoleManager::class.java).isRoleHeld(RoleManager.ROLE_CALL_SCREENING)

    private fun hasContactsPermission(context: Context) =
        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED

    private fun isActive(context: Context) = Prefs.isEnabled(context) && hasRole(context)
}
