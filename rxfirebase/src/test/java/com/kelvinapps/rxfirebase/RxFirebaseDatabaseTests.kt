package com.kelvinapps.rxfirebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.kelvinapps.rxfirebase.exceptions.RxFirebaseDataException
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
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

    private var testChildEventAdded: RxFirebaseChildEvent<TestData>? = null
    private var testChildEventChanged: RxFirebaseChildEvent<TestData>? = null
    private var testChildEventRemoved: RxFirebaseChildEvent<TestData>? = null
    private var testChildEventMoved: RxFirebaseChildEvent<TestData>? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        testDataList.add(testData)
        testDataMap.put("key", testData)
        testChildEventAdded = RxFirebaseChildEvent(testData, "root", RxFirebaseChildEvent.EventType.ADDED)
        testChildEventChanged = RxFirebaseChildEvent(testData, "root", RxFirebaseChildEvent.EventType.CHANGED)
        testChildEventRemoved = RxFirebaseChildEvent(testData, RxFirebaseChildEvent.EventType.REMOVED)
        testChildEventMoved = RxFirebaseChildEvent(testData, "root", RxFirebaseChildEvent.EventType.MOVED)

        `when`(mockFirebaseDataSnapshot.getValue(TestData::class.java)).thenReturn(testData)
        `when`(mockFirebaseDataSnapshot.key).thenReturn("key")
        `when`(mockFirebaseDataSnapshot.children).thenReturn(Arrays.asList(mockFirebaseDataSnapshot))
    }

    @Test
    @Throws(InterruptedException::class)
    fun testObserveSingleValue() {

        val testSubscriber = TestSubscriber<TestData>()

        mockDatabase.observeValueEvent()
                .first()
                .mapTo(TestData::class.java)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber)

        val argument = ArgumentCaptor.forClass(ValueEventListener::class.java)
        verify<DatabaseReference>(mockDatabase).addValueEventListener(argument.capture())
        argument.value.onDataChange(mockFirebaseDataSnapshot)

        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)
        testSubscriber.assertReceivedOnNext(listOf(testData))
        testSubscriber.assertCompleted()
        testSubscriber.unsubscribe()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testObserveSingleValue_Disconnected() {

        val testSubscriber = TestSubscriber<TestData>()
        RxFirebaseDatabase.observeValueEvent(mockDatabase).first().mapTo(TestData::class.java).subscribeOn(Schedulers.immediate()).subscribe(testSubscriber)

        val argument = ArgumentCaptor.forClass(ValueEventListener::class.java)
        verify<DatabaseReference>(mockDatabase).addValueEventListener(argument.capture())
        argument.value.onCancelled(DatabaseError.zzaer(DatabaseError.DISCONNECTED))

        testSubscriber.assertError(RxFirebaseDataException::class.java)
        testSubscriber.assertNotCompleted()
        testSubscriber.unsubscribe()
    }
