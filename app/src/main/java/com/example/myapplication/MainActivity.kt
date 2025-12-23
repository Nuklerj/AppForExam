package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.example.myapplication.R

class MainActivity : AppCompatActivity() {

    // Список для хранения данных (Категория -> Сумма)
    private val expenses = mutableMapOf<String, Float>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etAmount = findViewById<EditText>(R.id.etAmount)
        val etCategory = findViewById<EditText>(R.id.etCategory)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val pieChart = findViewById<PieChart>(R.id.pieChart)

        btnAdd.setOnClickListener {
            val amount = etAmount.text.toString().toFloatOrNull() ?: 0f
            val category = etCategory.text.toString().trim() // Убираем пробелы

            if (category.isNotEmpty() && amount > 0) {
                // Обновляем сумму, если категория уже есть, или добавляем новую
                expenses[category] = expenses.getOrDefault(category, 0f) + amount
                updateChart(pieChart)

                // Очистка полей
                etAmount.text.clear()
                etCategory.text.clear()
            }
        }

        // Инициализация графика при первом запуске (если есть данные)
        updateChart(pieChart)
    }

    private fun updateChart(pieChart: PieChart) {
        val entries = mutableListOf<PieEntry>()

        // Преобразуем данные из Map в формат для графика
        for ((category, amount) in expenses) {
            entries.add(PieEntry(amount, category))
        }

        if (entries.isEmpty()) {
            pieChart.clear() // Если данных нет, очищаем график
            pieChart.invalidate()
            return
        }

        val dataSet = PieDataSet(entries, "Расходы по категориям")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList() // Набор цветов
        dataSet.valueTextSize = 16f
        dataSet.valueTextColor = android.graphics.Color.BLACK // Цвет текста на графике
        dataSet.sliceSpace = 2f // Пространство между секторами

        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.description.isEnabled = false // Убираем описание
        pieChart.centerText = "Расходы"
        pieChart.setEntryLabelColor(android.graphics.Color.BLACK) // Цвет текста категорий
        pieChart.setEntryLabelTextSize(12f) // Размер текста категорий
        pieChart.animateY(1000) // Анимация появления
        pieChart.invalidate() // Обновление графика
    }
}