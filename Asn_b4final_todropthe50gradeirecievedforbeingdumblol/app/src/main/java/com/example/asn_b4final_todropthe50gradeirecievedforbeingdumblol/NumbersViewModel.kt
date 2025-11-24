package com.example.asn_b4final_todropthe50gradeirecievedforbeingdumblol

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.abs

data class NumbersUiState(
    val tiles: List<Int>,
    val moves: Int,
    val isSolved: Boolean
)

private const val GRID_SIZE = 3
private const val TILE_COUNT = GRID_SIZE * GRID_SIZE

class NumbersViewModel : ViewModel() {

    var uiState by mutableStateOf(newShuffledPuzzleState())
        private set

    fun onTileClicked(tileNumber: Int) {
        uiState = moveTileIfPossible(uiState, tileNumber)
    }

    fun onResetClicked() {
        uiState = newShuffledPuzzleState()
    }

    private fun newShuffledPuzzleState(): NumbersUiState {
        val tiles = generateShuffledSolvableTiles()
        return NumbersUiState(
            tiles = tiles,
            moves = 0,
            isSolved = isSolved(tiles)
        )
    }

    private fun moveTileIfPossible(state: NumbersUiState, tileNumber: Int): NumbersUiState {
        if (tileNumber == 0) return state

        val tiles = state.tiles
        val tileIndex = tiles.indexOf(tileNumber)
        val emptyIndex = tiles.indexOf(0)

        if (!canMove(tileIndex, emptyIndex)) return state

        val newTiles = tiles.toMutableList().apply {
            this[emptyIndex] = tileNumber
            this[tileIndex] = 0
        }.toList()

        val solved = isSolved(newTiles)

        return state.copy(
            tiles = newTiles,
            moves = state.moves + 1,
            isSolved = solved
        )
    }

    private fun generateShuffledSolvableTiles(): List<Int> {
        val base = (1..8).toList() + 0
        var candidate: List<Int>

        do {
            candidate = base.shuffled()
        } while (!isSolvable(candidate) || isSolved(candidate))

        return candidate
    }

    private fun isSolved(tiles: List<Int>): Boolean {
        if (tiles.size != TILE_COUNT) return false
        val solvedList = (1..8).toList() + 0
        return tiles == solvedList
    }

    private fun isSolvable(tiles: List<Int>): Boolean {
        val numbers = tiles.filter { it != 0 }
        var inversions = 0

        for (i in numbers.indices) {
            for (j in i + 1 until numbers.size) {
                if (numbers[i] > numbers[j]) inversions++
            }
        }
        return inversions % 2 == 0
    }

    private fun canMove(tileIndex: Int, emptyIndex: Int): Boolean {
        val tileRow = tileIndex / GRID_SIZE
        val tileCol = tileIndex % GRID_SIZE
        val emptyRow = emptyIndex / GRID_SIZE
        val emptyCol = emptyIndex % GRID_SIZE

        val rowDiff = abs(tileRow - emptyRow)
        val colDiff = abs(tileCol - emptyCol)
        return rowDiff + colDiff == 1
    }
}
