package com.jonathanpun.hktransport.cron

import com.jonathanpun.hktransport.db.StopsRepository
import com.jonathanpun.hktransport.repository.KMBRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class CronJob:CommandLineRunner {
    @Autowired
    lateinit var kmbRepository: KMBRepository
    @Autowired
    lateinit var stopsRepository: StopsRepository
    override fun run(vararg args: String?) {
        print("args:${args.joinToString()}")
        if(args.firstOrNull()!="cron")
         return
        GlobalScope.launch {
            val stops = kmbRepository.getAllStops()!!
            print("stop----")
            stopsRepository.saveAll(stops)
        }

    }
}