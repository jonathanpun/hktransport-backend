package com.jonathanpun.hktransport.repository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.persistence.Entity

@Serializable
data class KMBRoute(
    val route:String,
    val bound: String,
    @SerialName("service_type")
    val serviceType:String,
    @SerialName("orig_en")
    val origEn:String,
    @SerialName("orig_tc")
    val origTc:String,
    @SerialName("orig_sc")
    val origSc:String,
    @SerialName("dest_en")
    val destEn:String,
    @SerialName("dest_tc")
    val destTc:String,
    @SerialName("dest_sc")
    val destSc:String,
)