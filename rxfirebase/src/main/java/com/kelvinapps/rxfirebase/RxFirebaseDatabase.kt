package com.kelvinapps.rxfirebase

import com.google.firebase.database.*
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent.EventType
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
                            subscriber.onSubscribedNext(dataSnapshot)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            subscriber.onSubscribedError(RxFirebaseDataException(error))
                        }
                    })

            subscriber.add(Subscriptions.create { query.removeEventListener(valueEventListener) })
        }
    }

    fun observeChildEvent(query: Query): Observable<RxFirebaseChildEvent<DataSnapshot>> {
        return Observable.create { subscriber ->
            val childEventListener = query.addChildEventListener(
                    object : ChildEventListener {

                        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String) {
                            subscriber.onSubscribedNext(
                                    RxFirebaseChildEvent(dataSnapshot, previousChildName,
                                            EventType.ADDED))
                        }

                        override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String) {
                                subscriber.onSubscribedNext(
                                        RxFirebaseChildEvent(dataSnapshot, previousChildName,
                                                EventType.CHANGED))
                        }

                        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                                subscriber.onSubscribedNext(RxFirebaseChildEvent(dataSnapshot,
                                        EventType.REMOVED))
                        }

                        override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String) {
                                subscriber.onSubscribedNext(
                                        RxFirebaseChildEvent(dataSnapshot, previousChildName,
                                                EventType.MOVED))
                        }

                        override fun onCancelled(error: DatabaseError) {
                                subscriber.onSubscribedError(RxFirebaseDataException(error))
                        }
                    })

            subscriber.add(Subscriptions.create { query.removeEventListener(childEventListener) })
        }
    }

}
