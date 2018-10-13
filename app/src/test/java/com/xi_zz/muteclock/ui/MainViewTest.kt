package com.xi_zz.muteclock.ui

import android.os.Build
import com.xi_zz.muteclock.R
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.main.view.cancelEndTimeButton
import kotlinx.android.synthetic.main.main.view.cancelStartTimeButton
import kotlinx.android.synthetic.main.main.view.endTimeText
import kotlinx.android.synthetic.main.main.view.startTimeText
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@Config(sdk = [Build.VERSION_CODES.O])
@RunWith(RobolectricTestRunner::class)
class MainViewTest {

    private lateinit var view: MainView

    @MockK
    private lateinit var model: MainViewModel

    private lateinit var subject: BehaviorSubject<State>

    private var context = RuntimeEnvironment.application.applicationContext

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        view = MainView(context)

        subject = BehaviorSubject.create()
        every { model.state } returns subject
        view.bindViewModel(model)
    }

    @Test
    fun testStartTimeText() {
        Assert.assertEquals(context.getString(R.string.no_start_time), view.startTimeText.text)

        val time = LocalTime.now().truncatedTo(ChronoUnit.MINUTES)

        subject.onNext(State(startTime = time))
        Assert.assertEquals(time.toString(), view.startTimeText.text)

        subject.onNext(State(startTime = null))
        Assert.assertEquals(context.getString(R.string.no_start_time), view.startTimeText.text)
    }

    @Test
    fun testEndTimeText() {
        Assert.assertEquals(context.getString(R.string.no_start_time), view.startTimeText.text)

        val time = LocalTime.now().truncatedTo(ChronoUnit.MINUTES)

        subject.onNext(State(endTime = time))
        Assert.assertEquals(time.toString(), view.endTimeText.text)

        subject.onNext(State(endTime = null))
        Assert.assertEquals(context.getString(R.string.no_start_time), view.startTimeText.text)
    }

    @Test
    fun testCancelStartTime() {
        view.cancelStartTimeButton.performClick()
        verify { model.startTime = null }
    }

    @Test
    fun testCancelEndTime() {
        view.cancelEndTimeButton.performClick()
        verify { model.endTime = null }
    }

}