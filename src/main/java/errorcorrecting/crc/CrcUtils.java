package errorcorrecting.crc;

import core.util.BitUtils;

public class CrcUtils implements PolynomialCodes {
    
    public static int calculateCrc8(int data) {
        int shift = BitUtils.bitSetLength(data) - 1;
        int crc = (data << 8);
        while (shift > 0) {
            crc ^= POLYNOMIAL_CRC8_ATM << shift;
            shift = BitUtils.bitSetLength(crc) - 9;
        }
        return crc & 0xFF;
    }
    
    public static boolean checkCrc8(int data, int remainder) {
        return calculateCrc8(data) == remainder;
    }
    
}
