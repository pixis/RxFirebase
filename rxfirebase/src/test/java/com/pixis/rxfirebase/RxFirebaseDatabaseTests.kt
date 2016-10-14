package com.pixis.rxfirebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.verify
import rx.observers.TestSubscriber
import rx.schedulers.Schedulers
import java.util.*

/**
 * Created by Nick Moskalenko on 28/04/2016.
 */
class RxFirebaseDatabaseTests {

    @Mock
    private lateinit var mockDatabase: DatabaseReference

    @Mock
    private lateinit var mockFirebaseDataSnapshot: DataSnapshot

    private val testData = TestData()
    private val testDataList = ArrayList<TestData>()
    private val testDataMap = HashMap<String, TestData>()

    @Test
    @Throws(InterruptedException::class)
    fun testObserveSingleValue_Disconnected() {

        val testSubscriber = TestSubscriber<TestData>()
        RxFirebaseDatabase.observeValueEvent(mockDatabase).first().mapTo(TestData::class.java).subscribeOn(Schedulers.immediate()).subscribe(testSubscriber)

        val argument = ArgumentCaptor.forClass(ValueEventListener::class.java)
        verify<DatabaseReference>(mockDatabase).addValueEventListener(argument.capture())
        argument.value.onCancelled(DatabaseError.zzagb(DatabaseError.DISCONNECTED))

        testSubscriber.assertError(com.google.firebase.database.DatabaseException::class.java)
        testSubscriber.assertNotCompleted()
        testSubscriber.unsubscribe()
    }

    /*@Test
    @Throws(InterruptedException::class)
    fun testObserveValuesList_Failed() {

        val testSubscriber = TestSubscriber<List<TestData>>()
        RxFirebaseDatabase.observeValueEvent(mockDatabase).mapTo(TestData::class.java).subscribeOn(Schedulers.immediate()).toList().subscribe(testSubscriber)

        val argument = ArgumentCaptor.forClass(ValueEventListener::class.java)
        verify<DatabaseReference>(mockDatabase).addListenerForSingleValueEvent(argument.capture())
        argument.value.onCancelled(DatabaseException.zzadi(DatabaseException.OPERATION_FAILED))

        testSubscriber.assertError(DatabaseErrorException::class.java)
        testSubscriber.assertNotCompleted()
        testSubscriber.unsubscribe()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testObserveValue() {

        val testSubscriber = TestSubscriber<TestData>()
        RxFirebaseDatabase.observeValueEvent(mockDatabase, TestData::class.java).subscribeOn(Schedulers.immediate()).subscribe(testSubscriber)

        val argument = ArgumentCaptor.forClass(ValueEventListener::class.java)
        verify<DatabaseReference>(mockDatabase).addValueEventListener(argument.capture())
        argument.value.onDataChange(mockFirebaseDataSnapshot)

        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)
        testSubscriber.assertReceivedOnNext(listOf(testData))
        testSubscriber.assertNotCompleted()
        testSubscriber.unsubscribe()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testObserveValues() {

        val testSubscriber = TestSubscriber<TestData>()
        RxFirebaseDatabase.observeSingleValueEvent(mockDatabase, TestData::class.java).subscribeOn(Schedulers.immediate()).subscribe(testSubscriber)

        val argument = ArgumentCaptor.forClass(ValueEventListener::class.java)
        verify<DatabaseReference>(mockDatabase).addListenerForSingleValueEvent(argument.capture())
        argument.value.onDataChange(mockFirebaseDataSnapshot)

        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)
        testSubscriber.assertReceivedOnNext(listOf(testData))
        testSubscriber.assertCompleted()
        testSubscriber.unsubscribe()
    }*/

    internal inner class TestData {
        var id: Int = 0
        var str: String? = null
    }

}
