package com.jonathanpun.hktransport

import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

private val r2d = 180.0 / 3.141592653589793
private val d2r = 3.141592653589793 / 180.0
private val d2km = 111189.57696 * r2d

fun meters(lt1: Double, ln1: Double, lt2: Double, ln2: Double): Double {
    val x = lt1 * d2r
    val y = lt2 * d2r
    return acos(sin(x) * sin(y) + cos(x) * cos(y) * cos(d2r * (ln1 - ln2))) * d2km
}