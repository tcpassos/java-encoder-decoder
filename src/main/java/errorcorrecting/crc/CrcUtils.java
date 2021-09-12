package errorcorrecting.crc;

import core.util.BitUtils;

/**
 * Classe responsavel pelo tratamento de erro CRC.
 */
public class CrcUtils implements PolynomialCodes {
    
    /**
     * Calcula o resto da divisao polinomial CRC-8 AMT
     *
     * @param data Informacao a ser dividida
     * @return {@code int}
     */
    public static int calculateCrc8(int data) {
        int shift = BitUtils.bitSetLength(data) - 1;
        int crc = (data << 8);
        while (shift > 0) {
            crc ^= POLYNOMIAL_CRC8_ATM << shift;
            shift = BitUtils.bitSetLength(crc) - 9;
        }
        return crc & 0xFF;
    }

    /**
     * Verifica se o resto da divisao polinomial CRC-8 esta de acordo com o esperado
     *
     * @param data Informacao a ser processada
     * @param remainder Resto da divisao esperado
     * @return {@code boolean}
     */
    public static boolean checkCrc8(int data, int remainder) {
        return calculateCrc8(data) == remainder;
    }
    
}
