package ch.bfh.mad.eazytime.projects.addProject

import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as Is


class OnWidgetSwitchEnableUtilTest {

    @Test
    fun calculateDisplayableOnWidgetCount_NoInDb_NotInitial_NotChecked() {
        val countInDB = 0
        val currentSwitchState = false
        val projectWasInitialOnWidget = false
        assertThat(OnWidgetDisplayAmountUtil().calculateDisplayableOnWidgetCount(countInDB, currentSwitchState, projectWasInitialOnWidget), Is(0))
    }

    @Test
    fun calculateDisplayableOnWidgetCount_3InDb_NotInitial_NotChecked() {
        val countInDB = 3
        val currentSwitchState = false
        val projectWasInitialOnWidget = false
        assertThat(OnWidgetDisplayAmountUtil().calculateDisplayableOnWidgetCount(countInDB, currentSwitchState, projectWasInitialOnWidget), Is(3))
    }

    @Test
    fun calculateDisplayableOnWidgetCount_3InDb_wasInitial_NotChecked() {
        val countInDB = 3
        val currentSwitchState = false
        val projectWasInitialOnWidget = true
        assertThat(OnWidgetDisplayAmountUtil().calculateDisplayableOnWidgetCount(countInDB, currentSwitchState, projectWasInitialOnWidget), Is(2))
    }

    @Test
    fun calculateDisplayableOnWidgetCount_3InDb_notInitial_Checked() {
        val countInDB = 3
        val currentSwitchState = true
        val projectWasInitialOnWidget = false
        assertThat(OnWidgetDisplayAmountUtil().calculateDisplayableOnWidgetCount(countInDB, currentSwitchState, projectWasInitialOnWidget), Is(4))
    }

    @Test
    fun calculateDisplayableOnWidgetCount_3InDb_wasInitial_Checked() {
        val countInDB = 3
        val currentSwitchState = true
        val projectWasInitialOnWidget = true
        assertThat(OnWidgetDisplayAmountUtil().calculateDisplayableOnWidgetCount(countInDB, currentSwitchState, projectWasInitialOnWidget), Is(3))
    }

}