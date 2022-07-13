package com.jonathanpun.hktransport.cron

import com.jonathanpun.hktransport.db.RouteRepository
import com.jonathanpun.hktransport.db.RouteStopsRepository
import com.jonathanpun.hktransport.db.StopsRepository
import com.jonathanpun.hktransport.meters
import com.jonathanpun.hktransport.repository.KMBRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.decodeFromString
import kotlinx.serialization.serializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.xml.bind.JAXBElement.GlobalScope
import kotlin.system.exitProcess

@Component
@Order(3)
class DistanceCron : CommandLineRunner {
    @Autowired
    lateinit var kmbRepository: KMBRepository

    @Autowired
    lateinit var stopsRepository: StopsRepository

    @Autowired
    lateinit var routeRepository: RouteRepository

    @Autowired
    lateinit var routeStopRepository: RouteStopsRepository
    override fun run(vararg args: String?) {
        if (args.firstOrNull() != "distance")
            return
        val routeStops = routeStopRepository.findAll().toTypedArray().groupBy {
            "${it.route} ${it.serviceType} ${it.bound}"
        }
        val stops = stopsRepository.findAll().associateBy { it.stop }
        val routes = routeRepository.findAll().toTypedArray()
        var count = 0
        for (route in routes) {
            count +=1
            val routeStop = routeStops["${route.route} ${route.serviceType} ${route.bound}"] ?: continue
            //val routeStop = routeStops["N283 1 O"] ?: continue

            val lineGeometry = route.lineGeometry ?: continue
            val path = decodeFromString<List<List<Double>>>(serializer(), lineGeometry)
            if (path.isEmpty())
                continue
            val array = IntArray(routeStop.maxOf { it.seq } ?: 0)
            //println(route)
            routeStop.forEach { kmbRouteStop ->
                val stop = stops[kmbRouteStop.stop]?:return@forEach
                var min = Double.MAX_VALUE
                var minIndex = Int.MAX_VALUE
                path.forEachIndexed { index, doubles ->
                    val distance = meters(stop.lat.toDouble(), stop.long.toDouble(), doubles[1], doubles[0])
                    if (distance < min) {
                        minIndex = index
                        min = distance
                    }
                }
                array[kmbRouteStop.seq - 1] = minIndex
            }
            for (i in 1..array.lastIndex) {
                val start = if (array[i - 1] <= array[i]) array[i - 1] else array[i]
                val end = if (array[i - 1] <= array[i]) array[i] else array[i - 1]
                //println("${route} $start $end")
                var distance = 0.0
                val geoString = Json.encodeToString(serializer(), path.subList(start, end + 1))
                for (j in 1..end) {
                    distance += meters(path[j - 1][1], path[j - 1][0], path[j][1], path[j][0])
                }
                routeStop.find { it.seq == i+1 }?.let {
                    routeStopRepository.save(
                        it.copy(
                            lineGeometry = geoString,
                            distance = distance.toInt()
                        )
                    )
                }
            }
        }

        exitProcess(0)
    }


}