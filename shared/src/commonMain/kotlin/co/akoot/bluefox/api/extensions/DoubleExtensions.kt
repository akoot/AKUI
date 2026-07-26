package co.akoot.bluefox.api.extensions

val Double.f: String get() = String.format("%.2f", this)
val Double.percent: String get() = String.format("%.2f", percent())
fun Double.percent(denominator: Double = 1.0): Int = ((this / denominator) * 100).toInt()
