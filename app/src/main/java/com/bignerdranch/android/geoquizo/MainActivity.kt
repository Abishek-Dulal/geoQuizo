package com.bignerdranch.android.geoquizo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CHEAT_CODE=0;


class MainActivity : AppCompatActivity() {

    private lateinit var trueButton:Button
    private lateinit var falseButton:Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var prevButton: Button
    private lateinit var cheatButton:Button

    private val quizViewModel:QuizViewModel by lazy{
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton=findViewById(R.id.false_button)
        nextButton=findViewById(R.id.next_button)
        questionTextView=findViewById(R.id.question_text_view)
        prevButton=findViewById(R.id.previous_button)
        cheatButton=findViewById(R.id.cheat_button)

        trueButton.setOnClickListener {
             quizViewModel.addAnswer(checkAnswer(true))
        }
        falseButton.setOnClickListener{
            quizViewModel.addAnswer(checkAnswer(false))
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext();
            updateQuestion()
            doactivate()
            checkFinish()
        }

        prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
            doactivate()
            checkFinish()

        }

        cheatButton.setOnClickListener {
            startActivityForResult(CheatActivity.newIntent(this,quizViewModel.currentQuestionAnswer), REQUEST_CHEAT_CODE)
        }



        updateQuestion()
    }

    fun checkAnswer(userAnswer:Boolean):Boolean{
        val correctAnswer=quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        trueButton.isClickable=false
        falseButton.isClickable=false
        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show()
        return correctAnswer
    }

     fun doactivate(){
         if(quizViewModel.hasanswered()){
             trueButton.isClickable=false;
             falseButton.isClickable=false;
         }else{
             trueButton.isClickable=true;
             falseButton.isClickable=true;
         }
     }

     fun checkFinish(){
         if(quizViewModel.hasfinished()){
             Toast.makeText(this,"test finish :mark ${quizViewModel.getTotalMark()}",Toast.LENGTH_LONG).show()
         }
     }


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outPersistentState.putInt(KEY_INDEX,  quizViewModel.currentIndex)

    }



    fun updateQuestion(){
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       if(resultCode!= Activity.RESULT_OK){
           return
       }

        if (requestCode== REQUEST_CHEAT_CODE){
            quizViewModel.isCheater =data?.getBooleanExtra(EXTRA_ANSWER_SHOWN,false)?:false;
        }
    }

}
