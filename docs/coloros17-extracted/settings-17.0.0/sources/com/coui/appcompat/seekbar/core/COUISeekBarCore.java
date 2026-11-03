package com.coui.appcompat.seekbar.core;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.AbsSeekBar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.exifinterface.media.ExifInterface;
import androidx.slice.core.SliceHints;
import com.coui.appcompat.animation.dynamicanimation.COUIDynamicAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringAnimation;
import com.coui.appcompat.animation.dynamicanimation.COUISpringForce;
import com.coui.appcompat.contextutil.COUIContextUtil;
import com.coui.appcompat.darkmode.COUIDarkModeUtil;
import com.coui.appcompat.roundcorner.RoundCornerUtil;
import com.coui.appcompat.seekbar.TextDrawable;
import com.coui.appcompat.seekbar.animation.IAnimationHandler;
import com.coui.appcompat.seekbar.animation.StandardAnimationHandler;
import com.coui.appcompat.seekbar.effect.DeformationEffectRenderer;
import com.coui.appcompat.seekbar.effect.DrawPhase;
import com.coui.appcompat.seekbar.effect.EffectChainManager;
import com.coui.appcompat.seekbar.effect.IEffectRenderer;
import com.coui.appcompat.seekbar.handler.HorizontalOrientationHandler;
import com.coui.appcompat.seekbar.handler.IOrientationHandler;
import com.coui.appcompat.seekbar.listener.OnDeformedListener;
import com.coui.appcompat.seekbar.renderer.IProgressRenderer;
import com.coui.appcompat.seekbar.renderer.IThumbRenderer;
import com.coui.appcompat.seekbar.renderer.StandardProgressRenderer;
import com.coui.appcompat.seekbar.renderer.StandardThumbRenderer;
import com.coui.appcompat.statelistutil.COUIStateListUtil;
import com.coui.appcompat.view.DescendantOffsetUtil;
import com.coui.appcompat.view.ViewUtil;
import com.decouple.injector.adaptor.BaseConfig;
import com.google.android.setupdesign.view.GradientCircularProgressIndicator;
import com.oplus.backup.sdk.common.utils.Constants;
import com.oplus.dmp.sdk.index.IndexProtocol;
import com.oplus.dmp.sdk.search.SearchProtocol;
import com.oplus.graphics.OplusCanvas;
import com.oplus.graphics.OplusPathAdapter;
import com.oplus.settings.cloud.SettingsHelper;
import com.oplus.settings.feature.deviceinfo.regulatory.RegulatoryNodes;
import com.support.seekbar.R;
import java.text.NumberFormat;
import java.util.Locale;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.math.MathKt;
import kotlin.ranges.RangesKt;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u009a\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0007\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b$\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b\u001a\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\bJ\b\u0017\u0018\u0000 ú\u00022\u00020\u00012\u00020\u0002:\nú\u0002û\u0002ü\u0002ý\u0002þ\u0002B/\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b¢\u0006\u0002\u0010\nJ\u0016\u0010\u009f\u0001\u001a\u00030 \u00012\n\u0010¡\u0001\u001a\u0005\u0018\u00010¢\u0001H\u0016J\n\u0010£\u0001\u001a\u00030 \u0001H\u0002J\t\u0010¤\u0001\u001a\u000208H\u0014J\t\u0010¥\u0001\u001a\u000208H\u0002J\n\u0010¦\u0001\u001a\u00030 \u0001H\u0014J\n\u0010§\u0001\u001a\u00030 \u0001H\u0014J\n\u0010¨\u0001\u001a\u00030 \u0001H\u0004J\n\u0010©\u0001\u001a\u00030 \u0001H\u0002J\n\u0010ª\u0001\u001a\u00030 \u0001H\u0002J\t\u0010«\u0001\u001a\u00020\u0012H\u0014J\t\u0010¬\u0001\u001a\u00020RH\u0014J\t\u0010\u00ad\u0001\u001a\u00020nH\u0014J\n\u0010®\u0001\u001a\u00030\u008c\u0001H\u0014J\u0013\u0010¯\u0001\u001a\u00020^2\b\u0010°\u0001\u001a\u00030±\u0001H\u0016J\u0013\u0010²\u0001\u001a\u00030 \u00012\u0007\u0010³\u0001\u001a\u00020PH\u0016J\u0013\u0010´\u0001\u001a\u00030 \u00012\u0007\u0010³\u0001\u001a\u00020PH\u0014J\u0013\u0010µ\u0001\u001a\u00030 \u00012\u0007\u0010³\u0001\u001a\u00020PH\u0014J\u0013\u0010¶\u0001\u001a\u00030 \u00012\u0007\u0010³\u0001\u001a\u00020PH\u0014J\u0013\u0010·\u0001\u001a\u00030 \u00012\u0007\u0010³\u0001\u001a\u00020PH\u0014J\u0013\u0010¸\u0001\u001a\u00030 \u00012\u0007\u0010³\u0001\u001a\u00020PH\u0014J\n\u0010¹\u0001\u001a\u00030 \u0001H\u0014J\t\u0010º\u0001\u001a\u00020\bH\u0014J\n\u0010»\u0001\u001a\u00030 \u0001H\u0016J\u0013\u0010»\u0001\u001a\u00030 \u00012\u0007\u0010¼\u0001\u001a\u00020zH\u0016J\n\u0010½\u0001\u001a\u00030 \u0001H\u0017J\u0012\u0010¾\u0001\u001a\u00020N2\u0007\u0010³\u0001\u001a\u00020PH\u0002J\n\u0010¿\u0001\u001a\u00030 \u0001H\u0016J\u0012\u0010À\u0001\u001a\u00020z2\u0007\u0010Á\u0001\u001a\u00020\bH\u0002J\u0007\u0010Â\u0001\u001a\u000208J\u0013\u0010Ã\u0001\u001a\u0002082\b\u0010Ä\u0001\u001a\u00030\u0081\u0001H\u0016J\t\u0010Å\u0001\u001a\u000208H\u0014J\u0007\u0010Æ\u0001\u001a\u00020\u001aJ(\u0010Ç\u0001\u001a\u00020\b2\b\u0010È\u0001\u001a\u00030É\u00012\n\u0010Ê\u0001\u001a\u0005\u0018\u00010Ë\u00012\u0007\u0010Ì\u0001\u001a\u00020\bH\u0014J\t\u0010Í\u0001\u001a\u000208H\u0014J/\u0010Î\u0001\u001a\u0005\u0018\u0001HÏ\u0001\"\n\b\u0000\u0010Ï\u0001*\u00030¢\u00012\u000f\u0010Ð\u0001\u001a\n\u0012\u0005\u0012\u0003HÏ\u00010Ñ\u0001H\u0016¢\u0006\u0003\u0010Ò\u0001J\t\u0010Ó\u0001\u001a\u00020\bH\u0016J\t\u0010Ô\u0001\u001a\u000208H\u0004J\t\u0010Õ\u0001\u001a\u00020\bH\u0016J\u0007\u0010Ö\u0001\u001a\u000208J\t\u0010×\u0001\u001a\u00020\bH\u0016J\t\u0010Ø\u0001\u001a\u00020\bH\u0016J\t\u0010Ù\u0001\u001a\u00020\bH\u0016J\t\u0010Ú\u0001\u001a\u000208H\u0014J\u0012\u0010Û\u0001\u001a\u0002082\u0007\u0010Á\u0001\u001a\u00020\bH\u0002J\t\u0010Ü\u0001\u001a\u00020\bH\u0016J\u0012\u0010Ý\u0001\u001a\u00020\b2\u0007\u0010Á\u0001\u001a\u00020\bH\u0014J\u0013\u0010Þ\u0001\u001a\u0002082\b\u0010Ä\u0001\u001a\u00030\u0081\u0001H\u0016J\u0007\u0010ß\u0001\u001a\u000208J\u0012\u0010à\u0001\u001a\u00020\b2\u0007\u0010Á\u0001\u001a\u00020\bH\u0014J\u0012\u0010á\u0001\u001a\u0002082\u0007\u0010â\u0001\u001a\u000208H\u0016J\t\u0010ã\u0001\u001a\u00020\bH\u0016J\t\u0010ä\u0001\u001a\u00020\bH\u0016J\t\u0010å\u0001\u001a\u00020\bH\u0016J\t\u0010æ\u0001\u001a\u00020\bH\u0016J\u0007\u0010ç\u0001\u001a\u000208J\t\u0010è\u0001\u001a\u00020\bH\u0016J\t\u0010é\u0001\u001a\u00020\bH\u0016J\n\u0010ê\u0001\u001a\u00030\u0081\u0001H\u0016J\u0013\u0010ë\u0001\u001a\u0002082\b\u0010°\u0001\u001a\u00030±\u0001H\u0014J\u0014\u0010ì\u0001\u001a\u00030 \u00012\b\u0010°\u0001\u001a\u00030±\u0001H\u0014J\u0014\u0010í\u0001\u001a\u00030 \u00012\b\u0010°\u0001\u001a\u00030±\u0001H\u0014J\u0014\u0010î\u0001\u001a\u00030 \u00012\b\u0010°\u0001\u001a\u00030±\u0001H\u0014J\u0012\u0010ï\u0001\u001a\u00030 \u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0002J\n\u0010ð\u0001\u001a\u00030 \u0001H\u0014J,\u0010ñ\u0001\u001a\u00030 \u00012\u0006\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0014J\t\u0010ò\u0001\u001a\u000208H\u0014J\n\u0010ó\u0001\u001a\u00030 \u0001H\u0016J\u0014\u0010ô\u0001\u001a\u00030 \u00012\b\u0010õ\u0001\u001a\u00030ö\u0001H\u0016J\t\u0010÷\u0001\u001a\u00020^H\u0014J\t\u0010ø\u0001\u001a\u00020^H\u0016J\t\u0010ù\u0001\u001a\u00020^H\u0016J\n\u0010ú\u0001\u001a\u00030 \u0001H\u0014J\n\u0010û\u0001\u001a\u00030 \u0001H\u0014J\n\u0010ü\u0001\u001a\u00030 \u0001H\u0014J\u0013\u0010ý\u0001\u001a\u00030 \u00012\u0007\u0010³\u0001\u001a\u00020PH\u0014J\u0013\u0010þ\u0001\u001a\u00030 \u00012\u0007\u0010ÿ\u0001\u001a\u000208H\u0014J7\u0010\u0080\u0002\u001a\u00030 \u00012\u0007\u0010\u0081\u0002\u001a\u00020^2\u0007\u0010\u0082\u0002\u001a\u00020\b2\u0007\u0010\u0083\u0002\u001a\u00020\b2\u0007\u0010\u0084\u0002\u001a\u00020\b2\u0007\u0010\u0085\u0002\u001a\u00020\bH\u0014J\u001c\u0010\u0086\u0002\u001a\u00030 \u00012\u0007\u0010\u0087\u0002\u001a\u00020\b2\u0007\u0010\u0088\u0002\u001a\u00020\bH\u0014J\u001c\u0010\u0089\u0002\u001a\u00030 \u00012\u0007\u0010\u008a\u0002\u001a\u00020\b2\u0007\u0010\u008b\u0002\u001a\u00020^H\u0016J\u0016\u0010\u008c\u0002\u001a\u00030 \u00012\n\u0010Ä\u0001\u001a\u0005\u0018\u00010\u008d\u0002H\u0016J\u0013\u0010\u008e\u0002\u001a\u00030 \u00012\u0007\u0010\u008f\u0002\u001a\u00020\bH\u0016J\f\u0010\u0090\u0002\u001a\u0005\u0018\u00010\u008d\u0002H\u0016J.\u0010\u0091\u0002\u001a\u00030 \u00012\u0007\u0010\u0092\u0002\u001a\u00020\b2\u0007\u0010\u0093\u0002\u001a\u00020\b2\u0007\u0010\u0094\u0002\u001a\u00020\b2\u0007\u0010\u0095\u0002\u001a\u00020\bH\u0014J\u0013\u0010\u0096\u0002\u001a\u00030 \u00012\u0007\u0010\u0097\u0002\u001a\u00020^H\u0014J\u0013\u0010\u0098\u0002\u001a\u00030 \u00012\u0007\u0010\u0097\u0002\u001a\u00020^H\u0014J\u0013\u0010\u0099\u0002\u001a\u00020^2\b\u0010°\u0001\u001a\u00030±\u0001H\u0016J\n\u0010\u009a\u0002\u001a\u00030 \u0001H\u0014J\u001b\u0010\u009b\u0002\u001a\u0002082\u0007\u0010\u009c\u0002\u001a\u0002082\u0007\u0010\u009d\u0002\u001a\u000208H\u0014J\t\u0010\u009e\u0002\u001a\u00020\bH\u0002J\n\u0010\u009f\u0002\u001a\u00030 \u0001H\u0016J\n\u0010 \u0002\u001a\u00030 \u0001H\u0014J\n\u0010¡\u0002\u001a\u00030 \u0001H\u0014J\u0016\u0010¢\u0002\u001a\u00030 \u00012\n\u0010¡\u0001\u001a\u0005\u0018\u00010¢\u0001H\u0016J\n\u0010£\u0002\u001a\u00030 \u0001H\u0014J\n\u0010¤\u0002\u001a\u00030 \u0001H\u0014J$\u0010¥\u0002\u001a\u00030 \u00012\u0007\u0010¦\u0002\u001a\u00020^2\u000f\u0010§\u0002\u001a\n\u0012\u0005\u0012\u00030 \u00010¨\u0002H\u0004J\u0013\u0010©\u0002\u001a\u00030 \u00012\u0007\u0010ª\u0002\u001a\u000208H\u0017J\u0013\u0010«\u0002\u001a\u00030 \u00012\u0007\u0010¬\u0002\u001a\u000208H\u0017J\u0013\u0010\u00ad\u0002\u001a\u00030 \u00012\u0007\u0010®\u0002\u001a\u000208H\u0017J\u0013\u0010¯\u0002\u001a\u00030 \u00012\u0007\u0010°\u0002\u001a\u000208H\u0016J\u0013\u0010±\u0002\u001a\u00030 \u00012\u0007\u0010²\u0002\u001a\u000208H\u0017J\u0016\u0010³\u0002\u001a\u00030 \u00012\n\u0010´\u0002\u001a\u0005\u0018\u00010µ\u0002H\u0017J\u0015\u0010¶\u0002\u001a\u00030 \u00012\t\u0010·\u0002\u001a\u0004\u0018\u00010FH\u0016J\u0013\u0010¸\u0002\u001a\u00030 \u00012\u0007\u0010¹\u0002\u001a\u00020^H\u0016J\u0013\u0010º\u0002\u001a\u00030 \u00012\u0007\u0010»\u0002\u001a\u00020\bH\u0016J\u0016\u0010¼\u0002\u001a\u00030 \u00012\n\u0010½\u0002\u001a\u0005\u0018\u00010µ\u0002H\u0017J\u0011\u0010¾\u0002\u001a\u00030 \u00012\u0007\u0010¿\u0002\u001a\u000208J\u0013\u0010À\u0002\u001a\u00030 \u00012\u0007\u0010Á\u0002\u001a\u00020\bH\u0016J\u0013\u0010Â\u0002\u001a\u00030 \u00012\u0007\u0010Ã\u0002\u001a\u00020\bH\u0016J\u0015\u0010Ä\u0002\u001a\u00030 \u00012\t\u0010·\u0002\u001a\u0004\u0018\u00010DH\u0016J\u0015\u0010Å\u0002\u001a\u00030 \u00012\t\u0010·\u0002\u001a\u0004\u0018\u00010HH\u0016J\u0013\u0010Æ\u0002\u001a\u00030 \u00012\u0007\u0010Ç\u0002\u001a\u000208H\u0017J\u0013\u0010È\u0002\u001a\u00030 \u00012\u0007\u0010Á\u0001\u001a\u00020\bH\u0016J\u001c\u0010È\u0002\u001a\u00030 \u00012\u0007\u0010Á\u0001\u001a\u00020\b2\u0007\u0010É\u0002\u001a\u00020^H\u0016J%\u0010È\u0002\u001a\u00030 \u00012\u0007\u0010Á\u0001\u001a\u00020\b2\u0007\u0010É\u0002\u001a\u00020^2\u0007\u0010\u008b\u0002\u001a\u00020^H\u0016J\u0016\u0010Ê\u0002\u001a\u00030 \u00012\n\u0010Ë\u0002\u001a\u0005\u0018\u00010Ë\u0001H\u0016J\u0015\u0010Ì\u0002\u001a\u00030 \u00012\t\u0010Á\u0001\u001a\u0004\u0018\u00010zH\u0017J\u0013\u0010Í\u0002\u001a\u00030 \u00012\u0007\u0010Î\u0002\u001a\u000208H\u0017J\n\u0010Ï\u0002\u001a\u00030 \u0001H\u0017J\u0013\u0010Ð\u0002\u001a\u00030 \u00012\u0007\u0010Ñ\u0002\u001a\u000208H\u0017J\u0013\u0010Ò\u0002\u001a\u00030 \u00012\u0007\u0010Ó\u0002\u001a\u000208H\u0017J\u0013\u0010Ô\u0002\u001a\u00030 \u00012\u0007\u0010Õ\u0002\u001a\u000208H\u0016J\u0013\u0010Ö\u0002\u001a\u00030 \u00012\u0007\u0010×\u0002\u001a\u00020\bH\u0016J\u0016\u0010Ø\u0002\u001a\u00030 \u00012\n\u0010Ù\u0002\u001a\u0005\u0018\u00010Ë\u0001H\u0016J\u0015\u0010Ú\u0002\u001a\u00030 \u00012\t\u0010¼\u0001\u001a\u0004\u0018\u00010zH\u0017J\u0016\u0010Û\u0002\u001a\u00030 \u00012\n\u0010Ü\u0002\u001a\u0005\u0018\u00010Ë\u0001H\u0016J\u001c\u0010Ý\u0002\u001a\u00030 \u00012\u0007\u0010â\u0001\u001a\u0002082\u0007\u0010Þ\u0002\u001a\u00020^H\u0014J\u001c\u0010ß\u0002\u001a\u00030 \u00012\b\u0010à\u0002\u001a\u00030\u008a\u00012\u0006\u0010\u000b\u001a\u00020zH\u0003J\u0013\u0010á\u0002\u001a\u00020^2\b\u0010°\u0001\u001a\u00030±\u0001H\u0002J\t\u0010â\u0002\u001a\u00020^H\u0014J$\u0010ã\u0002\u001a\u00020^2\u0007\u0010ä\u0002\u001a\u00020\b2\u0007\u0010\u008b\u0002\u001a\u00020^2\u0007\u0010å\u0002\u001a\u00020\bH\u0004J\t\u0010æ\u0002\u001a\u00020^H\u0014J\u0013\u0010ç\u0002\u001a\u00030 \u00012\u0007\u0010è\u0002\u001a\u00020^H\u0017J\u0012\u0010é\u0002\u001a\u0002082\u0007\u0010ê\u0002\u001a\u000208H\u0014J\n\u0010ë\u0002\u001a\u00030 \u0001H\u0014J\u001c\u0010ì\u0002\u001a\u00030 \u00012\u0007\u0010\u008a\u0002\u001a\u00020\b2\u0007\u0010\u008b\u0002\u001a\u00020^H\u0014J\n\u0010í\u0002\u001a\u00030 \u0001H\u0002J\u001c\u0010î\u0002\u001a\u00030 \u00012\u0007\u0010\u0097\u0002\u001a\u00020^2\u0007\u0010ï\u0002\u001a\u00020^H\u0016J\u001c\u0010ð\u0002\u001a\u00030 \u00012\u0007\u0010\u0097\u0002\u001a\u00020^2\u0007\u0010ï\u0002\u001a\u00020^H\u0014J\u0013\u0010ñ\u0002\u001a\u00030 \u00012\u0007\u0010Á\u0001\u001a\u00020\bH\u0016J\u001c\u0010ñ\u0002\u001a\u00030 \u00012\u0007\u0010Á\u0001\u001a\u00020\b2\u0007\u0010ò\u0002\u001a\u00020^H\u0016J\u0013\u0010ó\u0002\u001a\u00030 \u00012\u0007\u0010Á\u0001\u001a\u00020\bH\u0016J\n\u0010ô\u0002\u001a\u00030 \u0001H\u0014J\u0014\u0010õ\u0002\u001a\u00030 \u00012\b\u0010°\u0001\u001a\u00030±\u0001H\u0014J\u0014\u0010ö\u0002\u001a\u00030 \u00012\b\u0010°\u0001\u001a\u00030±\u0001H\u0014J\n\u0010÷\u0002\u001a\u00030 \u0001H\u0014J\n\u0010ø\u0002\u001a\u00030 \u0001H\u0014J\n\u0010ù\u0002\u001a\u00030 \u0001H\u0014R$\u0010\f\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\b8V@VX\u0096\u000e¢\u0006\f\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0019\u001a\u00020\u001aX\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u0014\u0010\u001f\u001a\b\u0018\u00010 R\u00020\u0000X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010!\u001a\u0004\u0018\u00010\"X\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010#\u001a\u0004\u0018\u00010$X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010&\"\u0004\b'\u0010(R\u0010\u0010)\u001a\u0004\u0018\u00010*X\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010+\u001a\u0004\u0018\u00010,X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b-\u0010.\"\u0004\b/\u00100R\u001a\u00101\u001a\u00020\bX\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b2\u0010\u000e\"\u0004\b3\u0010\u0010R\u001a\u00104\u001a\u00020\bX\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b5\u0010\u000e\"\u0004\b6\u0010\u0010R\u000e\u00107\u001a\u000208X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010:\u001a\u000208X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b;\u0010<\"\u0004\b=\u0010>R\u001a\u0010?\u001a\u000208X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b@\u0010<\"\u0004\bA\u0010>R\u000e\u0010B\u001a\u000208X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010C\u001a\u0004\u0018\u00010DX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010E\u001a\u0004\u0018\u00010FX\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010G\u001a\u0004\u0018\u00010HX\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bI\u0010J\"\u0004\bK\u0010LR\u0010\u0010M\u001a\u0004\u0018\u00010NX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010O\u001a\u0004\u0018\u00010PX\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010Q\u001a\u0004\u0018\u00010RX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bS\u0010T\"\u0004\bU\u0010VR\u001a\u0010W\u001a\u00020XX\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bY\u0010Z\"\u0004\b[\u0010\\R\u001a\u0010]\u001a\u00020^X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b_\u0010`\"\u0004\ba\u0010bR\u0010\u0010c\u001a\u0004\u0018\u00010dX\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010e\u001a\u000208X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bf\u0010<\"\u0004\bg\u0010>R\u001a\u0010h\u001a\u00020\u0018X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bi\u0010j\"\u0004\bk\u0010lR\u001c\u0010m\u001a\u0004\u0018\u00010nX\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bo\u0010p\"\u0004\bq\u0010rR \u0010s\u001a\b\u0018\u00010 R\u00020\u0000X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bt\u0010u\"\u0004\bv\u0010wR\u000e\u0010x\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010y\u001a\u0004\u0018\u00010zX\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010{\u001a\u00020^X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b|\u0010`\"\u0004\b}\u0010bR\u000e\u0010~\u001a\u00020\u007fX\u0082\u0004¢\u0006\u0002\n\u0000R \u0010\u0080\u0001\u001a\u00030\u0081\u0001X\u0084\u000e¢\u0006\u0012\n\u0000\u001a\u0006\b\u0082\u0001\u0010\u0083\u0001\"\u0006\b\u0084\u0001\u0010\u0085\u0001R\u000f\u0010\u0086\u0001\u001a\u00020^X\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u0087\u0001\u001a\u00020\u001aX\u0084\u0004¢\u0006\t\n\u0000\u001a\u0005\b\u0088\u0001\u0010\u001cR\u0012\u0010\u0089\u0001\u001a\u0005\u0018\u00010\u008a\u0001X\u0082\u000e¢\u0006\u0002\n\u0000R\"\u0010\u008b\u0001\u001a\u0005\u0018\u00010\u008c\u0001X\u0084\u000e¢\u0006\u0012\n\u0000\u001a\u0006\b\u008d\u0001\u0010\u008e\u0001\"\u0006\b\u008f\u0001\u0010\u0090\u0001R\u001d\u0010\u0091\u0001\u001a\u000208X\u0084\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b\u0092\u0001\u0010<\"\u0005\b\u0093\u0001\u0010>R\u001d\u0010\u0094\u0001\u001a\u00020\bX\u0084\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b\u0095\u0001\u0010\u000e\"\u0005\b\u0096\u0001\u0010\u0010R'\u0010\u0097\u0001\u001a\u0002082\u0006\u0010\u000b\u001a\u0002088V@VX\u0096\u000e¢\u0006\u000e\u001a\u0005\b\u0098\u0001\u0010<\"\u0005\b\u0099\u0001\u0010>R'\u0010\u009a\u0001\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\b8V@VX\u0096\u000e¢\u0006\u000e\u001a\u0005\b\u009b\u0001\u0010\u000e\"\u0005\b\u009c\u0001\u0010\u0010R\u0014\u0010\u009d\u0001\u001a\u0004\u0018\u00010\bX\u0082\u000e¢\u0006\u0005\n\u0003\u0010\u009e\u0001¨\u0006ÿ\u0002"}, d2 = {"Lcom/coui/appcompat/seekbar/core/COUISeekBarCore;", "Landroid/widget/AbsSeekBar;", "Lcom/coui/appcompat/seekbar/core/ICOUISeekBar;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "defStyleRes", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "value", "interactionMin", "getInteractionMin", "()I", "setInteractionMin", "(I)V", "mAnimationHandler", "Lcom/coui/appcompat/seekbar/animation/IAnimationHandler;", "getMAnimationHandler", "()Lcom/coui/appcompat/seekbar/animation/IAnimationHandler;", "setMAnimationHandler", "(Lcom/coui/appcompat/seekbar/animation/IAnimationHandler;)V", "mBackgroundPath", "Landroid/graphics/Path;", "mBackgroundRect", "Landroid/graphics/RectF;", "getMBackgroundRect", "()Landroid/graphics/RectF;", "setMBackgroundRect", "(Landroid/graphics/RectF;)V", "mBackgroundSmoothRoundCornerHelper", "Lcom/coui/appcompat/seekbar/core/COUISeekBarCore$SmoothRoundCornerHelper;", "mCachedLocale", "Ljava/util/Locale;", "mDeformationRenderer", "Lcom/coui/appcompat/seekbar/effect/DeformationEffectRenderer;", "getMDeformationRenderer", "()Lcom/coui/appcompat/seekbar/effect/DeformationEffectRenderer;", "setMDeformationRenderer", "(Lcom/coui/appcompat/seekbar/effect/DeformationEffectRenderer;)V", "mEffectManager", "Lcom/coui/appcompat/seekbar/effect/EffectChainManager;", "mHeightSpringAnim", "Lcom/coui/appcompat/animation/dynamicanimation/COUISpringAnimation;", "getMHeightSpringAnim", "()Lcom/coui/appcompat/animation/dynamicanimation/COUISpringAnimation;", "setMHeightSpringAnim", "(Lcom/coui/appcompat/animation/dynamicanimation/COUISpringAnimation;)V", "mIncrement", "getMIncrement", "setMIncrement", "mInternalProgress", "getMInternalProgress", "setMInternalProgress", "mInvalidatePixelThreshold", "", "mLastDrawnProgressForThrottle", "mLastX", "getMLastX", "()F", "setMLastX", "(F)V", "mMaxBackgroundHeight", "getMMaxBackgroundHeight", "setMMaxBackgroundHeight", "mMoveDamping", "mOnBackgroundHeightChangedListener", "Lcom/coui/appcompat/seekbar/core/COUISeekBarCore$OnBackgroundHeightChangedListener;", "mOnDeformedListener", "Lcom/coui/appcompat/seekbar/listener/OnDeformedListener;", "mOnSeekBarChangeListener", "Lcom/coui/appcompat/seekbar/core/COUISeekBarCore$OnSeekBarChangeListener;", "getMOnSeekBarChangeListener", "()Lcom/coui/appcompat/seekbar/core/COUISeekBarCore$OnSeekBarChangeListener;", "setMOnSeekBarChangeListener", "(Lcom/coui/appcompat/seekbar/core/COUISeekBarCore$OnSeekBarChangeListener;)V", "mOplusCanvas", "Lcom/oplus/graphics/OplusCanvas;", "mOplusCanvasRef", "Landroid/graphics/Canvas;", "mOrientationHandler", "Lcom/coui/appcompat/seekbar/handler/IOrientationHandler;", "getMOrientationHandler", "()Lcom/coui/appcompat/seekbar/handler/IOrientationHandler;", "setMOrientationHandler", "(Lcom/coui/appcompat/seekbar/handler/IOrientationHandler;)V", "mPaint", "Landroid/graphics/Paint;", "getMPaint", "()Landroid/graphics/Paint;", "setMPaint", "(Landroid/graphics/Paint;)V", "mPendingRelease", "", "getMPendingRelease", "()Z", "setMPendingRelease", "(Z)V", "mPercentFormat", "Ljava/text/NumberFormat;", "mPixPerProgress", "getMPixPerProgress", "setMPixPerProgress", "mProgressPath", "getMProgressPath", "()Landroid/graphics/Path;", "setMProgressPath", "(Landroid/graphics/Path;)V", "mProgressRenderer", "Lcom/coui/appcompat/seekbar/renderer/IProgressRenderer;", "getMProgressRenderer", "()Lcom/coui/appcompat/seekbar/renderer/IProgressRenderer;", "setMProgressRenderer", "(Lcom/coui/appcompat/seekbar/renderer/IProgressRenderer;)V", "mProgressSmoothRoundCornerHelper", "getMProgressSmoothRoundCornerHelper", "()Lcom/coui/appcompat/seekbar/core/COUISeekBarCore$SmoothRoundCornerHelper;", "setMProgressSmoothRoundCornerHelper", "(Lcom/coui/appcompat/seekbar/core/COUISeekBarCore$SmoothRoundCornerHelper;)V", "mRefreshStyle", "mSeekBarRoleDescription", "", "mStartDragging", "getMStartDragging", "setMStartDragging", "mStartReleaseRunnable", "Ljava/lang/Runnable;", "mState", "Lcom/coui/appcompat/seekbar/core/SeekBarState;", "getMState", "()Lcom/coui/appcompat/seekbar/core/SeekBarState;", "setMState", "(Lcom/coui/appcompat/seekbar/core/SeekBarState;)V", "mSuppressInvalidateForThrottle", "mTempRect", "getMTempRect", "mTextDrawable", "Lcom/coui/appcompat/seekbar/TextDrawable;", "mThumbRenderer", "Lcom/coui/appcompat/seekbar/renderer/IThumbRenderer;", "getMThumbRenderer", "()Lcom/coui/appcompat/seekbar/renderer/IThumbRenderer;", "setMThumbRenderer", "(Lcom/coui/appcompat/seekbar/renderer/IThumbRenderer;)V", "mTouchDownX", "getMTouchDownX", "setMTouchDownX", "mTouchSlop", "getMTouchSlop", "setMTouchSlop", "moveDamping", "getMoveDamping", "setMoveDamping", "moveType", "getMoveType", "setMoveType", "pendingInteractionMinAttr", "Ljava/lang/Integer;", "addEffect", "", "effect", "Lcom/coui/appcompat/seekbar/effect/IEffectRenderer;", "attemptClaimDrag", "calculateDamping", "calculateProgressPosition", "calculateTouchDeformationValue", "cancelFlexibleFollowHandAnim", "cancelHeightSpring", "checkPendingReleaseAndPost", "clearDeformationValue", "createAnimationHandler", "createOrientationHandler", "createProgressRenderer", "createThumbRenderer", "dispatchTouchEvent", NotificationCompat.CATEGORY_EVENT, "Landroid/view/MotionEvent;", "draw", "canvas", "drawActiveTrack", "drawBackground", "drawEffects", "drawInactiveTrack", "drawOverlayEffects", "drawableStateChanged", "effectiveLowerBound", "ensureLabelsAdded", "text", "ensureLabelsRemoved", "ensureOplusCanvas", "ensureThumbPositionForEffects", "formatStateDescription", "progress", "getBackgroundDrawRadius", "getBackgroundHorizontalInset", "state", "getBackgroundRadiusForDraw", "getBackgroundRect", "getColor", "view", "Landroid/view/View;", "colorStateList", "Landroid/content/res/ColorStateList;", IndexProtocol.CONFIG_DFT_VAL, "getDefaultBackgroundEnlargeScale", "getEffect", ExifInterface.GPS_DIRECTION_TRUE, "effectClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)Lcom/coui/appcompat/seekbar/effect/IEffectRenderer;", "getEnd", "getEnlargeFraction", "getIncrement", "getInvalidatePixelThreshold", "getLabelHeight", "getMax", "getMin", "getMinEnlargeHeight", "getPercent", "getProgress", "getProgressLimit", "getProgressRectHeight", "getProgressStart", "getRealProgress", "getRealScale", "scale", "getRefreshStyle", "getSeekBarCenterX", "getSeekBarCenterY", "getSeekBarHeight", "getSeekBarLength", "getSeekBarWidth", "getStart", "getState", "getTouchPrimaryCoordinate", "handleMotionEventDown", "handleMotionEventUp", "handleMoveByMode", "initAccessibility", "initEnlargeAnim", "initState", "interactionFloorScale", "invalidate", "invalidateDrawable", RegulatoryNodes.ATTRIBUTE_DRAWABLE, "Landroid/graphics/drawable/Drawable;", "isInvalidateThrottleEnabled", "isLayoutRtl", "isVertical", "notifyDeformedListener", "onAttachedToWindow", "onDetachedFromWindow", "onDraw", "onHeightSpringUpdate", "fraction", "onLayout", "changed", "left", "top", "right", "bottom", "onMeasure", "widthMeasureSpec", "heightMeasureSpec", "onProgressAnimationEnd", "targetProgress", "fromUser", "onRestoreInstanceState", "Landroid/os/Parcelable;", "onRtlPropertiesChanged", "layoutDirection", "onSaveInstanceState", "onSizeChanged", "w", "h", "oldw", "oldh", "onStartTrackingTouch", "isFromUser", "onStopTrackingTouch", "onTouchEvent", "performFeedback", "primaryToHorizontalAbsoluteScale", "primary", SearchProtocol.QUERY_LENGTH, "progressBarMinCompat", "refresh", "refreshPixPerProgress", "releaseAnim", "removeEffect", "resetBumpingEdges", "resetDeformationValue", "runWithInvalidateSuppressed", "suppressed", "block", "Lkotlin/Function0;", "setBackgroundEnlargeScale", "backgroundEnlargeScale", "setBackgroundHeight", "backgroundHeight", "setBackgroundRadius", "backgroundRadius", "setBackgroundRoundCornerWeight", "roundCornerWeight", "setCustomProgressAnimDuration", "progressAnimDuration", "setCustomProgressAnimInterpolator", "progressAnimInterpolator", "Landroid/view/animation/Interpolator;", "setDeformedListener", "listener", "setEnabled", "enabled", "setIncrement", "increment", "setInterpolator", "interpolator", "setInvalidatePixelThreshold", "thresholdPx", "setMax", "max", "setMin", SliceHints.SUBTYPE_MIN, "setOnBackgroundHeightChangedListener", "setOnSeekBarChangeListener", "setPaddingHorizontal", "paddingHorizontal", "setProgress", "animate", "setProgressColor", "progressColor", "setProgressContentDescription", "setProgressEnlargeScale", "progressEnlargeScale", "setProgressFull", "setProgressHeight", "progressHeight", "setProgressRadius", "progressRadius", "setProgressRoundCornerWeight", "progressRoundCornerWeight", "setRefreshStyle", "refreshStyle", "setSeekBarBackgroundColor", "backgroundColor", "setText", "setThumbColor", "thumbColor", "setTouchScale", "updateDrawProgressScale", "setValueForLabel", "label", "shouldApplyMiddleDamping", "shouldEnlargeOnDown", "shouldThrottleInvalidate", "newProgress", "lastDrawnProgress", "shouldUpdateDrawProgressOnSetProgress", "showText", "isShow", "snapHeightToPixelGrid", "height", "startDrag", "startProgressTransitionAnim", "startReleaseSpring", "startTrackingTouch", "isTouching", "stopTrackingTouch", "superSetProgress", "shouldInvalidate", "syncProgressToParent", "touchAnim", "trackTouchEvent", "trackTouchEventByFinger", "updateColors", "updateLayoutState", "updateState", "Companion", "OnBackgroundHeightChangedListener", "OnSeekBarChangeListener", "SavedState", "SmoothRoundCornerHelper", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class COUISeekBarCore extends AbsSeekBar implements ICOUISeekBar {
    private static final int DAMPING_DISTANCE = 20;
    private static final float DEFAULT_BACKGROUND_ENLARGE_SCALE = 1.4f;
    private static final float HALF_DIVIDER_F = 2.0f;
    public static final float HEIGHT_SPRING_BOUNCE = 0.0f;
    public static final float HEIGHT_SPRING_BOUNCE_ENLARGE = 0.0f;
    public static final float HEIGHT_SPRING_RESPONSE = 0.5f;
    private static final float MAX_MOVE_DAMPING = 1.0f;
    public static final int MOVE_BY_DEFAULT = 0;
    public static final int MOVE_BY_DISTANCE = 2;
    public static final int MOVE_BY_FINGER = 1;
    private static final float SCALE_MAX = 1.0f;
    private static final float SCALE_MIN = 0.0f;
    private static final float SPRING_FINAL_POSITION = 100.0f;
    private IAnimationHandler mAnimationHandler;
    private Path mBackgroundPath;
    private RectF mBackgroundRect;
    private SmoothRoundCornerHelper mBackgroundSmoothRoundCornerHelper;
    private Locale mCachedLocale;
    private DeformationEffectRenderer mDeformationRenderer;
    private EffectChainManager mEffectManager;
    private COUISpringAnimation mHeightSpringAnim;
    private int mIncrement;
    private int mInternalProgress;
    private float mInvalidatePixelThreshold;
    private int mLastDrawnProgressForThrottle;
    private float mLastX;
    private float mMaxBackgroundHeight;
    private float mMoveDamping;
    private OnBackgroundHeightChangedListener mOnBackgroundHeightChangedListener;
    private OnDeformedListener mOnDeformedListener;
    private OnSeekBarChangeListener mOnSeekBarChangeListener;
    private OplusCanvas mOplusCanvas;
    private Canvas mOplusCanvasRef;
    private IOrientationHandler mOrientationHandler;
    private Paint mPaint;
    private boolean mPendingRelease;
    private NumberFormat mPercentFormat;
    private float mPixPerProgress;
    private Path mProgressPath;
    private IProgressRenderer mProgressRenderer;
    private SmoothRoundCornerHelper mProgressSmoothRoundCornerHelper;
    private int mRefreshStyle;
    private String mSeekBarRoleDescription;
    private boolean mStartDragging;
    private final Runnable mStartReleaseRunnable;
    private SeekBarState mState;
    private boolean mSuppressInvalidateForThrottle;
    private final RectF mTempRect;
    private TextDrawable mTextDrawable;
    private IThumbRenderer mThumbRenderer;
    private float mTouchDownX;
    private int mTouchSlop;
    private Integer pendingInteractionMinAttr;

    @Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\bæ\u0080\u0001\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&¨\u0006\b"}, d2 = {"Lcom/coui/appcompat/seekbar/core/COUISeekBarCore$OnBackgroundHeightChangedListener;", "", "onBackgroundHeightChanged", "", "seekBar", "Lcom/coui/appcompat/seekbar/core/COUISeekBarCore;", "curBackgroundHeight", "", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface OnBackgroundHeightChangedListener {
        void onBackgroundHeightChanged(COUISeekBarCore seekBar, float curBackgroundHeight);
    }

    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH&J\u0010\u0010\n\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\f"}, d2 = {"Lcom/coui/appcompat/seekbar/core/COUISeekBarCore$OnSeekBarChangeListener;", "", "onProgressChanged", "", "seekBar", "Lcom/coui/appcompat/seekbar/core/COUISeekBarCore;", "progress", "", "fromUser", "", "onStartTrackingTouch", "onStopTrackingTouch", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface OnSeekBarChangeListener {
        void onProgressChanged(COUISeekBarCore seekBar, int progress, boolean fromUser);

        void onStartTrackingTouch(COUISeekBarCore seekBar);

        void onStopTrackingTouch(COUISeekBarCore seekBar);
    }

    @Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u0011\b\u0016\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004B\u000f\b\u0012\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\tH\u0016R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u000b\"\u0004\b\u0010\u0010\r¨\u0006\u0016"}, d2 = {"Lcom/coui/appcompat/seekbar/core/COUISeekBarCore$SavedState;", "Landroid/view/View$BaseSavedState;", GradientCircularProgressIndicator.STATE_SUPER_STATE, "Landroid/os/Parcelable;", "(Landroid/os/Parcelable;)V", "parcel", "Landroid/os/Parcel;", "(Landroid/os/Parcel;)V", "mSaveInteractionMin", "", "getMSaveInteractionMin", "()I", "setMSaveInteractionMin", "(I)V", "mSaveProgress", "getMSaveProgress", "setMSaveProgress", "writeToParcel", "", "out", "flags", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class SavedState extends View.BaseSavedState {
        private int mSaveInteractionMin;
        private int mSaveProgress;
        public static final Parcelable.Creator<SavedState> CREATOR = new COUISeekBarCore$SavedState$Companion$CREATOR$1();

        public /* synthetic */ SavedState(Parcel parcel, DefaultConstructorMarker defaultConstructorMarker) {
            this(parcel);
        }

        public final int getMSaveInteractionMin() {
            return this.mSaveInteractionMin;
        }

        public final int getMSaveProgress() {
            return this.mSaveProgress;
        }

        public final void setMSaveInteractionMin(int i3) {
            this.mSaveInteractionMin = i3;
        }

        public final void setMSaveProgress(int i3) {
            this.mSaveProgress = i3;
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            Intrinsics.checkNotNullParameter(out, "out");
            super.writeToParcel(out, flags);
            out.writeInt(this.mSaveProgress);
            out.writeInt(this.mSaveInteractionMin);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mSaveProgress = parcel.readInt();
            this.mSaveInteractionMin = parcel.readInt();
        }
    }

    @Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\"\u0010\u0007\u001a\u0004\u0018\u00010\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u000e"}, d2 = {"Lcom/coui/appcompat/seekbar/core/COUISeekBarCore$SmoothRoundCornerHelper;", "", "path", "Landroid/graphics/Path;", "(Lcom/coui/appcompat/seekbar/core/COUISeekBarCore;Landroid/graphics/Path;)V", "<set-?>", "Lcom/oplus/graphics/OplusPathAdapter;", "pathAdapter", "getPathAdapter", "()Lcom/oplus/graphics/OplusPathAdapter;", "smoothStyleType", "", "getSmoothStyleType", "()I", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public final class SmoothRoundCornerHelper {
        private OplusPathAdapter pathAdapter;
        private final int smoothStyleType;
        final /* synthetic */ COUISeekBarCore this$0;

        public SmoothRoundCornerHelper(COUISeekBarCore cOUISeekBarCore, Path path) {
            Intrinsics.checkNotNullParameter(path, "path");
            this.this$0 = cOUISeekBarCore;
            int smoothStyleType = RoundCornerUtil.getSmoothStyleType();
            this.smoothStyleType = smoothStyleType;
            if (smoothStyleType == 1) {
                this.pathAdapter = new OplusPathAdapter(path, smoothStyleType);
            }
        }

        public final OplusPathAdapter getPathAdapter() {
            return this.pathAdapter;
        }

        public final int getSmoothStyleType() {
            return this.smoothStyleType;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.seekbar.core.COUISeekBarCore$initAccessibility$1, reason: invalid class name */
    @Metadata(d1 = {"\u0000/\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\"\u0010\b\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0016¨\u0006\u000e"}, d2 = {"com/coui/appcompat/seekbar/core/COUISeekBarCore$initAccessibility$1", "Landroidx/core/view/AccessibilityDelegateCompat;", "onInitializeAccessibilityNodeInfo", "", "host", "Landroid/view/View;", SettingsHelper.KEY_INFO, "Landroidx/core/view/accessibility/AccessibilityNodeInfoCompat;", "performAccessibilityAction", "", "action", "", Constants.MessagerConstants.ARGS_KEY, "Landroid/os/Bundle;", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass1 extends AccessibilityDelegateCompat {
        public AnonymousClass1() {
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            Intrinsics.checkNotNullParameter(host, "host");
            Intrinsics.checkNotNullParameter(info, "info");
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SET_PROGRESS);
            info.setRangeInfo(AccessibilityNodeInfoCompat.RangeInfoCompat.obtain(1, COUISeekBarCore.this.getMin(), COUISeekBarCore.this.getMax(), COUISeekBarCore.this.getProgress()));
            info.setRoleDescription(COUISeekBarCore.access$getMSeekBarRoleDescription$p(COUISeekBarCore.this));
            COUISeekBarCore cOUISeekBarCore = COUISeekBarCore.this;
            info.setStateDescription(COUISeekBarCore.access$formatStateDescription(cOUISeekBarCore, cOUISeekBarCore.getProgress()));
            if (COUISeekBarCore.this.isEnabled()) {
                int progress = COUISeekBarCore.this.getProgress();
                if (progress > COUISeekBarCore.this.getMin()) {
                    info.addAction(8192);
                }
                if (progress < COUISeekBarCore.this.getMax()) {
                    info.addAction(4096);
                }
            }
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public boolean performAccessibilityAction(View host, int action, Bundle args) {
            Intrinsics.checkNotNullParameter(host, "host");
            if (!COUISeekBarCore.this.isEnabled()) {
                return false;
            }
            if (action == 4096) {
                int mIncrement = COUISeekBarCore.this.getMIncrement() > 0 ? COUISeekBarCore.this.getMIncrement() : 1;
                COUISeekBarCore cOUISeekBarCore = COUISeekBarCore.this;
                cOUISeekBarCore.setProgress(cOUISeekBarCore.getProgress() + mIncrement, false, true);
                COUISeekBarCore cOUISeekBarCore2 = COUISeekBarCore.this;
                cOUISeekBarCore2.announceForAccessibility(COUISeekBarCore.access$formatStateDescription(cOUISeekBarCore2, cOUISeekBarCore2.getProgress()));
                return true;
            }
            if (action != 8192) {
                return super.performAccessibilityAction(host, action, args);
            }
            int mIncrement2 = COUISeekBarCore.this.getMIncrement() > 0 ? COUISeekBarCore.this.getMIncrement() : 1;
            COUISeekBarCore cOUISeekBarCore3 = COUISeekBarCore.this;
            cOUISeekBarCore3.setProgress(cOUISeekBarCore3.getProgress() - mIncrement2, false, true);
            COUISeekBarCore cOUISeekBarCore4 = COUISeekBarCore.this;
            cOUISeekBarCore4.announceForAccessibility(COUISeekBarCore.access$formatStateDescription(cOUISeekBarCore4, cOUISeekBarCore4.getProgress()));
            return true;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.seekbar.core.COUISeekBarCore$setProgress$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class C13521 extends Lambda implements Function0<Unit> {
        final /* synthetic */ int $newProgress;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public C13521(int i3) {
            super(0);
            this.$newProgress = i3;
        }

        @Override // kotlin.jvm.functions.Function0
        public /* bridge */ /* synthetic */ Unit invoke() {
            invoke2();
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2() {
            COUISeekBarCore.access$setProgress$s1270806985(COUISeekBarCore.this, this.$newProgress);
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUISeekBarCore(Context context) {
        this(context, null, 0, 0, 14, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public static /* synthetic */ void a(COUISeekBarCore cOUISeekBarCore, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        initEnlargeAnim$lambda$1(cOUISeekBarCore, cOUIDynamicAnimation, z2, f3, f4);
    }

    public static final /* synthetic */ String access$formatStateDescription(COUISeekBarCore cOUISeekBarCore, int i3) {
        return cOUISeekBarCore.formatStateDescription(i3);
    }

    public static final /* synthetic */ String access$getMSeekBarRoleDescription$p(COUISeekBarCore cOUISeekBarCore) {
        return cOUISeekBarCore.mSeekBarRoleDescription;
    }

    public static final /* synthetic */ void access$setProgress$s1270806985(COUISeekBarCore cOUISeekBarCore, int i3) {
        super.setProgress(i3);
    }

    private final void attemptClaimDrag() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }

    public static /* synthetic */ void b(COUISeekBarCore cOUISeekBarCore) {
        mStartReleaseRunnable$lambda$2(cOUISeekBarCore);
    }

    public static /* synthetic */ void c(COUISeekBarCore cOUISeekBarCore, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        initEnlargeAnim$lambda$0(cOUISeekBarCore, cOUIDynamicAnimation, f3, f4);
    }

    private final float calculateProgressPosition() {
        IOrientationHandler iOrientationHandler = this.mOrientationHandler;
        if (iOrientationHandler == null) {
            return 0.0f;
        }
        return iOrientationHandler.calculateThumbPosition(this.mState.getMax() - this.mState.getMin() > 0 ? (this.mState.getProgress() - this.mState.getMin()) / (this.mState.getMax() - this.mState.getMin()) : 0.0f, iOrientationHandler.getSeekBarLength(this), this.mState.getIsRtl());
    }

    private final void checkPendingReleaseAndPost() {
        if (!this.mPendingRelease || this.mState.getCurBackgroundHeight() - this.mState.getBackgroundHeight() < getMinEnlargeHeight()) {
            return;
        }
        this.mPendingRelease = false;
        post(this.mStartReleaseRunnable);
    }

    private final void clearDeformationValue() {
        if (this.mState.getScale() <= 0.0f || this.mState.getScale() >= 1.0f) {
            return;
        }
        resetDeformationValue();
    }

    private final OplusCanvas ensureOplusCanvas(Canvas canvas) {
        if (canvas != this.mOplusCanvasRef || this.mOplusCanvas == null) {
            this.mOplusCanvas = new OplusCanvas(canvas);
            this.mOplusCanvasRef = canvas;
        }
        OplusCanvas oplusCanvas = this.mOplusCanvas;
        Intrinsics.checkNotNull(oplusCanvas);
        return oplusCanvas;
    }

    private final String formatStateDescription(int progress) {
        Locale locale = getResources().getConfiguration().getLocales().get(0);
        if (locale != null && !Intrinsics.areEqual(locale, this.mCachedLocale)) {
            this.mCachedLocale = locale;
            this.mPercentFormat = NumberFormat.getPercentInstance(locale);
        }
        NumberFormat numberFormat = this.mPercentFormat;
        String str = numberFormat != null ? numberFormat.format(Float.valueOf(getPercent(progress))) : null;
        return str == null ? String.valueOf(progress) : str;
    }

    private final float getPercent(int progress) {
        float max = getMax();
        float min = getMin();
        float f3 = progress;
        float f4 = max - min;
        if (f4 <= 0.0f) {
            return 0.0f;
        }
        return RangesKt.coerceIn((f3 - min) / f4, 0.0f, 1.0f);
    }

    private final void initAccessibility(Context context) {
        this.mSeekBarRoleDescription = context.getString(R.string.coui_seek_bar_role_description);
        ViewCompat.setAccessibilityDelegate(this, new AnonymousClass1());
    }

    private static final void initEnlargeAnim$lambda$0(COUISeekBarCore this$0, COUIDynamicAnimation cOUIDynamicAnimation, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.onHeightSpringUpdate(RangesKt.coerceIn(f3 / 100.0f, 0.0f, 1.0f));
        this$0.checkPendingReleaseAndPost();
    }

    private static final void initEnlargeAnim$lambda$1(COUISeekBarCore this$0, COUIDynamicAnimation cOUIDynamicAnimation, boolean z2, float f3, float f4) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (z2 || !this$0.mPendingRelease) {
            return;
        }
        this$0.mPendingRelease = false;
        this$0.post(this$0.mStartReleaseRunnable);
    }

    private static final void mStartReleaseRunnable$lambda$2(COUISeekBarCore this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.startReleaseSpring();
    }

    private final int progressBarMinCompat() {
        return super.getMin();
    }

    private final void setValueForLabel(TextDrawable label, String value) {
        label.setText(value);
        if (isLayoutRtl()) {
            int start = getStart();
            label.setBounds(start, 0 - label.getIntrinsicHeight(), label.getIntrinsicWidth() + start, 0);
        } else {
            int width = getWidth() - getEnd();
            label.setBounds(width, 0 - label.getIntrinsicHeight(), width - label.getIntrinsicWidth(), 0);
        }
        Rect rect = new Rect(label.getBounds());
        ViewGroup contentView = ViewUtil.getContentView(this);
        if (contentView == null) {
            return;
        }
        DescendantOffsetUtil.offsetDescendantRect(contentView, this, rect);
        label.setBounds(rect);
        ViewOverlay contentViewOverlay = ViewUtil.getContentViewOverlay(this);
        if (contentViewOverlay != null) {
            contentViewOverlay.add(label);
        }
    }

    private final boolean shouldApplyMiddleDamping(MotionEvent event) {
        int width = (int) (((getWidth() - getStart()) - getEnd()) - (this.mState.getPaddingHorizontal() * 2));
        if (width <= 0) {
            return false;
        }
        int max = this.mState.getMax() - this.mState.getMin();
        return Float.compare(max > 0 ? (((float) this.mState.getProgress()) * ((float) width)) / ((float) max) : 0.0f, ((float) width) / 2.0f) == 0 && Math.abs(event.getX() - this.mLastX) < 20.0f;
    }

    private final void startReleaseSpring() {
        this.mPendingRelease = false;
        initEnlargeAnim();
        COUISpringAnimation cOUISpringAnimation = this.mHeightSpringAnim;
        if (cOUISpringAnimation == null) {
            return;
        }
        COUISpringForce spring = cOUISpringAnimation.getSpring();
        if (spring != null) {
            spring.setBounce(0.0f);
        }
        if (!cOUISpringAnimation.isRunning()) {
            cOUISpringAnimation.setStartValue(getEnlargeFraction() * 100.0f);
        }
        cOUISpringAnimation.animateToFinalPosition(0.0f);
    }

    public void addEffect(IEffectRenderer effect) {
        EffectChainManager effectChainManager = this.mEffectManager;
        if (effectChainManager == null || effect == null) {
            return;
        }
        if (effectChainManager != null) {
            effectChainManager.addEffect(effect);
        }
        invalidate();
    }

    public float calculateDamping() {
        if (getMMoveDamping() == 0.0f) {
            return 1.0f;
        }
        return getMMoveDamping();
    }

    public void calculateTouchDeformationValue() {
        DeformationEffectRenderer deformationEffectRenderer = this.mDeformationRenderer;
        if (deformationEffectRenderer != null) {
            deformationEffectRenderer.calculateTouchDeformationValue();
        }
    }

    public void cancelFlexibleFollowHandAnim() {
    }

    public final void cancelHeightSpring() {
        COUISpringAnimation cOUISpringAnimation = this.mHeightSpringAnim;
        if (cOUISpringAnimation != null && cOUISpringAnimation.isRunning()) {
            cOUISpringAnimation.cancel();
        }
        this.mPendingRelease = false;
        removeCallbacks(this.mStartReleaseRunnable);
    }

    public IAnimationHandler createAnimationHandler() {
        return new StandardAnimationHandler(this);
    }

    public IOrientationHandler createOrientationHandler() {
        return new HorizontalOrientationHandler();
    }

    public IProgressRenderer createProgressRenderer() {
        return new StandardProgressRenderer();
    }

    public IThumbRenderer createThumbRenderer() {
        return new StandardThumbRenderer();
    }

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        return super.dispatchTouchEvent(event);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        super.draw(canvas);
        drawOverlayEffects(canvas);
    }

    public void drawActiveTrack(Canvas canvas) {
        IThumbRenderer iThumbRenderer;
        IOrientationHandler iOrientationHandler;
        IProgressRenderer iProgressRenderer;
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        if (this.mState.getShowProgress() && (iProgressRenderer = this.mProgressRenderer) != null && iProgressRenderer != null) {
            iProgressRenderer.drawProgress(canvas, this.mState);
        }
        if (this.mState.getShowThumb() && this.mOrientationHandler != null && ((!this.mState.getShowProgress() || this.mState.getProgress() <= this.mState.getMin()) && (iOrientationHandler = this.mOrientationHandler) != null)) {
            iOrientationHandler.calculateProgressRect(this.mTempRect, this.mState);
        }
        if (!this.mState.getShowThumb() || (iThumbRenderer = this.mThumbRenderer) == null || iThumbRenderer == null) {
            return;
        }
        iThumbRenderer.drawThumb(canvas, this.mState);
    }

    public void drawBackground(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        if (this.mOrientationHandler == null) {
            return;
        }
        float curBackgroundHeight = this.mState.getCurBackgroundHeight();
        float curBackgroundRadius = this.mState.getCurBackgroundRadius();
        boolean z2 = (curBackgroundHeight == this.mState.getBackgroundHeight() || curBackgroundHeight == this.mMaxBackgroundHeight) ? false : true;
        if (z2) {
            float fSnapHeightToPixelGrid = snapHeightToPixelGrid(curBackgroundHeight);
            this.mState.setCurBackgroundHeight(fSnapHeightToPixelGrid);
            this.mState.setCurBackgroundRadius(fSnapHeightToPixelGrid / 2.0f);
        }
        IOrientationHandler iOrientationHandler = this.mOrientationHandler;
        if (iOrientationHandler != null) {
            iOrientationHandler.calculateBackgroundRect(this.mBackgroundRect, this, this.mState);
        }
        drawInactiveTrack(canvas);
        if (z2) {
            this.mState.setCurBackgroundHeight(curBackgroundHeight);
            this.mState.setCurBackgroundRadius(curBackgroundRadius);
        }
    }

    public void drawEffects(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        if (this.mEffectManager != null) {
            float fCalculateProgressPosition = calculateProgressPosition();
            EffectChainManager effectChainManager = this.mEffectManager;
            if (effectChainManager != null) {
                effectChainManager.drawEffects(canvas, this.mState, fCalculateProgressPosition);
            }
        }
    }

    public void drawInactiveTrack(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        SmoothRoundCornerHelper smoothRoundCornerHelper = this.mBackgroundSmoothRoundCornerHelper;
        if (smoothRoundCornerHelper == null) {
            return;
        }
        float backgroundRadiusForDraw = getBackgroundRadiusForDraw();
        int smoothStyleType = smoothRoundCornerHelper.getSmoothStyleType();
        if (smoothStyleType == 0) {
            this.mPaint.setColor(this.mState.getBackgroundColor());
            if (this.mState.getBackgroundRoundCornerWeight() == 0.0f) {
                canvas.drawRoundRect(this.mBackgroundRect, backgroundRadiusForDraw, backgroundRadiusForDraw, this.mPaint);
                return;
            }
            OplusCanvas oplusCanvasEnsureOplusCanvas = ensureOplusCanvas(canvas);
            RectF rectF = this.mBackgroundRect;
            oplusCanvasEnsureOplusCanvas.drawSmoothRoundRect(rectF.left, rectF.top, rectF.right, rectF.bottom, backgroundRadiusForDraw, backgroundRadiusForDraw, this.mPaint, this.mState.getBackgroundRoundCornerWeight());
            return;
        }
        if (smoothStyleType != 1) {
            this.mPaint.setColor(this.mState.getBackgroundColor());
            canvas.drawRoundRect(this.mBackgroundRect, backgroundRadiusForDraw, backgroundRadiusForDraw, this.mPaint);
            return;
        }
        this.mBackgroundPath.reset();
        canvas.save();
        OplusPathAdapter pathAdapter = smoothRoundCornerHelper.getPathAdapter();
        if (pathAdapter != null) {
            pathAdapter.addSmoothRoundRect(this.mBackgroundRect, backgroundRadiusForDraw, backgroundRadiusForDraw, Path.Direction.CCW);
        }
        canvas.clipPath(this.mBackgroundPath);
        canvas.drawColor(this.mState.getBackgroundColor());
        canvas.restore();
    }

    public void drawOverlayEffects(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        if (this.mEffectManager != null) {
            float fCalculateProgressPosition = calculateProgressPosition();
            EffectChainManager effectChainManager = this.mEffectManager;
            if (effectChainManager != null) {
                effectChainManager.drawEffects(canvas, this.mState, fCalculateProgressPosition, DrawPhase.OVERLAY);
            }
        }
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        updateColors();
    }

    public int effectiveLowerBound() {
        return this.mState.getInteractionMin();
    }

    public void ensureLabelsAdded(String text) {
        Intrinsics.checkNotNullParameter(text, "text");
        TextDrawable textDrawable = this.mTextDrawable;
        if (textDrawable == null) {
            return;
        }
        setValueForLabel(textDrawable, text);
    }

    public void ensureLabelsRemoved() {
        ViewOverlay contentViewOverlay = ViewUtil.getContentViewOverlay(this);
        TextDrawable textDrawable = this.mTextDrawable;
        if (contentViewOverlay == null || textDrawable == null) {
            return;
        }
        contentViewOverlay.remove(textDrawable);
    }

    public void ensureThumbPositionForEffects() {
        IOrientationHandler iOrientationHandler = this.mOrientationHandler;
        if (iOrientationHandler == null || iOrientationHandler == null) {
            return;
        }
        iOrientationHandler.calculateProgressRect(this.mTempRect, this.mState);
    }

    public final float getBackgroundDrawRadius() {
        return getBackgroundRadiusForDraw();
    }

    public float getBackgroundHorizontalInset(SeekBarState state) {
        Intrinsics.checkNotNullParameter(state, "state");
        return state.getCurBackgroundHeight() / 2;
    }

    public float getBackgroundRadiusForDraw() {
        return this.mState.getCurBackgroundHeight() / 2.0f;
    }

    /* JADX INFO: renamed from: getBackgroundRect, reason: from getter */
    public final RectF getMBackgroundRect() {
        return this.mBackgroundRect;
    }

    public int getColor(View view, ColorStateList colorStateList, int defaultValue) {
        Intrinsics.checkNotNullParameter(view, "view");
        return colorStateList == null ? defaultValue : colorStateList.getColorForState(view.getDrawableState(), defaultValue);
    }

    public float getDefaultBackgroundEnlargeScale() {
        return DEFAULT_BACKGROUND_ENLARGE_SCALE;
    }

    public <T extends IEffectRenderer> T getEffect(Class<T> effectClass) {
        Intrinsics.checkNotNullParameter(effectClass, "effectClass");
        EffectChainManager effectChainManager = this.mEffectManager;
        if (effectChainManager != null) {
            return (T) effectChainManager.getEffect(effectClass);
        }
        return null;
    }

    public int getEnd() {
        return getPaddingEnd();
    }

    public final float getEnlargeFraction() {
        float backgroundHeight = this.mMaxBackgroundHeight - this.mState.getBackgroundHeight();
        if (backgroundHeight <= 0.0f) {
            return 0.0f;
        }
        return RangesKt.coerceIn((this.mState.getCurBackgroundHeight() - this.mState.getBackgroundHeight()) / backgroundHeight, 0.0f, 1.0f);
    }

    /* JADX INFO: renamed from: getIncrement, reason: from getter */
    public int getMIncrement() {
        return this.mIncrement;
    }

    public int getInteractionMin() {
        return this.mState.getInteractionMin();
    }

    /* JADX INFO: renamed from: getInvalidatePixelThreshold, reason: from getter */
    public final float getMInvalidatePixelThreshold() {
        return this.mInvalidatePixelThreshold;
    }

    public int getLabelHeight() {
        TextDrawable textDrawable = this.mTextDrawable;
        if (textDrawable != null) {
            return textDrawable.getIntrinsicHeight();
        }
        return 0;
    }

    public final IAnimationHandler getMAnimationHandler() {
        return this.mAnimationHandler;
    }

    public final RectF getMBackgroundRect() {
        return this.mBackgroundRect;
    }

    public final DeformationEffectRenderer getMDeformationRenderer() {
        return this.mDeformationRenderer;
    }

    public final COUISpringAnimation getMHeightSpringAnim() {
        return this.mHeightSpringAnim;
    }

    public final int getMIncrement() {
        return this.mIncrement;
    }

    public final int getMInternalProgress() {
        return this.mInternalProgress;
    }

    public final float getMLastX() {
        return this.mLastX;
    }

    public final float getMMaxBackgroundHeight() {
        return this.mMaxBackgroundHeight;
    }

    public final OnSeekBarChangeListener getMOnSeekBarChangeListener() {
        return this.mOnSeekBarChangeListener;
    }

    public final IOrientationHandler getMOrientationHandler() {
        return this.mOrientationHandler;
    }

    public final Paint getMPaint() {
        return this.mPaint;
    }

    public final boolean getMPendingRelease() {
        return this.mPendingRelease;
    }

    public final float getMPixPerProgress() {
        return this.mPixPerProgress;
    }

    public final Path getMProgressPath() {
        return this.mProgressPath;
    }

    public final IProgressRenderer getMProgressRenderer() {
        return this.mProgressRenderer;
    }

    public final SmoothRoundCornerHelper getMProgressSmoothRoundCornerHelper() {
        return this.mProgressSmoothRoundCornerHelper;
    }

    public final boolean getMStartDragging() {
        return this.mStartDragging;
    }

    public final SeekBarState getMState() {
        return this.mState;
    }

    public final RectF getMTempRect() {
        return this.mTempRect;
    }

    public final IThumbRenderer getMThumbRenderer() {
        return this.mThumbRenderer;
    }

    public final float getMTouchDownX() {
        return this.mTouchDownX;
    }

    public final int getMTouchSlop() {
        return this.mTouchSlop;
    }

    @Override // android.widget.ProgressBar, com.coui.appcompat.seekbar.core.ICOUISeekBar
    public int getMax() {
        return super.getMax();
    }

    @Override // android.widget.ProgressBar, com.coui.appcompat.seekbar.core.ICOUISeekBar
    public int getMin() {
        return super.getMin();
    }

    public float getMinEnlargeHeight() {
        return 0.0f;
    }

    /* JADX INFO: renamed from: getMoveDamping, reason: from getter */
    public float getMMoveDamping() {
        return this.mMoveDamping;
    }

    public int getMoveType() {
        return this.mState.getMoveType();
    }

    @Override // android.widget.ProgressBar, com.coui.appcompat.seekbar.core.ICOUISeekBar
    public int getProgress() {
        return super.getProgress();
    }

    public int getProgressLimit(int progress) {
        int max = this.mState.getMax() - this.mState.getMin();
        return RangesKt.coerceIn(progress, this.mState.getMin() - max, this.mState.getMax() + max);
    }

    public float getProgressRectHeight(SeekBarState state) {
        Intrinsics.checkNotNullParameter(state, "state");
        return state.getCurProgressHeight();
    }

    public final float getProgressStart() {
        IOrientationHandler iOrientationHandler = this.mOrientationHandler;
        return iOrientationHandler != null ? iOrientationHandler.getProgressStart(this) : getStart() + this.mState.getPaddingHorizontal();
    }

    public int getRealProgress(int progress) {
        return RangesKt.coerceIn(progress, effectiveLowerBound(), this.mState.getMax());
    }

    public float getRealScale(float scale) {
        return RangesKt.coerceIn(scale, 0.0f, 1.0f);
    }

    /* JADX INFO: renamed from: getRefreshStyle, reason: from getter */
    public int getMRefreshStyle() {
        return this.mRefreshStyle;
    }

    public int getSeekBarCenterX() {
        return getPaddingLeft() + (((getWidth() - getPaddingLeft()) - getPaddingRight()) >> 1);
    }

    public int getSeekBarCenterY() {
        return getPaddingTop() + (((getHeight() - getPaddingBottom()) - getPaddingTop()) >> 1);
    }

    public int getSeekBarHeight() {
        return 0;
    }

    public final float getSeekBarLength() {
        IOrientationHandler iOrientationHandler = this.mOrientationHandler;
        return iOrientationHandler != null ? iOrientationHandler.getSeekBarLength(this) : getSeekBarWidth();
    }

    public int getSeekBarWidth() {
        return (int) (((getWidth() - getStart()) - getEnd()) - (this.mState.getPaddingHorizontal() * 2));
    }

    public int getStart() {
        return getPaddingStart();
    }

    public SeekBarState getState() {
        return this.mState;
    }

    public float getTouchPrimaryCoordinate(MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        return event.getX();
    }

    public void handleMotionEventDown(MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
    }

    public void handleMotionEventUp(MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        onStopTrackingTouch(true);
    }

    public void handleMoveByMode(MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        int moveType = this.mState.getMoveType();
        if (moveType != 0) {
            if (moveType == 1) {
                trackTouchEventByFinger(event);
                return;
            } else if (moveType != 2) {
                trackTouchEvent(event);
                return;
            }
        }
        trackTouchEvent(event);
    }

    public void initEnlargeAnim() {
        if (this.mHeightSpringAnim != null) {
            return;
        }
        FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
        COUISpringForce cOUISpringForce = new COUISpringForce();
        cOUISpringForce.setBounce(0.0f);
        cOUISpringForce.setResponse(0.5f);
        COUISpringAnimation spring = new COUISpringAnimation(floatValueHolder).setSpring(cOUISpringForce);
        this.mHeightSpringAnim = spring;
        if (spring != null) {
            spring.addUpdateListener(new d(this));
        }
        COUISpringAnimation cOUISpringAnimation = this.mHeightSpringAnim;
        if (cOUISpringAnimation != null) {
            cOUISpringAnimation.addEndListener(new e(this));
        }
    }

    public void initState(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) throws Throwable {
        Intrinsics.checkNotNullParameter(context, "context");
        TypedArray typedArray = null;
        try {
            try {
                TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.COUISeekBar, defStyleAttr, defStyleRes);
                try {
                    this.mState.setBackgroundColorStateList$coui_support_seekbar_release(typedArrayObtainStyledAttributes.getColorStateList(R.styleable.COUISeekBar_couiSeekBarBackgroundColor));
                    this.mState.setProgressColorStateList$coui_support_seekbar_release(typedArrayObtainStyledAttributes.getColorStateList(R.styleable.COUISeekBar_couiSeekBarProgressColor));
                    if (this.mState.getProgressColorStateList() == null) {
                        this.mState.setProgressColorStateList$coui_support_seekbar_release(COUIStateListUtil.createColorStateList(COUIContextUtil.getAttrColor(context, com.support.appcompat.R.attr.couiColorContainerTheme, 0), COUIContextUtil.getAttrColor(getContext(), com.support.appcompat.R.attr.couiColorDisable)));
                    }
                    this.mState.setThumbColorStateList$coui_support_seekbar_release(typedArrayObtainStyledAttributes.getColorStateList(R.styleable.COUISeekBar_couiSeekBarThumbColor));
                    SeekBarState seekBarState = this.mState;
                    seekBarState.setBackgroundColor(getColor(this, seekBarState.getBackgroundColorStateList(), COUIContextUtil.getColor(context, R.color.coui_seekbar_background_selector)));
                    int color = COUIContextUtil.getColor(context, R.color.coui_seekbar_progress_selector);
                    SeekBarState seekBarState2 = this.mState;
                    seekBarState2.setProgressColor(getColor(this, seekBarState2.getProgressColorStateList(), color));
                    SeekBarState seekBarState3 = this.mState;
                    seekBarState3.setThumbColor(getColor(this, seekBarState3.getThumbColorStateList(), COUIContextUtil.getColor(context, R.color.coui_seekbar_thumb_selector)));
                    this.mState.setThumbShadowColor(typedArrayObtainStyledAttributes.getColor(R.styleable.COUISeekBar_couiSeekBarThumbShadowColor, COUIContextUtil.getColor(context, R.color.coui_seekbar_thumb_shadow_color)));
                    this.mState.setProgressRadius(typedArrayObtainStyledAttributes.getDimension(R.styleable.COUISeekBar_couiSeekBarProgressRadius, context.getResources().getDimension(R.dimen.coui_seekbar_progress_radius)));
                    this.mState.setThumbRadius(context.getResources().getDimension(R.dimen.coui_seekbar_thumb_radius));
                    this.mState.setThumbMaxRadius(context.getResources().getDimension(R.dimen.coui_seekbar_thumb_max_radius));
                    this.mState.setBackgroundRadius(typedArrayObtainStyledAttributes.getDimension(R.styleable.COUISeekBar_couiSeekBarBackgroundRadius, context.getResources().getDimension(R.dimen.coui_seekbar_background_radius)));
                    this.mState.setProgressHeight(typedArrayObtainStyledAttributes.getDimension(R.styleable.COUISeekBar_couiSeekBarProgressHeight, context.getResources().getDimension(R.dimen.coui_seekbar_progress_height)));
                    this.mState.setBackgroundHeight(typedArrayObtainStyledAttributes.getDimension(R.styleable.COUISeekBar_couiSeekBarBackgroundHeight, context.getResources().getDimension(R.dimen.coui_seekbar_background_height)));
                    this.mState.setShowProgress(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUISeekBar_couiSeekBarShowProgress, true));
                    this.mState.setShowThumb(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUISeekBar_couiSeekBarShowThumb, true));
                    this.mState.setBackgroundEnlargeScale(typedArrayObtainStyledAttributes.getFloat(R.styleable.COUISeekBar_couiSeekBarBackGroundEnlargeScale, getDefaultBackgroundEnlargeScale()));
                    this.mState.setSupportDeformation(typedArrayObtainStyledAttributes.getBoolean(R.styleable.COUISeekBar_couiSeekBarDeformation, true));
                    int i3 = R.styleable.COUISeekBar_couiSeekBarInteractionMin;
                    this.pendingInteractionMinAttr = typedArrayObtainStyledAttributes.hasValue(i3) ? Integer.valueOf(typedArrayObtainStyledAttributes.getInteger(i3, 0)) : null;
                    this.mState.setBackgroundRoundCornerWeight(typedArrayObtainStyledAttributes.getFloat(R.styleable.COUISeekBar_couiSeekBarBackgroundRoundCornerWeight, 0.0f));
                    this.mState.setProgressRoundCornerWeight(typedArrayObtainStyledAttributes.getFloat(R.styleable.COUISeekBar_couiSeekBarProgressRoundCornerWeight, 0.0f));
                    this.mState.setThumbShadowRadiusSize(typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.COUISeekBar_couiSeekBarThumbShadowSize, 0));
                    this.mState.setThumbShadowOffsetY(context.getResources().getDimension(R.dimen.coui_seekbar_shadow_offset_y));
                    typedArrayObtainStyledAttributes.recycle();
                    SeekBarState seekBarState4 = this.mState;
                    seekBarState4.setCurProgressRadius(seekBarState4.getProgressRadius());
                    SeekBarState seekBarState5 = this.mState;
                    seekBarState5.setCurProgressHeight(seekBarState5.getProgressHeight());
                    SeekBarState seekBarState6 = this.mState;
                    seekBarState6.setCurThumbRadius(seekBarState6.getThumbRadius());
                    SeekBarState seekBarState7 = this.mState;
                    seekBarState7.setCurBackgroundRadius(seekBarState7.getBackgroundRadius());
                    SeekBarState seekBarState8 = this.mState;
                    seekBarState8.setCurBackgroundHeight(seekBarState8.getBackgroundHeight());
                    SeekBarState seekBarState9 = this.mState;
                    seekBarState9.setThumbOutRadius(seekBarState9.getProgressHeight() / 2.0f);
                    this.mState.setPaddingHorizontal((this.mState.getBackgroundHeight() * this.mState.getBackgroundEnlargeScale()) / 2.0f);
                    this.mState.setMax(getMax());
                    this.mState.setMin(getMin());
                    this.mState.setProgress(getProgress());
                    SeekBarState seekBarState10 = this.mState;
                    seekBarState10.setOldProgress(seekBarState10.getProgress());
                    Integer num = this.pendingInteractionMinAttr;
                    if (num != null) {
                        setInteractionMin(num.intValue());
                        this.pendingInteractionMinAttr = null;
                    }
                    updateColors();
                    IProgressRenderer iProgressRenderer = this.mProgressRenderer;
                    if (iProgressRenderer != null) {
                        iProgressRenderer.setProgressColor(this.mState.getProgressColor());
                    }
                    IThumbRenderer iThumbRenderer = this.mThumbRenderer;
                    if (iThumbRenderer != null) {
                        iThumbRenderer.setThumbColor(this.mState.getThumbColor());
                    }
                } catch (Exception e3) {
                } catch (Throwable th) {
                    th = th;
                    typedArray = typedArrayObtainStyledAttributes;
                    if (typedArray != null) {
                        typedArray.recycle();
                    }
                    throw th;
                }
            } catch (Exception e4) {
                throw e4;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public float interactionFloorScale() {
        int max = this.mState.getMax() - this.mState.getMin();
        if (max <= 0 || this.mState.getInteractionMin() <= this.mState.getMin()) {
            return 0.0f;
        }
        return (this.mState.getInteractionMin() - this.mState.getMin()) / max;
    }

    @Override // android.view.View
    public void invalidate() {
        if (this.mSuppressInvalidateForThrottle) {
            return;
        }
        super.invalidate();
    }

    @Override // android.widget.ProgressBar, android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        Intrinsics.checkNotNullParameter(drawable, "drawable");
        if (this.mSuppressInvalidateForThrottle) {
            return;
        }
        super.invalidateDrawable(drawable);
    }

    public boolean isInvalidateThrottleEnabled() {
        return false;
    }

    public boolean isLayoutRtl() {
        return getLayoutDirection() == 1;
    }

    public boolean isVertical() {
        return false;
    }

    public void notifyDeformedListener() {
        DeformationEffectRenderer deformationEffectRenderer = this.mDeformationRenderer;
        if (deformationEffectRenderer != null) {
            deformationEffectRenderer.notifyScaleChanged(this.mState.getProgress());
        }
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mState.setView$coui_support_seekbar_release(this);
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelHeightSpring();
        IAnimationHandler iAnimationHandler = this.mAnimationHandler;
        if (iAnimationHandler != null) {
            iAnimationHandler.stopAllAnimations();
        }
        this.mState.setView$coui_support_seekbar_release(null);
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public void onDraw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        updateState();
        drawBackground(canvas);
        drawActiveTrack(canvas);
        drawEffects(canvas);
    }

    public void onHeightSpringUpdate(float fraction) {
        SeekBarState seekBarState = this.mState;
        seekBarState.setCurBackgroundHeight(seekBarState.getBackgroundHeight() + ((this.mMaxBackgroundHeight - this.mState.getBackgroundHeight()) * fraction));
        SeekBarState seekBarState2 = this.mState;
        seekBarState2.setCurBackgroundRadius(seekBarState2.getCurBackgroundHeight() / 2.0f);
        OnBackgroundHeightChangedListener onBackgroundHeightChangedListener = this.mOnBackgroundHeightChangedListener;
        if (onBackgroundHeightChangedListener != null) {
            onBackgroundHeightChangedListener.onBackgroundHeightChanged(this, this.mState.getCurBackgroundHeight());
        }
        invalidate();
    }

    @Override // android.view.View
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            updateLayoutState();
        }
        refreshPixPerProgress();
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = View.MeasureSpec.getMode(heightMeasureSpec);
        int size = View.MeasureSpec.getSize(heightMeasureSpec);
        int size2 = View.MeasureSpec.getSize(widthMeasureSpec);
        int backgroundHeight = (int) ((this.mState.getBackgroundHeight() * this.mState.getBackgroundEnlargeScale()) + getPaddingTop() + getPaddingBottom());
        if (1073741824 != mode || size < backgroundHeight) {
            size = backgroundHeight;
        }
        setMeasuredDimension(size2, size);
    }

    public void onProgressAnimationEnd(int targetProgress, boolean fromUser) {
        this.mState.setProgress(targetProgress);
        SeekBarState seekBarState = this.mState;
        seekBarState.setOldProgress(seekBarState.getProgress());
        float min = this.mState.getMax() - this.mState.getMin() > 0 ? (targetProgress - this.mState.getMin()) / (this.mState.getMax() - this.mState.getMin()) : 0.0f;
        this.mState.setScale(min);
        this.mState.setDrawProgressScale(min);
        super.setProgress(targetProgress);
        OnSeekBarChangeListener onSeekBarChangeListener = this.mOnSeekBarChangeListener;
        if (onSeekBarChangeListener != null) {
            onSeekBarChangeListener.onProgressChanged(this, targetProgress, fromUser);
        }
        stopTrackingTouch(fromUser, false);
        invalidate();
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setInteractionMin(savedState.getMSaveInteractionMin());
        setProgress(savedState.getMSaveProgress());
    }

    @Override // android.widget.AbsSeekBar, android.view.View
    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        SeekBarState seekBarState = this.mState;
        if (seekBarState != null) {
            seekBarState.setRtl(getLayoutDirection() == 1);
        }
    }

    @Override // android.widget.ProgressBar, android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.setMSaveProgress(this.mState.getProgress());
        savedState.setMSaveInteractionMin(this.mState.getInteractionMin());
        return savedState;
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public void onSizeChanged(int w2, int h3, int oldw, int oldh) {
        super.onSizeChanged(w2, h3, oldw, oldh);
        updateLayoutState();
    }

    public void onStartTrackingTouch(boolean isFromUser) {
        startTrackingTouch(isFromUser, true);
    }

    public void onStopTrackingTouch(boolean isFromUser) {
        stopTrackingTouch(isFromUser, true);
    }

    @Override // android.widget.AbsSeekBar, android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (!isEnabled()) {
            if (event.getAction() != 1 && event.getAction() != 3) {
                return false;
            }
            handleMotionEventUp(event);
            return true;
        }
        int action = event.getAction();
        if (action == 0) {
            float touchPrimaryCoordinate = getTouchPrimaryCoordinate(event);
            this.mTouchDownX = touchPrimaryCoordinate;
            this.mLastX = touchPrimaryCoordinate;
            this.mInternalProgress = this.mState.getProgress();
            this.mState.setDragging(false);
            this.mStartDragging = false;
            this.mState.setBumpingEdges(false);
            handleMotionEventDown(event);
            if (shouldEnlargeOnDown()) {
                touchAnim();
            }
            return true;
        }
        if (action != 1) {
            if (action == 2) {
                if (this.mState.getIsStartFromMiddle() && shouldApplyMiddleDamping(event)) {
                    return true;
                }
                clearDeformationValue();
                if (this.mStartDragging) {
                    this.mState.setDragging(true);
                    handleMoveByMode(event);
                } else if (Math.abs(getTouchPrimaryCoordinate(event) - this.mTouchDownX) > this.mTouchSlop) {
                    startDrag();
                    if (!shouldEnlargeOnDown()) {
                        touchAnim();
                    }
                    this.mLastX = getTouchPrimaryCoordinate(event);
                    if (this.mState.getMoveType() != 2) {
                        handleMoveByMode(event);
                    }
                }
                resetBumpingEdges();
                invalidate();
                return true;
            }
            if (action != 3) {
                return true;
            }
        }
        if (shouldEnlargeOnDown() || this.mStartDragging) {
            releaseAnim();
        }
        if (this.mStartDragging && this.mState.getIsDragging()) {
            handleMoveByMode(event);
        }
        handleMotionEventUp(event);
        this.mState.setDragging(false);
        this.mStartDragging = false;
        invalidate();
        return true;
    }

    public void performFeedback() {
    }

    public float primaryToHorizontalAbsoluteScale(float primary, float length) {
        return this.mState.getIsRtl() ? (((getWidth() - primary) - getEnd()) - this.mState.getPaddingHorizontal()) / length : (primary - getProgressStart()) / length;
    }

    public void refresh() {
        if (this.mRefreshStyle == 0) {
            return;
        }
        TypedArray typedArrayObtainStyledAttributes = null;
        try {
            String resourceTypeName = getResources().getResourceTypeName(this.mRefreshStyle);
            if (TextUtils.equals(resourceTypeName, "attr")) {
                typedArrayObtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(null, R.styleable.COUISeekBar, this.mRefreshStyle, 0);
            } else if (TextUtils.equals(resourceTypeName, BaseConfig.KEY_STYLE)) {
                typedArrayObtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(null, R.styleable.COUISeekBar, 0, this.mRefreshStyle);
            }
            if (typedArrayObtainStyledAttributes != null) {
                ColorStateList colorStateList = typedArrayObtainStyledAttributes.getColorStateList(R.styleable.COUISeekBar_couiSeekBarProgressColor);
                if (colorStateList != null) {
                    setProgressColor(colorStateList);
                }
                ColorStateList colorStateList2 = typedArrayObtainStyledAttributes.getColorStateList(R.styleable.COUISeekBar_couiSeekBarBackgroundColor);
                if (colorStateList2 != null) {
                    setSeekBarBackgroundColor(colorStateList2);
                }
                ColorStateList colorStateList3 = typedArrayObtainStyledAttributes.getColorStateList(R.styleable.COUISeekBar_couiSeekBarThumbColor);
                if (colorStateList3 != null) {
                    setThumbColor(colorStateList3);
                }
                invalidate();
            }
        } finally {
            if (typedArrayObtainStyledAttributes != null) {
                typedArrayObtainStyledAttributes.recycle();
            }
        }
    }

    public void refreshPixPerProgress() {
        float seekBarLength = getSeekBarLength();
        if (seekBarLength <= 0.0f || this.mState.getMax() - this.mState.getMin() <= 0) {
            this.mPixPerProgress = 0.0f;
            return;
        }
        this.mPixPerProgress = seekBarLength / (this.mState.getMax() - this.mState.getMin());
        SeekBarState seekBarState = this.mState;
        float fProgressToScale = seekBarState.progressToScale(seekBarState.getProgress());
        if (Math.abs(this.mState.getScale() - fProgressToScale) > 0.001f) {
            this.mState.setScale(fProgressToScale);
            this.mState.setDrawProgressScale(fProgressToScale);
        }
    }

    public void releaseAnim() {
        COUISpringAnimation cOUISpringAnimation = this.mHeightSpringAnim;
        float minEnlargeHeight = getMinEnlargeHeight();
        float curBackgroundHeight = this.mState.getCurBackgroundHeight() - this.mState.getBackgroundHeight();
        if (cOUISpringAnimation == null || !cOUISpringAnimation.isRunning() || minEnlargeHeight <= 0.0f) {
            startReleaseSpring();
        } else if (curBackgroundHeight >= minEnlargeHeight) {
            startReleaseSpring();
        } else {
            this.mPendingRelease = true;
        }
    }

    public void removeEffect(IEffectRenderer effect) {
        EffectChainManager effectChainManager = this.mEffectManager;
        if (effectChainManager != null) {
            if (effectChainManager != null) {
                effectChainManager.removeEffect(effect);
            }
            invalidate();
        }
    }

    public void resetBumpingEdges() {
        if (!this.mState.getIsStartFromMiddle()) {
            if (this.mState.getScale() < 1.0f) {
                this.mState.setBumpingEdges(false);
            }
        } else {
            if (this.mState.getScale() >= 1.0f || this.mState.getScale() <= 0.0f) {
                return;
            }
            this.mState.setBumpingEdges(false);
        }
    }

    public void resetDeformationValue() {
    }

    public final void runWithInvalidateSuppressed(boolean suppressed, Function0<Unit> block) {
        Intrinsics.checkNotNullParameter(block, "block");
        if (!suppressed) {
            block.invoke();
            return;
        }
        this.mSuppressInvalidateForThrottle = true;
        try {
            block.invoke();
        } finally {
            this.mSuppressInvalidateForThrottle = false;
        }
    }

    @Deprecated(message = "此方法已废弃", replaceWith = @ReplaceWith(expression = "", imports = {}))
    public void setBackgroundEnlargeScale(float backgroundEnlargeScale) {
    }

    @Deprecated(message = "此方法已废弃", replaceWith = @ReplaceWith(expression = "", imports = {}))
    public void setBackgroundHeight(float backgroundHeight) {
    }

    @Deprecated(message = "此方法已废弃", replaceWith = @ReplaceWith(expression = "", imports = {}))
    public void setBackgroundRadius(float backgroundRadius) {
    }

    public void setBackgroundRoundCornerWeight(float roundCornerWeight) {
        this.mState.setBackgroundRoundCornerWeight(roundCornerWeight);
        invalidate();
    }

    @Deprecated(message = "此方法已废弃", replaceWith = @ReplaceWith(expression = "", imports = {}))
    public void setCustomProgressAnimDuration(float progressAnimDuration) {
    }

    @Deprecated(message = "此方法已废弃", replaceWith = @ReplaceWith(expression = "", imports = {}))
    public void setCustomProgressAnimInterpolator(Interpolator progressAnimInterpolator) {
    }

    public void setDeformedListener(OnDeformedListener listener) {
        this.mOnDeformedListener = listener;
        DeformationEffectRenderer deformationEffectRenderer = this.mDeformationRenderer;
        if (deformationEffectRenderer != null) {
            deformationEffectRenderer.setDeformedListener(listener);
        }
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        updateColors();
    }

    public void setIncrement(int increment) {
        this.mIncrement = Math.abs(increment);
    }

    public void setInteractionMin(int i3) {
        if (this.mState == null) {
            SeekBarState seekBarState = new SeekBarState();
            this.mState = seekBarState;
            seekBarState.setMin(progressBarMinCompat());
            this.mState.setMax(super.getMax());
            this.mState.setProgress(super.getProgress());
        }
        int iCoerceIn = RangesKt.coerceIn(i3, this.mState.getMin(), this.mState.getMax());
        if (iCoerceIn != this.mState.getInteractionMin()) {
            this.mState.setInteractionMin(iCoerceIn);
            if (this.mState.getProgress() < iCoerceIn) {
                setProgress(iCoerceIn);
            }
            invalidate();
        }
    }

    @Override // android.widget.ProgressBar
    @Deprecated(message = "此方法已废弃", replaceWith = @ReplaceWith(expression = "", imports = {}))
    public void setInterpolator(Interpolator interpolator) {
    }

    public final void setInvalidatePixelThreshold(float thresholdPx) {
        this.mInvalidatePixelThreshold = thresholdPx;
    }

    public final void setMAnimationHandler(IAnimationHandler iAnimationHandler) {
        this.mAnimationHandler = iAnimationHandler;
    }

    public final void setMBackgroundRect(RectF rectF) {
        Intrinsics.checkNotNullParameter(rectF, "<set-?>");
        this.mBackgroundRect = rectF;
    }

    public final void setMDeformationRenderer(DeformationEffectRenderer deformationEffectRenderer) {
        this.mDeformationRenderer = deformationEffectRenderer;
    }

    public final void setMHeightSpringAnim(COUISpringAnimation cOUISpringAnimation) {
        this.mHeightSpringAnim = cOUISpringAnimation;
    }

    public final void setMIncrement(int i3) {
        this.mIncrement = i3;
    }

    public final void setMInternalProgress(int i3) {
        this.mInternalProgress = i3;
    }

    public final void setMLastX(float f3) {
        this.mLastX = f3;
    }

    public final void setMMaxBackgroundHeight(float f3) {
        this.mMaxBackgroundHeight = f3;
    }

    public final void setMOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        this.mOnSeekBarChangeListener = onSeekBarChangeListener;
    }

    public final void setMOrientationHandler(IOrientationHandler iOrientationHandler) {
        this.mOrientationHandler = iOrientationHandler;
    }

    public final void setMPaint(Paint paint) {
        Intrinsics.checkNotNullParameter(paint, "<set-?>");
        this.mPaint = paint;
    }

    public final void setMPendingRelease(boolean z2) {
        this.mPendingRelease = z2;
    }

    public final void setMPixPerProgress(float f3) {
        this.mPixPerProgress = f3;
    }

    public final void setMProgressPath(Path path) {
        Intrinsics.checkNotNullParameter(path, "<set-?>");
        this.mProgressPath = path;
    }

    public final void setMProgressRenderer(IProgressRenderer iProgressRenderer) {
        this.mProgressRenderer = iProgressRenderer;
    }

    public final void setMProgressSmoothRoundCornerHelper(SmoothRoundCornerHelper smoothRoundCornerHelper) {
        this.mProgressSmoothRoundCornerHelper = smoothRoundCornerHelper;
    }

    public final void setMStartDragging(boolean z2) {
        this.mStartDragging = z2;
    }

    public final void setMState(SeekBarState seekBarState) {
        Intrinsics.checkNotNullParameter(seekBarState, "<set-?>");
        this.mState = seekBarState;
    }

    public final void setMThumbRenderer(IThumbRenderer iThumbRenderer) {
        this.mThumbRenderer = iThumbRenderer;
    }

    public final void setMTouchDownX(float f3) {
        this.mTouchDownX = f3;
    }

    public final void setMTouchSlop(int i3) {
        this.mTouchSlop = i3;
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, com.coui.appcompat.seekbar.core.ICOUISeekBar
    public void setMax(int max) {
        if (this.mState == null) {
            SeekBarState seekBarState = new SeekBarState();
            this.mState = seekBarState;
            seekBarState.setMin(progressBarMinCompat());
            this.mState.setProgress(super.getProgress());
        }
        if (max < this.mState.getMin()) {
            max = this.mState.getMin();
        }
        if (max != this.mState.getMax()) {
            this.mState.setMax(max);
            super.setMax(max);
            if (this.mState.getInteractionMin() > max) {
                this.mState.setInteractionMin(max);
            }
            if (this.mState.getProgress() > max) {
                setProgress(max);
            }
            invalidate();
            refreshPixPerProgress();
        }
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, com.coui.appcompat.seekbar.core.ICOUISeekBar
    public void setMin(int min) {
        if (this.mState == null) {
            SeekBarState seekBarState = new SeekBarState();
            this.mState = seekBarState;
            seekBarState.setMax(super.getMax());
            this.mState.setProgress(super.getProgress());
        }
        if (min < 0) {
            min = 0;
        }
        if (min > this.mState.getMax()) {
            min = this.mState.getMax();
        }
        if (min != this.mState.getMin()) {
            this.mState.setMin(min);
            super.setMin(min);
            if (this.mState.getInteractionMin() < min) {
                this.mState.setInteractionMin(min);
            }
            if (this.mState.getProgress() < min) {
                setProgress(min);
            }
            invalidate();
            refreshPixPerProgress();
        }
    }

    public void setMoveDamping(float f3) {
        this.mMoveDamping = f3;
    }

    public void setMoveType(int i3) {
        this.mState.setMoveType(i3);
    }

    public void setOnBackgroundHeightChangedListener(OnBackgroundHeightChangedListener listener) {
        this.mOnBackgroundHeightChangedListener = listener;
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) {
        this.mOnSeekBarChangeListener = listener;
    }

    @Deprecated(message = "此方法已废弃", replaceWith = @ReplaceWith(expression = "", imports = {}))
    public void setPaddingHorizontal(float paddingHorizontal) {
    }

    @Override // android.widget.ProgressBar, com.coui.appcompat.seekbar.core.ICOUISeekBar
    public void setProgress(int progress) {
        setProgress(progress, false);
    }

    public void setProgressColor(ColorStateList progressColor) {
        if (progressColor != null) {
            this.mState.setProgressColorStateList$coui_support_seekbar_release(progressColor);
            SeekBarState seekBarState = this.mState;
            seekBarState.setProgressColor(getColor(this, seekBarState.getProgressColorStateList(), COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_progress_selector)));
            invalidate();
        }
    }

    @Deprecated(message = "此方法已废弃", replaceWith = @ReplaceWith(expression = "", imports = {}))
    public void setProgressContentDescription(String progress) {
    }

    @Deprecated(message = "此方法已废弃", replaceWith = @ReplaceWith(expression = "", imports = {}))
    public void setProgressEnlargeScale(float progressEnlargeScale) {
    }

    @Deprecated(message = "此方法已废弃", replaceWith = @ReplaceWith(expression = "", imports = {}))
    public void setProgressFull() {
    }

    @Deprecated(message = "此方法已废弃", replaceWith = @ReplaceWith(expression = "", imports = {}))
    public void setProgressHeight(float progressHeight) {
    }

    @Deprecated(message = "此方法已废弃", replaceWith = @ReplaceWith(expression = "", imports = {}))
    public void setProgressRadius(float progressRadius) {
    }

    public void setProgressRoundCornerWeight(float progressRoundCornerWeight) {
        this.mState.setProgressRoundCornerWeight(progressRoundCornerWeight);
        invalidate();
    }

    public void setRefreshStyle(int refreshStyle) {
        this.mRefreshStyle = refreshStyle;
    }

    public void setSeekBarBackgroundColor(ColorStateList backgroundColor) {
        if (backgroundColor != null) {
            this.mState.setBackgroundColorStateList$coui_support_seekbar_release(backgroundColor);
            SeekBarState seekBarState = this.mState;
            seekBarState.setBackgroundColor(getColor(this, seekBarState.getBackgroundColorStateList(), COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_background_selector)));
            invalidate();
        }
    }

    @Deprecated(message = "此方法已废弃", replaceWith = @ReplaceWith(expression = "", imports = {}))
    public void setText(String text) {
    }

    public void setThumbColor(ColorStateList thumbColor) {
        if (thumbColor != null) {
            this.mState.setThumbColorStateList$coui_support_seekbar_release(thumbColor);
            SeekBarState seekBarState = this.mState;
            seekBarState.setThumbColor(getColor(this, seekBarState.getThumbColorStateList(), COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_thumb_selector)));
            invalidate();
        }
    }

    public void setTouchScale(float scale, boolean updateDrawProgressScale) {
        float fInteractionFloorScale = interactionFloorScale();
        if (!this.mState.getIsSupportDeformation()) {
            this.mState.setScale(RangesKt.coerceIn(scale, fInteractionFloorScale, 1.0f));
            if (updateDrawProgressScale) {
                SeekBarState seekBarState = this.mState;
                seekBarState.setDrawProgressScale(seekBarState.getScale());
                return;
            }
            return;
        }
        if (fInteractionFloorScale <= 0.0f) {
            fInteractionFloorScale = -1.0f;
        }
        this.mState.setScale(RangesKt.coerceIn(scale, fInteractionFloorScale, 2.0f));
        if (updateDrawProgressScale) {
            SeekBarState seekBarState2 = this.mState;
            seekBarState2.setDrawProgressScale(seekBarState2.getScale());
        }
        calculateTouchDeformationValue();
        notifyDeformedListener();
    }

    public boolean shouldEnlargeOnDown() {
        return true;
    }

    public final boolean shouldThrottleInvalidate(int newProgress, boolean fromUser, int lastDrawnProgress) {
        if (!isInvalidateThrottleEnabled() || fromUser) {
            return false;
        }
        float f3 = this.mInvalidatePixelThreshold;
        if (f3 <= 0.0f) {
            return false;
        }
        int min = this.mState.getMin();
        int max = this.mState.getMax();
        if (newProgress > min && newProgress < max) {
            float seekBarLength = getSeekBarLength();
            float f4 = max - min;
            if (seekBarLength <= 0.0f || f4 <= 0.0f || lastDrawnProgress == Integer.MIN_VALUE) {
                return false;
            }
            if (Math.abs(newProgress - lastDrawnProgress) / (f4 / seekBarLength) < f3) {
                return true;
            }
        }
        return false;
    }

    public boolean shouldUpdateDrawProgressOnSetProgress() {
        return true;
    }

    @Deprecated(message = "此方法已废弃", replaceWith = @ReplaceWith(expression = "", imports = {}))
    public void showText(boolean isShow) {
    }

    public float snapHeightToPixelGrid(float height) {
        return Math.round(height);
    }

    public void startDrag() {
        setPressed(true);
        onStartTrackingTouch(true);
        attemptClaimDrag();
    }

    public void startProgressTransitionAnim(int targetProgress, boolean fromUser) {
        int progress = this.mState.getProgress();
        IAnimationHandler iAnimationHandler = this.mAnimationHandler;
        if (iAnimationHandler != null) {
            if (iAnimationHandler != null) {
                iAnimationHandler.startProgressAnimation(progress, targetProgress, fromUser, this);
                return;
            }
            return;
        }
        this.mState.setProgress(targetProgress);
        SeekBarState seekBarState = this.mState;
        seekBarState.setOldProgress(seekBarState.getProgress());
        super.setProgress(targetProgress);
        float min = this.mState.getMax() - this.mState.getMin() > 0 ? (targetProgress - this.mState.getMin()) / (this.mState.getMax() - this.mState.getMin()) : 0.0f;
        this.mState.setScale(min);
        this.mState.setDrawProgressScale(min);
        OnSeekBarChangeListener onSeekBarChangeListener = this.mOnSeekBarChangeListener;
        if (onSeekBarChangeListener != null) {
            onSeekBarChangeListener.onProgressChanged(this, targetProgress, fromUser);
        }
        invalidate();
    }

    public void startTrackingTouch(boolean isFromUser, boolean isTouching) {
        OnSeekBarChangeListener onSeekBarChangeListener;
        if (isTouching) {
            this.mState.setDragging(true);
            this.mStartDragging = true;
        }
        if (!isFromUser || (onSeekBarChangeListener = this.mOnSeekBarChangeListener) == null) {
            return;
        }
        onSeekBarChangeListener.onStartTrackingTouch(this);
    }

    public void stopTrackingTouch(boolean isFromUser, boolean isTouching) {
        OnSeekBarChangeListener onSeekBarChangeListener;
        if (isTouching) {
            this.mState.setDragging(false);
            this.mStartDragging = false;
        }
        if (!isFromUser || (onSeekBarChangeListener = this.mOnSeekBarChangeListener) == null) {
            return;
        }
        onSeekBarChangeListener.onStopTrackingTouch(this);
    }

    public void superSetProgress(int progress) {
        superSetProgress(progress, true);
    }

    public void syncProgressToParent(int progress) {
        super.setProgress(progress);
    }

    public void touchAnim() {
        COUISpringForce spring;
        this.mPendingRelease = false;
        removeCallbacks(this.mStartReleaseRunnable);
        initEnlargeAnim();
        COUISpringAnimation cOUISpringAnimation = this.mHeightSpringAnim;
        if (cOUISpringAnimation != null && (spring = cOUISpringAnimation.getSpring()) != null) {
            spring.setBounce(0.0f);
        }
        float enlargeFraction = getEnlargeFraction();
        COUISpringAnimation cOUISpringAnimation2 = this.mHeightSpringAnim;
        if (cOUISpringAnimation2 != null) {
            cOUISpringAnimation2.setStartValue(enlargeFraction * 100.0f);
        }
        COUISpringAnimation cOUISpringAnimation3 = this.mHeightSpringAnim;
        if (cOUISpringAnimation3 != null) {
            cOUISpringAnimation3.animateToFinalPosition(100.0f);
        }
        IAnimationHandler iAnimationHandler = this.mAnimationHandler;
        if (iAnimationHandler != null) {
            iAnimationHandler.startThumbScaleAnimation(true);
        }
    }

    /* JADX WARN: Code duplicated, block: B:18:0x009e  */
    /* JADX WARN: Code duplicated, block: B:20:0x00a8  */
    /* JADX WARN: Code duplicated, block: B:22:0x00ac  */
    /* JADX WARN: Code duplicated, block: B:25:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:26:? A[RETURN, SYNTHETIC] */
    public void trackTouchEvent(MotionEvent event) {
        float fPrimaryToHorizontalAbsoluteScale;
        float fCalculateDamping;
        float fProgressToScale;
        int i3;
        int progress;
        OnSeekBarChangeListener onSeekBarChangeListener;
        Intrinsics.checkNotNullParameter(event, "event");
        float touchPrimaryCoordinate = getTouchPrimaryCoordinate(event);
        float seekBarLength = getSeekBarLength();
        if (seekBarLength <= 0.0f) {
            return;
        }
        int max = this.mState.getMax() - this.mState.getMin();
        if (!isVertical()) {
            if (this.mState.getMoveType() == 2) {
                float f3 = touchPrimaryCoordinate - this.mLastX;
                if (this.mState.getIsRtl()) {
                    f3 = -f3;
                }
                fCalculateDamping = f3 * calculateDamping();
                fProgressToScale = this.mState.progressToScale(this.mInternalProgress);
            } else {
                fPrimaryToHorizontalAbsoluteScale = primaryToHorizontalAbsoluteScale(touchPrimaryCoordinate, seekBarLength);
            }
            setTouchScale(fPrimaryToHorizontalAbsoluteScale, false);
            int progressLimit = getProgressLimit(MathKt.roundToInt((this.mState.getScale() * max) + this.mState.getMin()));
            i3 = this.mInternalProgress;
            progress = this.mState.getProgress();
            this.mInternalProgress = progressLimit;
            this.mState.setProgress(getRealProgress(progressLimit));
            super.setProgress(this.mState.getProgress());
            if (i3 != this.mInternalProgress) {
                this.mLastX = touchPrimaryCoordinate;
                if (progress != this.mState.getProgress()) {
                    onSeekBarChangeListener = this.mOnSeekBarChangeListener;
                    if (onSeekBarChangeListener != null) {
                        onSeekBarChangeListener.onProgressChanged(this, this.mState.getProgress(), true);
                    }
                    performFeedback();
                }
            }
        }
        fCalculateDamping = (this.mLastX - touchPrimaryCoordinate) * calculateDamping();
        fProgressToScale = this.mState.progressToScale(this.mInternalProgress);
        fPrimaryToHorizontalAbsoluteScale = fProgressToScale + (fCalculateDamping / seekBarLength);
        setTouchScale(fPrimaryToHorizontalAbsoluteScale, false);
        int progressLimit2 = getProgressLimit(MathKt.roundToInt((this.mState.getScale() * max) + this.mState.getMin()));
        i3 = this.mInternalProgress;
        progress = this.mState.getProgress();
        this.mInternalProgress = progressLimit2;
        this.mState.setProgress(getRealProgress(progressLimit2));
        super.setProgress(this.mState.getProgress());
        if (i3 != this.mInternalProgress) {
            this.mLastX = touchPrimaryCoordinate;
            if (progress != this.mState.getProgress()) {
                onSeekBarChangeListener = this.mOnSeekBarChangeListener;
                if (onSeekBarChangeListener != null) {
                    onSeekBarChangeListener.onProgressChanged(this, this.mState.getProgress(), true);
                }
                performFeedback();
            }
        }
    }

    public void trackTouchEventByFinger(MotionEvent event) {
        float fPrimaryToHorizontalAbsoluteScale;
        Intrinsics.checkNotNullParameter(event, "event");
        int iRoundToInt = MathKt.roundToInt(((getTouchPrimaryCoordinate(event) - this.mLastX) * calculateDamping()) + this.mLastX);
        float seekBarLength = getSeekBarLength();
        if (seekBarLength <= 0.0f) {
            return;
        }
        if (isVertical()) {
            int height = (getHeight() - getPaddingTop()) - getPaddingBottom();
            if (height <= 0) {
                return;
            } else {
                fPrimaryToHorizontalAbsoluteScale = ((getHeight() - getPaddingBottom()) - iRoundToInt) / height;
            }
        } else {
            fPrimaryToHorizontalAbsoluteScale = primaryToHorizontalAbsoluteScale(iRoundToInt, seekBarLength);
        }
        setTouchScale(fPrimaryToHorizontalAbsoluteScale, false);
        int progressLimit = getProgressLimit(MathKt.roundToInt((this.mState.getScale() * (this.mState.getMax() - this.mState.getMin())) + this.mState.getMin()));
        int i3 = this.mInternalProgress;
        int progress = this.mState.getProgress();
        this.mInternalProgress = progressLimit;
        this.mState.setProgress(getRealProgress(progressLimit));
        super.setProgress(this.mState.getProgress());
        if (i3 != this.mInternalProgress) {
            this.mLastX = iRoundToInt;
            if (progress != this.mState.getProgress()) {
                OnSeekBarChangeListener onSeekBarChangeListener = this.mOnSeekBarChangeListener;
                if (onSeekBarChangeListener != null) {
                    onSeekBarChangeListener.onProgressChanged(this, this.mState.getProgress(), true);
                }
                performFeedback();
            }
        }
    }

    public void updateColors() {
        if (this.mState == null) {
            return;
        }
        int color = COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_progress_selector);
        SeekBarState seekBarState = this.mState;
        seekBarState.setBackgroundColor(getColor(this, seekBarState.getBackgroundColorStateList(), COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_background_selector)));
        SeekBarState seekBarState2 = this.mState;
        seekBarState2.setProgressColor(getColor(this, seekBarState2.getProgressColorStateList(), color));
        SeekBarState seekBarState3 = this.mState;
        seekBarState3.setThumbColor(getColor(this, seekBarState3.getThumbColorStateList(), COUIContextUtil.getColor(getContext(), R.color.coui_seekbar_thumb_selector)));
        IProgressRenderer iProgressRenderer = this.mProgressRenderer;
        if (iProgressRenderer != null) {
            iProgressRenderer.setProgressColor(this.mState.getProgressColor());
        }
        IThumbRenderer iThumbRenderer = this.mThumbRenderer;
        if (iThumbRenderer != null) {
            iThumbRenderer.setThumbColor(this.mState.getThumbColor());
        }
    }

    public void updateLayoutState() {
        if (this.mOrientationHandler == null) {
            return;
        }
        this.mState.setRtl(getLayoutDirection() == 1);
        this.mState.setVertical(isVertical());
        this.mState.setCenterX(getSeekBarCenterX());
        this.mState.setCenterY(getSeekBarCenterY());
        SeekBarState seekBarState = this.mState;
        IOrientationHandler iOrientationHandler = this.mOrientationHandler;
        seekBarState.setProgressStart(iOrientationHandler != null ? iOrientationHandler.getProgressStart(this) : 0.0f);
    }

    public void updateState() {
        IOrientationHandler iOrientationHandler = this.mOrientationHandler;
        if (iOrientationHandler == null || iOrientationHandler == null) {
            return;
        }
        float seekBarLength = iOrientationHandler.getSeekBarLength(this);
        SeekBarState seekBarState = this.mState;
        IOrientationHandler iOrientationHandler2 = this.mOrientationHandler;
        seekBarState.setThumbPosition(iOrientationHandler2 != null ? iOrientationHandler2.calculateThumbPosition(seekBarState.getDrawProgressScale(), seekBarLength, this.mState.getIsRtl()) : 0.0f);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUISeekBarCore(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    @Override // android.widget.ProgressBar
    public void setProgress(int progress, boolean animate) {
        setProgress(progress, animate, false);
    }

    public void superSetProgress(int progress, boolean shouldInvalidate) {
        if (this.mState == null) {
            SeekBarState seekBarState = new SeekBarState();
            this.mState = seekBarState;
            seekBarState.setMin(progressBarMinCompat());
            this.mState.setMax(super.getMax());
        }
        int iCoerceIn = RangesKt.coerceIn(progress, this.mState.getMin(), this.mState.getMax());
        this.mState.setProgress(iCoerceIn);
        SeekBarState seekBarState2 = this.mState;
        seekBarState2.setOldProgress(seekBarState2.getProgress());
        float fProgressToScale = this.mState.progressToScale(iCoerceIn);
        this.mState.setScale(fProgressToScale);
        this.mState.setDrawProgressScale(fProgressToScale);
        super.setProgress(iCoerceIn);
        if (shouldInvalidate) {
            invalidate();
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUISeekBarCore(Context context, AttributeSet attributeSet, int i3) {
        this(context, attributeSet, i3, 0, 8, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public void ensureLabelsAdded() {
        TextDrawable textDrawable = this.mTextDrawable;
        if (textDrawable == null) {
            return;
        }
        setValueForLabel(textDrawable, String.valueOf(this.mState.getProgress()));
    }

    public void setProgress(int progress, boolean animate, boolean fromUser) {
        if (this.mState == null) {
            SeekBarState seekBarState = new SeekBarState();
            this.mState = seekBarState;
            seekBarState.setMin(progressBarMinCompat());
            this.mState.setMax(super.getMax());
        }
        cancelFlexibleFollowHandAnim();
        int progress2 = this.mState.getProgress();
        int iCoerceIn = RangesKt.coerceIn(progress, effectiveLowerBound(), this.mState.getMax());
        if (progress2 == iCoerceIn) {
            return;
        }
        if (animate) {
            startProgressTransitionAnim(iCoerceIn, fromUser);
        } else {
            this.mState.setProgress(iCoerceIn);
            this.mInternalProgress = iCoerceIn;
            float min = this.mState.getMax() - this.mState.getMin() > 0 ? (iCoerceIn - this.mState.getMin()) / (this.mState.getMax() - this.mState.getMin()) : 0.0f;
            this.mState.setScale(min);
            if (shouldUpdateDrawProgressOnSetProgress()) {
                SeekBarState seekBarState2 = this.mState;
                seekBarState2.setOldProgress(seekBarState2.getProgress());
                this.mState.setDrawProgressScale(min);
            }
            boolean zShouldThrottleInvalidate = shouldThrottleInvalidate(iCoerceIn, fromUser, this.mLastDrawnProgressForThrottle);
            runWithInvalidateSuppressed(zShouldThrottleInvalidate, new C13521(iCoerceIn));
            OnSeekBarChangeListener onSeekBarChangeListener = this.mOnSeekBarChangeListener;
            if (onSeekBarChangeListener != null) {
                onSeekBarChangeListener.onProgressChanged(this, iCoerceIn, fromUser);
            }
            if (!zShouldThrottleInvalidate) {
                if (isInvalidateThrottleEnabled()) {
                    this.mLastDrawnProgressForThrottle = iCoerceIn;
                }
                invalidate();
            }
        }
        resetDeformationValue();
    }

    public /* synthetic */ COUISeekBarCore(Context context, AttributeSet attributeSet, int i3, int i4, int i5, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i5 & 2) != 0 ? null : attributeSet, (i5 & 4) != 0 ? R.attr.couiSeekBarStyle : i3, (i5 & 8) != 0 ? R.style.COUISeekBar : i4);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUISeekBarCore(Context context, AttributeSet attributeSet, int i3, int i4) throws Throwable {
        super(context, attributeSet, i3, i4);
        Intrinsics.checkNotNullParameter(context, "context");
        this.mState = new SeekBarState();
        this.mPaint = new Paint(1);
        this.mBackgroundRect = new RectF();
        this.mTempRect = new RectF();
        this.mBackgroundPath = new Path();
        this.mProgressPath = new Path();
        this.mIncrement = 1;
        if (this.mState == null) {
            this.mState = new SeekBarState();
        }
        this.mState.setMin(progressBarMinCompat());
        this.mState.setMax(super.getMax());
        this.mState.setProgress(super.getProgress());
        this.mPaint = new Paint(1);
        this.mBackgroundSmoothRoundCornerHelper = new SmoothRoundCornerHelper(this, this.mBackgroundPath);
        this.mProgressSmoothRoundCornerHelper = new SmoothRoundCornerHelper(this, this.mProgressPath);
        this.mOrientationHandler = createOrientationHandler();
        this.mProgressRenderer = createProgressRenderer();
        this.mThumbRenderer = createThumbRenderer();
        this.mAnimationHandler = createAnimationHandler();
        this.mEffectManager = new EffectChainManager();
        initState(context, attributeSet, i3, i4);
        setClickable(true);
        setFocusable(true);
        this.mState.setView$coui_support_seekbar_release(this);
        this.mState.setOrientationHandler$coui_support_seekbar_release(this.mOrientationHandler);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mMaxBackgroundHeight = this.mState.getBackgroundHeight() * this.mState.getBackgroundEnlargeScale();
        initEnlargeAnim();
        this.mTextDrawable = new TextDrawable(context);
        initAccessibility(context);
        COUIDarkModeUtil.setForceDarkAllow(this, false);
        this.mStartReleaseRunnable = new f(this);
        this.mLastDrawnProgressForThrottle = Integer.MIN_VALUE;
    }
}