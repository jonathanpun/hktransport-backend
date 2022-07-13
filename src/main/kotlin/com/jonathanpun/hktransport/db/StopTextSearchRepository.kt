package com.jonathanpun.hktransport.db

import com.jonathanpun.hktransport.repository.StopTextSearch
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

@Component
interface StopTextSearchRepository:JpaRepository<StopTextSearch,String> {
}