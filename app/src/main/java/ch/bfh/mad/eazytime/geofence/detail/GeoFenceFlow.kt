package ch.bfh.mad.eazytime.geofence.detail

interface GeoFenceFlow {
    fun goToMarker()
    fun goToRadius()
    fun goToDetail()
    fun deleteGeoFence()
    fun saveGeoFence()
    fun leaveGeoFenceDetail()
}