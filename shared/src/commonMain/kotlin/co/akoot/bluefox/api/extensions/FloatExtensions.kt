package co.akoot.bluefox.api.extensions

val Float.f: String get() = String.format("%.2f", this)
val Float.percent: String get() = String.format("%.2f", percent())
fun Float.percent(denominator: Float = 1f): Int = ((this / denominator) * 100).toInt()