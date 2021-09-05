package errorcorrecting.crc;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CrcUtilsTest {
    
    @Test
    void generateCrc8Test() {
        assertEquals(0, CrcUtils.calculateCrc8(0));
        assertEquals(162, CrcUtils.calculateCrc8(87));
    }
    
}
