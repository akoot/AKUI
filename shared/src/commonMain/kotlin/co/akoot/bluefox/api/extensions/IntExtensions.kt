package co.akoot.bluefox.api.extensions

import co.akoot.bluefox.api.util.StringUtil

val Int.toRomanNumeral: String
    get() = StringUtil.toRomanNumeral(this) ?: toString()