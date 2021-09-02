package core;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidParameterException;

/**
 * Classe com metodos de escrita em uma stream de bits.
 */
public class OutputBitStream {
    
    private final int EMPTY_BYTE = 0b00000000;
    private final int FILLED_BYTE = 0b11111111;
    
    private OutputStream writer;
    private int buffer;
    private int allocatedInBuffer;
    
    public OutputBitStream(OutputStream writer) {
        this.writer = writer;
        this.allocatedInBuffer = 0;
    }
    
    /**
     * Escreve um único bit na stream
     *
     * @param bit Valor do bit
     * @throws IOException
     */
    public void writeBit(boolean bit) throws IOException {
        writeByte(bit ? 1 : 0, 1);
    }
    
    /**
     * Escreve um bit n vezes na stream
     *
     * @param bit Valor dos bits
     * @param n Comprimento da faixa de bits
     * @throws IOException
     */
    public void writeBits(boolean bit, int n) throws IOException {
        for(int i=0; i<n; i++) writeBit(bit);
    }
    
    /**
     * Escreve um byte na stream
     *
     * @param value Valor do byte
     * @throws IOException
     */
    public void writeByte(int value) throws IOException {
        writeByte(value, Byte.SIZE);
    }
    
    /**
     * Escreve um byte com um tamanho determinado
     *
     * @param value Valor do byte
     * @param size Tamanho do byte
     * @throws IOException 
     */
    public void writeByte(int value, int size) throws IOException {
        if (size > Byte.SIZE) throw new InvalidParameterException();
        value = _applyLengthMask(value, size);
        int shift = allocatedInBuffer + size - Byte.SIZE;
        buffer = buffer | _shift(value, shift);
        allocatedInBuffer += size;
        if (_write()) {
            buffer = _shift(value, allocatedInBuffer - Byte.SIZE);
        }    
    }
    
    /**
     * Escreve um array de bytes na stream
     *
     * @param bytes Array de bytes
     * @throws IOException
     */
    public void writeBytes(byte[] bytes) throws IOException {
        for(byte b: bytes) writeByte(b);
    }
    
    /**
     * Forca a escrita dos bits restantes da stream
     *
     * @throws IOException
     */
    public void flush() throws IOException {
        if (buffer != EMPTY_BYTE) {
            // System.out.print(String.format("%8s", Integer.toBinaryString(buffer)).replace(' ', '0'));
            writer.write(buffer);
        }
    }
    
    /**
     * Retorna o resultado de uma operacao de mascara com um byte para o comprimento especificado
     *
     * @param value Valor do byte
     * @param length Comprimento do byte
     * @return Byte apos a operacao
     */
    private int _applyLengthMask(int value, int length) {
        int mask = FILLED_BYTE & (FILLED_BYTE ^ (FILLED_BYTE << length));
        return (value & mask);
    }
    
    /**
     * Desloca o byte informado para esquerda se o deslocamento for negativo ou para direita caso positivo
     *
     * @param value Valor do byte a ser deslocado
     * @param shift Numero de bits para deslocamento
     * @return Byte deslocado
     */
    private int _shift(int value, int shift) {
        if (shift > 0) {
            return value >> shift;
        }
        return FILLED_BYTE & (value << Math.abs(shift));
    }
    
    /**
     * Escreve o byte do buffer no arquivo caso ja tenha atingido 8 bits
     *
     * @return {@code boolean}
     * @throws IOException
     */
    private boolean _write() throws IOException {
        if (allocatedInBuffer < Byte.SIZE) {
            return false;
        }
        // System.out.print(String.format("%8s", Integer.toBinaryString(buffer)).replace(' ', '0'));
        writer.write(buffer);
        allocatedInBuffer -= Byte.SIZE;
        buffer = EMPTY_BYTE;
        return true;
    }
    
}
