package com.coui.appcompat.bottomfloatingnavigationbar;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import com.coui.appcompat.log.COUILog;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
public class MenuXmlParserUtils {
    private static final String ICON_ATTR_NAME = "icon";
    private static final String LARGE_ICON_ATTR_NAME = "largeIcon";
    private static final String NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android";
    private static final String NAMESPACE_APP = "http://schemas.android.com/apk/res-auto";
    private static final String RED_DOT_NUMBER_ATTR_NAME = "redDotNumber";
    private static final String RED_DOT_TYPE_ATTR_NAME = "redDotType";
    private static final String TAG = "MenuXmlParserUtils";

    public static class MenuItemAttrs {
        public boolean isLargeIcon = false;
        public int redDotType = 0;
        public int redDotNumber = 0;
        public int iconResId = 0;
    }

    private static int parseItemId(AttributeSet attributeSet) {
        String attributeValue = attributeSet.getAttributeValue(NAMESPACE_ANDROID, "id");
        if (attributeValue != null) {
            if (attributeValue.startsWith("@")) {
                return attributeSet.getAttributeResourceValue(NAMESPACE_ANDROID, "id", 0);
            }
            try {
                return Integer.parseInt(attributeValue);
            } catch (NumberFormatException unused) {
            }
        }
        return 0;
    }

    public static Map<Integer, MenuItemAttrs> parseMenuItemAttrs(Context context, int i3) {
        int itemId;
        HashMap map = new HashMap();
        if (i3 != 0) {
            try {
                XmlResourceParser xml = context.getResources().getXml(i3);
                AttributeSet attributeSetAsAttributeSet = Xml.asAttributeSet(xml);
                for (int eventType = xml.getEventType(); eventType != 1; eventType = xml.next()) {
                    if (eventType == 2 && "item".equals(xml.getName()) && (itemId = parseItemId(attributeSetAsAttributeSet)) != 0) {
                        MenuItemAttrs menuItemAttrs = new MenuItemAttrs();
                        menuItemAttrs.iconResId = attributeSetAsAttributeSet.getAttributeResourceValue(NAMESPACE_ANDROID, "icon", 0);
                        menuItemAttrs.isLargeIcon = attributeSetAsAttributeSet.getAttributeBooleanValue(NAMESPACE_APP, LARGE_ICON_ATTR_NAME, false);
                        menuItemAttrs.redDotType = attributeSetAsAttributeSet.getAttributeIntValue(NAMESPACE_APP, RED_DOT_TYPE_ATTR_NAME, 0);
                        menuItemAttrs.redDotNumber = attributeSetAsAttributeSet.getAttributeIntValue(NAMESPACE_APP, RED_DOT_NUMBER_ATTR_NAME, 0);
                        map.put(Integer.valueOf(itemId), menuItemAttrs);
                    }
                }
                xml.close();
                return map;
            } catch (Exception e3) {
                COUILog.e(TAG, "Error parsing menu XML for custom attrs", e3);
            }
        }
        return map;
    }
}