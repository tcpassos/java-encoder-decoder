package encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Encoder {
    
    public void encode(InputStream reader, OutputStream writer) throws IOException;
    
    public void decode(InputStream reader, OutputStream writer) throws IOException;
    
}
