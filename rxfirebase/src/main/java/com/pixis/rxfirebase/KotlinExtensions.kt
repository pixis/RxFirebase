package com.pixis.rxfirebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Query
import rx.Observable

fun Query.observe(): Observable<DataSnapshot> {
    return RxFirebaseDatabase.observeValueEvent(this)
}

fun <T> Observable<DataSnapshot>.mapTo(clazz: Class<T>): Observable<T> {
    return map({ it.getValue(clazz) })
}

fun <T> Observable<DataSnapshot>.mapChildrenTo(clazz: Class<T>): Observable<List<T>> {
    return flatMap {
        Observable.from(it.children).mapTo(clazz).toList()
    }
}