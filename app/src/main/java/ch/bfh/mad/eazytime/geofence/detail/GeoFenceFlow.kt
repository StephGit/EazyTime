package ch.bfh.mad.eazytime.geofence.detail

interface GeoFenceFlow {

    enum class Step {
        MARKER, RADIUS, EDIT, DETAIL;

        companion object {
            fun getStepByName(name: String) = valueOf(name.toUpperCase())
        }
    }

    fun setStep(step: GeoFenceFlow.Step)
    fun getFenceName(): String
    fun goToMarker()
    fun goToRadius()
    fun goToEdit()
    fun saveGeoFence(geoFenceName: String)
    fun leaveGeoFenceDetail()
}