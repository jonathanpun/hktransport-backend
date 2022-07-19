package com.jonathanpun.hktransport.solver

import com.jonathanpun.hktransport.meters
import com.jonathanpun.hktransport.repository.KMBRouteStop
import com.jonathanpun.hktransport.repository.KMBStop
import org.springframework.stereotype.Component
import kotlin.math.min

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
    fun dfs(sourceStop:String,destStop:String,maxRoute:Int): List<DfsPath>? {
        val targetNearSet = nearStopHastMap[destStop]
        val reachedSet = mutableSetOf<String>()
        val queue = ArrayDeque<DfsPath>()
        queue.add(DfsPath(sourceStop, emptyList()))
        var minRoute = Int.MAX_VALUE
        val ans = mutableListOf<DfsPath>()
        while (queue.size>0){
            val currentStop = queue.removeFirst()
            if (currentStop.pathList.size==min(maxRoute,minRoute))
                continue
            //all the route that reach from the current stop
            val availableRouteList = stopHashMap[currentStop.stop]!!.map {
                val routeList = routeHashMap[it.first]!!
                routeList.subList(it.second,routeList.size)}
            //
            val nexStops = availableRouteList.flatten().map {routeStops->SearchNode("route:${routeStops.route}-bound${routeStops.bound}-serviceType${routeStops.serviceType}",routeStops.route,routeStops.serviceType,routeStops.bound,
                currentStop.stop,routeStops.stop
            ) }
            for (nextStop in nexStops){
                val dfsPath = DfsPath(nextStop.endStop,currentStop.pathList.toMutableList().apply{  add(nextStop) })
                if (reachedSet.contains(dfsPath.stop))
                    continue
                if (dfsPath.stop == destStop){
                    ans.add(dfsPath)
                    minRoute = min(minRoute,dfsPath.pathList.size)
                }
                else{
                    reachedSet.add(dfsPath.stop)
                    queue.add(dfsPath)
                }
            }
        }
        return ans
    }


    private lateinit var nearStopHastMap : MutableMap<String,List<String>>
    val stopHashMap = mutableMapOf<String,MutableList<Pair<String,Int>>>()
    val routeHashMap = mutableMapOf<String,MutableList<KMBRouteStop>>()
}
data class DfsPath(val stop: String, val pathList: List<SearchNode>)
data class SearchNode(val id:String,val route:String, val serviceType:String, val bound:String, val startStop:String, val endStop:String)