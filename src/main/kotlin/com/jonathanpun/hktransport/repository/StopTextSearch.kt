package com.jonathanpun.hktransport.repository

import kotlinx.serialization.SerialName
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import javax.persistence.*

@Entity
@Table(name="stopTextSearch")
@kotlinx.serialization.Serializable
data class StopTextSearch(
    @Id
    @GeneratedValue()
    @Column(name = "id")
    val id:Int?,
    @Column(name = "name_en")
    @SerialName("name_en")
    var nameEn: String,
    @Column(name="name_sc")
    @SerialName("name_sc")
    var nameSc: String,
    @Column(name="name_tc")
    @SerialName("name_tc")
    var nameTc: String,
    @Convert(converter = StringListConverter::class)
    var stops:List<String>
)

@Converter
class StringListConverter : AttributeConverter<List<String>,String?>{
    override fun convertToDatabaseColumn(attribute: List<String>?): String? {
        return if (attribute==null)
            null
        else
            Json.encodeToString(serializer(),attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): List<String> {
        return if (dbData==null)
            emptyList()
        else
            try {
                Json.decodeFromString(dbData)
            }catch (e:java.lang.Exception){
                emptyList()
            }
    }

}