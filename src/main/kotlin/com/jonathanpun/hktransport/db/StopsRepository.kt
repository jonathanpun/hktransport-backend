package com.jonathanpun.hktransport.db

import com.jonathanpun.hktransport.repository.KMBStop
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component

@Component
interface StopsRepository:JpaRepository<KMBStop,String> {
    fun findByNameEnContainingOrNameScContainingOrNameTcContaining(nameEn:String,nameSc:String,nameTc:String):List<KMBStop>
}