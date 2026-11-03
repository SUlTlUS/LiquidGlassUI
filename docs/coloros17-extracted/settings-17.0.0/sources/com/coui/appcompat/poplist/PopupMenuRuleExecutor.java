package com.coui.appcompat.poplist;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import com.coui.appcompat.log.COUILog;
import com.coui.appcompat.uiutil.Domain;
import com.coui.appcompat.uiutil.Executor;
import com.coui.appcompat.uiutil.Rule;

/* JADX INFO: loaded from: classes7.dex */
final class PopupMenuRuleExecutor implements Executor<PopupMenuRule, PopupMenuDomain> {
    private static final String TAG = "PopupMenuRuleExecutor";
    private StringBuilder mConfigRulesRecord;
    private final Executor<PopupMenuControlRule, PopupMenuDomain> mPopupMenuControlRuleExecutor = new AnonymousClass1();
    private final Executor<PopupMenuConfigRule, PopupMenuDomain> mPopupMenuConfigRuleExecutor = new AnonymousClass2();

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PopupMenuRuleExecutor$1, reason: invalid class name */
    public class AnonymousClass1 implements Executor<PopupMenuControlRule, PopupMenuDomain> {
        public AnonymousClass1() {
        }

        @Override // com.coui.appcompat.uiutil.Executor
        public /* bridge */ /* synthetic */ Executor execute(Rule rule, Domain domain) {
            return execute((PopupMenuControlRule) rule, (PopupMenuDomain) domain);
        }

        public Executor<PopupMenuControlRule, PopupMenuDomain> execute(PopupMenuControlRule popupMenuControlRule, PopupMenuDomain popupMenuDomain) {
            popupMenuControlRule.operation(popupMenuDomain);
            return this;
        }
    }

    /* JADX INFO: renamed from: com.coui.appcompat.poplist.PopupMenuRuleExecutor$2, reason: invalid class name */
    public class AnonymousClass2 implements Executor<PopupMenuConfigRule, PopupMenuDomain> {
        public AnonymousClass2() {
        }

        @Override // com.coui.appcompat.uiutil.Executor
        public /* bridge */ /* synthetic */ Executor execute(Rule rule, Domain domain) {
            return execute((PopupMenuConfigRule) rule, (PopupMenuDomain) domain);
        }

        public Executor<PopupMenuConfigRule, PopupMenuDomain> execute(PopupMenuConfigRule popupMenuConfigRule, PopupMenuDomain popupMenuDomain) {
            if (!popupMenuConfigRule.getPopupMenuRuleEnabled()) {
                COUILog.i(PopupMenuRuleExecutor.TAG, "Skip disabled rule " + popupMenuConfigRule);
                return this;
            }
            PopupMenuRuleExecutor.c(PopupMenuRuleExecutor.this, popupMenuConfigRule, popupMenuDomain);
            int type = popupMenuConfigRule.getType();
            if (type == 0) {
                popupMenuDomain.mWindow.set(popupMenuConfigRule.getDisplayFrame());
                return this;
            }
            if (type == 1) {
                popupMenuDomain.mAnchor.set(popupMenuConfigRule.getDisplayFrame());
                popupMenuDomain.mAnchorOutsets.set(popupMenuConfigRule.getOutsets());
                return this;
            }
            if (type == 2) {
                Rect rectB = PopupMenuRuleExecutor.b(PopupMenuRuleExecutor.this, popupMenuConfigRule.getDisplayFrame(), popupMenuConfigRule.getOutsets());
                PopupMenuRuleExecutor.a(PopupMenuRuleExecutor.this, rectB);
                boolean popupMenuVerticalRuleEnabled = popupMenuConfigRule.getPopupMenuVerticalRuleEnabled();
                int barrierDirection = popupMenuConfigRule.getBarrierDirection();
                if (barrierDirection == 0) {
                    Rect rect = popupMenuDomain.mWindowBarriers;
                    rect.left = Math.max(rect.left, rectB.right - popupMenuDomain.mWindow.left);
                    return this;
                }
                if (barrierDirection != 1) {
                    if (barrierDirection == 2) {
                        Rect rect2 = popupMenuDomain.mWindowBarriers;
                        rect2.right = Math.max(rect2.right, popupMenuDomain.mWindow.right - rectB.left);
                        return this;
                    }
                    if (barrierDirection != 3) {
                        if (barrierDirection == 4) {
                            Rect rect3 = popupMenuDomain.mWindowBarriers;
                            rect3.left = Math.max(rect3.left, rectB.left - popupMenuDomain.mWindow.left);
                            Rect rect4 = popupMenuDomain.mWindowBarriers;
                            rect4.right = Math.max(rect4.right, popupMenuDomain.mWindow.right - rectB.right);
                            if (popupMenuVerticalRuleEnabled) {
                                Rect rect5 = popupMenuDomain.mWindowBarriers;
                                rect5.top = Math.max(rect5.top, rectB.top - popupMenuDomain.mWindow.top);
                                Rect rect6 = popupMenuDomain.mWindowBarriers;
                                rect6.bottom = Math.max(rect6.bottom, popupMenuDomain.mWindow.bottom - rectB.bottom);
                                return this;
                            }
                        }
                    } else if (popupMenuVerticalRuleEnabled) {
                        Rect rect7 = popupMenuDomain.mWindowBarriers;
                        rect7.bottom = Math.max(rect7.bottom, popupMenuDomain.mWindow.bottom - rectB.top);
                        return this;
                    }
                } else if (popupMenuVerticalRuleEnabled) {
                    Rect rect8 = popupMenuDomain.mWindowBarriers;
                    rect8.top = Math.max(rect8.top, rectB.bottom - popupMenuDomain.mWindow.top);
                }
            } else if (type == 3) {
                popupMenuDomain.mSubMenuAnchor.set(popupMenuConfigRule.getDisplayFrame());
                return this;
            }
            return this;
        }
    }

