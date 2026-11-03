package com.coui.appcompat.edittext;

import android.os.SystemClock;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

/* JADX INFO: loaded from: classes7.dex */
public class COUIEditFastDeleteWatcher implements TextWatcher {
    private static final int MIN_DELETE_TIME = 100;
    private static final String TAG = "COUIEditFastDeleteWatcher";
    private Editable mBeforeText;
    private EditText mEdittext;
    private boolean mEnable = true;
    private long mFirstDeleteTime;
    private boolean mIsDeleting;
    private long mLastDeleteTime;

    public COUIEditFastDeleteWatcher(COUIEditText cOUIEditText) {
        this.mEdittext = cOUIEditText;
        cOUIEditText.addTextChangedListener(this);
    }

    private void log(String str) {
        Log.d(TAG, str);
    }

    private void resetAllState() {
        this.mFirstDeleteTime = SystemClock.elapsedRealtime();
        this.mIsDeleting = true;
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        if (this.mEnable) {
            if (editable.length() >= this.mBeforeText.length()) {
                this.mIsDeleting = false;
                return;
            }
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            long j3 = this.mLastDeleteTime;
            if (jElapsedRealtime - j3 > 1000 && j3 > 0) {
                this.mIsDeleting = false;
                this.mLastDeleteTime = 0L;
            }
            if (!this.mIsDeleting) {
                this.mIsDeleting = true;
                this.mFirstDeleteTime = SystemClock.elapsedRealtime();
            }
            if (jElapsedRealtime - this.mFirstDeleteTime < 4000) {
                this.mLastDeleteTime = SystemClock.elapsedRealtime();
                return;
            }
            if (jElapsedRealtime - this.mLastDeleteTime < 100) {
                int length = this.mBeforeText.length();
                int length2 = length - editable.length();
                this.mEnable = false;
                editable.append(this.mBeforeText.subSequence(length - length2, length));
                this.mEnable = true;
                return;
            }
            int length3 = editable.length();
            int i3 = 4 > length3 ? length3 : 4;
            this.mEnable = false;
            editable.delete(length3 - i3, length3);
            this.mLastDeleteTime = SystemClock.elapsedRealtime();
            this.mEnable = true;
            log("afterTextChanged done");
        }
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
        if (this.mEnable) {
            this.mBeforeText = new SpannableStringBuilder(charSequence.toString());
        }
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
    }
}