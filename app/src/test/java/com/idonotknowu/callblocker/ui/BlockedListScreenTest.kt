package com.idonotknowu.callblocker.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.idonotknowu.callblocker.data.BlockedCall
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BlockedListScreenTest {
    @get:Rule val rule = createComposeRule()

    @Test fun emptyStateExplainsHowListFills() {
        rule.setContent { AppTheme { BlockedListScreen(emptyList(), {}) } }
        rule.onNodeWithText("Nada por aqui").assertIsDisplayed()
        rule.onNodeWithText("0").assertIsDisplayed()
    }

    @Test fun listsNumbersAndTotal() {
        val calls = listOf(
            BlockedCall(2, "11988887777", 2_000),
            BlockedCall(1, "Desconhecido", 1_000),
        )
        rule.setContent { AppTheme { BlockedListScreen(calls, {}) } }
        rule.onNodeWithText("2").assertIsDisplayed()
        rule.onNodeWithText("ligações barradas").assertIsDisplayed()
        rule.onNodeWithText("11988887777").assertIsDisplayed()
        rule.onNodeWithText("Desconhecido").assertIsDisplayed()
    }

    @Test fun singularLabelForOneCall() {
        rule.setContent { AppTheme { BlockedListScreen(listOf(BlockedCall(1, "1", 1)), {}) } }
        rule.onNodeWithText("ligação barrada").assertIsDisplayed()
    }

    @Test fun backButtonInvokesCallback() {
        var backs = 0
        rule.setContent { AppTheme { BlockedListScreen(emptyList(), { backs++ }) } }
        rule.onNodeWithText("‹ Voltar").performClick()
        assertEquals(1, backs)
    }
}
