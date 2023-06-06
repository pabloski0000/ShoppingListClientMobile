package main.shoppilientmobile

import com.rickclephas.kmp.nsexceptionkt.crashlytics.CausedByStrategy
import com.rickclephas.kmp.nsexceptionkt.crashlytics.setCrashlyticsUnhandledExceptionHook

fun setUpCrashlytics() {
    setCrashlyticsUnhandledExceptionHook(CausedByStrategy.APPEND)
}
