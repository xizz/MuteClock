package com.xi_zz.muteclock

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalTime
import java.util.Optional

class MainViewModelTest {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var timeService: TimeService
    private lateinit var startTimeSubject: BehaviorSubject<Optional<LocalTime>>
    private lateinit var endTimeSubject: BehaviorSubject<Optional<LocalTime>>

    @Before
    fun setUp() {
        timeService = mockk(relaxed = true)

        val slot = slot<LocalTime>()

        startTimeSubject = BehaviorSubject.create<Optional<LocalTime>>()
        every {
            timeService.observeStartTime()
        } returns startTimeSubject
        every { timeService.setStartTime(capture(slot)) } answers {
            startTimeSubject.onNext(Optional.of(slot.captured))
        }

        endTimeSubject = BehaviorSubject.create<Optional<LocalTime>>()
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

}