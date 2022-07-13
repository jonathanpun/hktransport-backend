package com.jonathanpun.hktransport.repository

import io.netty.channel.ChannelOption
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Service
class LocationRepository {
private final val httpClient = HttpClient.create()
    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,10000)
    .responseTimeout(Duration.ofSeconds(5))
val apiClient = WebClient.builder().clientConnector(ReactorClientHttpConnector(httpClient))
    .codecs {
        it.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)
    }.baseUrl("https://geodata.gov.hk").build()

    suspend fun searchLocation(q:String): List<LocationResponse>? {
        return apiClient.get().uri("/gs/api/v1.0.0/locationSearch?q=${q}")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .awaitBodyOrNull<KMBGenericResponse<LocationResponse>>()?.data
    }
}