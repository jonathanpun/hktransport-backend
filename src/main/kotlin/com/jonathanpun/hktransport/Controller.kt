package com.jonathanpun.hktransport
import com.jonathanpun.hktransport.db.StopsRepository
import com.jonathanpun.hktransport.repository.KMBRoute
import com.jonathanpun.hktransport.repository.KMBRepository
import com.jonathanpun.hktransport.repository.KMBStop
import com.jonathanpun.hktransport.repository.KMBStopETA
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam


@RestController
class Controller{
    @Autowired
    lateinit var repository: KMBRepository
    @Autowired
    lateinit var stopsRepository: StopsRepository
    @GetMapping("/")
     suspend fun get(): List<KMBRoute>? {
        return repository.getAllRoute()
    }
    @GetMapping("/stops-eta/{stopId}")
    suspend fun getStopEta(@PathVariable("stopId") stopId:String): List<KMBStopETA>? {
        return repository.getStopEta(stopId)
    }

    @GetMapping("/stops")
    suspend fun  getStop(@RequestParam(name = "q") query:String): List<KMBStop> {
        println("-------------param---------------" + query)
       return stopsRepository.findByNameEnContainingOrNameScContainingOrNameTcContaining(query,query,query)
    }
}