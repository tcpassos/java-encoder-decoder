package encoder;

import core.InputBitStream;
import core.OutputBitStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class EliasGammaEncoder implements Encoder {

    @Override
    public void encode(BufferedReader reader, OutputStreamWriter writer) throws IOException {
        OutputBitStream bstream = new OutputBitStream(writer);
        int symbol = reader.read();
        while (symbol != -1) {
            symbol += 1; // Tratamento para reconhecer o simbolo zero
            int n = (int) (Math.log(symbol) / Math.log(2));
            bstream.writeBits(false, n);
            bstream.writeBit(true); // Stop bit
            bstream.writeByte((byte)(symbol - Math.pow(2, n)), n);
            symbol = reader.read();
        }
        bstream.writeRemaining();
    }

    @Override
    public void decode(BufferedReader reader, OutputStreamWriter writer) throws IOException {
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
