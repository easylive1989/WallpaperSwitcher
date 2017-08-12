package com.paulps.wallpaperswitcher;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class WallpaperModifyService extends IntentService {
    private static final String TAG = WallpaperModifyService.class.getSimpleName();

    private static final String PREFERENCE_TABLE_GLOBAL = "PreferenceGlobal";
    private static final String PREFERENCE_KEY_WP_NAME = "WallpaperName";

    public static final String DEFAULT_WP_1 = "Wp1";
    public static final String DEFAULT_WP_2 = "Wp2";

    private WallpaperManager mWallpaperManager;

    public WallpaperModifyService() {
        super(WallpaperModifyService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        setWallpaper(mWallpaperManager);
    }

    private void setWallpaper(WallpaperManager wallpaperManager) {
        try {
            if (isCurrentWp1()) {
                Bitmap bitmap = BitmapFactory.decodeFile(DirPathUtils.getCacheDir(this).getPath() + File.separator +  DEFAULT_WP_2);
                wallpaperManager.setBitmap(bitmap);
                setCurrentWpRes(DEFAULT_WP_2);
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(DirPathUtils.getCacheDir(this).getPath() + File.separator +  DEFAULT_WP_1);
                wallpaperManager.setBitmap(bitmap);
                setCurrentWpRes(DEFAULT_WP_1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isCurrentWp1() {
        return getCurrentWpRes().equals(DEFAULT_WP_1);
    }

    private String getCurrentWpRes() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences(PREFERENCE_TABLE_GLOBAL, Context.MODE_PRIVATE);
        return sp.getString(PREFERENCE_KEY_WP_NAME, "");
    }

    private void setCurrentWpRes(String wpName) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences(PREFERENCE_TABLE_GLOBAL, Context.MODE_PRIVATE);
        sp.edit().putString(PREFERENCE_KEY_WP_NAME, wpName).commit();
    }
}
