package com.jonathanpun.hktransport

import org.springframework.web.reactive.function.client.WebClient.ResponseSpec
import java.util.function.IntPredicate
import java.util.function.Predicate


class APIException():Exception()


fun ResponseSpec.handleError(){
    //this.onStatus(Predicate { t-> t.isError},{pr})
}