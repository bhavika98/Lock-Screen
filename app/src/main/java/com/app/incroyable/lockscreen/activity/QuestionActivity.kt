package com.app.incroyable.lockscreen.activity

import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.adapter.QuestionAdapter
import com.app.incroyable.lockscreen.base.BaseActivity
import com.app.incroyable.lockscreen.base.BaseBindingActivity
import com.app.incroyable.lockscreen.databinding.ActivityQuestionBinding
import com.app.incroyable.lockscreen.enums.SecurityType
import com.app.incroyable.lockscreen.model.QuestionData
import com.app.incroyable.lockscreen.storage.getDefaultPreferences
import com.app.incroyable.lockscreen.storage.getPrefQuestionData
import com.app.incroyable.lockscreen.storage.prefLockSet
import com.app.incroyable.lockscreen.storage.prefPatternEnable
import com.app.incroyable.lockscreen.storage.prefPinEnable
import com.app.incroyable.lockscreen.storage.prefSavedAnswer
import com.app.incroyable.lockscreen.storage.prefSavedQuestion
import com.app.incroyable.lockscreen.storage.questionStorage
import com.app.incroyable.lockscreen.storage.setPrefQuestionData
import com.app.incroyable.lockscreen.storage.setPreferences
import com.app.incroyable.lockscreen.utilities.commonDialog
import com.app.incroyable.lockscreen.utilities.toastMsg

class QuestionActivity : BaseBindingActivity<ActivityQuestionBinding>() {

    private var securityType: SecurityType? = null
    private var questionList = ArrayList<QuestionData>()
    private lateinit var questionAdapter: QuestionAdapter
    private var tempQuestion: Int = 0
    private var tempAnswer: String = ""
    private var setPassword: Boolean = true

    override fun setBinding(): ActivityQuestionBinding {
        return ActivityQuestionBinding.inflate(layoutInflater)
    }

    override fun getActivityContext(): BaseActivity {
        return this@QuestionActivity
    }

    override fun initView() {
        super.initView()

        setPassword = intent.getBooleanExtra("setPassword", true)
        securityType = intent.getStringExtra("securityType")?.let { SecurityType.valueOf(it) }

        if (!setPassword) {
            tempQuestion = getDefaultPreferences(prefSavedQuestion) as Int
            tempAnswer = getDefaultPreferences(prefSavedAnswer) as String
        }

        with(mBinding) {
            questionRecyclerView.layoutManager =
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

            questionList = ArrayList()
            if (getPrefQuestionData().size == 0) {
                questionList = questionStorage()
                setPrefQuestionData(questionList)
            } else questionList = getPrefQuestionData()

            setQuestion()

            questionAdapter = QuestionAdapter(
                this@QuestionActivity,
                getDefaultPreferences(prefSavedQuestion) as Int,
                questionList
            )

            mBinding.questionRecyclerView.adapter = questionAdapter
        }
    }

    private fun setQuestion() {
        questionList.sortBy { it.quality }

        val updatedList: ArrayList<QuestionData> = ArrayList()
        var previousQuality = -1

        for (questions in questionList) {
            if (questions.quality != previousQuality) {
                val sectionHeader = QuestionData(-1, questions.quality, "", true)
                updatedList.add(sectionHeader)
                previousQuality = questions.quality
            }
            updatedList.add(questions)
        }

        questionList.clear()
        questionList.addAll(updatedList)
    }

    override fun initViewListener() {
        super.initViewListener()

        setClickListener(
            mBinding.clickBack, mBinding.clickDone, mBinding.addQuestion
        )
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            mBinding.clickBack -> {
                finish()
            }

            mBinding.clickDone -> {
                //After save it will save in preference
                if (setPassword)
                    saveAnswer()
                else
                    changeAnswer()
            }

            mBinding.addQuestion -> {
                //add new question
                addQuestion()
            }
        }
    }

    private fun changeAnswer() {
        setPreferences(prefSavedQuestion, tempQuestion)
        setPreferences(prefSavedAnswer, tempAnswer)
        toastMsg(getString(R.string.question_answer_changed))
        finish()
    }

    private fun saveAnswer() {
        if (tempQuestion != 0) {
            when (securityType) {
                SecurityType.NONE -> {}
                SecurityType.PIN -> setPreferences(prefPinEnable, true)
                SecurityType.PATTERN -> setPreferences(prefPatternEnable, true)
                else -> {}
            }
            val message =
                if (securityType == SecurityType.PIN || securityType == SecurityType.PATTERN) {
                    getString(R.string.lock_set_successfully)
                } else
                    getString(R.string.security_question_done)
            setPreferences(prefLockSet, true)
            setPreferences(prefSavedQuestion, tempQuestion)
            setPreferences(prefSavedAnswer, tempAnswer)
            toastMsg(message)
            finish()
        } else {
            toastMsg(getString(R.string.set_security_question))
        }
    }

    //Delete Question from add manual
    fun deleteQuestion(position: Int, data: QuestionData) {
        commonDialog(layoutResId = R.layout.dialog_common,
            cancelable = true,
            title = getString(R.string.delete_question),
            message = "",
            positiveClickListener = { dialog, _ ->
                questionList.removeAt(position)
                val tempList = getPrefQuestionData()
                val pos = tempList.indexOfFirst { it.position == data.position }
                tempList.removeAt(pos)
                setPrefQuestionData(tempList)
                if (questionList.filter { it.quality == 0 }.size <= 1) {
                    questionList.removeAt(0)
                }
                dialog.dismiss()
                questionAdapter.notifyDataSetChanged()
            },
            negativeClickListener = {
            })
    }

    //Set Answer to selected question
    fun giveAnswer(data: QuestionData) {
        commonDialog(layoutResId = R.layout.dialog_give_answer,
            cancelable = true,
            title = getString(R.string.give_answer),
            message = data.question,
            positiveClickListener = { dialog, view ->
                val dialogInput = view.findViewById<EditText>(R.id.dialog_input)
                if (dialogInput.text.toString().isNotEmpty()) {
                    dialog.dismiss()
                    tempQuestion = data.position
                    tempAnswer = dialogInput.text.toString()
                    questionAdapter.updateData(tempQuestion)
                } else toastMsg(getString(R.string.please_write_something))
            },
            negativeClickListener = { _ ->
            })
    }

    //Add Manual Question
    private fun addQuestion() {
        commonDialog(layoutResId = R.layout.dialog_add_question,
            cancelable = true,
            title = getString(R.string.add_question),
            message = getString(R.string.own_question),
            positiveClickListener = { dialog, view ->
                val dialogInput = view.findViewById<EditText>(R.id.dialog_input)
                if (dialogInput.text.toString().isNotEmpty()) {
                    dialog.dismiss()

                    val pos: Int = questionList.size + 1
                    val tempList = getPrefQuestionData()
                    val newItem = QuestionData(pos, 0, dialogInput.text.toString(), false)
                    tempList.add(newItem)
                    setPrefQuestionData(tempList)

                    questionList.clear()
                    questionList.addAll(tempList)
                    setQuestion()
                    questionAdapter.notifyDataSetChanged()

                    val position = questionList.indexOfFirst { it.position == pos }
                    mBinding.questionRecyclerView.smoothScrollToPosition(position)
                } else toastMsg(getString(R.string.please_write_something))
            },
            negativeClickListener = { _ ->
            })
    }
}