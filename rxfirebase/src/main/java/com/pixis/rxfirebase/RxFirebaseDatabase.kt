package com.pixis.rxfirebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.jakewharton.rxrelay.BehaviorRelay
import rx.Observable
import rx.subscriptions.Subscriptions

object RxFirebaseDatabase {

    fun observeValueEvent(query: Query): Observable<DataSnapshot> {
        return BehaviorRelay.create { subscriber ->
            val valueEventListener = query.addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            subscriber.onNext(dataSnapshot)
                            subscriber.onCompleted()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            subscriber.onError(error.toException())
                        }
                    })

            subscriber.add(Subscriptions.create { query.removeEventListener(valueEventListener) })
        }
    }

}
/*
fun <T> setValue(query: DatabaseReference, value : T) : Subscriber<T> {
    query.setValue(value, RxCompletionListener(value))
}
class RxCompletionListener<T>(val subscriber : Subscriber<T>, val returnValue : T) : DatabaseReference.CompletionListener {

    override fun onComplete(databaseError: DatabaseError?, databaseReference: DatabaseReference?) {
        if(databaseError == null) {
            subscriber.onNext(returnValue)
            subscriber.onCompleted()
        }
        else {
            subscriber.onError(databaseError.toException())
        }
    }


}*/