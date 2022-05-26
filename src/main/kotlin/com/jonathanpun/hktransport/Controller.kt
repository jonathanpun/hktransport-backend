package com.jonathanpun.hktransport
import com.jonathanpun.hktransport.repository.KMBRoute
import com.jonathanpun.hktransport.repository.KMBRepository
import com.jonathanpun.hktransport.repository.KMBStopETA
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


@RestController
class Controller{
    @Autowired
    lateinit var repository: KMBRepository
    @GetMapping("/")
     suspend fun get(): List<KMBRoute>? {
        return repository.getAllRoute()
    }
    @GetMapping("/stops-eta/{stopId}")
    suspend fun get(@PathVariable("stopId") stopId:String): List<KMBStopETA>? {
        return repository.getStopEta(stopId)
    }
}