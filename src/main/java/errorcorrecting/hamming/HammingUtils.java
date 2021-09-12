package errorcorrecting.hamming;

import core.util.BitUtils;

/**
 * Classe responsavel pelo tratamento de erro Hamming.
 */
public class HammingUtils {

    /**
     * Retorna o codeword Hamming de 7 bits a partir de um simbolo de 4 bits
     *
     * @param symbol Simbolo a ser processado
     * @return {@code int}
     */
    public static int getCodeword(int symbol) {
        // Sequencia codificada: r1 r2 m1 r4 m2 m3 m4
        int r1 = getFirstBitHamming(symbol) << 6;
        int r2 = getSecondBitHamming(symbol) << 5;
        int r4 = getThirdBitHamming(symbol) << 3;
        int m1 = (symbol << 1) & 0b10000;
        int m234 = symbol & 0b0111;
        return r1 | r2 | r4 | m1 | m234;
    }

    /**
     * Retorna o simbolo decodificado de um codeword Hamming
     *
     * @param codeword Codeword Hamming
     * @return {@code int}
     */
    public static int parseCodeword(int codeword) {
        int m1 = (codeword >> 1) & 0b1000;
        int m234 = codeword & 0b0111;
        int symbol = m1 | m234;
        return symbol;
    }

    /**
     * Retorna o simbolo decodificado de um codeword Hamming corrigindo os bits inconsistentes
     *
     * @param codeword Codeword Hamming
     * @return {@code int}
     */
    public static int parseAndCorrectCodeword(int codeword) {
        int symbol = parseCodeword(codeword);
        int r1Check = ((codeword >> 6) & 1) ^ getFirstBitHamming(symbol);
        int r2Check = ((codeword >> 5) & 1) ^ getSecondBitHamming(symbol);
        int r4Check = ((codeword >> 3) & 1) ^ getThirdBitHamming(symbol);
        int hammingCheck = (r1Check << 2) | (r2Check << 1) | r4Check;
        // Se não houve alteracao nos bits da informacao
        if (hammingCheck == 0b000) {
            return symbol;
        }
        // m1 m2 m3 m4 <= bits da informacao
        //  3  2  1  0 <= posicao
        switch (hammingCheck) {
            case 0b101: // r1, r4 = m1
                symbol = BitUtils.toggleBit(symbol, 3);
                break;
            case 0b111: // r1, r2, r4 = m2
                symbol = BitUtils.toggleBit(symbol, 2);
                break;
            case 0b110: // r1, r2 = m3
                symbol = BitUtils.toggleBit(symbol, 1);
                break;
            case 0b011: // r2, r4 = m4
                symbol = BitUtils.toggleBit(symbol, 0);
                break;
        }
        return symbol;
    }

    private static int getFirstBitHamming(int b) {
        return Integer.bitCount(b & 0b1110) % 2 == 0 ? 0 : 1;
    }

    private static int getSecondBitHamming(int b) {
        return Integer.bitCount(b & 0b0111) % 2 == 0 ? 0 : 1;
    }

    private static int getThirdBitHamming(int b) {
        return Integer.bitCount(b & 0b1101) % 2 == 0 ? 0 : 1;
    }

}
