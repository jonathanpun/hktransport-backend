package com.jonathanpun.hktransport.cron

import com.jonathanpun.hktransport.db.RouteRepository
import com.jonathanpun.hktransport.db.RouteStopsRepository
import com.jonathanpun.hktransport.db.StopTextSearchRepository
import com.jonathanpun.hktransport.db.StopsRepository
import com.jonathanpun.hktransport.solver.RouteSuggestionModel
import com.jonathanpun.hktransport.repository.KMBRouteStop
import com.jonathanpun.hktransport.repository.KMBStop
import com.jonathanpun.hktransport.repository.StopTextSearch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(2)
class Graph : CommandLineRunner {
    @Autowired
    lateinit var stopsRepository: StopsRepository

    @Autowired
    lateinit var routeRepository: RouteRepository

    @Autowired
    lateinit var routeStopRepository: RouteStopsRepository

    @Autowired
    lateinit var routeSuggestionModel :RouteSuggestionModel

    @Autowired
    lateinit var stopTextSearchRepository: StopTextSearchRepository
    override fun run(vararg args: String?) {
        if (args.isNotEmpty())
            return
        val stops = stopsRepository.findAll().toTypedArray()
        val routeStops = routeStopRepository.findAll().toTypedArray()
        routeSuggestionModel.init(stops,routeStops)
//        val map = mutableMapOf<String,MutableList<KMBStop>>()
//        for (stop in stops){
//            map.getOrPut(stop.nameTc) { mutableListOf<KMBStop>() }.add(stop)
//        }
//        map.forEach { t, u ->
//            stopTextSearchRepository.save(StopTextSearch(nameEn = u.first().nameEn, nameSc = u.first().nameSc, nameTc = u.first().nameTc, stops = u.map { it.stop },id=null))
//        }
    }


