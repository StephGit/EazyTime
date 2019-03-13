package ch.bfh.mad.eazytime.geofence.detail

interface GeoFenceFlow {

    enum class Step {
        MARKER, RADIUS, EDIT;

        companion object {
            fun getStepByName(name: String) = valueOf(name.toUpperCase())
        }
    }

    fun setStep(step: GeoFenceFlow.Step)
    fun getFenceName(): String
    fun goBack()
    fun goToMarker()
    fun goToRadius()
    fun goToEdit()
    fun saveOrUpdate(geoFenceName: String)
    fun leaveGeoFenceDetail()
}