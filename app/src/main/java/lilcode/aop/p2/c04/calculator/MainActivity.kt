package lilcode.aop.p2.c04.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.room.Room
import lilcode.aop.p2.c04.calculator.model.History
import java.lang.NumberFormatException
import kotlin.math.exp

class MainActivity : AppCompatActivity() {

    // 계산식 텍스트뷰
    private val expressionTextView: TextView by lazy {
        findViewById<TextView>(R.id.expressionTextView)
    }

    // 실시간결과 텍스트뷰
    private val resultTextView: TextView by lazy {
        findViewById<TextView>(R.id.resultTextView)
    }

    // 히스토리 레이아웃
    private val historyLayout: View by lazy {
        findViewById<View>(R.id.historyLayout)
    }

    // 히스토리 리니어 레이아웃
    private val historyLinearLayout: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.historyLinearLayout)
    }

    private var isOperator = false // 오퍼레이터 입력하다 왔는지 체크
    private var hasOperator = false // 현재는 연산자 1번만 사용 가능 하도록.

    lateinit var db: AppDatabase // 전역

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // 다크 테마 NO
        setContentView(R.layout.activity_main)

        // onCrate 시 db 변수에 앱데이터베이스 빌드해서 할당
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "historyDB"
        ).build()
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

        resultTextView.text = calculateExpression()

    }

    private fun operatorButtonClicked(operator: String) {
        // 연산자 이미 입력된 경우 무시
        if (expressionTextView.text.isEmpty()) {
            return
        }

        when {
            isOperator -> { // 연산자를 입력하다 온 경우에는 연산자 수정
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
        val expressionTexts = expressionTextView.text.split(" ")

        if (expressionTextView.text.isEmpty() || expressionTexts.size == 1) { // 1: 숫자만 들어온 경우
            return
        }

        if (expressionTexts.size != 3 && hasOperator) {
            // 숫자와 연산자까지만 입력되고 마지막 값이 안온경우
            Toast.makeText(this, "아직 완성되지 않은 수식입니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val expressionText = expressionTextView.text.toString()
        val resultText = calculateExpression()

        // 디비에 넣어주는 부분
        // DB 입출력 과정은 메인스레드 외 추가 스레드에서 해야함
        // Thread 에는 Runnable 구현체가 들어감
        Thread(Runnable {
            // uid: null 로주어도 기본키라 자동으로 +1되서 들어감
            db.historyDao().insertHistory(History(null, expressionText, resultText))
        }).start()

        resultTextView.text = ""
        expressionTextView.text = resultText // 계산 결과값 올리기

        isOperator = false
        hasOperator = false
    }

    private fun calculateExpression(): String {
        // ExpressionTextView 에서 가져온 내용으로 계산한 결과 반환
        val expressionTexts = expressionTextView.text.split(" ")

        if (hasOperator.not() || expressionTexts.size != 3) {
            return ""
        } else if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            return ""
        }

        val exp1 = expressionTexts[0].toBigInteger()
        val exp2 = expressionTexts[2].toBigInteger()
        val op = expressionTexts[1]

        return when (op) {
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "*" -> (exp1 * exp2).toString()
            "/" -> (exp1 / exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""
        }
    }

    fun historyButtonClicked(v: View) {

        historyLayout.isVisible = true

        historyLinearLayout.removeAllViews() // 리니어 레이아웃 하위에 있는 모든 뷰 삭제

        // 디비에서 모든 기록 가져오기
        // 뷰에 모든 기록 할당하기
        Thread(Runnable {
            db.historyDao().getAll().reversed().forEach {
                // 뷰 생성하여 넣어주기
                // 레이아웃 인플레이터 기능 사용 해보기
                // ui 스레드 열기
                runOnUiThread {
                    // 핸들러에 포스팅될 내용 작성

                    // R.layout.history_row 에서 인플레이트를 시킴., root랑 attachToRoot. 나중에 addview를 통해 붙일거라 null, false
                    val historyView =
                        LayoutInflater.from(this).inflate(R.layout.history_row, null, false)
                    historyView.findViewById<TextView>(R.id.expressionTextView).text = it.expression
                    historyView.findViewById<TextView>(R.id.resultTextView).text = "= ${it.result}"

                    historyLinearLayout.addView(historyView) // 뷰 추가
                }
            } // 리스트 뒤집어서 가져오기
        }).start()

    }

    fun closeHistoryButtonClicked(v: View) {
        historyLayout.isVisible = false
    }

    fun historyClearButtonClicked(v: View) {
        // 디비에서 모든 기록 삭제
        // 뷰에서 모든 기록 삭제
        historyLinearLayout.removeAllViews()
        Thread(Runnable {
            db.historyDao().deleteAll()
        }).start()
    }

    fun clearButtonClicked(v: View) {
        expressionTextView.text = ""
        resultTextView.text = ""
        isOperator = false
        hasOperator = false
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