 /*   fun bfs() {
        val stops = stopsRepository.findAll()
        val hashMap = mutableMapOf<String, Node>()
        stops.forEach {
            hashMap[it.stop] = Node(it, mutableListOf())
        }
        val routeStops = routeStopRepository.findAll().toTypedArray()
        routeStops.sortWith(compareBy<KMBRouteStop?>({ it?.route }, { it?.bound }, { it?.serviceType }, { it?.seq }))
        for (i in 0 until routeStops.lastIndex) {
            val firstStop = routeStops[i]
            val secondStop = routeStops[i + 1]
            if (firstStop.route != secondStop.route || firstStop.bound != secondStop.bound || firstStop.serviceType != secondStop.serviceType) {
                continue
            }
            val firstNode = hashMap[firstStop.stop]
            val secondNode = hashMap[secondStop.stop]
            if (firstNode == null || secondNode == null)
                continue
            firstNode.next.add(Path(firstStop, secondNode))
        }
        //println(hashMap.size)
*
         *   {
        "lat": "22.373034",
        "long": "114.180291",
        "name_en": "TAI WAI STATION BUS TERMINUS",
        "name_sc": "大围站总站",
        "name_tc": "大圍站總站",
        "stop": "0390D5DEEEC05A7B"
        }
        {
        "lat": "22.371600",
        "long": "114.115525",
        "name_en": "TAI HO ROAD TSUEN WAN",
        "name_sc": "荃湾大河道",
        "name_tc": "荃灣大河道",
        "stop": "01642BC44DCFDB3C"
        }


        val sourceNode = hashMap["0390D5DEEEC05A7B"]!!
        val destNode = hashMap["01642BC44DCFDB3C"]!!
        //BFS
        val visited = mutableSetOf<String>()
        val q = ArrayDeque<NodeWithPath>()
        q.add(NodeWithPath(sourceNode, mutableListOf()))
        visited.add(sourceNode.stop.stop)
        var ans: NodeWithPath? = null
        while (q.size > 0) {
            val nodeWithPath = q.removeFirst()
            if (nodeWithPath.node == destNode) {
                ans = nodeWithPath
                q.clear()
            }
            nodeWithPath.node.next.forEach {
                if (visited.contains(it.node.stop.stop))
                    return@forEach
                else {
                    visited.add(it.node.stop.stop)
                    q.add(NodeWithPath(it.node, nodeWithPath.path.toMutableList().apply {
                        add(it)
                    }))
                }
            }
        }
        print(ans)
        //result : multiple route
    }

    fun dfs() {
        val stops = stopsRepository.findAll().toTypedArray()
        val routeStops = routeStopRepository.findAll().toTypedArray()
        val stopHashMap = mutableMapOf<String, MutableList<Pair<String, Int>>>()
        val routeHashMap = mutableMapOf<String, MutableList<KMBRouteStop>>()
        routeStops.sortWith(compareBy<KMBRouteStop?>({ it?.route }, { it?.bound }, { it?.serviceType }, { it?.seq }))
        var prevIdentifyString: String? = null
        var currentList: MutableList<KMBRouteStop> = mutableListOf()
        for (i in 0..routeStops.lastIndex) {
            val routeStop = routeStops[i]
            val routeIdentifyString =
                "route:${routeStop.route}-bound${routeStop.bound}-serviceType${routeStop.serviceType}"
            if (i == 0) {
                prevIdentifyString =
                    "route:${routeStop.route}-bound${routeStop.bound}-serviceType${routeStop.serviceType}"
                stopHashMap.getOrPut(routeStop.stop) { mutableListOf() }.add(prevIdentifyString to currentList.size)
                currentList.add(routeStop)
            } else if (routeIdentifyString == prevIdentifyString) {
                stopHashMap.getOrPut(routeStop.stop) { mutableListOf() }.add(prevIdentifyString to currentList.size)
                currentList.add(routeStop)
            } else {
                routeHashMap[prevIdentifyString.orEmpty()] = currentList
                prevIdentifyString = routeIdentifyString
                currentList = mutableListOf()
                stopHashMap.getOrPut(routeStop.stop) { mutableListOf() }.add(prevIdentifyString to currentList.size)
                currentList.add(routeStop)
            }
        }
        val nearStopHastMap = mutableMapOf<String, List<String>>()
        for (i in 0..stops.lastIndex) {
            val list = mutableListOf<String>()
            val current = stops[i]
            for (j in 0..stops.lastIndex) {
                val compare = stops[j]
                val dis = meters(
                    current.lat.toDouble(),
                    current.long.toDouble(),
                    compare.lat.toDouble(),
                    compare.long.toDouble()
                )
                if (dis < 500) {
                    list.add(compare.stop)
                }
            }
            nearStopHastMap[current.stop] = list
        }
        val sourceStop = "0390D5DEEEC05A7B"
        val destStop = "61D7306AC40C4FB8"
        val targetNearSet = nearStopHastMap[destStop]
        val reachedSet = mutableSetOf<String>()
        val queue = ArrayDeque<DfsPath>()
        queue.add(DfsPath(sourceStop, emptyList()))
        while (queue.size > 0) {
            val currentStop = queue.removeFirst()
            val availableRouteList = stopHashMap[currentStop.stop]!!.map {
                val routeList = routeHashMap[it.first]!!
                routeList.subList(it.second, routeList.size)
            }
            val nexStops = availableRouteList.flatten()
                .map { "route:${it.route}-bound${it.bound}-serviceType${it.serviceType}" to it.stop }
            val unreachedStop = nexStops.filter { !reachedSet.contains(it.second) }
                .map { DfsPath(it.second, currentStop.pathList.toMutableList().apply { add(it.first to currentStop.stop) }) }
            val target = unreachedStop.find { targetNearSet!!.contains(it.stop) }
            if (target != null) {
                queue.clear()
                print(target)
                break
            }
            queue.addAll(unreachedStop)
            reachedSet.addAll(unreachedStop)

        }
        println("Done")
    }

    fun meters(lt1: Double, ln1: Double, lt2: Double, ln2: Double): Double {
        val x = lt1 * d2r
        val y = lt2 * d2r
        return acos(sin(x) * sin(y) + cos(x) * cos(y) * cos(d2r * (ln1 - ln2))) * d2km
    }

    private val r2d = 180.0 / 3.141592653589793
    private val d2r = 3.141592653589793 / 180.0
    private val d2km = 111189.57696 * r2d
*/

}



data class NodeWithPath(val node: Node, val path: MutableList<Path>, val latestRoute: String? = null)

data class Node(val stop: KMBStop, val next: MutableList<Path>)

data class Path(val routeStop: KMBRouteStop, val node: Node)
