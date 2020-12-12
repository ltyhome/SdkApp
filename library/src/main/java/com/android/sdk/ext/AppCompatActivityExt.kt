package com.android.sdk.ext

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.sdk.R
import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.coroutinespermission.PermissionManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun AppCompatActivity.checkPermissions(
    vararg permissions: String,
    onGranted: () -> Unit,
    onDenied: () -> Unit = {}
) {
    GlobalScope.launch {
        when(PermissionManager.requestPermissions(this@checkPermissions, 1001, *permissions)){
            is PermissionResult.PermissionGranted -> onGranted.invoke()
            is PermissionResult.ShowRational -> ktxRunOnUi {
                showRationaleDialog({
                    checkPermissions(
                        *permissions,
                        onGranted = onGranted
                    )
                }) { onDenied.invoke() }
            }
            is PermissionResult.PermissionDenied -> ktxRunOnUi { showSettingDialog { onDenied.invoke() } }
            is PermissionResult.PermissionDeniedPermanently -> ktxRunOnUi { showSettingDialog { onDenied.invoke() } }
        }
    }
}


fun AppCompatActivity.showRationaleDialog(
    proceed: () -> Unit,
    cancel: () -> Unit
) {
    AlertDialog.Builder(this)
        .setCancelable(false)
        .setTitle(getString(R.string.permission_remind))
        .setMessage(getString(R.string.permission_message))
        .setPositiveButton(getString(R.string.permission_positive)) { _, _ -> proceed.invoke() }
        .setNegativeButton(getString(R.string.permission_negative)) { _, _ -> cancel.invoke() }
        .show()
}

fun AppCompatActivity.showSettingDialog(neverAsk: () -> Unit) {
    AlertDialog.Builder(this)
        .setCancelable(false)
        .setTitle(getString(R.string.permission_remind))
        .setMessage(getString(R.string.permission_setting_message))
        .setPositiveButton(getString(R.string.permission_setting)) { _, _ ->
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            intent.data = Uri.fromParts("package", packageName, null)
            startActivity(intent)
        }
        .setNegativeButton(getString(R.string.permission_negative)) { _, _ -> neverAsk.invoke() }
        .show()
}
