package com.jonathanpun.hktransport
import com.jonathanpun.hktransport.repository.KMBRoute
import com.jonathanpun.hktransport.repository.KMBRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping


@RestController
class Controller{
    @Autowired
    lateinit var repository: KMBRepository
    @GetMapping("/")
     suspend fun get(): List<KMBRoute>? {
        return repository.getAllRoute()
    }
}