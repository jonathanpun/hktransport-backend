package com.jonathanpun.hktransport.repository
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class KMBGenericResponse<T>(
    val type:String,
    val version:String,
    @SerialName("generated_timestamp")
    val generatedTimestamp:String,
    val data:List<T>
)