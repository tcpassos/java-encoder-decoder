package errorcorrecting.hamming;

public class HammingUtils {

    public static int getHammingCodeword(int symbol) {
        // r1 r2 m1 r4 m2 m3 m4
        int r1 = getFirstBitHamming(symbol) << 6;
        int r2 = getSecondBitHamming(symbol) << 5;
        int r4 = getThirdBitHamming(symbol) << 3;
        int m1 = (symbol << 1) & 0b10000;
        int m234 = symbol & 0b0111;
        return r1 | r2 | r4 | m1 | m234;
    }

    public static int parseHammingCodeword(int codeword) {
        int r1 = (codeword >> 6) & 1;
        int r2 = (codeword >> 5) & 1;
        int r4 = (codeword >> 3) & 1;
        int m1 = (codeword >> 1) & 0b1000;
        int m234 = codeword & 0b0111;
        // TODO: Implementar tratamento de ruído
        return m1 | m234;
    }

    private static int getFirstBitHamming(int b) {
        return (b ^ 0b1101) % 2 == 0 ? 0 : 1;
    }

    private static int getSecondBitHamming(int b) {
        return (b ^ 0b0111) % 2 == 0 ? 0 : 1;
    }

    private static int getThirdBitHamming(int b) {
        return (b ^ 0b1101) % 2 == 0 ? 0 : 1;
    }

}
