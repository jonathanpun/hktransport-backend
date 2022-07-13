package com.jonathanpun.hktransport.db

import com.jonathanpun.hktransport.repository.KMBRouteStop
import com.jonathanpun.hktransport.repository.KMBRouteStopId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

@Component
interface RouteStopsRepository:JpaRepository<KMBRouteStop,KMBRouteStopId> {
    fun findByRouteAndBoundAndServiceType(route:String,bound:String,serviceType:String):List<KMBRouteStop>
}