package com.jonathanpun.hktransport.repository

import KMBWebResponse
import io.netty.channel.ChannelOption
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserter
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import org.springframework.web.reactive.function.client.body
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Service
class KMBRepository {
    private final val httpClient = HttpClient.create()
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,10000)
        .responseTimeout(Duration.ofSeconds(5))
    val apiClient = WebClient.builder().clientConnector(ReactorClientHttpConnector(httpClient))
        .codecs {
        it.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)
    }.baseUrl("https://data.etabus.gov.hk").build()
    val webClient = WebClient.builder().clientConnector(ReactorClientHttpConnector(httpClient))
        .codecs {
        it.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)
    }.baseUrl("https://search.kmb.hk").build()

    suspend fun getAllRoute(): List<KMBRoute>? {
        return apiClient.get().uri("/v1/transport/kmb/route/")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .awaitBodyOrNull<KMBGenericResponse<KMBRoute>>()?.data
    }
    suspend fun getAllStops():List<KMBStop>?{
        return  apiClient.get().uri("/v1/transport/kmb/stop")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .awaitBodyOrNull<KMBGenericResponse<KMBStop>>()?.data
    }

    suspend fun getStopEta(stopId:String):List<KMBStopETA>?{
        return apiClient.get().uri("/v1/transport/kmb/stop-eta/${stopId}")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .awaitBodyOrNull<KMBGenericResponse<KMBStopETA>>()?.data
    }

    suspend fun getAllRouteStops():List<KMBRouteStop>?{
        return apiClient.get().uri("/v1/transport/kmb/route-stop")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .awaitBodyOrNull<KMBGenericResponse<KMBRouteStop>>()?.data
    }

    suspend fun getRouteEta(route:String,serviceType:String):List<KMBStopETA>?{
        return apiClient.get().uri("/v1/transport/kmb/route-eta/${route}/${serviceType}")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .awaitBodyOrNull<KMBGenericResponse<KMBStopETA>>()?.data
    }

    suspend fun  getWebEta(route:String,bound:String,serviceType: String): KMBWebResponse? {
        return webClient.post().uri { uriBuilder->uriBuilder.path("/KMBWebSite/Function/FunctionRequest.ashx")
            .queryParam("action","getstops")
            .queryParam("route",route)
            .queryParam("bound",if (bound.lowercase()=="o") "1" else "2")
            .queryParam("serviceType",serviceType).build()}
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .awaitBodyOrNull<KMBWebResponse>()
    }
}