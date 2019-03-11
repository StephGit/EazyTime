package ch.bfh.mad.eazytime.projects

import org.joda.time.LocalDateTime

data class ProjectListItem(
    val id: Long?,
    val name: String?,
    val shortCode: String?,
    val previousTimeSeconds: Int?,
    val currentStartTime: LocalDateTime?,
    val color: String?,
    val default: Boolean?,
    val onWidget: Boolean?,
    val active: Boolean,
    val isDeleted: Boolean
)