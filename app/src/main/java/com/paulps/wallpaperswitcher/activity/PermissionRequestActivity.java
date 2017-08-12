package com.paulps.wallpaperswitcher.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionRequestActivity extends Activity {

    private final int REQUEST_PERMISSION = 123;

    protected void ensurePermissions(String... permissions) {
        if (hasPermissions(permissions)) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION);
        }
    }

    private boolean hasPermissions(String... permissions) {
        boolean hasAllPermissions = false;
        for(String permission : permissions) {
            hasAllPermissions &= ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED;
        }
        return hasAllPermissions;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            finish();
        }
    }
}