    public static /* bridge */ /* synthetic */ void a(PopupMenuRuleExecutor popupMenuRuleExecutor, Rect rect) {
        popupMenuRuleExecutor.ensureBarrierLegal(rect);
    }

    public static /* bridge */ /* synthetic */ Rect b(PopupMenuRuleExecutor popupMenuRuleExecutor, Rect rect, Rect rect2) {
        return popupMenuRuleExecutor.getOutsetsRect(rect, rect2);
    }

    public static /* bridge */ /* synthetic */ void c(PopupMenuRuleExecutor popupMenuRuleExecutor, PopupMenuConfigRule popupMenuConfigRule, PopupMenuDomain popupMenuDomain) {
        popupMenuRuleExecutor.recordConfigRules(popupMenuConfigRule, popupMenuDomain);
    }

    private void ensureBarrierLegal(Rect rect) {
        if (rect.left < 0) {
            Log.e(TAG, "barrier left < 0 !!");
            rect.left = 0;
        }
        if (rect.top < 0) {
            Log.e(TAG, "barrier top < 0 !!");
            rect.top = 0;
        }
        if (rect.right < 0) {
            Log.e(TAG, "barrier right < 0 !!");
            rect.right = 0;
        }
        if (rect.bottom < 0) {
            Log.e(TAG, "barrier bottom < 0 !!");
            rect.bottom = 0;
        }
    }

