package com.jonathanpun.hktransport.db

import com.jonathanpun.hktransport.repository.KMBRouteStop
import com.jonathanpun.hktransport.repository.KMBRouteStopId
import org.springframework.data.jpa.repository.JpaRepository

interface RouteStopsRepository:JpaRepository<KMBRouteStop,KMBRouteStopId> {
}