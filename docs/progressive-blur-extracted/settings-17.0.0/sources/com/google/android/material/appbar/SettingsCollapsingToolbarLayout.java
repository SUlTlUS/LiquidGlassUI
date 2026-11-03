package com.google.android.material.appbar;

import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewParent;
import com.android.settings.R;
import com.google.android.material.animation.AnimationUtils;

/* JADX INFO: loaded from: classes8.dex */
public class SettingsCollapsingToolbarLayout extends COUICollapsingToolbarLayout {
    private static final int DEFAULT_NORMAL_TEXT_COLOR_ALPHA = 255;
    private COUIAppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener;

    public class OffsetUpdateListener implements COUIAppBarLayout.OnOffsetChangedListener {
        public /* synthetic */ OffsetUpdateListener(SettingsCollapsingToolbarLayout settingsCollapsingToolbarLayout, q qVar) {
            this();
        }

        @Override // com.google.android.material.appbar.COUIAppBarLayout.OnOffsetChangedListener, com.google.android.material.appbar.COUIAppBarLayout.BaseOnOffsetChangedListener
        public void onOffsetChanged(COUIAppBarLayout cOUIAppBarLayout, int i3) {
            float expansionFraction = SettingsCollapsingToolbarLayout.this.getCollapsingTextHelper().getExpansionFraction();
            int iLerp = AnimationUtils.lerp(255, 0, expansionFraction);
            TextPaint collapsingTextPaint = SettingsCollapsingToolbarLayout.this.getCollapsingTextPaint();
            if (collapsingTextPaint != null) {
                collapsingTextPaint.setAlpha(iLerp);
            }
            COUICollapsingTextHelper collapsingTextHelper = SettingsCollapsingToolbarLayout.this.getCollapsingTextHelper();
            float fLerp = AnimationUtils.lerp(1.0f, 0.7f, expansionFraction);
            if (collapsingTextHelper != null) {
                collapsingTextHelper.scale = fLerp;
            }
        }

        private OffsetUpdateListener() {
        }
    }

    public SettingsCollapsingToolbarLayout(Context context) {
        super(context);
    }

    @Override // com.google.android.material.appbar.COUICollapsingToolbarLayout, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        if (parent instanceof COUIAppBarLayout) {
            COUIAppBarLayout cOUIAppBarLayout = (COUIAppBarLayout) parent;
            if (this.mOnOffsetChangedListener == null) {
                this.mOnOffsetChangedListener = new OffsetUpdateListener(this, null);
            }
            cOUIAppBarLayout.addOnOffsetChangedListener(this.mOnOffsetChangedListener);
        }
    }

    @Override // com.google.android.material.appbar.COUICollapsingToolbarLayout, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ViewParent parent = getParent();
        COUIAppBarLayout.OnOffsetChangedListener onOffsetChangedListener = this.mOnOffsetChangedListener;
        if (onOffsetChangedListener == null || !(parent instanceof COUIAppBarLayout)) {
            return;
        }
        ((COUIAppBarLayout) parent).removeOnOffsetChangedListener(onOffsetChangedListener);
    }

    @Override // com.google.android.material.appbar.COUICollapsingToolbarLayout, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i3, int i4) {
        super.onMeasure(i3, i4);
        setExpandedTitleMarginStart(getResources().getDimensionPixelSize(R.dimen.dimen_24));
    }

    public SettingsCollapsingToolbarLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SettingsCollapsingToolbarLayout(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
    }
}