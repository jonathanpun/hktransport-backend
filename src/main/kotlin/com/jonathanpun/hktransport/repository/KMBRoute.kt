package com.jonathanpun.hktransport.repository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "route")
@Serializable
data class KMBRoute(
    @Id
    val route:String,
    @Column(name="bound")
    @SerialName("bound")
    val bound: String,
    @Column(name="service_type")
    @SerialName("service_type")
    val serviceType:String,
    @SerialName("orig_en")
    @Column(name="orig_en")
    val origEn:String,
    @SerialName("orig_tc")
    @Column(name="orig_tc")
    val origTc:String,
    @SerialName("orig_sc")
    @Column(name="orig_sc")
    val origSc:String,
    @SerialName("dest_en")
    @Column(name="dest_en")
    val destEn:String,
    @SerialName("dest_tc")
    @Column(name="dest_tc")
    val destTc:String,
    @SerialName("dest_sc")
    @Column(name="dest_sc")
    val destSc:String,
)