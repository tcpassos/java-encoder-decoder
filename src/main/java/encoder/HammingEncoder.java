package encoder;

import core.bitstream.InputBitStream;
import core.bitstream.OutputBitStream;
import errorcorrecting.crc.CrcUtils;
import errorcorrecting.hamming.HammingUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HammingEncoder implements Encoder {

    @Override
    public void encode(InputStream reader, OutputStream writer) throws IOException {
        InputBitStream ibstream = new InputBitStream(reader);
        OutputBitStream obstream = new OutputBitStream(writer);
        int header = _getHeader(reader, writer);
        obstream.writeByte(CrcUtils.calculateCrc8(header)); // Byte com o resto CRC
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
        int header = _getHeader(reader, writer);
        int crc = reader.read(); // TODO: Implementar tratamento de erro
        while(ibstream.hasNext()) {
            int codeword = ibstream.next(7);
            int symbol = HammingUtils.parseHammingCodeword(codeword);
            obstream.writeByte(symbol, 4);
        }
    }
    
    private int _getHeader(InputStream reader, OutputStream writer) throws IOException {
        int coding = reader.read();
        writer.write(coding);
        int param = reader.read();
        writer.write(param);
        return (coding << Byte.SIZE) | param;
    }

}
