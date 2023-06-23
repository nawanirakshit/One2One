package android.sleek.construction.utils

object InternalDeepLink {
    const val DOMAIN = "SleekConstruction://"

    const val BILLS = "${DOMAIN}bills"
    const val CREW_SCHEDULING = "${DOMAIN}crew-scheduling"
    const val DAILY_LOGS = "${DOMAIN}daily-logs"
    const val INCIDENTS = "${DOMAIN}incidents"
    const val TO_DO = "${DOMAIN}to-do"
    const val TIME_CARD = "${DOMAIN}time-card"

    fun makeCustomDeepLink(id: String): String {
        return "${DOMAIN}customDeepLink?id=${id}"
    }
}