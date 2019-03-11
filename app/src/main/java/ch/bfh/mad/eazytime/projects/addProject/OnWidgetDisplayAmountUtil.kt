package ch.bfh.mad.eazytime.projects.addProject

class OnWidgetDisplayAmountUtil {

    fun calculateDisplayableOnWidgetCount(onWidgetCountInDB: Int, currentSwitchState: Boolean, projectWasInitialOnWidget: Boolean): Int {
        var count = onWidgetCountInDB
        if (currentSwitchState && !projectWasInitialOnWidget) {
            count++
        } else if (!currentSwitchState && projectWasInitialOnWidget) {
            count--
        }
        return count
    }
}