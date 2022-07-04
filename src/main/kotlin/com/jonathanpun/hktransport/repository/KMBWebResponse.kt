
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName
@Serializable
data class KMBWebResponse(
    @SerialName("data")
    val `data`: Data?,
    @SerialName("result")
    val result: Boolean?
)

@Serializable
data class Data(
    @SerialName("additionalInfo")
    val additionalInfo: AdditionalInfo? = null,
    @SerialName("basicInfo")
    val basicInfo: BasicInfo? = null,
    @SerialName("route")
    val route: Route? = null,
    @SerialName("routeStops")
    val routeStops: List<RouteStop>? = null
)

@Serializable
data class AdditionalInfo(
    @SerialName("ENG")
    val eNG: String?,
    @SerialName("SC")
    val sC: String?,
    @SerialName("TC")
    val tC: String?
)

@Serializable
data class BasicInfo(
    @SerialName("Airport")
    val airport: String?,
    @SerialName("BusType")
    val busType: String?,
    @SerialName("DestCName")
    val destCName: String?,
    @SerialName("DestEName")
    val destEName: String?,
    @SerialName("DestSCName")
    val destSCName: String?,
    @SerialName("OriCName")
    val oriCName: String?,
    @SerialName("OriEName")
    val oriEName: String?,
    @SerialName("OriSCName")
    val oriSCName: String?,
    @SerialName("Overnight")
    val overnight: String?,
    @SerialName("Racecourse")
    val racecourse: String?,
    @SerialName("ServiceTypeENG")
    val serviceTypeENG: String?,
    @SerialName("ServiceTypeSC")
    val serviceTypeSC: String?,
    @SerialName("ServiceTypeTC")
    val serviceTypeTC: String?,
    @SerialName("Special")
    val special: String?
)

@Serializable
data class Route(
    @SerialName("bound")
    val bound: Int?,
    @SerialName("lineGeometry")
    val lineGeometry: String?,
    @SerialName("route")
    val route: String?,
    @SerialName("serviceType")
    val serviceType: Int?
)

@Serializable
data class RouteStop(
    @SerialName("AirFare")
    val airFare: String?,
    @SerialName("BSICode")
    val bSICode: String?,
    @SerialName("Bound")
    val bound: String?,
    @SerialName("CLocation")
    val cLocation: String?,
    @SerialName("CName")
    val cName: String?,
    @SerialName("Direction")
    val direction: String?,
    @SerialName("ELocation")
    val eLocation: String?,
    @SerialName("EName")
    val eName: String?,
    @SerialName("Route")
    val route: String?,
    @SerialName("SCLocation")
    val sCLocation: String?,
    @SerialName("SCName")
    val sCName: String?,
    @SerialName("Seq")
    val seq: String?,
    @SerialName("ServiceType")
    val serviceType: String?,
    @SerialName("X")
    val x: String?,
    @SerialName("Y")
    val y: String?
)

@Serializable
data class KMBRoutePath(
    @SerialName("paths")
    val paths: List<List<List<Double>>>?
)