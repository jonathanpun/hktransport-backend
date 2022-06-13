package com.jonathanpun.hktransport.db

import com.jonathanpun.hktransport.repository.KMBRoute
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

@Component
interface RouteRepository:JpaRepository<KMBRoute,String>{
    fun findByRouteContaining(query: String,pageable:Pageable):List<KMBRoute>
    fun findByRouteAndBound(id:String,bound:String):List<KMBRoute>
    fun findByRouteAndBoundAndServiceType(id:String,bound:String,serviceType:String):KMBRoute

}