package ch.bfh.mad.eazytime.projects

data class ProjectListItem(
    val name: String?,
    val shortCode: String?,
    val currentTime: Int?,
    val color: String?,
    val default: Boolean?,
    val active: Boolean
)