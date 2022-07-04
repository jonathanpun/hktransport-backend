package com.jonathanpun.hktransport.repository

import com.vladmihalcea.hibernate.type.json.JsonType
import kotlinx.serialization.*
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table


@Entity
@Table(name = "route")
@Serializable
@IdClass(KMBRouteId::class)
@TypeDef(
    name = "json",
    typeClass = JsonType::class
    )
data class KMBRoute(
    @Id
    val route:String,
    @Id
    @SerialName("bound")
    val bound: String,
    @Id
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
    @SerialName("line_geometry")
    @Type(type = "json")
    @Column(name = "line_geometry", columnDefinition = "json")
    @Serializable(with = RawJsonSerializer::class)
    val lineGeometry:String? = null
)
@Embeddable
@Serializable
data class KMBRouteId(
    val route:String,
    val bound: String,
    val serviceType:String,
    ):java.io.Serializable

//work around for json property
object RawJsonSerializer : JsonTransformingSerializer<String>(String.serializer()) {
    override fun transformSerialize(element: JsonElement): JsonElement {
        val string = Json.decodeFromString<String>((element as JsonPrimitive).toString())
        return Json.decodeFromString(string)
    }
}