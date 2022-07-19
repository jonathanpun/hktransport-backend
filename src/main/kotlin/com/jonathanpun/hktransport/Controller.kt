package com.jonathanpun.hktransport
import com.jonathanpun.hktransport.db.RouteRepository
import com.jonathanpun.hktransport.db.RouteStopsRepository
import com.jonathanpun.hktransport.db.StopTextSearchRepository
import com.jonathanpun.hktransport.db.StopsRepository
import com.jonathanpun.hktransport.solver.RouteSuggestionModel
import com.jonathanpun.hktransport.repository.*
import com.jonathanpun.hktransport.solver.DfsPath
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
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
    @Autowired
    lateinit var routeStopsRepository: RouteStopsRepository
    @Autowired
    lateinit var routeSuggestionModel: RouteSuggestionModel
    @Autowired
    lateinit var stopTextSearchRepository: StopTextSearchRepository
    @GetMapping("/")
     suspend fun get(): List<KMBRoute>? {
        return repository.getAllRoute()
    }
    @GetMapping("/stops-eta/{stopId}")
    suspend fun getStopEta(@PathVariable("stopId") stopId:String): List<KMBStopETA>? {
        return repository.getStopEta(stopId)
    }

    @GetMapping("/stops")
    suspend fun  searchStop(@RequestParam(name = "q") query:String,@RequestParam("limit")limit: Int?): List<StopTextSearch> {
        println("-------------param---------------" + query)
       return stopTextSearchRepository.findByNameEnContainingOrNameScContainingOrNameTcContaining(query,query,query,Pageable.ofSize(limit?:10))
    }
    @GetMapping("/stop/{stopId}")
    suspend fun getStop(@PathVariable("stopId") stopId: String): KMBStop{
        return stopsRepository.getById(stopId)
    }

    @GetMapping("/stop/near-stop")
    suspend fun getNearStop(@RequestParam("lat") lat:Double,@RequestParam("lng")lng:Double): List<KMBStop> {
        val threshold = 0.001
        return stopsRepository.findByLatBetweenAndLongBetween(lat-threshold,lat+threshold,lng-threshold,lng+threshold)
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

    @GetMapping("/route-stops/{route}/{bound}/{serviceType}")
    suspend fun getRouteStop(@PathVariable(name = "route") route:String,@PathVariable(name = "bound") bound:String,@PathVariable(name ="serviceType")serviceType:String):List<KMBStop>{
        return routeStopsRepository.findByRouteAndBoundAndServiceType(route, bound, serviceType).sortedBy { it.seq }.map {
            stopsRepository.getById(it.stop)
        }
    }
    @GetMapping("/route-eta/{route}/{serviceType}")
    suspend fun getRouteEta(@PathVariable(name = "route") route:String,@PathVariable(name ="serviceType")serviceType:String): List<KMBStopETA>? {
        return repository.getRouteEta(route, serviceType)
    }

    @GetMapping("/stop-text-search/{id}")
    suspend  fun getStopTextSearch(@PathVariable(name = "id")id:Int):StopTextSearch?{
        return stopTextSearchRepository.getById(id)
    }

    @GetMapping("/route-query")
    suspend fun getRoute(@RequestParam("sourceStop")sourceStopGroupId:Int, @RequestParam("destStop")destStopGroupId:Int): List<ResponseModel>? {
        val sourceStops =  stopTextSearchRepository.getById(sourceStopGroupId).stops
        val destStops = stopTextSearchRepository.getById(destStopGroupId).stops
        val dfsPath = mutableListOf<DfsPath>()
        val max = 2
        for (sourceStop in sourceStops){
            for(destStop in destStops){
                val dfs = routeSuggestionModel.dfs(sourceStop,destStop,max)?:continue
                dfsPath.addAll(dfs)
            }
        }
        dfsPath.sortBy { it.pathList.size }
        return dfsPath.take(3).map {
            val responseRouteStops = mutableListOf<ResponseModel.RouteStop>()
            it.pathList.map {
                val route = routeRepository.getById(KMBRouteId(it.route,it.bound,it.serviceType))
                val routeStops = routeStopsRepository.findByRouteAndBoundAndServiceType(it.route,it.bound,it.serviceType).sortedBy { it.seq }
                val stopIds= mutableListOf<String>()
                val iterator = routeStops.iterator()
                val pathList = mutableListOf<List<Double>>()
                var stop:KMBRouteStop
                do {
                    stop = iterator.next()
                }while (stop.stop!=it.startStop)
                do {
                    stopIds.add(stop.stop)
                    try {
                        pathList.addAll(Json.decodeFromString<List<List<Double>>>(serializer(),stop.lineGeometry.orEmpty()))
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                    stop = iterator.next()
                }while (stop.stop!= it.endStop)
                stopIds.add(it.endStop)
                try {
                    pathList.addAll(Json.decodeFromString<List<List<Double>>>(serializer(),stop.lineGeometry.orEmpty()))
                }catch (e:Exception){
                    e.printStackTrace()
                }
                responseRouteStops.add(
                    ResponseModel.RouteStop(
                    route,
                    stopIds.map {
                        stopsRepository.getById(it)
                    },
                        pathList
                ))
            }
            ResponseModel(responseRouteStops)
        }


//        if (path==null)
//            return null
//        else{
//            val responseRouteStops = mutableListOf<ResponseModel.RouteStop>()
//
//        }
    }
}

@Serializable
data class ResponseModel(val routeStops:List<RouteStop>){
    @Serializable
    data class RouteStop(val route: KMBRoute,val stops:List<KMBStop>,val pathList:List<List<Double>>)
}