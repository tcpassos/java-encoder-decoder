package encoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public interface Encoder {
    
    public void encode(BufferedReader reader, OutputStreamWriter writer) throws IOException;
    
    public void decode(BufferedReader reader, OutputStreamWriter writer) throws IOException;
    
}
