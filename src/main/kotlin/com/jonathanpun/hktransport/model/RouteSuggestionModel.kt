package com.jonathanpun.hktransport.model

import com.jonathanpun.hktransport.meters
import com.jonathanpun.hktransport.repository.KMBRoute
import com.jonathanpun.hktransport.repository.KMBRouteStop
import com.jonathanpun.hktransport.repository.KMBStop
import org.springframework.stereotype.Component
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

@Component
class RouteSuggestionModel {

    fun init(stops:Array<KMBStop>,routeStops:Array<KMBRouteStop>){
        routeStops.sortWith(compareBy<KMBRouteStop?> ({it?.route},{it?.bound},{it?.serviceType},{it?.seq}))
        var prevIdentifyString:String? = null
        var currentList:MutableList<KMBRouteStop> = mutableListOf()
        for ( i in 0..routeStops.lastIndex){
            val routeStop = routeStops[i]
            val routeIdentifyString = "route:${routeStop.route}-bound${routeStop.bound}-serviceType${routeStop.serviceType}"
            if (i==0){
                prevIdentifyString = "route:${routeStop.route}-bound${routeStop.bound}-serviceType${routeStop.serviceType}"
                stopHashMap.getOrPut(routeStop.stop) { mutableListOf() }.add( prevIdentifyString to currentList.size)
                currentList.add(routeStop)
            }else if(routeIdentifyString == prevIdentifyString){
                stopHashMap.getOrPut(routeStop.stop) { mutableListOf() }.add( prevIdentifyString to currentList.size)
                currentList.add(routeStop)
            }else{
                routeHashMap[prevIdentifyString.orEmpty()] = currentList
                prevIdentifyString = routeIdentifyString
                currentList = mutableListOf()
                stopHashMap.getOrPut(routeStop.stop) { mutableListOf() }.add( prevIdentifyString to currentList.size)
                currentList.add(routeStop)
            }
        }
         nearStopHastMap = mutableMapOf()
        for (i in 0..stops.lastIndex){
            val list = mutableListOf<String>()
            val current = stops[i]
            for (j in 0 .. stops.lastIndex){
                val compare = stops[j]
                val dis = meters(current.lat.toDouble(),current.long.toDouble(),compare.lat.toDouble(),compare.long.toDouble())
                if (dis<500){
                    list.add(compare.stop)
                }
            }
            nearStopHastMap[current.stop] = list
        }
    }
    fun dfs(sourceStop:String,destStop:String): DfsPath? {
        val targetNearSet = nearStopHastMap[destStop]
        val reachedSet = mutableSetOf<String>()
        val queue = ArrayDeque<DfsPath>()
        queue.add(DfsPath(sourceStop, emptyList()))
        while (queue.size>0){
            val currentStop = queue.removeFirst()
            //all the route that reach from the current stop
            val availableRouteList = stopHashMap[currentStop.stop]!!.map {
                val routeList = routeHashMap[it.first]!!
                routeList.subList(it.second,routeList.size)}
            //
            val nexStops = availableRouteList.flatten().map {routeStops->SearchNode("route:${routeStops.route}-bound${routeStops.bound}-serviceType${routeStops.serviceType}",routeStops.route,routeStops.serviceType,routeStops.bound,
                currentStop.stop,routeStops.stop
            ) }
            val unreachedStop = nexStops.filter { !reachedSet.contains(it.id) }.map { DfsPath(it.endStop,currentStop.pathList.toMutableList().apply{  add(it) }) }
            val target = unreachedStop.find { targetNearSet!!.contains(it.stop)  }
            if (target!= null){
                queue.clear()
                return target
            }
            queue.addAll(unreachedStop)
            reachedSet.addAll(unreachedStop.map { it.stop })
        }
        return null
    }


    private lateinit var nearStopHastMap : MutableMap<String,List<String>>
    val stopHashMap = mutableMapOf<String,MutableList<Pair<String,Int>>>()
    val routeHashMap = mutableMapOf<String,MutableList<KMBRouteStop>>()
}
data class DfsPath(val stop: String, val pathList: List<SearchNode>)
data class SearchNode(val id:String,val route:String, val serviceType:String, val bound:String, val startStop:String, val endStop:String)