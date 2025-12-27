package com.example.tictactoe

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var startLayout: LinearLayout
    private lateinit var gameLayout: LinearLayout
    private lateinit var gridLayout: GridLayout
    private lateinit var tvStatus: TextView
    private lateinit var btnPlayAgain: Button
    
    // Game State
    // 0 = empty, 1 = X, 2 = O
    private val board = IntArray(9) { 0 }
    private var isPlayerX = true
    private var gameActive = true
    
    // Winning combinations (indices)
    private val winPositions = arrayOf(
        intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8), // Rows
        intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8), // Cols
        intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)                       // Diagonals
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Background Animation
        val mainLayout = findViewById<RelativeLayout>(R.id.mainLayout)
        val animDrawable = mainLayout.background as AnimationDrawable
        animDrawable.setEnterFadeDuration(2000)
        animDrawable.setExitFadeDuration(2000)
        animDrawable.start()

        // Init Views
        startLayout = findViewById(R.id.startLayout)
        gameLayout = findViewById(R.id.gameLayout)
        gridLayout = findViewById(R.id.gridLayout)
        tvStatus = findViewById(R.id.tvStatus)
        btnPlayAgain = findViewById(R.id.btnPlayAgain)
        val btnStartGame = findViewById<Button>(R.id.btnStartGame)

        // Setup Start Button
        btnStartGame.setOnClickListener {
            startLayout.visibility = View.GONE
            gameLayout.visibility = View.VISIBLE
            initializeBoard()
        }

        // Setup Play Again Button
        btnPlayAgain.setOnClickListener {
            resetGame()
        }
    }

    private fun initializeBoard() {
        gridLayout.removeAllViews()
        for (i in 0 until 9) {
            val button = Button(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 250
                    height = 250
                    setMargins(10, 10, 10, 10)
                }
                textSize = 40f
                setOnClickListener { onCellClicked(this, i) }
            }
            gridLayout.addView(button)
        }
        resetGame()
    }

    private fun onCellClicked(btn: Button, index: Int) {
        if (!gameActive || board[index] != 0) return

        // Update Logic
        board[index] = if (isPlayerX) 1 else 2
        
        // Update UI
        btn.text = if (isPlayerX) "X" else "O"
        btn.setTextColor(ContextCompat.getColor(this, 
            if (isPlayerX) R.color.player_x_color else R.color.player_o_color))

        if (checkWinner()) {
            gameActive = false
            val winnerName = if (isPlayerX) "X" else "O"
            tvStatus.text = getString(R.string.winner_msg, winnerName)
            btnPlayAgain.visibility = View.VISIBLE
        } else if (board.none { it == 0 }) {
            gameActive = false
            tvStatus.text = getString(R.string.draw_msg)
            btnPlayAgain.visibility = View.VISIBLE
        } else {
            // Switch Turn
            isPlayerX = !isPlayerX
            tvStatus.text = if (isPlayerX) getString(R.string.turn_x) else getString(R.string.turn_o)
        }
    }

    private fun checkWinner(): Boolean {
        for (pos in winPositions) {
            if (board[pos[0]] != 0 &&
                board[pos[0]] == board[pos[1]] &&
                board[pos[1]] == board[pos[2]]) {
                return true
            }
        }
        return false
    }

    private fun resetGame() {
        gameActive = true
        isPlayerX = true
        board.fill(0)
        tvStatus.text = getString(R.string.turn_x)
        btnPlayAgain.visibility = View.GONE
        
        // Clear buttons text
        for (i in 0 until gridLayout.childCount) {
            (gridLayout.getChildAt(i) as Button).text = ""
        }
    }
}
