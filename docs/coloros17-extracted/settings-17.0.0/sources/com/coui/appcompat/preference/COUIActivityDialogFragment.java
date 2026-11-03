package com.coui.appcompat.preference;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.preference.ListPreferenceDialogFragmentCompat;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.coui.appcompat.version.COUIVersionUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.support.preference.R;

/* JADX INFO: loaded from: classes7.dex */
public class COUIActivityDialogFragment extends ListPreferenceDialogFragmentCompat {
    private int mClickedDialogEntryIndex;
    private AppCompatDialog mDialog;

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIActivityDialogFragment$1, reason: invalid class name */
    public class AnonymousClass1 extends AppCompatDialog {
        public AnonymousClass1(Context context, int i3) {
            super(context, i3);
        }

        @Override // android.app.Dialog, android.view.Window.Callback
        public boolean onMenuItemSelected(int i3, MenuItem menuItem) {
            if (menuItem.getItemId() != 16908332) {
                return super.onMenuItemSelected(i3, menuItem);
            }
            dismiss();
            return true;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIActivityDialogFragment$2, reason: invalid class name */
    public class AnonymousClass2 implements View.OnClickListener {
        public AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            COUIActivityDialogFragment.access$000(COUIActivityDialogFragment.this).dismiss();
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIActivityDialogFragment$3, reason: invalid class name */
    public class AnonymousClass3 implements Runnable {
        final /* synthetic */ AppBarLayout val$appbarLayout;
        final /* synthetic */ ListView val$list;

        public AnonymousClass3(AppBarLayout appBarLayout, ListView listView) {
            this.val$appbarLayout = appBarLayout;
            this.val$list = listView;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (COUIActivityDialogFragment.this.isAdded()) {
                int measuredHeight = this.val$appbarLayout.getMeasuredHeight() + COUIActivityDialogFragment.this.getResources().getDimensionPixelSize(R.dimen.support_preference_listview_margin_top);
                View view = new View(this.val$appbarLayout.getContext());
                view.setVisibility(4);
                view.setLayoutParams(new AbsListView.LayoutParams(-1, measuredHeight));
                this.val$list.addHeaderView(view);
            }
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIActivityDialogFragment$4, reason: invalid class name */
    public class AnonymousClass4 extends CheckedItemAdapter {
        final /* synthetic */ AppCompatDialog val$dialog;
        final /* synthetic */ ListView val$list;

        /* JADX INFO: renamed from: com.coui.appcompat.preference.COUIActivityDialogFragment$4$1, reason: invalid class name */
        public class AnonymousClass1 implements View.OnClickListener {
            final /* synthetic */ int val$position;

            public AnonymousClass1(int i3) {
                this.val$position = i3;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                COUIActivityDialogFragment.access$102(COUIActivityDialogFragment.this, this.val$position);
                COUIActivityDialogFragment.this.onClick(null, -1);
                AnonymousClass4.this.val$dialog.dismiss();
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass4(Context context, int i3, int i4, CharSequence[] charSequenceArr, ListView listView, AppCompatDialog appCompatDialog) {
            super(context, i3, i4, charSequenceArr);
            this.val$list = listView;
            this.val$dialog = appCompatDialog;
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int i3, View view, ViewGroup viewGroup) {
            View view2 = super.getView(i3, view, viewGroup);
            if (i3 == COUIActivityDialogFragment.access$100(COUIActivityDialogFragment.this)) {
                ListView listView = this.val$list;
                listView.setItemChecked(listView.getHeaderViewsCount() + i3, true);
            }
            View viewFindViewById = view2.findViewById(R.id.item_divider);
            int count = getCount();
            if (viewFindViewById != null) {
                if (count == 1 || i3 == count - 1) {
                    viewFindViewById.setVisibility(8);
                } else {
                    viewFindViewById.setVisibility(0);
                }
            }
            view2.setOnClickListener(new AnonymousClass1(i3));
            COUICardListHelper.setItemCardBackground(view2, COUICardListHelper.getPositionInGroup(COUIActivityDialogFragment.access$200(COUIActivityDialogFragment.this).getEntries().length, i3));
            return view2;
        }
    }

    public static class CheckedItemAdapter extends ArrayAdapter<CharSequence> {
        public CheckedItemAdapter(Context context, int i3, int i4, CharSequence[] charSequenceArr) {
            super(context, i3, i4, charSequenceArr);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public long getItemId(int i3) {
            return i3;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public boolean hasStableIds() {
            return true;
        }
    }

    public static /* synthetic */ AppCompatDialog access$000(COUIActivityDialogFragment cOUIActivityDialogFragment) {
        return cOUIActivityDialogFragment.mDialog;
    }

    public static /* synthetic */ int access$100(COUIActivityDialogFragment cOUIActivityDialogFragment) {
        return cOUIActivityDialogFragment.mClickedDialogEntryIndex;
    }

    public static /* synthetic */ int access$102(COUIActivityDialogFragment cOUIActivityDialogFragment, int i3) {
        cOUIActivityDialogFragment.mClickedDialogEntryIndex = i3;
        return i3;
    }

    public static /* synthetic */ COUIActivityDialogPreference access$200(COUIActivityDialogFragment cOUIActivityDialogFragment) {
        return cOUIActivityDialogFragment.getListPreference();
    }

    private COUIActivityDialogPreference getListPreference() {
        return (COUIActivityDialogPreference) getPreference();
    }

    public static int getStatusBarHeight(Context context) {
        int identifier = context.getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier <= 0) {
            return -1;
        }
        try {
            return context.getApplicationContext().getResources().getDimensionPixelSize(identifier);
        } catch (Exception e3) {
            e3.printStackTrace();
            return -1;
        }
    }

    private View getStatusBarView(Context context) {
        int statusBarHeight = getStatusBarHeight(context);
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, statusBarHeight));
        return imageView;
    }

    public static COUIActivityDialogFragment newInstance(String str) {
        COUIActivityDialogFragment cOUIActivityDialogFragment = new COUIActivityDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", str);
        cOUIActivityDialogFragment.setArguments(bundle);
        return cOUIActivityDialogFragment;
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        int i3;
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(getActivity(), com.support.dialog.R.style.Theme_COUI_ActivityDialog);
        this.mDialog = anonymousClass1;
        if (anonymousClass1.getWindow() != null) {
            Window window = anonymousClass1.getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(1024);
            window.setStatusBarColor(0);
            int systemUiVisibility = decorView.getSystemUiVisibility();
            int oSVersionCode = COUIVersionUtil.getOSVersionCode();
            boolean z2 = getResources().getBoolean(R.bool.list_status_white_enabled);
            if (oSVersionCode >= 6 || oSVersionCode == 0) {
                window.addFlags(Integer.MIN_VALUE);
                if (COUIDarkModeUtil.isCOUIDarkThemeOrNightMode(anonymousClass1.getContext())) {
                    i3 = systemUiVisibility & (-8209);
                } else {
                    i3 = !z2 ? systemUiVisibility | 8192 : systemUiVisibility | 256;
                }
                decorView.setSystemUiVisibility(i3);
            }
        }
        View viewInflate = LayoutInflater.from(getActivity()).inflate(R.layout.coui_preference_listview, (ViewGroup) null);
        COUIToolbar cOUIToolbar = (COUIToolbar) viewInflate.findViewById(R.id.toolbar);
        cOUIToolbar.setNavigationIcon(ContextCompat.getDrawable(cOUIToolbar.getContext(), com.support.appcompat.R.drawable.coui_back_arrow));
        cOUIToolbar.setNavigationOnClickListener(new AnonymousClass2());
        AppBarLayout appBarLayout = (AppBarLayout) viewInflate.findViewById(R.id.abl);
        ListView listView = (ListView) viewInflate.findViewById(R.id.coui_preference_listview);
        View viewFindViewById = viewInflate.findViewById(R.id.divider_line);
        if (getResources().getBoolean(R.bool.is_dialog_preference_immersive)) {
            viewFindViewById.setVisibility(8);
        }
        ViewCompat.setNestedScrollingEnabled(listView, true);
        View statusBarView = getStatusBarView(appBarLayout.getContext());
        appBarLayout.addView(statusBarView, 0, statusBarView.getLayoutParams());
        appBarLayout.post(new AnonymousClass3(appBarLayout, listView));
        if (getListPreference() != null) {
            this.mClickedDialogEntryIndex = getListPreference().findIndexOfValue(getListPreference().getValue());
            cOUIToolbar.setTitle(getListPreference().getDialogTitle());
            listView.setAdapter((ListAdapter) new AnonymousClass4(getActivity(), R.layout.coui_preference_listview_item, R.id.checkedtextview, getListPreference().getEntries(), listView, anonymousClass1));
        }
        listView.setChoiceMode(1);
        anonymousClass1.setContentView(viewInflate);
        return anonymousClass1;
    }

    @Override // androidx.preference.ListPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat
    public void onDialogClosed(boolean z2) {
        COUIActivityDialogPreference listPreference = getListPreference();
        if (!z2 || this.mClickedDialogEntryIndex < 0) {
            return;
        }
        String string = getListPreference().getEntryValues()[this.mClickedDialogEntryIndex].toString();
        if (listPreference.callChangeListener(string)) {
            listPreference.setValue(string);
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (getPreference() == null) {
            dismiss();
        }
    }
}