package com.lladlam.melox.ui

import org.junit.Test

class MeloXTabBarTest {
    @Test fun originalTabsAndSeparateSearchAreValid() {
        validateMeloXTabs(MeloXDefaultTabs, AppTab.Home)
        validateMeloXTabs(MeloXDefaultTabs, AppTab.Search)
        validateMeloXTabs(listOf(AppTab.Cloud), AppTab.Cloud)
    }
    @Test(expected = IllegalArgumentException::class)
    fun emptyTabsFailBeforeGeometryDividesByZero() = validateMeloXTabs(emptyList(), AppTab.Home)
    @Test(expected = IllegalArgumentException::class)
    fun duplicateTabsFail() = validateMeloXTabs(listOf(AppTab.Home, AppTab.Home), AppTab.Home)
    @Test(expected = IllegalArgumentException::class)
    fun searchIsNotAPrimaryTab() = validateMeloXTabs(listOf(AppTab.Home, AppTab.Search), AppTab.Home)
    @Test(expected = IllegalArgumentException::class)
    fun absentSelectionFails() = validateMeloXTabs(listOf(AppTab.Home), AppTab.Library)
}
