package lilcode.aop.p2.c04.calculator.model

import androidx.room.ColumnInfo
import androidx.room.Entity // room 사용
import androidx.room.PrimaryKey

// 데이터 클래스로 선언
@Entity // room 사용(room의 데이터 클래스) db 테이블
data class History(
    @PrimaryKey val uid: Int?, // 유니크한 아이디 (기본키로 사용되는 듯)
    @ColumnInfo(name = "expression") val expression: String?,
    @ColumnInfo(name = "result") val result: String?
)