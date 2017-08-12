package com.paulps.wallpaperswitcher;

import android.content.Context;

import java.io.File;

public class DirPathUtils {
    public static File getCacheDir(Context context) {
        return context.getCacheDir();
    }
}
