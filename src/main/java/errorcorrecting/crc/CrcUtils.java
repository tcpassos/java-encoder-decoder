package errorcorrecting.crc;

import core.util.BitUtils;

public class CrcUtils implements PolynomialCodes {
    
    public static int calculateCrc8(int symbol) {
        int shift = BitUtils.bitSetLength(symbol) - 1;
        int crc = (symbol << 8);
        while (shift > 0) {
            crc ^= POLYNOMIAL_CRC8 << shift;
            shift = BitUtils.bitSetLength(crc) - 9;
        }
        return crc;
    }
    
}
