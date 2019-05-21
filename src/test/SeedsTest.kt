package com.neelkamath.seeds.test

import com.neelkamath.seeds.Cell
import com.neelkamath.seeds.Seeds
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SeedsTest {
    @Test
    fun `a time step should progress the grid to its next generation`() = with(
        Seeds(
            listOf(
                mutableListOf(Cell.ALIVE, Cell.DEAD, Cell.DEAD),
                mutableListOf(Cell.DEAD, Cell.ALIVE, Cell.DEAD),
                mutableListOf(Cell.DEAD, Cell.DEAD, Cell.ALIVE)
            )
        )
    ) {
        step()
        assertEquals(
            listOf(
                mutableListOf(Cell.DEAD, Cell.ALIVE, Cell.DEAD),
                mutableListOf(Cell.ALIVE, Cell.DEAD, Cell.ALIVE),
                mutableListOf(Cell.DEAD, Cell.ALIVE, Cell.DEAD)
            ),
            grid
        )
    }

    @Test
    fun `the game should be over when there are zero alive cells`() =
        assertTrue(Seeds(listOf(mutableListOf(Cell.DEAD), mutableListOf(Cell.DEAD))).gameIsOver())
}