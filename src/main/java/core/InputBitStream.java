package core;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;

/**
 * Classe com metodos para leitura de uma stream de bits
 */
public class InputBitStream {

    private final int FILLED_BYTE = 0b11111111;
    private InputStream reader;
    private int currentByte;
    private int nextByte;
    private int remaining;

    public InputBitStream(InputStream reader) {
        this.reader = reader;
        this.remaining = 0;
    }

    /**
     * Percorre a stream de bits enquanto o proximo bit possuir o valor...
     * informado retornando a quantidade de bits percorridos
     *
     * @param value Valor dos bits
     * @return Quantidade dos bits com o valor informado
     * @throws IOException
     */
    public long countWhile(boolean value) throws IOException {
        long count = 0;
        while (hasNext()) {
            if (value ^ isNextBitSet()) break;
            count++;
            nextBit();
        }
        return count;
    }

    /**
     * Retorna o valor alocado nos proximos n bits da stream
     *
     * @param length Comprimento do byte a ser retornado
     * @return Byte alocado nos proximos n bits da stream
     * @throws IOException 
     */
    public int next(int length) throws IOException {
        if (length > Byte.SIZE) {
            throw new InvalidParameterException();
        }
        if (!hasNext()) {
            return -1;
        }
        int byteToReturn = currentByte >> (Byte.SIZE - length);
        _shiftLeft(length);
        remaining -= length;
        return byteToReturn;
    }
    
    /**
     * Retorna o valor do proximo bit da stream
     *
     * @return {@code boolean}
     * @throws IOException
     */
    public boolean nextBit() throws IOException {
        return next(1) != 0;
    }
    
    /**
     * Indica se o proximo bit da stream esta setado como true
     *
     * @return {@code boolean}
     * @throws IOException
     */
    public boolean isNextBitSet() throws IOException {
        if (!hasNext()) {
            return false;
        }
        int shift = remaining >= Byte.SIZE ? Byte.SIZE - 1 : remaining - 1;
        int nextBit = currentByte >> shift;
        return nextBit > 0;
    }

    /**
     * Indica se existem bits restantes na stream
     *
     * @return {@code boolean}
     * @throws IOException 
     */
    public boolean hasNext() throws IOException {
        _fetchNextByte();
        return remaining > 0;
    }

    /**
     * Carrega o proximo byte no buffer se necessario
     *
     * @throws IOException 
     */
    private void _fetchNextByte() throws IOException {
        if (remaining > Byte.SIZE) {
            return;
        }
        int next = reader.read();
        if (next != -1) {
            nextByte = next;
            currentByte |= nextByte >> remaining;
            nextByte = (nextByte << (Byte.SIZE - remaining)) & FILLED_BYTE;
            remaining += Byte.SIZE;
        }
    }

    /**
     * Desloca os bytes do buffer n bits para esquerda
     *
     * @param shift Quantidade de bits a serem deslocados
     */
    private void _shiftLeft(int shift) {
        currentByte = (currentByte << shift) & FILLED_BYTE;
        currentByte |= nextByte >> (Byte.SIZE - shift);
        nextByte = (nextByte << shift) & FILLED_BYTE;
    }

}
