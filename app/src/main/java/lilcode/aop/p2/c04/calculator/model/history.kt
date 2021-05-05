package lilcode.aop.p2.c04.calculator.model

import androidx.room.Entity // room 사용

// 데이터 클래스로 선언
@Entity // room 사용
data class History(
    val uid: Int?, // 유니크한 아이디
    val expression: String?,
    val result: String?
)