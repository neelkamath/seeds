package com.neelkamath.seeds

import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.XYSeries
import org.knowm.xchart.style.Styler
import org.knowm.xchart.style.markers.Square
import java.awt.Color
import javax.swing.SwingUtilities

class Player(private val seeds: Seeds) {
    private val chart = XYChartBuilder().theme(Styler.ChartTheme.Matlab).title("Seed").build().apply {
        val data = ChartData(seeds.grid)
        addSeries("ALIVE", data.aliveAbscissas, data.aliveOrdinates)
        addSeries("DEAD", data.deadAbscissas, data.deadOrdinates)
        with(styler) {
            seriesMarkers = arrayOf(Square())
            defaultSeriesRenderStyle = XYSeries.XYSeriesRenderStyle.Scatter
            seriesColors = arrayOf(Color.WHITE, Color.BLACK)
            plotBackgroundColor = Color.BLACK
            isLegendVisible = false
            isXAxisTicksVisible = false
            isYAxisTicksVisible = false
            isPlotGridLinesVisible = false
        }
    }
    private val gui = SwingWrapper(chart).apply { displayChart() }
    private var generation = 0

    init {
        while (true) {
            Thread.sleep(25)
            seeds.step()
            if (seeds.gameIsOver()) {
                update {
                    chart.removeSeries("ALIVE")
                    chart.removeSeries("DEAD")
                }
                break
            }
            update {
                val data = ChartData(seeds.grid)
                chart.updateXYSeries("ALIVE", data.aliveAbscissas, data.aliveOrdinates, null)
                chart.updateXYSeries("DEAD", data.deadAbscissas, data.deadOrdinates, null)
            }
        }
    }

    private fun update(function: () -> Unit) = SwingUtilities.invokeLater {
        chart.title = "Generation ${++generation}"
        function()
        gui.repaintChart()
    }

    private class ChartData(grid: Grid) {
        private val coordinates =
            grid.mapIndexed { x, row -> row.mapIndexed { y, cell -> Coordinate(cell, y + 1, grid.size - x) } }.flatten()
        private val aliveCoordinates = coordinates.filter { it.cell == Cell.ALIVE }
        private val deadCoordinates = coordinates.filter { it.cell == Cell.DEAD }
        val aliveAbscissas = aliveCoordinates.map { it.x }.toMutableList()
        val aliveOrdinates = aliveCoordinates.map { it.y }.toMutableList()
        val deadAbscissas = deadCoordinates.map { it.x }.toMutableList()
        val deadOrdinates = deadCoordinates.map { it.y }.toMutableList()

        private data class Coordinate(val cell: Cell, val x: Int, val y: Int)
    }
}
