package encoder;

import core.InputBitStream;
import core.OutputBitStream;
import encoder.util.HammingUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HammingEncoder implements Encoder {

    private final int header;

    public HammingEncoder(int header) {
        this.header = header;
    }

    @Override
    public void encode(InputStream reader, OutputStream writer) throws IOException {
        InputBitStream ibstream = new InputBitStream(reader);
        OutputBitStream obstream = new OutputBitStream(writer);
        obstream.writeByte(header, 2);
        obstream.writeByte(0); // TODO: Inserir o CRC aqui
        while (ibstream.hasNext()) {
            int symbol = ibstream.next(4);
            int codeword = HammingUtils.getHammingCodeword(symbol);
            obstream.writeByte(codeword, 7);
        }
        obstream.flush();
    }

    @Override
    public void decode(InputStream reader, OutputStream writer) throws IOException {
        InputBitStream ibstream = new InputBitStream(reader);
        OutputBitStream obstream = new OutputBitStream(writer);
        while(ibstream.hasNext()) {
            int codeword = ibstream.next(7);
            int symbol = HammingUtils.parseHammingCodeword(codeword);
            obstream.writeByte(symbol, 4);
        }
    }

}
