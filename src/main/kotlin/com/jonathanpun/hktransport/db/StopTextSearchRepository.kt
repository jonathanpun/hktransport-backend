package com.jonathanpun.hktransport.db

import com.jonathanpun.hktransport.repository.KMBStop
import com.jonathanpun.hktransport.repository.StopTextSearch
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

@Component
interface StopTextSearchRepository:JpaRepository<StopTextSearch,Int> {
    fun findByNameEnContainingOrNameScContainingOrNameTcContaining(nameEn:String,nameSc:String,nameTc:String,pageable: Pageable):List<StopTextSearch>
}