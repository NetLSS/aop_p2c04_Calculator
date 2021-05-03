package lilcode.aop.p2.c04.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // 다크 테마 NO
        setContentView(R.layout.activity_main)
    }

    fun buttonClicked(v: View){

    }

    fun resultButtonClicked(v: View){

    }

    fun historyButtonClicked(v: View){

    }

    fun clearButtonClicked(v: View){

    }
}