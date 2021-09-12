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
    
    /**
     * Muda o estado de um bit na posicao especificada
     *
     * @param data Valor original
     * @param index Index do bit a ser alterado
     * @return {@code int}
     */
    public static int toggleBit(int data, int index) {
        if (index < 0 || index > (Integer.SIZE - 1)) throw new IllegalArgumentException();
        return data ^ (1 << index);
    }
    
}
