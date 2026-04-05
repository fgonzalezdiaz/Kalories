package com.example.myapplication.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityUsageReportsBinding
import com.example.myapplication.helpers.SumadorTiempoUso
import com.example.myapplication.helpers.UsageCo2Estimator
import com.example.myapplication.persistence.UsageSnapshot
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.coroutines.launch

class UsageReportsActivity : TrackedAppCompatActivity() {

    private lateinit var binding: ActivityUsageReportsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUsageReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackInformes.setOnClickListener { finish() }
        setupCharts()

        lifecycleScope.launch {
            val s = SumadorTiempoUso.repository?.getSnapshot() ?: return@launch
            applySnapshot(s)
        }
    }

    private fun setupCharts() {
        val textColor = getColor(R.color.text_secondary)

        binding.barChartTiempos.apply {
            description.isEnabled = false
            legend.textColor = textColor
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.textColor = textColor
            axisLeft.textColor = textColor
            axisRight.isEnabled = false
            setFitBars(true)
        }

        binding.pieChartDistribucion.apply {
            description.isEnabled = false
            legend.textColor = textColor
            setUsePercentValues(true)
            holeRadius = 40f
            setEntryLabelColor(getColor(R.color.text_primary))
        }

        binding.barChartHistorial.apply {
            description.isEnabled = false
            legend.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.textColor = textColor
            xAxis.granularity = 1f
            xAxis.labelCount = 2
            axisLeft.textColor = textColor
            axisRight.isEnabled = false
            setFitBars(true)
        }
    }

    private fun applySnapshot(s: UsageSnapshot) {
        binding.tvTiempoTotal.text = getString(
            R.string.tiempo_total_formato,
            formatDuration(s.totalForegroundMs),
        )
        binding.tvKwhEstimado.text = getString(
            R.string.kwh_estimado_formato,
            UsageCo2Estimator.estimatedKwh(s.totalForegroundMs),
        )
        binding.tvCo2Estimado.text = getString(
            R.string.co2_estimado_formato,
            UsageCo2Estimator.estimatedCo2Kg(s.totalForegroundMs),
        )

        val sorted = s.screenTimeMs.entries
            .filter { it.value > 0L }
            .sortedByDescending { it.value }
            .take(10)

        if (sorted.isEmpty()) {
            binding.barChartTiempos.clear()
            binding.barChartTiempos.invalidate()
            binding.pieChartDistribucion.clear()
            binding.pieChartDistribucion.invalidate()
        } else {
            val labels = sorted.map { shortenLabel(it.key) }
            val entries = sorted.mapIndexed { i, e ->
                BarEntry(i.toFloat(), e.value / 60_000f)
            }

            val barSet = BarDataSet(entries, getString(R.string.minutos_por_pantalla)).apply {
                colors = listOf(
                    getColor(R.color.primary),
                    getColor(R.color.secondary),
                    getColor(R.color.limeGreen),
                    getColor(R.color.iceBlue),
                    getColor(R.color.modernCoral),
                    getColor(R.color.warning),
                    getColor(R.color.info),
                )
                valueTextColor = getColor(R.color.text_primary)
                valueTextSize = 10f
            }
            val barData = BarData(barSet).apply { barWidth = 0.55f }
            binding.barChartTiempos.data = barData
            binding.barChartTiempos.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            binding.barChartTiempos.xAxis.labelRotationAngle = -28f
            binding.barChartTiempos.invalidate()

            val pieEntries = sorted.map { PieEntry(it.value.toFloat(), shortenLabel(it.key)) }
            val pieSet = PieDataSet(pieEntries, "").apply {
                sliceSpace = 2f
                colors = listOf(
                    getColor(R.color.primary),
                    getColor(R.color.secondary),
                    getColor(R.color.limeGreen),
                    getColor(R.color.iceBlue),
                    getColor(R.color.modernCoral),
                    getColor(R.color.warning),
                    getColor(R.color.info),
                )
                valueTextSize = 11f
                valueTextColor = getColor(R.color.text_primary)
            }
            val pieData = PieData(pieSet).apply {
                setValueFormatter(PercentFormatter(binding.pieChartDistribucion))
            }
            binding.pieChartDistribucion.data = pieData
            binding.pieChartDistribucion.invalidate()
        }

        val histEntries = listOf(
            BarEntry(0f, s.historialItemsAdded.toFloat()),
            BarEntry(1f, s.historialItemsRemoved.toFloat()),
        )
        val histSet = BarDataSet(histEntries, "").apply {
            colors = listOf(getColor(R.color.success), getColor(R.color.error))
            valueTextColor = getColor(R.color.text_primary)
            valueTextSize = 12f
        }
        val histData = BarData(histSet).apply { barWidth = 0.4f }
        binding.barChartHistorial.data = histData
        binding.barChartHistorial.xAxis.valueFormatter = IndexAxisValueFormatter(
            listOf(getString(R.string.items_anadidos), getString(R.string.items_eliminados)),
        )
        binding.barChartHistorial.invalidate()
    }

    private fun shortenLabel(name: String): String =
        if (name.length <= 14) name else name.take(12) + "..."

    private fun formatDuration(ms: Long): String {
        if (ms <= 0L) return "0 min"
        val minutos = ms / 60_000
        val h = minutos / 60
        val m = minutos % 60
        return if (h > 0) "${h}h ${m}min" else "${m}min"
    }
}
