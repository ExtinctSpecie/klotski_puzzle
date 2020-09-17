package com.skarra.klotski.presentation.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.skarra.klotski.Board
import com.skarra.klotski.R
import com.skarra.klotski.presentation.model.MainActivityViewState
import com.skarra.klotski.presentation.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivityView {

    fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
        viewModel: MainActivityViewModel,
        lifecycleOwner: LifecycleOwner

    ): View {
        val view = inflater.inflate(R.layout.activity_main, container, false)
        setClickListeners(view, viewModel)
        observeViewModel(view, viewModel, lifecycleOwner)
        viewModel.calculateSolutions()
        return view
    }

    private fun setClickListeners(view: View, viewModel: MainActivityViewModel) {
        view.display_automatically.apply {
            setOnClickListener {
                viewModel.automatic()
                it.visibility = View.GONE
                view.stop_automatically.visibility = View.VISIBLE
            }
        }

        view.stop_automatically.apply {
            setOnClickListener {
                viewModel.stopAutomatic()
                it.visibility = View.GONE
                view.display_automatically.visibility = View.VISIBLE
            }
        }

        view.next_state_btn.setOnClickListener {
            viewModel.currentStep.value?.let {
                if (it < viewModel.fastestRoute.value?.size ?: 0) {
                    val nextStep = it + 1
                    viewModel.currentStep.postValue(nextStep)
                }
            }
        }

        view.previous_state_btn.setOnClickListener {
            viewModel.currentStep.value?.let {
                if (it > 0) {
                    val previousStep = it - 1
                    viewModel.currentStep.postValue(previousStep)
                }
            }
        }
    }

    private fun observeViewModel(
        view: View,
        viewModel: MainActivityViewModel,
        lifecycleOwner: LifecycleOwner
    ) {

        viewModel.currentStep.observe(lifecycleOwner, Observer {
            printBoardState(view, it, viewModel)
        })

        viewModel.viewState.observe(lifecycleOwner, Observer {
            if (it is MainActivityViewState.Loading) view.loading_pb.show() else view.loading_pb.hide()
        })

        viewModel.fastestRoute.observe(lifecycleOwner, Observer {
            view.fastest_route_steps_tv.text =
                view.resources.getString(R.string.fastest_route_steps, it.size.toString())
        })

        viewModel.slowestRoute.observe(lifecycleOwner, Observer {
            view.slowest_route_steps_tv.text =
                view.resources.getString(R.string.slowest_route_steps, it.size.toString())
        })
    }

    private fun printBoardState(view: View, step: Int, viewModel: MainActivityViewModel) {
        viewModel.fastestRoute.value?.let {
            val board = it[step]
            val tableView = view.findViewById<TableLayout>(R.id.table_layout)
            tableView.row_1.row_1_piece_1
            board.squares.forEachIndexed { index, i ->
                val color = getColor(i)
                ((tableView.getChildAt(index / Board.WIDTH) as? TableRow?)?.getChildAt(index % Board.WIDTH))?.also {
                    it.setBackgroundColor(ContextCompat.getColor(tableView.context, color))
                }
            }
        }
    }

    private fun printBoardState(view: View, boards: List<Board>) {
        var delay = 1000L
        for (board in boards) {
            view.postDelayed({
                val tableView = view.findViewById<TableLayout>(R.id.table_layout)
                tableView.row_1.row_1_piece_1
                board.squares.forEachIndexed { index, i ->
                    val color = getColor(i)
                    ((tableView.getChildAt(index / Board.WIDTH) as? TableRow?)?.getChildAt(index % Board.WIDTH))?.also {
                        it.setBackgroundColor(ContextCompat.getColor(tableView.context, color))
                    }
                }
            }, delay)
            delay += 1000L
        }
    }

    @ColorRes
    private fun getColor(square: Int): Int {
        return when (square) {
            0 -> return R.color.colorPrimary
            1 -> return R.color.piece1
            2 -> return R.color.piece2
            3 -> return R.color.piece3
            4 -> return R.color.piece4
            5 -> return R.color.piece5
            6 -> return R.color.piece6
            7 -> return R.color.piece7
            8 -> return R.color.piece8
            9 -> return R.color.piece9
            else -> android.R.color.white
        }
    }
}