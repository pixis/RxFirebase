package com.kelvinapps.rxfirebase

import com.google.firebase.auth.*
import rx.Observable

/**
 * Created by Nick Moskalenko on 24/05/2016.
 */
object RxFirebaseUser {

    fun getToken(firebaseUser: FirebaseUser,
                 forceRefresh: Boolean): Observable<GetTokenResult> {
        return Observable.create { subscriber -> RxHandler.assignOnTask(subscriber, firebaseUser.getToken(forceRefresh)) }
    }

    fun updateEmail(firebaseUser: FirebaseUser,
                    email: String): Observable<Void> {
        return Observable.create { subscriber -> RxHandler.assignOnTask(subscriber, firebaseUser.updateEmail(email)) }
    }

    fun updatePassword(firebaseUser: FirebaseUser,
                       password: String): Observable<Void> {
        return Observable.create { subscriber -> RxHandler.assignOnTask(subscriber, firebaseUser.updatePassword(password)) }
    }

    fun updateProfile(firebaseUser: FirebaseUser,
                      request: UserProfileChangeRequest): Observable<Void> {
        return Observable.create { subscriber -> RxHandler.assignOnTask(subscriber, firebaseUser.updateProfile(request)) }
    }

    fun delete(firebaseUser: FirebaseUser): Observable<Void> {
        return Observable.create { subscriber -> RxHandler.assignOnTask(subscriber, firebaseUser.delete()) }
    }

    fun reauthenticate(firebaseUser: FirebaseUser,
                       credential: AuthCredential): Observable<Void> {
        return Observable.create { subscriber -> RxHandler.assignOnTask(subscriber, firebaseUser.reauthenticate(credential)) }
    }

    fun linkWithCredential(firebaseUser: FirebaseUser,
                           credential: AuthCredential): Observable<AuthResult> {
        return Observable.create { subscriber -> RxHandler.assignOnTask(subscriber, firebaseUser.linkWithCredential(credential)) }
    }


}
