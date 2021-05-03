package lilcode.aop.p2.c04.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import kotlin.math.exp

class MainActivity : AppCompatActivity() {

    private val expressionTextView: TextView by lazy {
        findViewById<TextView>(R.id.expressionTextView)
    }

    private val resultTextView: TextView by lazy {
        findViewById<TextView>(R.id.resultTextView)
    }

    private var isOperator = false // 오퍼레이터 입력하다 왔는지 체크
    private var hasOperator = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // 다크 테마 NO
        setContentView(R.layout.activity_main)
    }

    fun buttonClicked(v: View) {
        when (v.id) {
            R.id.button0 -> numberButtonClicked("0")
            R.id.button1 -> numberButtonClicked("1")
            R.id.button2 -> numberButtonClicked("2")
            R.id.button3 -> numberButtonClicked("3")
            R.id.button4 -> numberButtonClicked("4")
            R.id.button5 -> numberButtonClicked("5")
            R.id.button6 -> numberButtonClicked("6")
            R.id.button7 -> numberButtonClicked("7")
            R.id.button8 -> numberButtonClicked("8")
            R.id.button9 -> numberButtonClicked("9")
            R.id.buttonPlus -> operatorButtonClicked("+")
            R.id.buttonMinus -> operatorButtonClicked("-")
            R.id.buttonMulti -> operatorButtonClicked("*")
            R.id.buttonDivide -> operatorButtonClicked("/")
            R.id.buttonModulo -> operatorButtonClicked("%")
        }
    }

    private fun numberButtonClicked(number: String) {

        if (isOperator) {
            expressionTextView.append(" ")
        }

        isOperator = false


        val expressionText = expressionTextView.text.split(" ")

        // 15자리 이상일 경우 연산 불가하도록 처리
        if (expressionText.isNotEmpty() && expressionText.last().length >= 15) {
            Toast.makeText(this, "최대 15자리 까지만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return
        } else if (expressionText.last().isEmpty() && number == "0") { // 0 이 가장 앞에 온 경우 처리
            Toast.makeText(this, "0은 제일 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        expressionTextView.append(number)

        // TODO resultTextView 실시간으로 계산 결과를 넣어야 하는 기능

    }

    private fun operatorButtonClicked(operator: String) {
        // 연산자 이미 입력된 경우 무시
        if (expressionTextView.text.isEmpty()) {
            return
        }

        when {
            isOperator -> {
                val text = expressionTextView.text.toString()
                expressionTextView.text = text.dropLast(1) + operator
            }
            hasOperator -> {
                // 지금은 데모 버전으로 연산자를 딱 한번만 사용할 수 있도록 제한
                Toast.makeText(this, "연산자는 한 번만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                expressionTextView.append(" $operator")
            }
        }

        val ssb = SpannableStringBuilder(expressionTextView.text)

        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.green)), // 컬러 자원 가져오기
            expressionTextView.text.length - 1, // 지금 추가한 연산자 한개 만 변경
            expressionTextView.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        ) // ?

        expressionTextView.text = ssb

        isOperator = true
        hasOperator = true
    }

    fun resultButtonClicked(v: View) {

    }

    fun historyButtonClicked(v: View) {

    }

    fun clearButtonClicked(v: View) {

    }
}

// 확장 함수 정의
fun String.isNumber(): Boolean {
    return try {
        this.toBigInteger() // 무한대 까지 저장 가능한 자료형
        true
    } catch (e: NumberFormatException) {
        false
    }
}