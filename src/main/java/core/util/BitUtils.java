package core.util;

public class BitUtils {
    
    /**
     * Retorna o comprimento do set de bits informados
     * Tambem pode ser considerado a posicao do bit mais significativo
     *
     * @param bitset Bits
     * @return {@code int}
     */
    public static int bitSetLength(int bitset) {
        int length;
        for (length=0; bitset!=0; ++length) {
            bitset >>= 1;
        }
        return length;
    }
    
}
