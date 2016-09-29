package com.kelvinapps.rxfirebase

import com.google.firebase.database.*
import com.kelvinapps.rxfirebase.exceptions.RxFirebaseDataException
import rx.Observable
import rx.subscriptions.Subscriptions

/**
 * Created by Nick Moskalenko on 15/05/2016.
 */
object RxFirebaseDatabase {

    fun observeValueEvent(query: Query): Observable<DataSnapshot> {
        return Observable.create { subscriber ->
            val valueEventListener = query.addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            subscriber.onNext(dataSnapshot)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            subscriber.onError(RxFirebaseDataException(error))
                        }
                    })

            subscriber.add(Subscriptions.create { query.removeEventListener(valueEventListener) })
        }
    }

    fun observeChildEvent(query: Query): Observable<DataSnapshotEvent<DataSnapshot>> {
        return Observable.create { subscriber ->
            val childEventListener = query.addChildEventListener(
                    object : ChildEventListener {

                        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                            val eventData = EventData(key = dataSnapshot.key,
                                    previousChildKey = previousChildName.orEmpty(),
                                    eventType = EventType.ADDED)
                            val dataSnapshotEvent = DataSnapshotEvent(eventData, dataSnapshot)
                            subscriber.onNext(dataSnapshotEvent)
                        }

                        override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                            val eventData = EventData(key = dataSnapshot.key,
                                    previousChildKey = previousChildName.orEmpty(),
                                    eventType = EventType.CHANGED)
                            val dataSnapshotEvent = DataSnapshotEvent(eventData, dataSnapshot)
                            subscriber.onNext(dataSnapshotEvent)
                        }

                        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                            val eventData = EventData(key = dataSnapshot.key,
                                    previousChildKey = "",
                                    eventType = EventType.REMOVED)
                            val dataSnapshotEvent = DataSnapshotEvent(eventData, dataSnapshot)
                            subscriber.onNext(dataSnapshotEvent)
                        }

                        override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                            val eventData = EventData(key = dataSnapshot.key,
                                    previousChildKey = previousChildName.orEmpty(),
                                    eventType = EventType.MOVED)
                            val dataSnapshotEvent = DataSnapshotEvent(eventData, dataSnapshot)
                            subscriber.onNext(dataSnapshotEvent)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            subscriber.onError(RxFirebaseDataException(error))
                        }
                    })

            subscriber.add(Subscriptions.create { query.removeEventListener(childEventListener) })
        }
    }

}
