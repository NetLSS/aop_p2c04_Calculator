package lilcode.aop.p2.c04.calculator.dao

import androidx.room.Dao
import androidx.room.Query
import lilcode.aop.p2.c04.calculator.model.History

// room에 연결된 Dao
// entitiy 조회등 어떻게 할건지
@Dao
interface HistoryDao {

    @Query("SELECT * FROM history") // 쿼리문 작성
    fun getAll(): List<History>



}