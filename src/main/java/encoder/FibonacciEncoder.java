package encoder;

import bean.CodingType;
import core.InputBitStream;
import core.OutputBitStream;
import encoder.util.FileEncoderUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FibonacciEncoder implements Encoder {
    
    private final int FILLED_BYTE = 0b11111111;
    
    private TreeMap<Integer, Integer> fibonacci;
    
    public FibonacciEncoder() {
        _loadFibonacciSequence();
    }

    @Override
    public void encode(InputStream reader, OutputStream writer) throws IOException {
        FileEncoderUtils.writeHeader(writer, CodingType.FIBONACCI);
        OutputBitStream bstream = new OutputBitStream(writer);
        int symbol = reader.read();
        while (symbol != -1) {
            symbol += 1;
            _writeCodeword(symbol, bstream);
            bstream.writeBit(true);
            symbol = reader.read();
        }
        bstream.flush();
    }

    @Override
    public void decode(InputStream reader, OutputStream writer) throws IOException {
        InputBitStream bstream = new InputBitStream(reader);
        int index = 0;
        int symbol = 0;
        while(bstream.hasNext()) {
            index += bstream.countWhile(false);
            bstream.nextBit();
            // Evita a escrita de lixo no final do arquivo
            if (!bstream.hasNext()) break;
            symbol += fibonacci.keySet().toArray(new Integer[0])[index];
            index++;
            if (bstream.isNextBitSet()) {
                bstream.nextBit(); // Stop bit
                writer.write(symbol - 1);
                index = 0;
                symbol = 0;
            }
        }
    }
    
    private void _loadFibonacciSequence() {
        fibonacci = new TreeMap<>();
        int previous;
        int current = 1;
        int next = 1;
        AtomicInteger count = new AtomicInteger();
        while(current <= FILLED_BYTE) {
            previous = current;
            current = next;
            fibonacci.put(current, count.getAndIncrement());
            next = previous + current;
        }
    }
    
    private int _writeCodeword(int value, OutputBitStream bstream) throws IOException {
        if (value == 0) return -1;
        Entry<Integer, Integer> entry = fibonacci.floorEntry(value);
        int valueToSubtract = entry.getKey();
        value -= valueToSubtract;
        int currentPosition = entry.getValue();
        int lastPosition = _writeCodeword(value, bstream);
        bstream.writeBits(false, currentPosition - lastPosition - 1);
        bstream.writeBit(true);
        return currentPosition;
    }
    
}
