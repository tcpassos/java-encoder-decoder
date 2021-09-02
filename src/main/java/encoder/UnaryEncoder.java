package encoder;

import core.InputBitStream;
import core.OutputBitStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UnaryEncoder implements Encoder {

    @Override
    public void encode(InputStream reader, OutputStream writer) throws IOException {
        OutputBitStream bstream = new OutputBitStream(writer);
        int symbol = reader.read();
        while (symbol != -1) {
            bstream.writeBits(false, symbol);
            bstream.writeBit(true);
            symbol = reader.read();
        }
        bstream.writeRemaining();
    }

    @Override
    public void decode(InputStream reader, OutputStream writer) throws IOException {
        InputBitStream bstream = new InputBitStream(reader);
        while(bstream.hasNext()) {
            int symbol = (int) bstream.countWhile(false);
            if (!bstream.isNextBitSet()) break;
            bstream.nextBit();
            writer.write(symbol);
        }
    }
    
}
