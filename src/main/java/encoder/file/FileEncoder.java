package encoder.file;

import core.util.FileUtils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import encoder.bean.CodingType;
import encoder.Encoder;
import encoder.HammingEncoder;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class FileEncoder {

    public File encode(File inputFile, CodingType coding, int k) throws IOException {
        File outputFile = FileUtils.appendExtension(inputFile, "enc");
        try(InputStream reader = new FileInputStream(inputFile);
            OutputStream writer = new FileOutputStream(outputFile)) {
            Encoder encoder = FileEncoderUtils.getEncoder(coding, k);
            encoder.encode(reader, writer);
        } catch (IOException ex) {
            FileEncoderLogger.getLogger().severe(ex.getMessage());
            throw new IOException(ex);
        }
        return outputFile;
    }

    public void decode(File inputFile) throws IOException {
        File outputFile = FileUtils.changeExtension(inputFile, "dec");
        try(InputStream reader = new FileInputStream(inputFile);
            OutputStream writer = new FileOutputStream(outputFile)) {
            int coding = reader.read();
            int k = reader.read();
            Encoder encoder = FileEncoderUtils.getEncoder(CodingType.findByHeader(coding), k);
            encoder.decode(reader, writer);
        } catch (IOException ex) {
            FileEncoderLogger.getLogger().severe(ex.getMessage());
            throw new IOException(ex);
        }
    }
    
    public File generateEcc(File inputFile) throws IOException {
        File outputFile = FileUtils.appendExtension(inputFile, "ecc");
        try(InputStream reader = new FileInputStream(inputFile);
            OutputStream writer = new FileOutputStream(outputFile)) {
            Encoder encoder = new HammingEncoder();
            encoder.encode(reader, writer);
        } catch (IOException ex) {
            FileEncoderLogger.getLogger().severe(ex.getMessage());
            throw new IOException(ex);
        }
        return outputFile;
    }
    
    public File extractEcc(File inputFile) throws IOException {
        File outputFile = FileUtils.removeExtension(inputFile);
        try(InputStream reader = new FileInputStream(inputFile.getAbsolutePath());
            OutputStream writer = new FileOutputStream(outputFile)) {
            Encoder encoder = new HammingEncoder();
            encoder.decode(reader, writer);
        } catch (IOException ex) {
            FileEncoderLogger.getLogger().severe(ex.getMessage());
            throw new IOException(ex);
        }
        return outputFile;
    }

}
