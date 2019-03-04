package ch.bfh.mad.eazytime

interface EazyTimeNavigator {

    fun openAddProjectActivity()

    fun openUpdateProjectActivity(projectId: Long)

    fun openCalendarDetailActivity(workDayId: Long)
}