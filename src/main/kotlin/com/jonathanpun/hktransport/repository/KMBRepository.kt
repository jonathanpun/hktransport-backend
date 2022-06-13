package com.jonathanpun.hktransport.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull

@Service
class KMBRepository {
    val webClient = WebClient.builder().codecs {
        it.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)
    }.baseUrl("https://data.etabus.gov.hk").build()

    suspend fun getAllRoute(): List<KMBRoute>? {
        return webClient.get().uri("/v1/transport/kmb/route/")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .awaitBodyOrNull<KMBGenericResponse<KMBRoute>>()?.data
    }
    suspend fun getAllStops():List<KMBStop>?{
        return  webClient.get().uri("/v1/transport/kmb/stop")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .awaitBodyOrNull<KMBGenericResponse<KMBStop>>()?.data
    }

    suspend fun getStopEta(stopId:String):List<KMBStopETA>?{
        return webClient.get().uri("/v1/transport/kmb/stop-eta/${stopId}")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .awaitBodyOrNull<KMBGenericResponse<KMBStopETA>>()?.data
    }

    suspend fun getAllRouteStops():List<KMBRouteStop>?{
        return webClient.get().uri("/v1/transport/kmb/route-stop")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .awaitBodyOrNull<KMBGenericResponse<KMBRouteStop>>()?.data
    }
}