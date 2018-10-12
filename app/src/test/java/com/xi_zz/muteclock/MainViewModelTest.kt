package com.xi_zz.muteclock

import com.xi_zz.muteclock.Util.unwrapMap
import com.xi_zz.muteclock.ui.MainViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.reactivex.subjects.PublishSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalTime
import java.util.Optional

class MainViewModelTest {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var timeService: TimeService
    private lateinit var startTimeSubject: PublishSubject<Optional<LocalTime>>
    private lateinit var endTimeSubject: PublishSubject<Optional<LocalTime>>

    @Before
    fun setUp() {
        timeService = mockk(relaxed = true)

        val slot = slot<LocalTime>()

        startTimeSubject = PublishSubject.create<Optional<LocalTime>>()
        every {
            timeService.observeStartTime()
        } returns startTimeSubject
        every { timeService.setStartTime(capture(slot)) } answers {
            startTimeSubject.onNext(Optional.of(slot.captured))
        }

        endTimeSubject = PublishSubject.create<Optional<LocalTime>>()
        every {
            timeService.observeEndTime()
        } returns endTimeSubject
        every { timeService.setEndTime(capture(slot)) } answers {
            endTimeSubject.onNext(Optional.of(slot.captured))
        }

        mainViewModel = MainViewModel(timeService)
    }

    @Test
    fun testSetStartTime() {
        val time = LocalTime.of(10, 45)
        mainViewModel.startTime = time
        Assert.assertEquals(time, mainViewModel.startTime)
    }

    @Test
    fun testSetEndTime() {
        val time = LocalTime.of(10, 45)
        mainViewModel.endTime = time
        Assert.assertEquals(time, mainViewModel.endTime)
    }

    @Test
    fun testReceiveStartTime() {
        val time1 = LocalTime.of(10, 45)
        val time2 = LocalTime.of(11, 45)


        val testObserver = mainViewModel.state.unwrapMap { it.startTime }.test()

        startTimeSubject.onNext(Optional.of(time1))
        startTimeSubject.onNext(Optional.empty())
        startTimeSubject.onNext(Optional.of(time2))

        testObserver.assertValues(time1, time2)
    }

    @Test
    fun testReceiveEndTime() {
        val time1 = LocalTime.of(10, 45)
        val time2 = LocalTime.of(11, 45)


        val testObserver = mainViewModel.state.unwrapMap { it.endTime }.test()

        endTimeSubject.onNext(Optional.of(time1))
        endTimeSubject.onNext(Optional.empty())
        endTimeSubject.onNext(Optional.of(time2))

        testObserver.assertValues(time1, time2)
    }
}