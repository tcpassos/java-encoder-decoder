package encoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import bean.CodingType;
import java.io.InputStream;
import java.io.OutputStream;

public class FileEncoder {

    public String encode(String inputFileName, CodingType coding, int k) {
        String outputFileName = inputFileName.concat(".enc");
        try(InputStream reader = _getReader(inputFileName); OutputStream writer = _getWriter(outputFileName)) {
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
        try(InputStream reader = _getReader(inputFileName); OutputStream writer = _getWriter(outputFileName)) {
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
            case FIBONACCI:
                return new FibonacciEncoder();
            default:
                throw new IllegalArgumentException();
        }
    }

    private InputStream _getReader(String fileName) throws IOException {
        return new FileInputStream(fileName);
    }
    
    private OutputStream _getWriter(String fileName) throws FileNotFoundException {
        return new FileOutputStream(fileName);
    }

}
