package com.alfasoft.pers

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alfasoft.pers.database.ScoreViewModel
import com.alfasoft.pers.models.ScoreModel
import kotlinx.android.synthetic.main.activity_result.*

class ScoreActivity : AppCompatActivity() {
    private lateinit var scoreVM: ScoreViewModel
    private var mScores = ArrayList<ScoreModel>()
    private var array = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        scoreVM = ViewModelProvider(this).get(ScoreViewModel::class.java)
        scoreVM.allScore.observe(this, Observer { scores ->
            scores?.let {
                mScores.addAll(it)
                mScores.sortBy {it.value}
                mScores.reverse()
                var i = 0
                mScores.forEach {
                    if (i<10) {
                        array.add(it.value.toString())
                        Log.d("TAG", it.value.toString())
                        i++
                    }
                }

                val adapter = ArrayAdapter(this,
                    R.layout.item_listview, array)

                listViewScore.setAdapter(adapter)
            }
        })
    }
}
