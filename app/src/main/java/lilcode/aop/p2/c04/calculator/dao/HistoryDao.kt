package lilcode.aop.p2.c04.calculator.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import lilcode.aop.p2.c04.calculator.model.History

// room에 연결된 Dao
// entitiy 조회등 어떻게 할건지
@Dao
interface HistoryDao {

    @Query("SELECT * FROM history") // 쿼리문 작성
    fun getAll(): List<History>

    @Insert
    fun insertHistory(history: History)

    @Query("DELETE FROM history") // 테이블 전체 삭제
    fun deleteAll()

    @Delete // 해당 히스트로리만 제거
    fun delete(history: History)

    // 조건 가진 결과만 가져오기 (1개만)
    @Query("SELECT * FROM History WHERE result LIKE :result LIMIT 1")
    fun findByResult(result: String): History
}