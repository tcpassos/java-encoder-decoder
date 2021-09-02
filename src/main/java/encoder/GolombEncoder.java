package encoder;

import core.OutputBitStream;
import core.InputBitStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GolombEncoder implements Encoder {
    
    private int divider;
    private int binarySequenceSize;
    
    public GolombEncoder(int divider) {
        this.divider = divider;
        binarySequenceSize = (int) (Math.log(divider) / Math.log(2));
    }

    @Override
    public void encode(InputStream reader, OutputStream writer) throws IOException {
        OutputBitStream bstream = new OutputBitStream(writer);
        int symbol = reader.read();
        while (symbol != -1) {
            bstream.writeBits(false, symbol / divider);
            bstream.writeBit(true); // Stop bit
            bstream.writeByte(symbol % divider, binarySequenceSize);
            symbol = reader.read();
        }
        bstream.writeRemaining();
    }
    
    @Override
    public void decode(InputStream reader, OutputStream writer) throws IOException {
        InputBitStream bstream = new InputBitStream(reader);
        while(bstream.hasNext()) {
            int unarySequenceLength = (int) bstream.countWhile(false);
            // Evita a escrita de lixo no final do arquivo
            if (!bstream.hasNext()) break;
            bstream.next(1); // Stop bit
            int remainder = bstream.next(binarySequenceSize);
            int symbol = unarySequenceLength * divider + remainder;
            writer.write(symbol);
        }
    }

}
