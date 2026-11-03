package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.coui.appcompat.edittext.COUIEditText;
import com.coui.appcompat.edittext.COUIInputView;
import com.coui.appcompat.edittext.COUIScrolledEditText;
import com.support.input.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIInputPreference extends COUIPreference {
    private CharSequence mContent;
    private COUIEditText mEditText;
    private COUICardListItemInputView mInputView;
    private View mPreferenceView;
    private CharSequence mTitle;

    public static class COUICardListItemInputView extends COUIInputView {
        boolean mJustShowFocusLine;

        public COUICardListItemInputView(Context context) {
            this(context, null);
        }

        @Override // com.coui.appcompat.edittext.COUIInputView
        public int getEdittextPaddingBottom() {
            return !TextUtils.isEmpty(this.mTitle) ? getResources().getDimensionPixelSize(R.dimen.coui_input_edit_error_text_has_title_padding_bottom) : (int) getResources().getDimension(R.dimen.coui_input_edit_text_no_title_padding_bottom_inPreference);
        }

        @Override // com.coui.appcompat.edittext.COUIInputView
        public int getEdittextPaddingTop() {
            return !TextUtils.isEmpty(this.mTitle) ? getResources().getDimensionPixelSize(R.dimen.coui_input_edit_text_has_title_padding_top) : (int) getResources().getDimension(R.dimen.coui_input_edit_text_no_title_padding_top_inPreference);
        }

        @Override // com.coui.appcompat.edittext.COUIInputView
        public COUIEditText instanceCOUIEditText(Context context, AttributeSet attributeSet) {
            context.getTheme().applyStyle(com.support.preference.R.style.COUIInputPreferenceTheme, true);
            COUIScrolledEditText cOUIScrolledEditText = new COUIScrolledEditText(context, attributeSet, R.attr.couiInputPreferenceEditTextStyle);
            cOUIScrolledEditText.setTextDirection(5);
            cOUIScrolledEditText.setShowDeleteIcon(false);
            cOUIScrolledEditText.setVerticalScrollBarEnabled(false);
            return cOUIScrolledEditText;
        }

        @Override // com.coui.appcompat.edittext.COUIInputView
        public boolean isIsCardSingleInput() {
            return true;
        }

        public void setJustShowFocusLine(boolean z2) {
            if (this.mJustShowFocusLine != z2) {
                this.mJustShowFocusLine = z2;
                COUIEditText cOUIEditText = this.mEditText;
                if (cOUIEditText != null) {
                    cOUIEditText.setJustShowFocusLine(z2);
                }
            }
        }

        public COUICardListItemInputView(Context context, AttributeSet attributeSet) {
            this(context, attributeSet, 0);
        }

        public COUICardListItemInputView(Context context, AttributeSet attributeSet, int i3) {
            super(context, attributeSet, i3);
            this.mJustShowFocusLine = false;
        }
    }

    public COUIInputPreference(Context context) {
        this(context, null);
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.recyclerview.widget.COUIRecyclerView.ICOUIDividerDecorationInterface
    public boolean drawDivider() {
        if (this.mEditText.isErrorState()) {
            return false;
        }
        return super.drawDivider();
    }

    public CharSequence getContent() {
        COUIEditText cOUIEditText = this.mEditText;
        return cOUIEditText != null ? cOUIEditText.getCouiEditTexttNoEllipsisText() : this.mContent;
    }

    public COUIEditText getEditText() {
        return this.mEditText;
    }

    public CharSequence getHint() {
        return this.mInputView.getHint();
    }

    public COUIInputView getInputView() {
        return this.mInputView;
    }

    public View getPreferenceView() {
        return this.mPreferenceView;
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View view = preferenceViewHolder.itemView;
        this.mPreferenceView = view;
        if (view instanceof COUICardListSelectedItemLayout) {
            ((COUICardListSelectedItemLayout) view).consumeDispatchingEventForState(true);
        }
        ViewGroup viewGroup = (ViewGroup) this.mPreferenceView.findViewById(com.support.preference.R.id.edittext_container);
        if (viewGroup != null) {
            if (!this.mInputView.equals((COUICardListItemInputView) viewGroup.findViewById(android.R.id.input))) {
                this.mInputView.getEditText().refresh();
                ViewParent parent = this.mInputView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(this.mInputView);
                }
                viewGroup.removeAllViews();
                viewGroup.addView(this.mInputView, -1, -2);
                int positionInGroup = COUICardListHelper.getPositionInGroup(this);
                if (positionInGroup == 3 || positionInGroup == 4) {
                    this.mInputView.getEditText().setBoxBackgroundMode(3);
                }
            }
        }
        this.mInputView.setEnabled(isEnabled());
    }

    @Override // androidx.preference.Preference
    public Object onGetDefaultValue(TypedArray typedArray, int i3) {
        return typedArray.getString(i3);
    }

    @Override // androidx.preference.Preference
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable == null || !parcelable.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setContent(savedState.mText);
    }

    @Override // androidx.preference.Preference
    public Parcelable onSaveInstanceState() {
        Parcelable parcelableOnSaveInstanceState = super.onSaveInstanceState();
        if (isPersistent()) {
            return parcelableOnSaveInstanceState;
        }
        SavedState savedState = new SavedState(parcelableOnSaveInstanceState);
        CharSequence charSequence = this.mContent;
        if (charSequence != null) {
            savedState.mText = charSequence.toString();
        }
        return savedState;
    }

    @Override // androidx.preference.Preference
    public void onSetInitialValue(boolean z2, Object obj) {
        if (TextUtils.isEmpty(this.mContent)) {
            return;
        }
        setContent(z2 ? getPersistedString(this.mContent.toString()) : (String) obj);
    }

    public void setContent(CharSequence charSequence) {
        COUIEditText cOUIEditText = this.mEditText;
        if (cOUIEditText != null) {
            cOUIEditText.setCouiEditTexttNoEllipsisText((String) charSequence);
            this.mContent = charSequence;
            return;
        }
        if (!TextUtils.equals(this.mContent, charSequence)) {
            notifyChanged();
        }
        boolean zShouldDisableDependents = shouldDisableDependents();
        this.mContent = charSequence;
        if (charSequence != null) {
            persistString(charSequence.toString());
        }
        boolean zShouldDisableDependents2 = shouldDisableDependents();
        if (zShouldDisableDependents2 != zShouldDisableDependents) {
            notifyDependencyChange(zShouldDisableDependents2);
        }
    }

    public void setHint(CharSequence charSequence) {
        CharSequence hint = getHint();
        if ((charSequence != null || hint == null) && (charSequence == null || charSequence.equals(hint))) {
            return;
        }
        this.mInputView.setHint(charSequence);
        notifyChanged();
    }

    @Override // androidx.preference.Preference
    public boolean shouldDisableDependents() {
        return TextUtils.isEmpty(this.mContent) || super.shouldDisableDependents();
    }

    public static class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new AnonymousClass1();
        String mText;

        /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIInputPreference$SavedState$1, reason: invalid class name */
        public class AnonymousClass1 implements Parcelable.Creator<SavedState> {
            @Override // android.os.Parcelable.Creator
            public /* bridge */ /* synthetic */ SavedState createFromParcel(Parcel parcel) {
                return createFromParcel(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public /* bridge */ /* synthetic */ SavedState[] newArray(int i3) {
                return newArray(i3);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i3) {
                return new SavedState[i3];
            }
        }

        public SavedState(Parcel parcel) {
            super(parcel);
            this.mText = parcel.readString();
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i3) {
            super.writeToParcel(parcel, i3);
            parcel.writeString(this.mText);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }

    public COUIInputPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, com.support.preference.R.attr.couiInputPreferenceStyle);
    }

    public COUIInputPreference(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, com.support.preference.R.style.Preference_COUI_COUIInputPreference);
    }

    public COUIInputPreference(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.support.preference.R.styleable.COUIInputPreference, i3, i4);
        this.mContent = typedArrayObtainStyledAttributes.getText(com.support.preference.R.styleable.COUIInputPreference_couiContent);
        boolean z2 = typedArrayObtainStyledAttributes.getBoolean(com.support.preference.R.styleable.COUIInputPreference_couiJustShowFocusLine, true);
        typedArrayObtainStyledAttributes.recycle();
        TypedArray typedArrayObtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, androidx.preference.R.styleable.Preference, i3, i4);
        this.mTitle = typedArrayObtainStyledAttributes2.getText(androidx.preference.R.styleable.Preference_android_title);
        typedArrayObtainStyledAttributes2.recycle();
        COUICardListItemInputView cOUICardListItemInputView = new COUICardListItemInputView(context, attributeSet);
        this.mInputView = cOUICardListItemInputView;
        cOUICardListItemInputView.setId(android.R.id.input);
        this.mInputView.setTitle(this.mTitle);
        this.mEditText = this.mInputView.getEditText();
        this.mInputView.setJustShowFocusLine(z2);
    }
}