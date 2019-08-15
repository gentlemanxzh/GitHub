package com.gentleman.common

import org.junit.Assert
import org.junit.Test


class ExampleUnitTest {
    @Test
    fun testBoolean() {
        val result = getABoolean().yes {
           println("1")
        }.otherwise {
            println("2")
        }

        val result2 = getABoolean().no {
            println("2")
        }.otherwise {
            println("1")
        }
    }

    fun getABoolean() = false
}
