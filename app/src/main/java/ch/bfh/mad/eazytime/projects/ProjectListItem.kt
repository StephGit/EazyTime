package ch.bfh.mad.eazytime.projects

data class ProjectListItem(
    val id: Long?,
    val name: String?,
    val shortCode: String?,
    val currentTime: Int?,
    val color: String?,
    val default: Boolean?,
    val onWidget: Boolean?,
    val active: Boolean
)