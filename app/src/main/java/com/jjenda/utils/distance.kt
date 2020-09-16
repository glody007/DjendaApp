package com.jjenda.utils

import android.location.Location
import android.util.Log

abstract class MeansOfTransport {
    abstract val averageSpeed: Float
    abstract val maximumDistance: Float
    abstract val suffix: String

    fun travellingTime(distance: Float) = distance / averageSpeed

    fun travellingTimeWithUnit(distance: Float): String {
        val time = travellingTime(distance)
        return if(time >= 1) "${time.toInt()} heures" else "${(time * 60).toInt()} minutes"
    }

    fun travellingTimeString(distance: Float): String {
        return "A ${travellingTimeWithUnit(distance)} de chez vous $suffix"
    }
}



object Human: MeansOfTransport() {
    override val averageSpeed: Float = 7F
    override val maximumDistance: Float = 2F
    override val suffix: String = "a pied"
}

object Car: MeansOfTransport() {
    override val averageSpeed: Float = 50F
    override val maximumDistance: Float = 100F
    override val suffix: String = "en voiture"
}

object Plane: MeansOfTransport() {
    override val averageSpeed: Float = 700F
    override val maximumDistance: Float = 40000F
    override val suffix: String = "en avion"
}

class LocationDistance(locationA: Location, locationB: Location){
    private var distance: Float = locationA.distanceTo(locationB) / 1000

    fun timeFromAToBWithBestMeansOfTransportString(): String {
        return when {
            distance <= Human.maximumDistance -> Human.travellingTimeString(distance)
            distance <= Car.maximumDistance -> Car.travellingTimeString(distance)
            else -> Plane.travellingTimeString(distance)
        }
    }

}