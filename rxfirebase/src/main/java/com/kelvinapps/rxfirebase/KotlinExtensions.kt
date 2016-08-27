package com.kelvinapps.rxfirebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Query
import rx.Observable
import rx.Subscriber

/**
 * Created by Work on 8/26/2016.
 */
fun <T> Subscriber<T>.onSubscribedNext(value : T) {
    if(!this.isUnsubscribed) {
        this.onNext(value)
    }
}
fun <T> Subscriber<T>.onSubscribedError(throwable: Throwable) {
    if(!this.isUnsubscribed) {
        this.onError(throwable)
    }
}

fun Query.observeValueEvent() : Observable<DataSnapshot> {
    return RxFirebaseDatabase.observeValueEvent(this)
}

fun Query.observeChildEvent() : Observable<RxFirebaseChildEvent<DataSnapshot>> {
    return RxFirebaseDatabase.observeChildEvent(this)
}

fun Observable<DataSnapshot>.flatten() : Observable<DataSnapshot> {
    return this.flatMap { Observable.from(it.children) }
}

@JvmName("mapTo")
fun <T> Observable<DataSnapshot>.mapTo(clazz: Class<T>) : Observable<T> {
    return this.map(DataSnapshotMapper.of(clazz))
}

@JvmName("mapChildTo")
fun <T> Observable<RxFirebaseChildEvent<DataSnapshot>>.mapTo(clazz: Class<T>) : Observable<RxFirebaseChildEvent<T>> {
    return this.map(DataSnapshotMapper.ofChild(clazz))
}

//Compound
fun <T> Query.observeValueEvent(clazz: Class<T>) : Observable<T> {
    return RxFirebaseDatabase.observeValueEvent(this).mapTo(clazz)
}
fun <T> Query.observeChildEvent(clazz: Class<T>) : Observable<RxFirebaseChildEvent<T>> {
    return RxFirebaseDatabase.observeChildEvent(this).mapTo(clazz)
}