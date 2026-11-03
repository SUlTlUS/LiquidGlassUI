package com.coui.appcompat.seekbar.core;

import android.content.res.ColorStateList;
import androidx.slice.core.SliceHints;
import com.coui.appcompat.seekbar.handler.IOrientationHandler;
import kotlin.Metadata;

/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0002\b,\n\u0002\u0010\u000b\n\u0002\b8\n\u0002\u0018\u0002\n\u0002\bZ\n\u0002\u0010\u000e\n\u0002\b%\n\u0002\u0018\u0002\n\u0002\b\f\u0018\u0000 \u0083\u00022\u00020\u0001:\u0002\u0083\u0002B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0081\u0002\u001a\u00020\u00112\u0007\u0010\u0086\u0001\u001a\u00020\u0004J\u0007\u0010\u0082\u0002\u001a\u00020\u0000R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR(\u0010\u000b\u001a\u0004\u0018\u00010\n2\b\u0010\t\u001a\u0004\u0018\u00010\n@@X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0010\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u001a\u0010\u0016\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0013\"\u0004\b\u0018\u0010\u0015R\u001a\u0010\u0019\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0013\"\u0004\b\u001b\u0010\u0015R\u001a\u0010\u001c\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0013\"\u0004\b\u001e\u0010\u0015R\u001a\u0010\u001f\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0013\"\u0004\b!\u0010\u0015R\u001a\u0010\"\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u0006\"\u0004\b$\u0010\bR\u001a\u0010%\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\u0006\"\u0004\b'\u0010\bR\u001a\u0010(\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u0013\"\u0004\b*\u0010\u0015R\u001a\u0010+\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b,\u0010\u0013\"\u0004\b-\u0010\u0015R\u001a\u0010.\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b/\u0010\u0013\"\u0004\b0\u0010\u0015R\u001a\u00101\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b2\u0010\u0013\"\u0004\b3\u0010\u0015R\u001a\u00104\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b5\u0010\u0013\"\u0004\b6\u0010\u0015R\u001a\u00107\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b8\u0010\u0013\"\u0004\b9\u0010\u0015R\u001a\u0010:\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b;\u0010\u0013\"\u0004\b<\u0010\u0015R\u001a\u0010=\u001a\u00020>X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b?\u0010@\"\u0004\bA\u0010BR\u001a\u0010C\u001a\u00020>X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bD\u0010@\"\u0004\bE\u0010BR\u001a\u0010F\u001a\u00020>X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bG\u0010@\"\u0004\bH\u0010BR\u001a\u0010I\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bJ\u0010\u0013\"\u0004\bK\u0010\u0015R\u001a\u0010L\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bM\u0010\u0013\"\u0004\bN\u0010\u0015R\u001a\u0010O\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bP\u0010\u0013\"\u0004\bQ\u0010\u0015R\u001a\u0010R\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bS\u0010\u0013\"\u0004\bT\u0010\u0015R\u001a\u0010U\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bV\u0010\u0006\"\u0004\bW\u0010\bR\u001a\u0010X\u001a\u00020>X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bX\u0010@\"\u0004\bY\u0010BR\u001a\u0010Z\u001a\u00020>X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bZ\u0010@\"\u0004\b[\u0010BR\u001a\u0010\\\u001a\u00020>X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\\\u0010@\"\u0004\b]\u0010BR\u001a\u0010^\u001a\u00020>X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b^\u0010@\"\u0004\b_\u0010BR\u001a\u0010`\u001a\u00020>X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b`\u0010@\"\u0004\ba\u0010BR\u001a\u0010b\u001a\u00020>X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bb\u0010@\"\u0004\bc\u0010BR\u001a\u0010d\u001a\u00020>X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bd\u0010@\"\u0004\be\u0010BR\u001a\u0010f\u001a\u00020>X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bf\u0010@\"\u0004\bg\u0010BR\u001a\u0010h\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bi\u0010\u0006\"\u0004\bj\u0010\bR\u001a\u0010k\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bl\u0010\u0006\"\u0004\bm\u0010\bR\u001a\u0010n\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bo\u0010\u0006\"\u0004\bp\u0010\bR\u001a\u0010q\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\br\u0010\u0006\"\u0004\bs\u0010\bR\u001a\u0010t\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bu\u0010\u0006\"\u0004\bv\u0010\bR(\u0010x\u001a\u0004\u0018\u00010w2\b\u0010\t\u001a\u0004\u0018\u00010w@@X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\by\u0010z\"\u0004\b{\u0010|R\u001a\u0010}\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b~\u0010\u0013\"\u0004\b\u007f\u0010\u0015R\u001d\u0010\u0080\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b\u0081\u0001\u0010\u0013\"\u0005\b\u0082\u0001\u0010\u0015R\u001d\u0010\u0083\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b\u0084\u0001\u0010\u0013\"\u0005\b\u0085\u0001\u0010\u0015R\u001d\u0010\u0086\u0001\u001a\u00020\u0004X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b\u0087\u0001\u0010\u0006\"\u0005\b\u0088\u0001\u0010\bR\u001d\u0010\u0089\u0001\u001a\u00020\u0004X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b\u008a\u0001\u0010\u0006\"\u0005\b\u008b\u0001\u0010\bR+\u0010\u008c\u0001\u001a\u0004\u0018\u00010\n2\b\u0010\t\u001a\u0004\u0018\u00010\n@@X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b\u008d\u0001\u0010\r\"\u0005\b\u008e\u0001\u0010\u000fR\u001d\u0010\u008f\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b\u0090\u0001\u0010\u0013\"\u0005\b\u0091\u0001\u0010\u0015R\u001d\u0010\u0092\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b\u0093\u0001\u0010\u0013\"\u0005\b\u0094\u0001\u0010\u0015R\u001d\u0010\u0095\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b\u0096\u0001\u0010\u0013\"\u0005\b\u0097\u0001\u0010\u0015R\u001d\u0010\u0098\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b\u0099\u0001\u0010\u0013\"\u0005\b\u009a\u0001\u0010\u0015R\u001d\u0010\u009b\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b\u009c\u0001\u0010\u0013\"\u0005\b\u009d\u0001\u0010\u0015R\u001d\u0010\u009e\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b\u009f\u0001\u0010\u0013\"\u0005\b \u0001\u0010\u0015R\u001d\u0010¡\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b¢\u0001\u0010\u0013\"\u0005\b£\u0001\u0010\u0015R\u001d\u0010¤\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b¥\u0001\u0010\u0013\"\u0005\b¦\u0001\u0010\u0015R\u001d\u0010§\u0001\u001a\u00020\u0004X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b¨\u0001\u0010\u0006\"\u0005\b©\u0001\u0010\bR\u001d\u0010ª\u0001\u001a\u00020\u0004X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b«\u0001\u0010\u0006\"\u0005\b¬\u0001\u0010\bR\u001d\u0010\u00ad\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b®\u0001\u0010\u0013\"\u0005\b¯\u0001\u0010\u0015R\u001d\u0010°\u0001\u001a\u00020\u0004X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b±\u0001\u0010\u0006\"\u0005\b²\u0001\u0010\bR\u001d\u0010³\u0001\u001a\u00020\u0004X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b´\u0001\u0010\u0006\"\u0005\bµ\u0001\u0010\bR\u001d\u0010¶\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b·\u0001\u0010\u0013\"\u0005\b¸\u0001\u0010\u0015R\u001d\u0010¹\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\bº\u0001\u0010\u0013\"\u0005\b»\u0001\u0010\u0015R\u001d\u0010¼\u0001\u001a\u00020\u0004X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b½\u0001\u0010\u0006\"\u0005\b¾\u0001\u0010\bR\u001d\u0010¿\u0001\u001a\u00020>X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\bÀ\u0001\u0010@\"\u0005\bÁ\u0001\u0010BR\u001d\u0010Â\u0001\u001a\u00020>X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\bÃ\u0001\u0010@\"\u0005\bÄ\u0001\u0010BR\u001d\u0010Å\u0001\u001a\u00020>X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\bÆ\u0001\u0010@\"\u0005\bÇ\u0001\u0010BR\u001d\u0010È\u0001\u001a\u00020>X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\bÉ\u0001\u0010@\"\u0005\bÊ\u0001\u0010BR\u001d\u0010Ë\u0001\u001a\u00020>X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\bÌ\u0001\u0010@\"\u0005\bÍ\u0001\u0010BR\u001d\u0010Î\u0001\u001a\u00020\u0004X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\bÏ\u0001\u0010\u0006\"\u0005\bÐ\u0001\u0010\bR\"\u0010Ñ\u0001\u001a\u0005\u0018\u00010Ò\u0001X\u0086\u000e¢\u0006\u0012\n\u0000\u001a\u0006\bÓ\u0001\u0010Ô\u0001\"\u0006\bÕ\u0001\u0010Ö\u0001R\u001d\u0010×\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\bØ\u0001\u0010\u0013\"\u0005\bÙ\u0001\u0010\u0015R\u001d\u0010Ú\u0001\u001a\u00020\u0004X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\bÛ\u0001\u0010\u0006\"\u0005\bÜ\u0001\u0010\bR+\u0010Ý\u0001\u001a\u0004\u0018\u00010\n2\b\u0010\t\u001a\u0004\u0018\u00010\n@@X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\bÞ\u0001\u0010\r\"\u0005\bß\u0001\u0010\u000fR\u001d\u0010à\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\bá\u0001\u0010\u0013\"\u0005\bâ\u0001\u0010\u0015R\u001d\u0010ã\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\bä\u0001\u0010\u0013\"\u0005\bå\u0001\u0010\u0015R\u001d\u0010æ\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\bç\u0001\u0010\u0013\"\u0005\bè\u0001\u0010\u0015R\u001d\u0010é\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\bê\u0001\u0010\u0013\"\u0005\bë\u0001\u0010\u0015R\u001d\u0010ì\u0001\u001a\u00020\u0004X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\bí\u0001\u0010\u0006\"\u0005\bî\u0001\u0010\bR\u001d\u0010ï\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\bð\u0001\u0010\u0013\"\u0005\bñ\u0001\u0010\u0015R\u001d\u0010ò\u0001\u001a\u00020\u0004X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\bó\u0001\u0010\u0006\"\u0005\bô\u0001\u0010\bR\u001d\u0010õ\u0001\u001a\u00020>X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\bö\u0001\u0010@\"\u0005\b÷\u0001\u0010BR/\u0010ù\u0001\u001a\u0005\u0018\u00010ø\u00012\t\u0010\t\u001a\u0005\u0018\u00010ø\u0001@@X\u0086\u000e¢\u0006\u0012\n\u0000\u001a\u0006\bú\u0001\u0010û\u0001\"\u0006\bü\u0001\u0010ý\u0001R\u001d\u0010þ\u0001\u001a\u00020\u0011X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\bÿ\u0001\u0010\u0013\"\u0005\b\u0080\u0002\u0010\u0015¨\u0006\u0084\u0002"}, d2 = {"Lcom/coui/appcompat/seekbar/core/SeekBarState;", "", "()V", "backgroundColor", "", "getBackgroundColor", "()I", "setBackgroundColor", "(I)V", "<set-?>", "Landroid/content/res/ColorStateList;", "backgroundColorStateList", "getBackgroundColorStateList", "()Landroid/content/res/ColorStateList;", "setBackgroundColorStateList$coui_support_seekbar_release", "(Landroid/content/res/ColorStateList;)V", "backgroundEnlargeScale", "", "getBackgroundEnlargeScale", "()F", "setBackgroundEnlargeScale", "(F)V", "backgroundHeight", "getBackgroundHeight", "setBackgroundHeight", "backgroundRadius", "getBackgroundRadius", "setBackgroundRadius", "backgroundRadiusEnlargeScale", "getBackgroundRadiusEnlargeScale", "setBackgroundRadiusEnlargeScale", "backgroundRoundCornerWeight", "getBackgroundRoundCornerWeight", "setBackgroundRoundCornerWeight", "centerX", "getCenterX", "setCenterX", "centerY", "getCenterY", "setCenterY", "curBackgroundHeight", "getCurBackgroundHeight", "setCurBackgroundHeight", "curBackgroundRadius", "getCurBackgroundRadius", "setCurBackgroundRadius", "curPaddingVertical", "getCurPaddingVertical", "setCurPaddingVertical", "curProgressHeight", "getCurProgressHeight", "setCurProgressHeight", "curProgressRadius", "getCurProgressRadius", "setCurProgressRadius", "curThumbRadius", "getCurThumbRadius", "setCurThumbRadius", "drawProgressScale", "getDrawProgressScale", "setDrawProgressScale", "enableAdaptiveVibrator", "", "getEnableAdaptiveVibrator", "()Z", "setEnableAdaptiveVibrator", "(Z)V", "enableVibrator", "getEnableVibrator", "setEnableVibrator", "extendProgressRectForRoundCorner", "getExtendProgressRectForRoundCorner", "setExtendProgressRectForRoundCorner", "heightBottomDeformedDownValue", "getHeightBottomDeformedDownValue", "setHeightBottomDeformedDownValue", "heightBottomDeformedUpValue", "getHeightBottomDeformedUpValue", "setHeightBottomDeformedUpValue", "heightTopDeformedDownValue", "getHeightTopDeformedDownValue", "setHeightTopDeformedDownValue", "heightTopDeformedUpValue", "getHeightTopDeformedUpValue", "setHeightTopDeformedUpValue", "interactionMin", "getInteractionMin", "setInteractionMin", "isBumpingEdges", "setBumpingEdges", "isDragging", "setDragging", "isFollowThumb", "setFollowThumb", "isPhysicalEnabled", "setPhysicalEnabled", "isRtl", "setRtl", "isStartFromMiddle", "setStartFromMiddle", "isSupportDeformation", "setSupportDeformation", "isVertical", "setVertical", "max", "getMax", "setMax", "maxHeight", "getMaxHeight", "setMaxHeight", SliceHints.SUBTYPE_MIN, "getMin", "setMin", "moveType", "getMoveType", "setMoveType", "oldProgress", "getOldProgress", "setOldProgress", "Lcom/coui/appcompat/seekbar/handler/IOrientationHandler;", "orientationHandler", "getOrientationHandler", "()Lcom/coui/appcompat/seekbar/handler/IOrientationHandler;", "setOrientationHandler$coui_support_seekbar_release", "(Lcom/coui/appcompat/seekbar/handler/IOrientationHandler;)V", "padding", "getPadding", "setPadding", "paddingHorizontal", "getPaddingHorizontal", "setPaddingHorizontal", "paddingVertical", "getPaddingVertical", "setPaddingVertical", "progress", "getProgress", "setProgress", "progressColor", "getProgressColor", "setProgressColor", "progressColorStateList", "getProgressColorStateList", "setProgressColorStateList$coui_support_seekbar_release", "progressEnd", "getProgressEnd", "setProgressEnd", "progressEnlargeScale", "getProgressEnlargeScale", "setProgressEnlargeScale", "progressHeight", "getProgressHeight", "setProgressHeight", "progressRadius", "getProgressRadius", "setProgressRadius", "progressRadiusEnlargeScale", "getProgressRadiusEnlargeScale", "setProgressRadiusEnlargeScale", "progressRoundCornerWeight", "getProgressRoundCornerWeight", "setProgressRoundCornerWeight", "progressStart", "getProgressStart", "setProgressStart", "scale", "getScale", "setScale", "secondaryProgress", "getSecondaryProgress", "setSecondaryProgress", "secondaryProgressColor", "getSecondaryProgressColor", "setSecondaryProgressColor", "secondaryProgressScale", "getSecondaryProgressScale", "setSecondaryProgressScale", "sectionActiveMarkColor", "getSectionActiveMarkColor", "setSectionActiveMarkColor", "sectionInactiveMarkColor", "getSectionInactiveMarkColor", "setSectionInactiveMarkColor", "sectionMarkRadius", "getSectionMarkRadius", "setSectionMarkRadius", "sectionThumbX", "getSectionThumbX", "setSectionThumbX", "seekbarMinWidth", "getSeekbarMinWidth", "setSeekbarMinWidth", "showGlitterEffect", "getShowGlitterEffect", "setShowGlitterEffect", "showProgress", "getShowProgress", "setShowProgress", "showSecondaryProgress", "getShowSecondaryProgress", "setShowSecondaryProgress", "showText", "getShowText", "setShowText", "showThumb", "getShowThumb", "setShowThumb", "textColor", "getTextColor", "setTextColor", "textContent", "", "getTextContent", "()Ljava/lang/String;", "setTextContent", "(Ljava/lang/String;)V", "textMarginTop", "getTextMarginTop", "setTextMarginTop", "thumbColor", "getThumbColor", "setThumbColor", "thumbColorStateList", "getThumbColorStateList", "setThumbColorStateList$coui_support_seekbar_release", "thumbMaxRadius", "getThumbMaxRadius", "setThumbMaxRadius", "thumbOutRadius", "getThumbOutRadius", "setThumbOutRadius", "thumbPosition", "getThumbPosition", "setThumbPosition", "thumbRadius", "getThumbRadius", "setThumbRadius", "thumbShadowColor", "getThumbShadowColor", "setThumbShadowColor", "thumbShadowOffsetY", "getThumbShadowOffsetY", "setThumbShadowOffsetY", "thumbShadowRadiusSize", "getThumbShadowRadiusSize", "setThumbShadowRadiusSize", "useSeparateProgressAndThumbScale", "getUseSeparateProgressAndThumbScale", "setUseSeparateProgressAndThumbScale", "Lcom/coui/appcompat/seekbar/core/COUISeekBarCore;", "view", "getView", "()Lcom/coui/appcompat/seekbar/core/COUISeekBarCore;", "setView$coui_support_seekbar_release", "(Lcom/coui/appcompat/seekbar/core/COUISeekBarCore;)V", "widthDeformedValue", "getWidthDeformedValue", "setWidthDeformedValue", "progressToScale", "snapshot", "Companion", "coui-support-seekbar_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class SeekBarState {
    public static final int DEFAULT_MAX = 100;
    private int backgroundColor;
    private ColorStateList backgroundColorStateList;
    private float backgroundEnlargeScale;
    private float backgroundHeight;
    private float backgroundRadius;
    private float backgroundRadiusEnlargeScale;
    private float backgroundRoundCornerWeight;
    private int centerX;
    private int centerY;
    private float curBackgroundHeight;
    private float curBackgroundRadius;
    private float curPaddingVertical;
    private float curProgressHeight;
    private float curProgressRadius;
    private float curThumbRadius;
    private float drawProgressScale;
    private float heightBottomDeformedDownValue;
    private float heightBottomDeformedUpValue;
    private float heightTopDeformedDownValue;
    private float heightTopDeformedUpValue;
    private int interactionMin;
    private boolean isBumpingEdges;
    private boolean isDragging;
    private boolean isFollowThumb;
    private boolean isRtl;
    private boolean isStartFromMiddle;
    private boolean isVertical;
    private int maxHeight;
    private int min;
    private int moveType;
    private int oldProgress;
    private IOrientationHandler orientationHandler;
    private float padding;
    private float paddingHorizontal;
    private float paddingVertical;
    private int progress;
    private int progressColor;
    private ColorStateList progressColorStateList;
    private float progressEnd;
    private float progressEnlargeScale;
    private float progressHeight;
    private float progressRadius;
    private float progressRadiusEnlargeScale;
    private float progressRoundCornerWeight;
    private float progressStart;
    private float scale;
    private int secondaryProgress;
    private int secondaryProgressColor;
    private float secondaryProgressScale;
    private int sectionActiveMarkColor;
    private int sectionInactiveMarkColor;
    private float sectionMarkRadius;
    private int seekbarMinWidth;
    private boolean showGlitterEffect;
    private boolean showText;
    private int textColor;
    private String textContent;
    private float textMarginTop;
    private int thumbColor;
    private ColorStateList thumbColorStateList;
    private float thumbMaxRadius;
    private float thumbOutRadius;
    private float thumbPosition;
    private float thumbRadius;
    private int thumbShadowColor;
    private float thumbShadowOffsetY;
    private int thumbShadowRadiusSize;
    private boolean useSeparateProgressAndThumbScale;
    private COUISeekBarCore view;
    private float widthDeformedValue;
    private int max = 100;
    private float sectionThumbX = -1.0f;
    private boolean showProgress = true;
    private boolean showThumb = true;
    private boolean showSecondaryProgress = true;
    private boolean isSupportDeformation = true;
    private boolean extendProgressRectForRoundCorner = true;
    private boolean isPhysicalEnabled = true;
    private boolean enableVibrator = true;
    private boolean enableAdaptiveVibrator = true;

    public final int getBackgroundColor() {
        return this.backgroundColor;
    }

    public final ColorStateList getBackgroundColorStateList() {
        return this.backgroundColorStateList;
    }

    public final float getBackgroundEnlargeScale() {
        return this.backgroundEnlargeScale;
    }

    public final float getBackgroundHeight() {
        return this.backgroundHeight;
    }

    public final float getBackgroundRadius() {
        return this.backgroundRadius;
    }

    public final float getBackgroundRadiusEnlargeScale() {
        return this.backgroundRadiusEnlargeScale;
    }

    public final float getBackgroundRoundCornerWeight() {
        return this.backgroundRoundCornerWeight;
    }

    public final int getCenterX() {
        return this.centerX;
    }

    public final int getCenterY() {
        return this.centerY;
    }

    public final float getCurBackgroundHeight() {
        return this.curBackgroundHeight;
    }

    public final float getCurBackgroundRadius() {
        return this.curBackgroundRadius;
    }

    public final float getCurPaddingVertical() {
        return this.curPaddingVertical;
    }

    public final float getCurProgressHeight() {
        return this.curProgressHeight;
    }

    public final float getCurProgressRadius() {
        return this.curProgressRadius;
    }

    public final float getCurThumbRadius() {
        return this.curThumbRadius;
    }

    public final float getDrawProgressScale() {
        return this.drawProgressScale;
    }

    public final boolean getEnableAdaptiveVibrator() {
        return this.enableAdaptiveVibrator;
    }

    public final boolean getEnableVibrator() {
        return this.enableVibrator;
    }

    public final boolean getExtendProgressRectForRoundCorner() {
        return this.extendProgressRectForRoundCorner;
    }

    public final float getHeightBottomDeformedDownValue() {
        return this.heightBottomDeformedDownValue;
    }

    public final float getHeightBottomDeformedUpValue() {
        return this.heightBottomDeformedUpValue;
    }

    public final float getHeightTopDeformedDownValue() {
        return this.heightTopDeformedDownValue;
    }

    public final float getHeightTopDeformedUpValue() {
        return this.heightTopDeformedUpValue;
    }

    public final int getInteractionMin() {
        return this.interactionMin;
    }

    public final int getMax() {
        return this.max;
    }

    public final int getMaxHeight() {
        return this.maxHeight;
    }

    public final int getMin() {
        return this.min;
    }

    public final int getMoveType() {
        return this.moveType;
    }

    public final int getOldProgress() {
        return this.oldProgress;
    }

    public final IOrientationHandler getOrientationHandler() {
        return this.orientationHandler;
    }

    public final float getPadding() {
        return this.padding;
    }

    public final float getPaddingHorizontal() {
        return this.paddingHorizontal;
    }

    public final float getPaddingVertical() {
        return this.paddingVertical;
    }

    public final int getProgress() {
        return this.progress;
    }

    public final int getProgressColor() {
        return this.progressColor;
    }

    public final ColorStateList getProgressColorStateList() {
        return this.progressColorStateList;
    }

    public final float getProgressEnd() {
        return this.progressEnd;
    }

    public final float getProgressEnlargeScale() {
        return this.progressEnlargeScale;
    }

    public final float getProgressHeight() {
        return this.progressHeight;
    }

    public final float getProgressRadius() {
        return this.progressRadius;
    }

    public final float getProgressRadiusEnlargeScale() {
        return this.progressRadiusEnlargeScale;
    }

    public final float getProgressRoundCornerWeight() {
        return this.progressRoundCornerWeight;
    }

    public final float getProgressStart() {
        return this.progressStart;
    }

    public final float getScale() {
        return this.scale;
    }

    public final int getSecondaryProgress() {
        return this.secondaryProgress;
    }

    public final int getSecondaryProgressColor() {
        return this.secondaryProgressColor;
    }

    public final float getSecondaryProgressScale() {
        return this.secondaryProgressScale;
    }

    public final int getSectionActiveMarkColor() {
        return this.sectionActiveMarkColor;
    }

    public final int getSectionInactiveMarkColor() {
        return this.sectionInactiveMarkColor;
    }

    public final float getSectionMarkRadius() {
        return this.sectionMarkRadius;
    }

    public final float getSectionThumbX() {
        return this.sectionThumbX;
    }

    public final int getSeekbarMinWidth() {
        return this.seekbarMinWidth;
    }

    public final boolean getShowGlitterEffect() {
        return this.showGlitterEffect;
    }

    public final boolean getShowProgress() {
        return this.showProgress;
    }

    public final boolean getShowSecondaryProgress() {
        return this.showSecondaryProgress;
    }

    public final boolean getShowText() {
        return this.showText;
    }

    public final boolean getShowThumb() {
        return this.showThumb;
    }

    public final int getTextColor() {
        return this.textColor;
    }

    public final String getTextContent() {
        return this.textContent;
    }

    public final float getTextMarginTop() {
        return this.textMarginTop;
    }

    public final int getThumbColor() {
        return this.thumbColor;
    }

    public final ColorStateList getThumbColorStateList() {
        return this.thumbColorStateList;
    }

    public final float getThumbMaxRadius() {
        return this.thumbMaxRadius;
    }

    public final float getThumbOutRadius() {
        return this.thumbOutRadius;
    }

    public final float getThumbPosition() {
        return this.thumbPosition;
    }

    public final float getThumbRadius() {
        return this.thumbRadius;
    }

    public final int getThumbShadowColor() {
        return this.thumbShadowColor;
    }

    public final float getThumbShadowOffsetY() {
        return this.thumbShadowOffsetY;
    }

    public final int getThumbShadowRadiusSize() {
        return this.thumbShadowRadiusSize;
    }

    public final boolean getUseSeparateProgressAndThumbScale() {
        return this.useSeparateProgressAndThumbScale;
    }

    public final COUISeekBarCore getView() {
        return this.view;
    }

    public final float getWidthDeformedValue() {
        return this.widthDeformedValue;
    }

    /* JADX INFO: renamed from: isBumpingEdges, reason: from getter */
    public final boolean getIsBumpingEdges() {
        return this.isBumpingEdges;
    }

    /* JADX INFO: renamed from: isDragging, reason: from getter */
    public final boolean getIsDragging() {
        return this.isDragging;
    }

    /* JADX INFO: renamed from: isFollowThumb, reason: from getter */
    public final boolean getIsFollowThumb() {
        return this.isFollowThumb;
    }

    /* JADX INFO: renamed from: isPhysicalEnabled, reason: from getter */
    public final boolean getIsPhysicalEnabled() {
        return this.isPhysicalEnabled;
    }

    /* JADX INFO: renamed from: isRtl, reason: from getter */
    public final boolean getIsRtl() {
        return this.isRtl;
    }

    /* JADX INFO: renamed from: isStartFromMiddle, reason: from getter */
    public final boolean getIsStartFromMiddle() {
        return this.isStartFromMiddle;
    }

    /* JADX INFO: renamed from: isSupportDeformation, reason: from getter */
    public final boolean getIsSupportDeformation() {
        return this.isSupportDeformation;
    }

    /* JADX INFO: renamed from: isVertical, reason: from getter */
    public final boolean getIsVertical() {
        return this.isVertical;
    }

    public final float progressToScale(int progress) {
        int i3 = this.max;
        int i4 = this.min;
        if (i3 > i4) {
            return (progress - i4) / (i3 - i4);
        }
        return 0.0f;
    }

    public final void setBackgroundColor(int i3) {
        this.backgroundColor = i3;
    }

    public final void setBackgroundColorStateList$coui_support_seekbar_release(ColorStateList colorStateList) {
        this.backgroundColorStateList = colorStateList;
    }

    public final void setBackgroundEnlargeScale(float f3) {
        this.backgroundEnlargeScale = f3;
    }

    public final void setBackgroundHeight(float f3) {
        this.backgroundHeight = f3;
    }

    public final void setBackgroundRadius(float f3) {
        this.backgroundRadius = f3;
    }

    public final void setBackgroundRadiusEnlargeScale(float f3) {
        this.backgroundRadiusEnlargeScale = f3;
    }

    public final void setBackgroundRoundCornerWeight(float f3) {
        this.backgroundRoundCornerWeight = f3;
    }

    public final void setBumpingEdges(boolean z2) {
        this.isBumpingEdges = z2;
    }

    public final void setCenterX(int i3) {
        this.centerX = i3;
    }

    public final void setCenterY(int i3) {
        this.centerY = i3;
    }

    public final void setCurBackgroundHeight(float f3) {
        this.curBackgroundHeight = f3;
    }

    public final void setCurBackgroundRadius(float f3) {
        this.curBackgroundRadius = f3;
    }

    public final void setCurPaddingVertical(float f3) {
        this.curPaddingVertical = f3;
    }

    public final void setCurProgressHeight(float f3) {
        this.curProgressHeight = f3;
    }

    public final void setCurProgressRadius(float f3) {
        this.curProgressRadius = f3;
    }

    public final void setCurThumbRadius(float f3) {
        this.curThumbRadius = f3;
    }

    public final void setDragging(boolean z2) {
        this.isDragging = z2;
    }

    public final void setDrawProgressScale(float f3) {
        this.drawProgressScale = f3;
    }

    public final void setEnableAdaptiveVibrator(boolean z2) {
        this.enableAdaptiveVibrator = z2;
    }

    public final void setEnableVibrator(boolean z2) {
        this.enableVibrator = z2;
    }

    public final void setExtendProgressRectForRoundCorner(boolean z2) {
        this.extendProgressRectForRoundCorner = z2;
    }

    public final void setFollowThumb(boolean z2) {
        this.isFollowThumb = z2;
    }

    public final void setHeightBottomDeformedDownValue(float f3) {
        this.heightBottomDeformedDownValue = f3;
    }

    public final void setHeightBottomDeformedUpValue(float f3) {
        this.heightBottomDeformedUpValue = f3;
    }

    public final void setHeightTopDeformedDownValue(float f3) {
        this.heightTopDeformedDownValue = f3;
    }

    public final void setHeightTopDeformedUpValue(float f3) {
        this.heightTopDeformedUpValue = f3;
    }

    public final void setInteractionMin(int i3) {
        this.interactionMin = i3;
    }

    public final void setMax(int i3) {
        this.max = i3;
    }

    public final void setMaxHeight(int i3) {
        this.maxHeight = i3;
    }

    public final void setMin(int i3) {
        this.min = i3;
    }

    public final void setMoveType(int i3) {
        this.moveType = i3;
    }

    public final void setOldProgress(int i3) {
        this.oldProgress = i3;
    }

    public final void setOrientationHandler$coui_support_seekbar_release(IOrientationHandler iOrientationHandler) {
        this.orientationHandler = iOrientationHandler;
    }

    public final void setPadding(float f3) {
        this.padding = f3;
    }

    public final void setPaddingHorizontal(float f3) {
        this.paddingHorizontal = f3;
    }

    public final void setPaddingVertical(float f3) {
        this.paddingVertical = f3;
    }

    public final void setPhysicalEnabled(boolean z2) {
        this.isPhysicalEnabled = z2;
    }

    public final void setProgress(int i3) {
        this.progress = i3;
    }

    public final void setProgressColor(int i3) {
        this.progressColor = i3;
    }

    public final void setProgressColorStateList$coui_support_seekbar_release(ColorStateList colorStateList) {
        this.progressColorStateList = colorStateList;
    }

    public final void setProgressEnd(float f3) {
        this.progressEnd = f3;
    }

    public final void setProgressEnlargeScale(float f3) {
        this.progressEnlargeScale = f3;
    }

    public final void setProgressHeight(float f3) {
        this.progressHeight = f3;
    }

    public final void setProgressRadius(float f3) {
        this.progressRadius = f3;
    }

    public final void setProgressRadiusEnlargeScale(float f3) {
        this.progressRadiusEnlargeScale = f3;
    }

    public final void setProgressRoundCornerWeight(float f3) {
        this.progressRoundCornerWeight = f3;
    }

    public final void setProgressStart(float f3) {
        this.progressStart = f3;
    }

    public final void setRtl(boolean z2) {
        this.isRtl = z2;
    }

    public final void setScale(float f3) {
        this.scale = f3;
    }

    public final void setSecondaryProgress(int i3) {
        this.secondaryProgress = i3;
    }

    public final void setSecondaryProgressColor(int i3) {
        this.secondaryProgressColor = i3;
    }

    public final void setSecondaryProgressScale(float f3) {
        this.secondaryProgressScale = f3;
    }

    public final void setSectionActiveMarkColor(int i3) {
        this.sectionActiveMarkColor = i3;
    }

    public final void setSectionInactiveMarkColor(int i3) {
        this.sectionInactiveMarkColor = i3;
    }

    public final void setSectionMarkRadius(float f3) {
        this.sectionMarkRadius = f3;
    }

    public final void setSectionThumbX(float f3) {
        this.sectionThumbX = f3;
    }

    public final void setSeekbarMinWidth(int i3) {
        this.seekbarMinWidth = i3;
    }

    public final void setShowGlitterEffect(boolean z2) {
        this.showGlitterEffect = z2;
    }

    public final void setShowProgress(boolean z2) {
        this.showProgress = z2;
    }

    public final void setShowSecondaryProgress(boolean z2) {
        this.showSecondaryProgress = z2;
    }

    public final void setShowText(boolean z2) {
        this.showText = z2;
    }

    public final void setShowThumb(boolean z2) {
        this.showThumb = z2;
    }

    public final void setStartFromMiddle(boolean z2) {
        this.isStartFromMiddle = z2;
    }

    public final void setSupportDeformation(boolean z2) {
        this.isSupportDeformation = z2;
    }

    public final void setTextColor(int i3) {
        this.textColor = i3;
    }

    public final void setTextContent(String str) {
        this.textContent = str;
    }

    public final void setTextMarginTop(float f3) {
        this.textMarginTop = f3;
    }

    public final void setThumbColor(int i3) {
        this.thumbColor = i3;
    }

    public final void setThumbColorStateList$coui_support_seekbar_release(ColorStateList colorStateList) {
        this.thumbColorStateList = colorStateList;
    }

    public final void setThumbMaxRadius(float f3) {
        this.thumbMaxRadius = f3;
    }

    public final void setThumbOutRadius(float f3) {
        this.thumbOutRadius = f3;
    }

    public final void setThumbPosition(float f3) {
        this.thumbPosition = f3;
    }

    public final void setThumbRadius(float f3) {
        this.thumbRadius = f3;
    }

    public final void setThumbShadowColor(int i3) {
        this.thumbShadowColor = i3;
    }

    public final void setThumbShadowOffsetY(float f3) {
        this.thumbShadowOffsetY = f3;
    }

    public final void setThumbShadowRadiusSize(int i3) {
        this.thumbShadowRadiusSize = i3;
    }

    public final void setUseSeparateProgressAndThumbScale(boolean z2) {
        this.useSeparateProgressAndThumbScale = z2;
    }

    public final void setVertical(boolean z2) {
        this.isVertical = z2;
    }

    public final void setView$coui_support_seekbar_release(COUISeekBarCore cOUISeekBarCore) {
        this.view = cOUISeekBarCore;
    }

    public final void setWidthDeformedValue(float f3) {
        this.widthDeformedValue = f3;
    }

    public final SeekBarState snapshot() {
        SeekBarState seekBarState = new SeekBarState();
        seekBarState.progress = this.progress;
        seekBarState.max = this.max;
        seekBarState.min = this.min;
        seekBarState.interactionMin = this.interactionMin;
        seekBarState.oldProgress = this.oldProgress;
        seekBarState.secondaryProgress = this.secondaryProgress;
        seekBarState.progressColor = this.progressColor;
        seekBarState.backgroundColor = this.backgroundColor;
        seekBarState.thumbColor = this.thumbColor;
        seekBarState.secondaryProgressColor = this.secondaryProgressColor;
        seekBarState.thumbShadowColor = this.thumbShadowColor;
        seekBarState.scale = this.scale;
        seekBarState.drawProgressScale = this.drawProgressScale;
        seekBarState.progressRadius = this.progressRadius;
        seekBarState.curProgressRadius = this.curProgressRadius;
        seekBarState.progressHeight = this.progressHeight;
        seekBarState.curProgressHeight = this.curProgressHeight;
        seekBarState.thumbRadius = this.thumbRadius;
        seekBarState.curThumbRadius = this.curThumbRadius;
        seekBarState.thumbMaxRadius = this.thumbMaxRadius;
        seekBarState.thumbOutRadius = this.thumbOutRadius;
        seekBarState.backgroundRadius = this.backgroundRadius;
        seekBarState.curBackgroundRadius = this.curBackgroundRadius;
        seekBarState.backgroundHeight = this.backgroundHeight;
        seekBarState.curBackgroundHeight = this.curBackgroundHeight;
        seekBarState.backgroundEnlargeScale = this.backgroundEnlargeScale;
        seekBarState.progressEnlargeScale = this.progressEnlargeScale;
        seekBarState.backgroundRadiusEnlargeScale = this.backgroundRadiusEnlargeScale;
        seekBarState.progressRadiusEnlargeScale = this.progressRadiusEnlargeScale;
        seekBarState.seekbarMinWidth = this.seekbarMinWidth;
        seekBarState.maxHeight = this.maxHeight;
        seekBarState.showText = this.showText;
        seekBarState.textContent = this.textContent;
        seekBarState.textColor = this.textColor;
        seekBarState.textMarginTop = this.textMarginTop;
        seekBarState.padding = this.padding;
        seekBarState.paddingHorizontal = this.paddingHorizontal;
        seekBarState.paddingVertical = this.paddingVertical;
        seekBarState.curPaddingVertical = this.curPaddingVertical;
        seekBarState.sectionThumbX = this.sectionThumbX;
        seekBarState.sectionActiveMarkColor = this.sectionActiveMarkColor;
        seekBarState.sectionInactiveMarkColor = this.sectionInactiveMarkColor;
        seekBarState.sectionMarkRadius = this.sectionMarkRadius;
        seekBarState.thumbShadowOffsetY = this.thumbShadowOffsetY;
        seekBarState.thumbShadowRadiusSize = this.thumbShadowRadiusSize;
        seekBarState.centerX = this.centerX;
        seekBarState.centerY = this.centerY;
        seekBarState.thumbPosition = this.thumbPosition;
        seekBarState.progressStart = this.progressStart;
        seekBarState.progressEnd = this.progressEnd;
        seekBarState.isRtl = this.isRtl;
        seekBarState.isVertical = this.isVertical;
        seekBarState.isDragging = this.isDragging;
        seekBarState.showProgress = this.showProgress;
        seekBarState.showThumb = this.showThumb;
        seekBarState.showSecondaryProgress = this.showSecondaryProgress;
        seekBarState.isFollowThumb = this.isFollowThumb;
        seekBarState.isSupportDeformation = this.isSupportDeformation;
        seekBarState.showGlitterEffect = this.showGlitterEffect;
        seekBarState.isBumpingEdges = this.isBumpingEdges;
        seekBarState.isStartFromMiddle = this.isStartFromMiddle;
        seekBarState.useSeparateProgressAndThumbScale = this.useSeparateProgressAndThumbScale;
        seekBarState.extendProgressRectForRoundCorner = this.extendProgressRectForRoundCorner;
        seekBarState.isPhysicalEnabled = this.isPhysicalEnabled;
        seekBarState.enableVibrator = this.enableVibrator;
        seekBarState.enableAdaptiveVibrator = this.enableAdaptiveVibrator;
        seekBarState.moveType = this.moveType;
        seekBarState.heightTopDeformedUpValue = this.heightTopDeformedUpValue;
        seekBarState.heightBottomDeformedDownValue = this.heightBottomDeformedDownValue;
        seekBarState.widthDeformedValue = this.widthDeformedValue;
        seekBarState.heightBottomDeformedUpValue = this.heightBottomDeformedUpValue;
        seekBarState.heightTopDeformedDownValue = this.heightTopDeformedDownValue;
        seekBarState.backgroundRoundCornerWeight = this.backgroundRoundCornerWeight;
        seekBarState.progressRoundCornerWeight = this.progressRoundCornerWeight;
        seekBarState.view = null;
        seekBarState.orientationHandler = null;
        return seekBarState;
    }
}