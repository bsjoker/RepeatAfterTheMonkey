package com.alfasoft.pers

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.ViewModelProvider
import com.alfasoft.pers.database.ScoreViewModel
import com.alfasoft.pers.models.ScoreModel
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*
import kotlin.collections.ArrayList

class GameActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var scoreVM: ScoreViewModel

    var arrayMonkeysComps = Arrays.asList(
        R.drawable.enemy_1,
        R.drawable.enemy_2,
        R.drawable.enemy_3,
        R.drawable.enemy_4,
        R.drawable.enemy_5
    )
    var arrayYourMonkeysComps = Arrays.asList(
        R.drawable.your_monkey_1,
        R.drawable.your_monkey_2,
        R.drawable.your_monkey_3,
        R.drawable.your_monkey_4,
        R.drawable.your_monkey_5
    )
    var arrayMonkeysCompsOrder = Arrays.asList(0, 1, 2, 3, 4, 0, 1, 2, 3, 4)
    val arrayIvLife: ArrayList<ImageView> = arrayListOf()
    val arrayIvMonkey : ArrayList<ImageView> = arrayListOf()
    var number = 0
    var points = 0
    var lifeLeft = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        fillArraysImageView()

        scoreVM = ViewModelProvider(this).get(ScoreViewModel::class.java)
        startGame()
    }

    private fun fillArraysImageView() {
        arrayIvLife.apply {
            add(ivLife1)
            add(ivLife2)
            add(ivLife3)
        }

        arrayIvMonkey.apply {
            add(ivMonkey1)
            add(ivMonkey2)
            add(ivMonkey3)
            add(ivMonkey4)
            add(ivMonkey5)
        }
    }

    private fun startGame() {
        enableListeners()
        resetGameData()

        Collections.shuffle(arrayMonkeysCompsOrder)
        ivMonkeyEnemy.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                arrayMonkeysComps[arrayMonkeysCompsOrder[number]]
            )
        )
    }

    private fun resetGameData() {
        number = 0
        points = 0
        lifeLeft = 2

        tvScore.text = points.toString()
        arrayIvLife.forEach {
            it.alpha = 1.0f
        }

        finalField.visibility = View.GONE
    }

    private fun enableListeners() {
        arrayIvMonkey.forEach {
            it.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivMonkey1 -> checkCoincidence(0)
            R.id.ivMonkey2 -> checkCoincidence(1)
            R.id.ivMonkey3 -> checkCoincidence(2)
            R.id.ivMonkey4 -> checkCoincidence(3)
            R.id.ivMonkey5 -> checkCoincidence(4)
            R.id.btnAgain -> startGame()
            R.id.btnScores -> startActivity(Intent(this@GameActivity, ScoreActivity::class.java))
        }
    }

    private fun checkCoincidence(numPlayer: Int) {
        ivMonkeyPlayer.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                arrayYourMonkeysComps[numPlayer]
            )
        )

        if (numPlayer == arrayMonkeysCompsOrder[number]) {
            with(tvScore){
                points = points + 10
                text = points.toString()
            }

            ivPlus10.apply {
                visibility = View.VISIBLE
                startBestAnimation()
            }
        } else {
            if (lifeLeft > 0) {
                when (lifeLeft) {
                    1 -> ivLife2.alpha = 0.0f
                    2 -> ivLife3.alpha = 0.0f
                }
            } else {
                ivLife1.alpha = 0.0f
                endGame(points)
            }
            lifeLeft--
        }

        if (number < 9) {
            number++
            ivMonkeyEnemy.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    arrayMonkeysComps[arrayMonkeysCompsOrder[number]]
                )
            )
        } else {
            endGame(points)
        }
    }

    private fun endGame(score: Int) {
        disableListeners()

        finalField.visibility = View.VISIBLE
        tvFinalScore.text = score.toString()

        btnAgain.setOnClickListener(this)
        btnScores.setOnClickListener(this)

        var scoreModel = ScoreModel(id = score.toLong(), value = score).also {
            scoreVM.insert(it)
        }
    }

    private fun disableListeners() {
        arrayIvMonkey.forEach {
            it.setOnClickListener(null)
        }
    }
}

private fun ImageView.startBestAnimation() {
    val oa = ObjectAnimator.ofPropertyValuesHolder(
        this,
        PropertyValuesHolder.ofFloat("translationY", 0f, -200f),
        PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f)
    ).apply {
        interpolator = FastOutSlowInInterpolator()
        duration = 500
    }.start()
}