/*
    @Test
    @Throws(InterruptedException::class)
    fun testObserveValuesList_Failed() {

        val testSubscriber = TestSubscriber<List<TestData>>()
        RxFirebaseDatabase.observeSingleValueEvent(mockDatabase, TestData::class.java).subscribeOn(Schedulers.immediate()).toList().subscribe(testSubscriber)

        val argument = ArgumentCaptor.forClass(ValueEventListener::class.java)
        verify<DatabaseReference>(mockDatabase).addListenerForSingleValueEvent(argument.capture())
        argument.value.onCancelled(DatabaseError.zzadi(DatabaseError.OPERATION_FAILED))

        testSubscriber.assertError(RxFirebaseDataException::class.java)
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
    }

    @Test
    @Throws(InterruptedException::class)
    fun testObserveValuesList() {

        val testSubscriber = TestSubscriber<List<TestData>>()
        RxFirebaseDatabase.observeSingleValueEvent(mockDatabase, TestData::class.java).subscribeOn(Schedulers.immediate()).toList().subscribe(testSubscriber)

        val argument = ArgumentCaptor.forClass(ValueEventListener::class.java)
        verify<DatabaseReference>(mockDatabase).addListenerForSingleValueEvent(argument.capture())
        argument.value.onDataChange(mockFirebaseDataSnapshot)

        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)
        testSubscriber.assertReceivedOnNext(listOf<List<TestData>>(testDataList))
        testSubscriber.assertCompleted()
        testSubscriber.unsubscribe()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testObserveValuesMap() {

        val testSubscriber = TestSubscriber<Map<String, TestData>>()
        RxFirebaseDatabase.observeSingleValueEvent(mockDatabase).subscribeOn(Schedulers.immediate()).toMap(Func1<com.google.firebase.database.DataSnapshot, kotlin.String> { dataSnapshot -> dataSnapshot.key }, Func1<com.google.firebase.database.DataSnapshot, com.kelvinapps.rxfirebase.RxFirebaseDatabaseTests.TestData> { dataSnapshot -> dataSnapshot.getValue(TestData::class.java) }, Func0<kotlin.collections.Map<kotlin.String, com.kelvinapps.rxfirebase.RxFirebaseDatabaseTests.TestData>> { LinkedHashMap() }).subscribe(testSubscriber)

        val argument = ArgumentCaptor.forClass(ValueEventListener::class.java)
        verify<DatabaseReference>(mockDatabase).addListenerForSingleValueEvent(argument.capture())
        argument.value.onDataChange(mockFirebaseDataSnapshot)

        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)
        testSubscriber.assertReceivedOnNext(listOf<Map<String, TestData>>(testDataMap))
        testSubscriber.assertCompleted()
        testSubscriber.unsubscribe()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testObserveChildrenEvents_Added() {

        val testSubscriber = TestSubscriber<RxFirebaseChildEvent<TestData>>()
        RxFirebaseDatabase.observeChildEvent(mockDatabase, TestData::class.java).subscribeOn(Schedulers.immediate()).subscribe(testSubscriber)

        val argument = ArgumentCaptor.forClass(ChildEventListener::class.java)
        verify<DatabaseReference>(mockDatabase).addChildEventListener(argument.capture())
        argument.value.onChildAdded(mockFirebaseDataSnapshot, "root")

        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)
        testSubscriber.assertReceivedOnNext(listOf<RxFirebaseChildEvent<TestData>>(testChildEventAdded))
        testSubscriber.assertNotCompleted()
        testSubscriber.unsubscribe()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testObserveChildrenEvents_Changed() {

        val testSubscriber = TestSubscriber<RxFirebaseChildEvent<TestData>>()
        RxFirebaseDatabase.observeChildEvent(mockDatabase, TestData::class.java).subscribeOn(Schedulers.immediate()).subscribe(testSubscriber)

        val argument = ArgumentCaptor.forClass(ChildEventListener::class.java)
        verify<DatabaseReference>(mockDatabase).addChildEventListener(argument.capture())
        argument.value.onChildChanged(mockFirebaseDataSnapshot, "root")

        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)
        testSubscriber.assertReceivedOnNext(listOf<RxFirebaseChildEvent<TestData>>(testChildEventChanged))
        testSubscriber.assertNotCompleted()
        testSubscriber.unsubscribe()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testObserveChildrenEvents_Removed() {

        val testSubscriber = TestSubscriber<RxFirebaseChildEvent<TestData>>()
        RxFirebaseDatabase.observeChildEvent(mockDatabase, TestData::class.java).subscribeOn(Schedulers.immediate()).subscribe(testSubscriber)

        val argument = ArgumentCaptor.forClass(ChildEventListener::class.java)
        verify<DatabaseReference>(mockDatabase).addChildEventListener(argument.capture())
        argument.value.onChildRemoved(mockFirebaseDataSnapshot)

        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)
        testSubscriber.assertReceivedOnNext(listOf<RxFirebaseChildEvent<TestData>>(testChildEventRemoved))
        testSubscriber.assertNotCompleted()
        testSubscriber.unsubscribe()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testObserveChildrenEvents_Moved() {

        val testSubscriber = TestSubscriber<RxFirebaseChildEvent<TestData>>()
        RxFirebaseDatabase.observeChildEvent(mockDatabase, TestData::class.java).subscribeOn(Schedulers.immediate()).subscribe(testSubscriber)

        val argument = ArgumentCaptor.forClass(ChildEventListener::class.java)
        verify<DatabaseReference>(mockDatabase).addChildEventListener(argument.capture())
        argument.value.onChildMoved(mockFirebaseDataSnapshot, "root")

        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)
        testSubscriber.assertReceivedOnNext(listOf<RxFirebaseChildEvent<TestData>>(testChildEventMoved))
        testSubscriber.assertNotCompleted()
        testSubscriber.unsubscribe()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testObserveChildrenEvents_Cancelled() {

        val testSubscriber = TestSubscriber<RxFirebaseChildEvent<TestData>>()
        RxFirebaseDatabase.observeChildEvent(mockDatabase, TestData::class.java).subscribeOn(Schedulers.immediate()).subscribe(testSubscriber)

        val argument = ArgumentCaptor.forClass(ChildEventListener::class.java)
        verify<DatabaseReference>(mockDatabase).addChildEventListener(argument.capture())
        argument.value.onCancelled(DatabaseError.zzadi(DatabaseError.DISCONNECTED))

        testSubscriber.assertError(RxFirebaseDataException::class.java)
        testSubscriber.assertNotCompleted()
        testSubscriber.unsubscribe()
    }
*/
    internal inner class TestData {
        var id: Int = 0
        var str: String? = null
    }

}
