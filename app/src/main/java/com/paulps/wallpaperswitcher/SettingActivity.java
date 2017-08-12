package com.paulps.wallpaperswitcher;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.io.File;

public class SettingActivity extends PermissionRequestActivity {
    private static final String TAG = SettingActivity.class.getSimpleName();

    private int mAppWidgetId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        mAppWidgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                                                AppWidgetManager.INVALID_APPWIDGET_ID);

        ensurePermissions(  Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    public void onDoneClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, intent);
        finish();
    }

    public static class SettingsFragment extends PreferenceFragment {

        private final String PREFERENCE_SET_WP1 = "setWp1";
        private final String PREFERENCE_SET_WP2 = "setWp2";

        private final int REQUEST_WP1_IMAGE_SELECT = 1234;
        private final int REQUEST_WP2_IMAGE_SELECT = 1235;

        private WallpaperManager mWallpaperManager;

        private Preference.OnPreferenceClickListener mOnSetWpPrefClick =  new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                int requestCode = preference.getKey().equals(PREFERENCE_SET_WP1) ?
                        REQUEST_WP1_IMAGE_SELECT :
                        REQUEST_WP2_IMAGE_SELECT;
                startActivityForResult(preference.getIntent(), requestCode);
                return true;
            }
        };

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mWallpaperManager = WallpaperManager.getInstance(getActivity());

            addPreferencesFromResource(R.xml.setting);

            Preference setWp1Pref = findPreference(PREFERENCE_SET_WP1);
            setWp1Pref.setOnPreferenceClickListener(mOnSetWpPrefClick);
            Preference setWp2Pref = findPreference(PREFERENCE_SET_WP2);
            setWp2Pref.setOnPreferenceClickListener(mOnSetWpPrefClick);
        }

        @Override
        public void onActivityResult(int reqCode, int resCode, Intent data) {
            if (reqCode == REQUEST_WP1_IMAGE_SELECT || reqCode == REQUEST_WP2_IMAGE_SELECT) {
                Uri imageUri = data.getData();
                File cropImageFile = FileUtils.getFile(getActivity(), imageUri);
                String fileName = reqCode == REQUEST_WP1_IMAGE_SELECT ?
                        WallpaperModifyService.DEFAULT_WP_1 :
                        WallpaperModifyService.DEFAULT_WP_2;
                FileUtils.copy(getActivity(), cropImageFile, DirPathUtils.getCacheDir(getActivity()).getPath() + File.separator +  fileName);
            }
        }
    }
}
