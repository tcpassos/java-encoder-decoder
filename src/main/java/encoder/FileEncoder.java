package encoder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import bean.CodingType;

public class FileEncoder {

    public String encode(String inputFileName, CodingType coding, int k) {
        String outputFileName = inputFileName.concat(".enc");
        try(BufferedReader reader = _getReader(inputFileName); OutputStreamWriter writer = _getWriter(outputFileName)) {
            writer.write(coding.getHeader());
            writer.write(k);
            Encoder encoder = _getEncoderByType(coding, k);
            encoder.encode(reader, writer);
        } catch (IOException ex) {
            System.exit(1);
        }
        return outputFileName;
    }
    
    public void decode(String inputFileName) {
        String outputFileName = inputFileName.concat(".dec");
        try(BufferedReader reader = _getReader(inputFileName); OutputStreamWriter writer = _getWriter(outputFileName)) {
            int coding = reader.read();
            int k = reader.read();
            Encoder encoder = _getEncoderByType(CodingType.findByHeader(coding), k);
            encoder.decode(reader, writer);
        } catch (IOException ex) {
            System.exit(1);
        }
    }
    
    private Encoder _getEncoderByType(CodingType coding, int k) {
        switch (coding) {
            case GOLOMB:
                return new GolombEncoder(k);
            case ELIAS_GAMMA:
                return new EliasGammaEncoder();
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
