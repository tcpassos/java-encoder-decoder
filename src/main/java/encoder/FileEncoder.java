package encoder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class FileEncoder {
    
    private final int ENCODING_TYPE_GOLOMB = 0;
    
    public String encodeGolomb(String inputFileName, int k) {
        return _encode(inputFileName, ENCODING_TYPE_GOLOMB, k);
    }
    
    public void decode(String inputFileName) {
        String outputFileName = inputFileName.concat(".dec");
        try(BufferedReader reader = _getReader(inputFileName); OutputStreamWriter writer = _getWriter(outputFileName)) {
            Encoder encoder = _getDecoder(reader);
            encoder.decode(reader, writer);
        } catch (IOException ex) {
            System.exit(1);
        }
    }
    
    private String _encode(String inputFileName, int encodingType, int k) {
        String outputFileName = inputFileName.concat(".enc");
        try(BufferedReader reader = _getReader(inputFileName); OutputStreamWriter writer = _getWriter(outputFileName)) {
            writer.write(encodingType);
            writer.write(k);
            Encoder encoder = new GolombEncoder(k);
            encoder.encode(reader, writer);
        } catch (IOException ex) {
            System.exit(1);
        }
        return outputFileName;
    }

    private Encoder _getDecoder(BufferedReader reader) throws IOException {
        int encoding = reader.read();
        int k = reader.read();
        switch (encoding) {
            case ENCODING_TYPE_GOLOMB:
                return new GolombEncoder(k);
            default:
                throw new IllegalArgumentException();
        }
    }

    private BufferedReader _getReader(String fileName) throws IOException {
        FileInputStream inputStream = new FileInputStream(fileName);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8.name());
        return new BufferedReader(inputStreamReader);
    }
    
    private OutputStreamWriter _getWriter(String fileName) throws FileNotFoundException {
        FileOutputStream outputStream = new FileOutputStream(fileName);
        return new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
    }

}
