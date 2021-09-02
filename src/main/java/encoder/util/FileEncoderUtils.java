package encoder.util;

import bean.CodingType;
import encoder.DeltaEncoder;
import encoder.EliasGammaEncoder;
import encoder.Encoder;
import encoder.FibonacciEncoder;
import encoder.GolombEncoder;
import encoder.UnaryEncoder;
import java.io.IOException;
import java.io.OutputStream;

public class FileEncoderUtils {
    
    public static Encoder getEncoder(CodingType coding, int param) {
        switch (coding) {
            case GOLOMB:
                return new GolombEncoder(param);
            case ELIAS_GAMMA:
                return new EliasGammaEncoder();
            case FIBONACCI:
                return new FibonacciEncoder();
            case UNARY:
                return new UnaryEncoder();
            case DELTA:
                return new DeltaEncoder(param);
            default:
                throw new IllegalArgumentException();
        }
    }
    
    public static void writeHeader(OutputStream writer, CodingType coding) throws IOException {
        writeHeader(writer, coding, 0);
    }

    public static void writeHeader(OutputStream writer, CodingType coding, int param) throws IOException {
        writer.write(coding.getHeader());
        writer.write(param);
    }
    
}
