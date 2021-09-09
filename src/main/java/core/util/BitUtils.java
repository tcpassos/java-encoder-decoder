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
     * Define o valor de um bit especificado como 1
     *
     * @param data Valor original
     * @param index Index do bit a ser setado
     * @return {@code int}
     */
    public static int setBit(int data, int index) {
        if (index < 0 || index > (Integer.SIZE - 1)) throw new IllegalArgumentException();
        return data | (1 << index);
    }
    
    /**
     * Converte um inteiro em um array de inteiros que podem assumir o valor de um ou zero
     * Exemplo: 3 => [1, 0, 1]
     * 
     * @param bitset Valor a ser convertido
     * @return {@code int[]}
     */
    public static int[] toBitArray(int bitset) {
        int[] bitArray = new int[bitSetLength(bitset)];
        for (int i=0; i<bitArray.length; i++) {
            bitArray[i] = (bitset >> (bitArray.length - i - 1)) & 1;
        }
        return bitArray;
    }
    
}
