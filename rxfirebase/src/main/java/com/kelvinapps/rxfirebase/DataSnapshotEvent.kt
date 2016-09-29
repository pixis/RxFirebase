package com.kelvinapps.rxfirebase

/**
 * Created by Nick Moskalenko on 17/05/2016.
 */
data class DataSnapshotEvent<out T>(val eventData : EventData, val value : T)