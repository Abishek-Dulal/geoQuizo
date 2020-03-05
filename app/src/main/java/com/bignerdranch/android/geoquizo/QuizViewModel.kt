package com.bignerdranch.android.geoquizo

import android.util.Log
import androidx.lifecycle.ViewModel


class QuizViewModel : ViewModel() {

    private val questionBank= listOf<Question>(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))


     var isCheater=false;
     val answeredQuestions:HashMap<Int,Int> = hashMapOf()

    var currentIndex=0

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev(){
       currentIndex = if (currentIndex==0){
            questionBank.size-1
        }else{
            currentIndex-1
        }
    }

    fun addAnswer(answer:Boolean){
        val marks =  if (answer) 1 else 0
        answeredQuestions.put(currentIndex,marks)
    }

    fun hasanswered():Boolean{
       return currentIndex in answeredQuestions.keys
    }

    fun hasfinished():Boolean{
        return answeredQuestions.size==questionBank.size
    }

    fun getTotalMark():Int{
        return answeredQuestions.values.sum()
    }


}