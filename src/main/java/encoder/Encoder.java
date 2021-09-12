package encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface com metodos para a codificacao e decodificacao de uma stream de dados.
 */
public interface Encoder {
    
    public void encode(InputStream reader, OutputStream writer) throws IOException;
    
    public void decode(InputStream reader, OutputStream writer) throws IOException;
    
}
