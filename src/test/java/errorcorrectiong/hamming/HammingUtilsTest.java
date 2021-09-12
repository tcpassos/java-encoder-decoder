package errorcorrectiong.hamming;

import errorcorrecting.hamming.HammingUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HammingUtilsTest {
    
    @Test
    void getCodewordTest() {
        assertEquals(0, HammingUtils.getCodeword(0));
        assertEquals(41, HammingUtils.getCodeword(1));
        assertEquals(75, HammingUtils.getCodeword(3));
        assertEquals(39, HammingUtils.getCodeword(7));
        assertEquals(127, HammingUtils.getCodeword(15));
    }
    
    @Test
    void parseCodewordTest() {
        assertEquals(0, HammingUtils.parseCodeword(0));
        assertEquals(1, HammingUtils.parseCodeword(41));
        assertEquals(3, HammingUtils.parseCodeword(75));
        assertEquals(7, HammingUtils.parseCodeword(39));
        assertEquals(15, HammingUtils.parseCodeword(127));
    }
    
    @Test
    void parseAndCorrectCodewordTest() {
        assertEquals(0, HammingUtils.parseAndCorrectCodeword(0));
        assertEquals(1, HammingUtils.parseAndCorrectCodeword(41));
        assertEquals(1, HammingUtils.parseAndCorrectCodeword(43));
        assertEquals(3, HammingUtils.parseAndCorrectCodeword(75));
        assertEquals(3, HammingUtils.parseAndCorrectCodeword(67));
        assertEquals(7, HammingUtils.parseAndCorrectCodeword(39));
        assertEquals(7, HammingUtils.parseAndCorrectCodeword(7));
        assertEquals(15, HammingUtils.parseAndCorrectCodeword(127));
        assertEquals(15, HammingUtils.parseAndCorrectCodeword(119));
    }
    
}
