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

import java.io.IOException;

public class WallpaperModifyService extends IntentService {
    private static final String TAG = WallpaperModifyService.class.getSimpleName();

    private static final String PREFERENCE_TABLE_GLOBAL = "PreferenceGlobal";
    private static final String PREFERENCE_KEY_WP_RES = "WallpaperResource";

    private final int DEFAULT_WP_ANIM = R.raw.default_wallpaper_anim;
    private final int DEFAULT_WP_NORMAL = R.raw.default_wallpaper_normal;

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
            if (isCurrentWpNormal()) {
                wallpaperManager.setResource(DEFAULT_WP_ANIM);
                setCurrentWpRes(DEFAULT_WP_ANIM);
            } else {
                wallpaperManager.setResource(DEFAULT_WP_NORMAL);
                setCurrentWpRes(DEFAULT_WP_NORMAL);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isCurrentWpNormal() {
        return getCurrentWpRes() == DEFAULT_WP_NORMAL;
    }

    private int getCurrentWpRes() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences(PREFERENCE_TABLE_GLOBAL, Context.MODE_PRIVATE);
        return sp.getInt(PREFERENCE_KEY_WP_RES, 0);
    }

    private void setCurrentWpRes(int wpRes) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences(PREFERENCE_TABLE_GLOBAL, Context.MODE_PRIVATE);
        sp.edit().putInt(PREFERENCE_KEY_WP_RES, wpRes).commit();
    }
}
