package com.jonathanpun.hktransport.cron

import KMBRoutePath
import com.jonathanpun.hktransport.db.RouteRepository
import com.jonathanpun.hktransport.db.RouteStopsRepository
import com.jonathanpun.hktransport.db.StopsRepository
import com.jonathanpun.hktransport.repository.KMBRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.locationtech.proj4j.CRSFactory
import org.locationtech.proj4j.CoordinateReferenceSystem
import org.locationtech.proj4j.CoordinateTransformFactory
import org.locationtech.proj4j.ProjCoordinate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class CronJob:CommandLineRunner {
    @Autowired
    lateinit var kmbRepository: KMBRepository
    @Autowired
    lateinit var stopsRepository: StopsRepository
    @Autowired
    lateinit var routeRepository: RouteRepository
    @Autowired
    lateinit var routeStopRepository: RouteStopsRepository
    override fun run(vararg args: String?) {
        print("args:${args.joinToString()}")
        if(args.firstOrNull()!="cron")
         return
        GlobalScope.launch {
            val stops = kmbRepository.getAllStops()!!
            stopsRepository.saveAll(stops)
            val routes = kmbRepository.getAllRoute()!!.mapIndexed {i, it ->
                val webData = kmbRepository.getWebEta(it.route,it.bound,it.serviceType)
                val replacedString = webData?.data?.route?.lineGeometry?.replace("paths","\"paths\"")
                println("current: ${i}-- ${it}")
                val transformedString = replacedString?.let { transformCoordinate(it) }
                println(transformedString)
                routeRepository.save(it.copy(lineGeometry = transformedString))
            }
            //routeRepository.saveAll(routes)
            val routeStops = kmbRepository.getAllRouteStops()!!
            routeStopRepository.saveAll(routeStops)
            System.exit(0)
        }

    }

    private fun transformCoordinate(sourceString:String):String{
        val crsFactory = CRSFactory()
        val source = crsFactory.createFromName("EPSG:2326")
        val dest = crsFactory.createFromName("epsg:4326")
        val factory = CoordinateTransformFactory().createTransform(source, dest)
        val kmbRoutePath=Json.decodeFromString(KMBRoutePath.serializer(),sourceString)
        val resultCoordinate = ProjCoordinate()
        val transformedPath = kmbRoutePath.paths?.flatten()?.map {
            factory.transform(ProjCoordinate(it[0],it[1]),resultCoordinate)
            listOf(resultCoordinate.x,resultCoordinate.y)
        }
        return Json.encodeToString(ListSerializer(ListSerializer(Double.serializer())),transformedPath.orEmpty())
    }
}