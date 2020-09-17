package com.skarra.klotski.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skarra.klotski.Board
import com.skarra.klotski.Klotski
import com.skarra.klotski.presentation.exitCoordinates
import com.skarra.klotski.presentation.exitPieceLabel
import com.skarra.klotski.presentation.map
import com.skarra.klotski.presentation.model.MainActivityViewState
import com.skarra.klotski.presentation.squares
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel(
    private val calculateSolutions: CalculateSolutions
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val viewState = MutableLiveData<MainActivityViewState>()
    private val solvedRoutes = MutableLiveData<List<List<Board>>>()
    private lateinit var disposableLooper: Disposable
    val fastestRoute = MutableLiveData<List<Board>>()
    val slowestRoute = MutableLiveData<List<Board>>()
    val currentStep = MutableLiveData<Int>()


    fun automatic() {
        val currentStep = currentStep.value ?: 0
        val maxStep = fastestRoute.value?.size ?: 0

        if (::disposableLooper.isInitialized) disposableLooper.dispose()

        disposableLooper = calculateSolutions.automaticSteps(currentStep, maxStep)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                if (it < maxStep) {
                    this.currentStep.value = it
                }
            }.addTo(compositeDisposable)
    }

    fun stopAutomatic() {
        if (::disposableLooper.isInitialized) disposableLooper.dispose()
    }

    fun calculateSolutions() {
        viewState.postValue(MainActivityViewState.Loading)
        Single.fromCallable {
            calculateSolutionsForKlotski()
            calculateFastestAndSlowestRoute()
            viewState.postValue(MainActivityViewState.NoLoading)
        }
            .subscribeOn(Schedulers.io())
            .subscribeBy {
            }.addTo(compositeDisposable)
    }

    private fun calculateSolutionsForKlotski() {
        val solver = Klotski(
            exitPieceLabel,
            exitCoordinates,
            squares,
            map
        )

        solver.solve()
        val allSolvedRoutes = solver.allSolvedRoutes()
        solvedRoutes.postValue(allSolvedRoutes)
    }

    private fun calculateFastestAndSlowestRoute() {

        viewState.postValue(MainActivityViewState.Loading)
        Single.fromCallable {
            calculateSolutionsForKlotski()
            viewState.postValue(MainActivityViewState.NoLoading)
        }.subscribeOn(Schedulers.io()).subscribeBy {
        }.addTo(compositeDisposable)

        var max = 0
        var maxIndex = 0
        var min = Int.MAX_VALUE
        var minIndex = 0

        this.solvedRoutes.value?.forEachIndexed { index, it ->
            if (it.size > max) {
                max = it.size
                maxIndex = index
            }
            if (it.size < min) {
                min = it.size
                minIndex = index
            }
        }
        this.fastestRoute.postValue(this.solvedRoutes.value?.get(minIndex)?.reversed())
        this.slowestRoute.postValue(this.solvedRoutes.value?.get(maxIndex)?.reversed())
        this.currentStep.postValue(0)
    }
}