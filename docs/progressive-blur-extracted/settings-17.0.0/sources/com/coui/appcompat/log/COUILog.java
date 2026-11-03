package com.coui.appcompat.log;

import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes.dex */
public class COUILog {
    private static final String COUI_LOG_FLAG = "COUI";
    public static final int LEVEL_ASSERT = 7;
    public static final int LEVEL_DEBUG = 3;
    public static final int LEVEL_ERROR = 6;
    public static final int LEVEL_INFO = 4;
    public static final int LEVEL_SILENT = Integer.MAX_VALUE;
    public static final int LEVEL_VERBOSE = 2;
    public static final int LEVEL_WARN = 5;
    public static final boolean LOG_ASSERT;
    public static final boolean LOG_DEBUG;
    public static final boolean LOG_ERROR;
    public static final boolean LOG_INFO;
    private static int LOG_LEVEL = 0;
    public static final boolean LOG_SILENT;
    public static final boolean LOG_VERBOSE;
    public static final boolean LOG_WARN;
    private static final String STACK_TAG = "StackTrace";
    private static final boolean STACK_TRACE = false;

    /* JADX INFO: loaded from: classes7.dex */
    @Retention(RetentionPolicy.SOURCE)
    public @interface COUILogLevel {
    }

    static {
        boolean zIsLoggable = isLoggable(COUI_LOG_FLAG, 2);
        LOG_VERBOSE = zIsLoggable;
        boolean zIsLoggable2 = isLoggable(COUI_LOG_FLAG, 3);
        LOG_DEBUG = zIsLoggable2;
        boolean zIsLoggable3 = isLoggable(COUI_LOG_FLAG, 4);
        LOG_INFO = zIsLoggable3;
        boolean zIsLoggable4 = isLoggable(COUI_LOG_FLAG, 5);
        LOG_WARN = zIsLoggable4;
        boolean zIsLoggable5 = isLoggable(COUI_LOG_FLAG, 6);
        LOG_ERROR = zIsLoggable5;
        boolean zIsLoggable6 = isLoggable(COUI_LOG_FLAG, 7);
        LOG_ASSERT = zIsLoggable6;
        LOG_SILENT = (zIsLoggable || zIsLoggable2 || zIsLoggable3 || zIsLoggable4 || zIsLoggable5 || zIsLoggable6) ? false : true;
        LOG_LEVEL = 4;
    }

    public static void d(String str, String str2) {
        if (LOG_LEVEL <= 3 || Log.isLoggable(str, 3) || LOG_DEBUG) {
            Log.d(str, str2);
        }
    }

    public static void e(String str, String str2) {
        if (LOG_LEVEL <= 6 || Log.isLoggable(str, 6) || LOG_ERROR) {
            Log.e(str, str2);
        }
    }

    public static int getCOUILogLevel() {
        return LOG_LEVEL;
    }

    public static void i(String str, String str2) {
        if (LOG_LEVEL <= 4 || Log.isLoggable(str, 4) || LOG_INFO) {
            Log.i(str, str2);
        }
    }

    public static boolean isLoggable(String str, int i3) {
        return Log.isLoggable(str, i3);
    }

    public static void logStatus(String str) {
        boolean z2;
        boolean zIsLoggable = str.equals(COUI_LOG_FLAG) ? LOG_VERBOSE : Log.isLoggable(str, 2);
        boolean zIsLoggable2 = str.equals(COUI_LOG_FLAG) ? LOG_DEBUG : Log.isLoggable(str, 3);
        boolean zIsLoggable3 = str.equals(COUI_LOG_FLAG) ? LOG_INFO : Log.isLoggable(str, 2);
        boolean zIsLoggable4 = str.equals(COUI_LOG_FLAG) ? LOG_WARN : Log.isLoggable(str, 2);
        boolean zIsLoggable5 = str.equals(COUI_LOG_FLAG) ? LOG_ERROR : Log.isLoggable(str, 2);
        boolean zIsLoggable6 = str.equals(COUI_LOG_FLAG) ? LOG_ASSERT : Log.isLoggable(str, 2);
        if (str.equals(COUI_LOG_FLAG)) {
            z2 = LOG_SILENT;
        } else {
            z2 = (zIsLoggable || zIsLoggable2 || zIsLoggable3 || zIsLoggable4 || zIsLoggable5 || zIsLoggable6) ? false : true;
        }
        Log.println(7, COUI_LOG_FLAG, "Log status for tag: " + str + "\nVERBOSE: " + zIsLoggable + "\nDEBUG: " + zIsLoggable2 + "\nINFO: " + zIsLoggable3 + "\nWARN: " + zIsLoggable4 + "\nERROR: " + zIsLoggable5 + "\nASSERT: " + zIsLoggable6 + "\nSILENT: " + z2);
    }

    public static void printStackTrace(StackTraceElement[] stackTraceElementArr, String str, boolean z2) {
    }

    public static void setCOUILogLevel(int i3) {
        LOG_LEVEL = i3;
    }

    public static void v(String str, String str2) {
        if (LOG_LEVEL <= 2 || Log.isLoggable(str, 2) || LOG_VERBOSE) {
            Log.v(str, str2);
        }
    }

    public static void w(String str, String str2) {
        if (LOG_LEVEL <= 5 || Log.isLoggable(str, 5) || LOG_WARN) {
            Log.w(str, str2);
        }
    }

    public static void wtf(String str, String str2) {
        Log.wtf(str, str2);
    }

    public static void wtf(boolean z2, String str, String str2) {
        if (z2) {
            Log.wtf(str, str2);
        }
    }

    public static void d(boolean z2, String str, String str2) {
        if (z2) {
            Log.d(str, str2);
        }
    }

    public static void e(boolean z2, String str, String str2) {
        if (z2) {
            Log.e(str, str2);
        }
    }

    public static void i(boolean z2, String str, String str2) {
        if (z2) {
            Log.i(str, str2);
        }
    }

    public static void v(boolean z2, String str, String str2) {
        if (z2) {
            Log.v(str, str2);
        }
    }

    public static void w(boolean z2, String str, String str2) {
        if (z2) {
            Log.w(str, str2);
        }
    }

    public static void wtf(String str, Throwable th) {
        Log.wtf(str, th);
    }

    public static void d(String str, String str2, Throwable th) {
        Log.d(str, str2, th);
    }

    public static void e(String str, String str2, Throwable th) {
        Log.e(str, str2, th);
    }

    public static void i(String str, String str2, Throwable th) {
        Log.i(str, str2, th);
    }

    public static void v(String str, String str2, Throwable th) {
        Log.v(str, str2, th);
    }

    public static void w(String str, String str2, Throwable th) {
        Log.w(str, str2, th);
    }

    public static void wtf(boolean z2, String str, Throwable th) {
        if (z2) {
            Log.wtf(str, th);
        }
    }

    public static void d(boolean z2, String str, String str2, Throwable th) {
        if (z2) {
            Log.d(str, str2, th);
        }
    }

    public static void e(boolean z2, String str, String str2, Throwable th) {
        if (z2) {
            Log.e(str, str2, th);
        }
    }

    public static void i(boolean z2, String str, String str2, Throwable th) {
        if (z2) {
            Log.i(str, str2, th);
        }
    }

    public static void v(boolean z2, String str, String str2, Throwable th) {
        if (z2) {
            Log.v(str, str2, th);
        }
    }

    public static void w(boolean z2, String str, String str2, Throwable th) {
        if (z2) {
            Log.w(str, str2, th);
        }
    }

    public static void logStatus() {
        logStatus(COUI_LOG_FLAG);
    }
}