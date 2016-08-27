package com.kelvinapps.rxfirebase

import com.google.firebase.database.DataSnapshot
import com.kelvinapps.rxfirebase.exceptions.RxFirebaseDataCastException
import rx.exceptions.Exceptions
import rx.functions.Func1

/**
 * Created by renanferrari on 09/08/16.
 */

abstract class DataSnapshotMapper<T, U> private constructor() : Func1<T, U> {

    private class TypedDataSnapshotMapper<U>(private val clazz: Class<U>) : DataSnapshotMapper<DataSnapshot, U>() {

        override fun call(dataSnapshot: DataSnapshot): U? {
            if(!dataSnapshot.exists())
                return null

            val value = dataSnapshot.getValue(clazz)
            if (value != null) {
                return value
            } else {
                throw Exceptions.propagate(RxFirebaseDataCastException(
                        "unable to cast firebase data response to " + clazz.simpleName))
            }
        }
    }

    private class ChildEventDataSnapshotMapper<U>(clazz: Class<U>) : DataSnapshotMapper<RxFirebaseChildEvent<DataSnapshot>, RxFirebaseChildEvent<U>>() {

        private val dataSnapshotMapper: DataSnapshotMapper<DataSnapshot, U>

        init {
            this.dataSnapshotMapper = DataSnapshotMapper.of(clazz)
        }

        override fun call(rxFirebaseChildEvent: RxFirebaseChildEvent<DataSnapshot>): RxFirebaseChildEvent<U> {
            val value = dataSnapshotMapper.call(rxFirebaseChildEvent.value)
            return RxFirebaseChildEvent(value, rxFirebaseChildEvent.previousChildName,
                    rxFirebaseChildEvent.eventType)
        }
    }

    companion object {

        fun <U> of(clazz: Class<U>): DataSnapshotMapper<DataSnapshot, U> {
            return TypedDataSnapshotMapper(clazz)
        }

        fun <U> ofChild(clazz: Class<U>): DataSnapshotMapper<RxFirebaseChildEvent<DataSnapshot>, RxFirebaseChildEvent<U>> {
            return ChildEventDataSnapshotMapper(clazz)
        }
    }
}
