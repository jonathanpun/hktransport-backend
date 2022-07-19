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
    var lat: Double,
    @Column(name="`long`")
    var long: Double,
    @Column(name = "name_en")
    @SerialName("name_en")
    var nameEn: String,
    @Column(name="name_sc")
    @SerialName("name_sc")
    var nameSc: String,
    @Column(name="name_tc")
    @SerialName("name_tc")
    var nameTc: String,
    @Id
    var stop: String
)