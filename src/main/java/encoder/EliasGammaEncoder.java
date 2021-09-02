package encoder;

import bean.CodingType;
import core.InputBitStream;
import core.OutputBitStream;
import encoder.util.FileEncoderUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EliasGammaEncoder implements Encoder {

    @Override
    public void encode(InputStream reader, OutputStream writer) throws IOException {
        FileEncoderUtils.writeHeader(writer, CodingType.ELIAS_GAMMA);
        OutputBitStream bstream = new OutputBitStream(writer);
        int symbol = reader.read();
        while (symbol != -1) {
            symbol += 1; // Tratamento para reconhecer o simbolo zero
            int n = (int) (Math.log(symbol) / Math.log(2));
            bstream.writeBits(false, n);
            bstream.writeBit(true); // Stop bit
            bstream.writeByte((int) (symbol - Math.pow(2, n)), n);
            symbol = reader.read();
        }
        bstream.flush();
    }

    @Override
    public void decode(InputStream reader, OutputStream writer) throws IOException {
        InputBitStream bstream = new InputBitStream(reader);
        while(bstream.hasNext()) {
            int n = (int) bstream.countWhile(false);
            // Evita a escrita de lixo no final do arquivo
            if (!bstream.hasNext()) break;
            bstream.next(1); // Stop bit
            int suffix = bstream.next(n);
            int symbol = (int) Math.pow(2, n) + suffix - 1;
            writer.write(symbol);
        }
    }
    
}
