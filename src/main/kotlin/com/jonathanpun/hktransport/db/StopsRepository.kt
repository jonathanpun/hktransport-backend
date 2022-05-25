package com.jonathanpun.hktransport.db

import com.jonathanpun.hktransport.repository.KMBStop
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component

@Component
interface StopsRepository:CrudRepository<KMBStop,String> {}