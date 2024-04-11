package dam.a47500.helloworld

import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class MainActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var constraintLayout: ConstraintLayout

    //private val backgroundImages = intArrayOf(R.drawable.spring_background, R.drawable.summer_background)
    private val backgroundImages = intArrayOf(
        R.drawable.winter_background,
        R.drawable.spring_background,
        R.drawable.summer_background,
        R.drawable.fall_background
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //dados do calendario
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)

        // encontrar view pelo id
        constraintLayout = findViewById<ConstraintLayout>(R.id.constraintBackground)
        // definir background image de acordo com o mês actual
        constraintLayout.setBackgroundResource(backgroundImages[getCurrentSeasonIndex(currentMonth)])
        // encontrar calendarView pelo id
        calendarView = findViewById(R.id.calendarView)
        // definir um listener para a mudança de datas selecionadas
        calendarView.setOnDateChangeListener { _, _, month, _ ->
            changeBackgroundImage(month)
        }

    }

    private fun getCurrentSeasonIndex(month: Int): Int {
        return when (month) {
            // Inverno
            Calendar.DECEMBER, Calendar.JANUARY, Calendar.FEBRUARY -> 0
            // Primavera
            Calendar.MARCH, Calendar.APRIL, Calendar.MAY -> 1
            // Verão
            Calendar.JUNE, Calendar.JULY, Calendar.AUGUST -> 2
            // Outono
            else -> 3
        }
    }


    private fun changeBackgroundImage(month: Int) {
        val index = getCurrentSeasonIndex(month)
        constraintLayout.setBackgroundResource(backgroundImages[index])
    }

}