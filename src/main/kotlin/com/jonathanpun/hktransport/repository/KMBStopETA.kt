package com.jonathanpun.hktransport.repository
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName

@Serializable
data class KMBStopETA(
    @SerialName("co")
    val co: String,
    @SerialName("data_timestamp")
    val dataTimestamp: String,
    @SerialName("dest_en")
    val destEn: String,
    @SerialName("dest_sc")
    val destSc: String,
    @SerialName("dest_tc")
    val destTc: String,
    @SerialName("dir")
    val dir: String,
    @SerialName("eta")
    val eta: String?,
    @SerialName("eta_seq")
    val etaSeq: Int,
    @SerialName("rmk_en")
    val rmkEn: String,
    @SerialName("rmk_sc")
    val rmkSc: String,
    @SerialName("rmk_tc")
    val rmkTc: String,
    @SerialName("route")
    val route: String,
    @SerialName("seq")
    val seq: Int,
    @SerialName("service_type")
    val serviceType: Int
)
