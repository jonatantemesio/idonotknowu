package com.idonotknowu.callblocker

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BlockPolicyTest {
    private val known = setOf("11999990000")
    private val lookup: (String) -> Boolean = { it in known }

    private fun decide(
        enabled: Boolean = true,
        canRead: Boolean = true,
        number: String? = "11888880000",
    ) = BlockPolicy.shouldBlock(enabled, canRead, number, lookup)

    @Test fun blocksUnknownNumber() = assertTrue(decide())

    @Test fun allowsSavedContact() = assertFalse(decide(number = "11999990000"))

    @Test fun blocksHiddenNumber() = assertTrue(decide(number = null))

    @Test fun allowsEverythingWhenDisabled() {
        assertFalse(decide(enabled = false))
        assertFalse(decide(enabled = false, number = null))
    }

    @Test fun failsOpenWithoutContactsPermission() {
        assertFalse(decide(canRead = false))
        assertFalse(decide(canRead = false, number = null))
    }

    @Test fun doesNotQueryContactsForHiddenNumberOrWhenInactive() {
        var calls = 0
        val counting: (String) -> Boolean = { calls++; false }
        BlockPolicy.shouldBlock(true, true, null, counting)
        BlockPolicy.shouldBlock(false, true, "1", counting)
        BlockPolicy.shouldBlock(true, false, "1", counting)
        assertEquals(0, calls)
    }
}
