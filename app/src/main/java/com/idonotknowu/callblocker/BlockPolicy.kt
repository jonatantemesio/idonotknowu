package com.idonotknowu.callblocker

object BlockPolicy {
    /**
     * Fail-open: inactive or unable to read contacts -> allow. A hidden (null) number is not
     * in the contacts, so it is blocked.
     */
    fun shouldBlock(
        enabled: Boolean,
        canReadContacts: Boolean,
        number: String?,
        isInContacts: (String) -> Boolean,
    ): Boolean = enabled && canReadContacts && (number == null || !isInContacts(number))
}
