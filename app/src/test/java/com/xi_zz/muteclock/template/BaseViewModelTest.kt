package com.xi_zz.muteclock.template

import android.os.Bundle
import android.os.Parcelable
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import kotlinx.android.parcel.Parcelize
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BaseViewModelTest {

    private lateinit var testViewModel: TestViewModel

    private val initState = State(0)
    @Before
    fun setUp() {
        testViewModel = TestViewModel(initState)
    }

    @Test
    fun testMutateState() {
        val state1 = State(5)
        val state2 = State(10)

        val test = testViewModel.state.test()

        testViewModel.mutateState(state1)
        testViewModel.mutateState(state2)

        test.assertValueCount(3)
        test.assertValues(initState, state1, state2)
    }

    @Test
    fun testSaveState() {
        val newState = State(5)
        val stateSlot = slot<State>()
        val bundle = mockk<Bundle>(relaxed = true)

        every {
            bundle.putParcelable(testViewModel.javaClass.name, capture(stateSlot))
        } just Runs

        testViewModel.mutateState(newState)
        testViewModel.saveState(bundle)

        assertEquals(newState, stateSlot.captured)
    }

    @Test
    fun testLoadState() {
        val savedState = State(5)
        val bundle = mockk<Bundle>(relaxed = true)

        every {
            bundle.getParcelable<State>(testViewModel.javaClass.name)
        } returns savedState

        testViewModel.loadState(bundle)
        testViewModel.state.test()
                .assertValue(savedState)
    }

    @Parcelize
    data class State(val num: Int) : Parcelable

    class TestViewModel(state: State) : BaseViewModel<State>(state)
}