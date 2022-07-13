package com.jonathanpun.hktransport.repository
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName


class LocationResponse : ArrayList<LocationResponseItem>()

@Serializable
data class LocationResponseItem(
    @SerialName("addressEN")
    val addressEN: String?,
    @SerialName("addressZH")
    val addressZH: String?,
    @SerialName("nameEN")
    val nameEN: String?,
    @SerialName("nameZH")
    val nameZH: String?,
    @SerialName("x")
    val x: Double?,
    @SerialName("y")
    val y: Double?
)