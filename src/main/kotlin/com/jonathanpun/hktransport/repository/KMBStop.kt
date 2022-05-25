package com.jonathanpun.hktransport.repository

import kotlinx.serialization.SerialName
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="stops")
@kotlinx.serialization.Serializable
data class KMBStop(
    @Column(name = "lat")
    val lat: String,
    @Column(name="long")
    val long: String,
    @Column(name = "name_en")
    @SerialName("name_en")
    val nameEn: String,
    @Column(name="name_sc")
    @SerialName("name_sc")
    val nameSc: String,
    @Column(name="name_tc")
    @SerialName("name_tc")
    val nameTc: String,
    @Id
    val stop: String
)