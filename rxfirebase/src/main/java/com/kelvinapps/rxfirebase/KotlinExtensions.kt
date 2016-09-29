package com.kelvinapps.rxfirebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Query
import rx.Observable

fun Query.observeValueEvent(): Observable<DataSnapshot> {
    return RxFirebaseDatabase.observeValueEvent(this)
}
fun Query.observeChildEvent(): Observable<DataSnapshotEvent<DataSnapshot>> {
    return RxFirebaseDatabase.observeChildEvent(this)
}

fun Observable<DataSnapshot>.flatten(): Observable<DataSnapshot> {
    return this.flatMap { Observable.from(it.children) }
}

@JvmName("mapTo")
fun <T> Observable<DataSnapshot>.mapTo(clazz: Class<T>): Observable<T> {
    return this.map(DataSnapshotMapper.of(clazz))
}

@JvmName("mapChildTo")
fun <T> Observable<DataSnapshotEvent<DataSnapshot>>.mapTo(clazz: Class<T>): Observable<DataSnapshotEvent<T>> {
    return this.map(DataSnapshotMapper.ofChild(clazz))
}