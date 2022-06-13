package com.jonathanpun.hktransport
import com.jonathanpun.hktransport.db.RouteRepository
import com.jonathanpun.hktransport.db.StopsRepository
import com.jonathanpun.hktransport.repository.KMBRoute
import com.jonathanpun.hktransport.repository.KMBRepository
import com.jonathanpun.hktransport.repository.KMBStop
import com.jonathanpun.hktransport.repository.KMBStopETA
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
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
    @Autowired
    lateinit var routeRepository: RouteRepository
    @GetMapping("/")
     suspend fun get(): List<KMBRoute>? {
        return repository.getAllRoute()
    }
    @GetMapping("/stops-eta/{stopId}")
    suspend fun getStopEta(@PathVariable("stopId") stopId:String): List<KMBStopETA>? {
        return repository.getStopEta(stopId)
    }

    @GetMapping("/stops")
    suspend fun  searchStop(@RequestParam(name = "q") query:String): List<KMBStop> {
        println("-------------param---------------" + query)
       return stopsRepository.findByNameEnContainingOrNameScContainingOrNameTcContaining(query,query,query)
    }
    @GetMapping("/stop/{stopId}")
    suspend fun getStop(@PathVariable("stopId") stopId: String): KMBStop{
        return stopsRepository.getById(stopId)
    }
    @GetMapping("/routes")
    suspend fun searchRoutes(@RequestParam(name = "q") query:String,@RequestParam(name = "limit") limit:Int?):List<KMBRoute>{
        return routeRepository.findByRouteContaining(query,if (limit==null) Pageable.unpaged() else Pageable.ofSize(limit))
    }

    @GetMapping("/routes/{routeId}/{bound}")
    suspend fun getRouteWithBound(@PathVariable(name= "routeId") routeId:String,@PathVariable(name = "bound") bound:String):List<KMBRoute>{
        return routeRepository.findByRouteAndBound(routeId,bound)
    }
    @GetMapping("/routes/{routeId}/{bound}/{serviceType}")
    suspend fun getRouteWithBoundAndServiceType(@PathVariable(name= "routeId") routeId:String,@PathVariable(name = "bound") bound:String,@PathVariable(name ="serviceType")serviceType:String):KMBRoute{
        return routeRepository.findByRouteAndBoundAndServiceType(routeId,bound,serviceType)
    }
}