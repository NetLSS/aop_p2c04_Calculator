package lilcode.aop.p2.c04.calculator

import androidx.room.Database
import androidx.room.RoomDatabase
import lilcode.aop.p2.c04.calculator.dao.HistoryDao
import lilcode.aop.p2.c04.calculator.model.History

// 데이터 베이스가 만들어질때 히스토리 테이블을 사용하겠다고 등록
// 그리고 버전을 작성해주어야함. 앱 업데이트 할 경우 디비가 바뀔 수 있는데 변경이 되었을 때
// 최신 버전 디비로 마이그레이션을 해주어셔 데이터가 날라가지 않게.
@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() { // 추상 클래스
    abstract fun historyDao(): HistoryDao // AppDatabase 생성시 HistoryDao를 가져가서 사용할 수 있게
}