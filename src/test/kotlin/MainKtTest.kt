import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import java.io.File

class MainKtTest {

    @Test
    @DisplayName("Should return the correct count")
    fun mainTest() {
        val expected :Long = 4
        val hllc = HyperLogLogCounter();
        val theFile = File("addr.txt")
        val result = hllc.getCount(theFile);
        assertEquals(expected, result)
    }
}