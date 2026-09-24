package com.idonotknowu.callblocker.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w411dp-h891dp-xxhdpi")
class HomeScreenTest {
    @get:Rule val rule = createComposeRule()

    @Test fun inactiveShowsActivateAndDisabledStatus() {
        rule.setContent { AppTheme { HomeScreen(false, 0, {}, {}) } }
        rule.onNodeWithText("Ativar").assertIsDisplayed()
        rule.onNodeWithText("Proteção desativada").assertIsDisplayed()
    }

    @Test fun activeShowsDeactivateAndActiveStatus() {
        rule.setContent { AppTheme { HomeScreen(true, 0, {}, {}) } }
        rule.onNodeWithText("Desativar").assertIsDisplayed()
        rule.onNodeWithText("Proteção ativa").assertIsDisplayed()
    }

    @Test fun showsBlockedCountOnCard() {
        rule.setContent { AppTheme { HomeScreen(true, 7, {}, {}) } }
        rule.onNodeWithText("7").assertIsDisplayed()
    }

    @Test fun buttonsInvokeCallbacks() {
        var toggles = 0
        var opened = 0
        rule.setContent { AppTheme { HomeScreen(false, 0, { toggles++ }, { opened++ }) } }
        rule.onNodeWithText("Ativar").performClick()
        rule.onNodeWithText("Ver bloqueadas").performClick()
        assertEquals(1, toggles)
        assertEquals(1, opened)
    }
}
