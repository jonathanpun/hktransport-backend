package com.jonathanpun.hktransport.repository
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "route_stops")
@Serializable
@IdClass(KMBRouteStopId::class)
data class KMBRouteStop(
    @Id
    @Column(name = "bound")
    @SerialName("bound")
    val bound: String,
    @Id
    @Column(name = "route")
    @SerialName("route")
    val route: String,
    @Id
    @Column(name = "seq")
    @SerialName("seq")
    val seq: Int,
    @Column(name = "serviceType")
    @SerialName("service_type")
    @Id
    val serviceType: String,
    @Column(name = "stop")
    @SerialName("stop")
    val stop: String
)

@Embeddable
data class KMBRouteStopId(
    val bound:String,
    val route:String,
    val seq:Int,
    val serviceType: String
):java.io.Serializable