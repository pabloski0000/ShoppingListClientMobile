package main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository

class ObjectToKeep (
    val type: Class<*>,
    val key: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ObjectToKeep) return false

        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }
}