    private Rect getOutsetsRect(Rect rect, Rect rect2) {
        return new Rect(rect.left - rect2.left, rect.top - rect2.top, rect.right + rect2.right, rect.bottom + rect2.bottom);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void recordBarrierRules(PopupMenuConfigRule popupMenuConfigRule, PopupMenuDomain popupMenuDomain) {
        Rect outsetsRect = getOutsetsRect(popupMenuConfigRule.getDisplayFrame(), popupMenuConfigRule.getOutsets());
        int barrierDirection = popupMenuConfigRule.getBarrierDirection();
        if (barrierDirection == -1) {
            this.mConfigRulesRecord.append("#BARRIER_GONE:");
        } else if (barrierDirection == 0) {
            this.mConfigRulesRecord.append("#BARRIER_FROM_LEFT:");
        } else if (barrierDirection == 1) {
            this.mConfigRulesRecord.append("#BARRIER_FROM_TOP:");
        } else if (barrierDirection == 2) {
            this.mConfigRulesRecord.append("#BARRIER_FROM_RIGHT:");
        } else if (barrierDirection == 3) {
            this.mConfigRulesRecord.append("#BARRIER_FROM_BOTTOM:");
        } else if (barrierDirection == 4) {
            this.mConfigRulesRecord.append("#BARRIER_WINDOW:");
        }
        this.mConfigRulesRecord.append("vertical rule enabled:");
        this.mConfigRulesRecord.append(popupMenuConfigRule.getPopupMenuVerticalRuleEnabled());
        this.mConfigRulesRecord.append(" old domain window barrier:");
        this.mConfigRulesRecord.append(popupMenuDomain.mWindowBarriers);
        this.mConfigRulesRecord.append(" barrier:");
        this.mConfigRulesRecord.append(outsetsRect);
        this.mConfigRulesRecord.append(" domain window:");
        this.mConfigRulesRecord.append(popupMenuDomain.mWindow);
        this.mConfigRulesRecord.append(" rule: ");
        this.mConfigRulesRecord.append(popupMenuConfigRule);
        if (popupMenuConfigRule instanceof View) {
            this.mConfigRulesRecord.append(" parent: ");
            this.mConfigRulesRecord.append(((View) popupMenuConfigRule).getParent());
        }
        this.mConfigRulesRecord.append("\n");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void recordConfigRules(PopupMenuConfigRule popupMenuConfigRule, PopupMenuDomain popupMenuDomain) {
        if (this.mConfigRulesRecord == null) {
            this.mConfigRulesRecord = new StringBuilder();
        }
        int type = popupMenuConfigRule.getType();
        if (type == 0) {
            this.mConfigRulesRecord.append("#TYPE_WINDOW: display frame: ");
            this.mConfigRulesRecord.append(popupMenuConfigRule.getDisplayFrame());
            this.mConfigRulesRecord.append(" rule: ");
            this.mConfigRulesRecord.append(popupMenuConfigRule);
            if (popupMenuConfigRule instanceof View) {
                this.mConfigRulesRecord.append(" parent: ");
                this.mConfigRulesRecord.append(((View) popupMenuConfigRule).getParent());
            }
            this.mConfigRulesRecord.append("\n");
            return;
        }
        if (type == 1) {
            this.mConfigRulesRecord.append("#TYPE_ANCHOR: display frame: ");
            this.mConfigRulesRecord.append(popupMenuConfigRule.getDisplayFrame());
            this.mConfigRulesRecord.append(" outsets: ");
            this.mConfigRulesRecord.append(popupMenuConfigRule.getOutsets());
            this.mConfigRulesRecord.append(" rule: ");
            this.mConfigRulesRecord.append(popupMenuConfigRule);
            if (popupMenuConfigRule instanceof View) {
                this.mConfigRulesRecord.append(" parent: ");
                this.mConfigRulesRecord.append(((View) popupMenuConfigRule).getParent());
            }
            this.mConfigRulesRecord.append("\n");
            return;
        }
        if (type == 2) {
            recordBarrierRules(popupMenuConfigRule, popupMenuDomain);
            return;
        }
        if (type != 3) {
            return;
        }
        this.mConfigRulesRecord.append("#TYPE_SUBMENU_ANCHOR: display frame: ");
        this.mConfigRulesRecord.append(popupMenuConfigRule.getDisplayFrame());
        this.mConfigRulesRecord.append(" rule: ");
        this.mConfigRulesRecord.append(popupMenuConfigRule);
        if (popupMenuConfigRule instanceof View) {
            this.mConfigRulesRecord.append(" parent: ");
            this.mConfigRulesRecord.append(((View) popupMenuConfigRule).getParent());
        }
        this.mConfigRulesRecord.append("\n");
    }

    public void beginConfigRulesRecord() {
        this.mConfigRulesRecord = new StringBuilder();
    }

    public void endConfigRulesRecord() {
        StringBuilder sb = this.mConfigRulesRecord;
        if (sb != null) {
            COUILog.i(TAG, sb.toString());
        } else {
            COUILog.e(TAG, "No config rules record! Not initialized!");
        }
    }

    @Override // com.coui.appcompat.uiutil.Executor
    public /* bridge */ /* synthetic */ Executor execute(Rule rule, Domain domain) {
        return execute((PopupMenuRule) rule, (PopupMenuDomain) domain);
    }

    public Executor<PopupMenuRule, PopupMenuDomain> execute(PopupMenuRule popupMenuRule, PopupMenuDomain popupMenuDomain) {
        if (popupMenuRule instanceof PopupMenuControlRule) {
            this.mPopupMenuControlRuleExecutor.execute((PopupMenuControlRule) popupMenuRule, popupMenuDomain);
            return this;
        }
        if (popupMenuRule instanceof PopupMenuConfigRule) {
            this.mPopupMenuConfigRuleExecutor.execute((PopupMenuConfigRule) popupMenuRule, popupMenuDomain);
        }
        return this;
    }
}