package encoder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import bean.CodingType;
import encoder.util.FileEncoderUtils;
import java.io.InputStream;
import java.io.OutputStream;

public class FileEncoder {

    public String encode(String inputFileName, CodingType coding, int k) {
        String outputFileName = inputFileName.concat(".enc");
        try(InputStream reader = new FileInputStream(inputFileName);
            OutputStream writer = new FileOutputStream(outputFileName)) {
            Encoder encoder = FileEncoderUtils.getEncoder(coding, k);
            encoder.encode(reader, writer);
        } catch (IOException ex) {
            System.exit(1);
        }
        return outputFileName;
    }
    
    public void decode(String inputFileName) {
        String outputFileName = inputFileName.concat(".dec");
        try(InputStream reader = new FileInputStream(inputFileName);
            OutputStream writer = new FileOutputStream(outputFileName)) {
            int coding = reader.read();
            int k = reader.read();
            Encoder encoder = FileEncoderUtils.getEncoder(CodingType.findByHeader(coding), k);
            encoder.decode(reader, writer);
        } catch (IOException ex) {
            System.exit(1);
        }
    }

}
