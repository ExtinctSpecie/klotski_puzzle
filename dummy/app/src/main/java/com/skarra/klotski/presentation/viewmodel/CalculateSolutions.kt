package com.skarra.klotski.presentation.viewmodel

import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class CalculateSolutions {

    fun automaticSteps(currentStep: Int, maxStep: Int): Observable<Int> {
        return Observable.range(currentStep, maxStep)
            .concatMap {
                Observable.just(it).delay(1000, TimeUnit.MILLISECONDS)
            }
    }
}