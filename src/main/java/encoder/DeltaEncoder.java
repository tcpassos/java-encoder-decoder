package encoder;

import bean.CodingType;
import core.InputBitStream;
import core.OutputBitStream;
import encoder.util.FileEncoderUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DeltaEncoder implements Encoder {
    
    private final int NOT_CHANGED = 0b00;
    private final int END_OF_FILE = 0b01;
    private final int ADDED       = 0b10;
    private final int SUBTRACTED  = 0b11;
    
    private int suffixLength;

    public DeltaEncoder() {
        this.suffixLength = 0;
    }

    public DeltaEncoder(int suffixLength) {
        this.suffixLength = suffixLength;
    }

    @Override
    public void encode(InputStream inputStream, OutputStream writer) throws IOException {
        InputStream reader = _loadLengthAndgetStream(inputStream);
        FileEncoderUtils.writeHeader(writer, CodingType.DELTA, suffixLength);
        int current = reader.read();
        int previous;
        if (current != -1) writer.write(current); // Primeiro simbolo
        OutputBitStream bstream = new OutputBitStream(writer);
        while (current != -1) {
            previous = current;
            current = reader.read();
            if (current == -1) {
                _writeInstruction(END_OF_FILE, bstream);
            } else if (current == previous) {
                _writeInstruction(NOT_CHANGED, bstream);
            } else {
                _writeInstruction(current > previous ? ADDED : SUBTRACTED, bstream);
                bstream.writeByte(Math.abs(current - previous) - 1, suffixLength);
            }
        }
        bstream.flush();
    }

    @Override
    public void decode(InputStream reader, OutputStream writer) throws IOException {
        int current = reader.read(); // Primeiro simbolo
        int previous = current;
        InputBitStream bstream = new InputBitStream(reader);
        if (!bstream.hasNext()) {
            return;
        }
        writer.write(current);
        while(bstream.hasNext()) {
            switch (bstream.next(2)) {
                case NOT_CHANGED:
                    break;
                case ADDED:
                    current = previous + (bstream.next(suffixLength) + 1);
                    break;
                case SUBTRACTED:
                    current = previous - (bstream.next(suffixLength) + 1);
                    break;
                case END_OF_FILE:
                    return;
            }
            previous = current;
            writer.write(current);
        }
    }
    
    private InputStream _loadLengthAndgetStream(InputStream reader) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int biggestDelta = 0;
        int current = _readByteIntoByteArray(reader, baos);
        int previous = current;
        while (current != -1) {
            int currentDelta = current == previous ? 0 : Math.abs(current - previous) - 1;
            if (currentDelta > biggestDelta) {
                biggestDelta = currentDelta;
            }
            previous = current;
            current = _readByteIntoByteArray(reader, baos);
        }
        suffixLength = (int)(Math.log(biggestDelta) / Math.log(2)) + 1;
        return new ByteArrayInputStream(baos.toByteArray());
    }
    
    private void _writeInstruction(int instruction, OutputBitStream bstream) throws IOException {
        bstream.writeByte(instruction, 2);
    }
    
    private int _readByteIntoByteArray(InputStream reader, ByteArrayOutputStream baos) throws IOException {
        int symbol = reader.read();
        if (symbol != -1) {
            baos.write(symbol);
        }
        return symbol;
    }
    
}
