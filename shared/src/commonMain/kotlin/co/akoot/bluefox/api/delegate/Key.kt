package co.akoot.bluefox.api.delegate

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Key(val namespace: String, val path: String)