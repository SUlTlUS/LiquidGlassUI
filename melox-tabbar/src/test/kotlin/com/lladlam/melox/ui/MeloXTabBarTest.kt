package com.lladlam.melox.ui

import org.junit.Test

class MeloXTabBarTest {
    @Test fun indexedHostTabsKeepSeparateActionAndArbitrarySlotCount() {
        val tabs = List(9) { AppTab.custom(it.toString()) }
        validateMeloXTabs(tabs, tabs.last())
        validateMeloXTabs(tabs, AppTab.Search)
    }
    @Test(expected = IllegalArgumentException::class)
    fun repeatedHostSlotKeysAreRejected() {
        val slot = AppTab.custom("publish")
        validateMeloXTabs(listOf(slot, AppTab.custom("publish")), slot)
    }
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
