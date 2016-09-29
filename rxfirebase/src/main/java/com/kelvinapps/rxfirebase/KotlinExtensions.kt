package com.kelvinapps.rxfirebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Query
import rx.Observable

fun Query.observeValueEvent(): Observable<DataSnapshot> {
    return RxFirebaseDatabase.observeValueEvent(this)
}

fun <T> Observable<DataSnapshot>.mapTo(clazz: Class<T>): Observable<T> {
    return map({ it.getValue(clazz) })
}