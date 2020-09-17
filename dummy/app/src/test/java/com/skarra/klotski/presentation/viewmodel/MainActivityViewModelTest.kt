package com.skarra.klotski.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Rule
import org.junit.Test


class MainActivityViewModelTest {

    private val calculateSolutions: CalculateSolutions = mock()
    val sut = MainActivityViewModel(calculateSolutions)


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var rxJavaRule = RxSynchronizeSchedulersRule()

    @Test
    fun `automatic SHOULD dispatch 1 as next step`() {
        sut.currentStep.value = 0
        sut.fastestRoute.value = arrayListOf(mock(), mock())

        val observable = Observable.just(1)

        whenever(calculateSolutions.automaticSteps(0, 2)).thenReturn(observable)

        sut.automatic()

        assertThat(sut.currentStep.value).isEqualTo(1)
    }

    @Test
    fun `automatic SHOULD not dispatch any new value`() {
        sut.currentStep.value = 0
        sut.fastestRoute.value = arrayListOf(mock(), mock())

        val observable = Observable.just(2)

        whenever(calculateSolutions.automaticSteps(0, 2)).thenReturn(observable)

        sut.automatic()

        assertThat(sut.currentStep.value).isEqualTo(0)
    }
}