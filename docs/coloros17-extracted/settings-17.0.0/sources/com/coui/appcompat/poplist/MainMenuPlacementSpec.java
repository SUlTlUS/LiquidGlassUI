package com.coui.appcompat.poplist;

import com.coui.appcompat.log.COUILog;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes7.dex */
public final class MainMenuPlacementSpec {
    public static final int ALIGN_DEFAULT = 0;
    public static final int ALIGN_END = 2;
    public static final int ALIGN_START = 1;
    public static final MainMenuPlacementSpec DEFAULT = ofSideAndAlignment(0, 0);
    public static final int SIDE_ABOVE = 1;
    public static final int SIDE_AUTO = 0;
    public static final int SIDE_BELOW = 2;
    public static final int SIDE_END = 4;
    public static final int SIDE_START = 3;
    private static final String TAG = "MainMenuPlaceSpec";
    private final int mAlignment;
    private final int mSide;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Alignment {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Side {
    }

    private MainMenuPlacementSpec(int i3, int i4) {
        this.mSide = i3;
        this.mAlignment = i4;
    }

    private static boolean isValidAlignment(int i3) {
        return i3 == 0 || i3 == 1 || i3 == 2;
    }

    private static boolean isValidSide(int i3) {
        return i3 == 0 || i3 == 1 || i3 == 2 || i3 == 3 || i3 == 4;
    }

    public static MainMenuPlacementSpec ofSideAndAlignment(int i3, int i4) {
        if (isValidSide(i3) && isValidAlignment(i4)) {
            return new MainMenuPlacementSpec(i3, i4);
        }
        COUILog.w(TAG, "ofSideAndAlignment: unsupported spec side=" + i3 + ", alignment=" + i4 + "; use default.");
        return DEFAULT;
    }

    public int getAlignment() {
        return this.mAlignment;
    }

    public int getSide() {
        return this.mSide;
    }

    public boolean isCrossAxisStartOrEnd() {
        int i3 = this.mAlignment;
        return i3 == 1 || i3 == 2;
    }

    public boolean isDefault() {
        return this.mSide == 0 && this.mAlignment == 0;
    }

    public boolean isSideForced() {
        int i3 = this.mSide;
        return i3 == 3 || i3 == 4;
    }

    public boolean isVerticalForced() {
        int i3 = this.mSide;
        return i3 == 1 || i3 == 2;
    }
}