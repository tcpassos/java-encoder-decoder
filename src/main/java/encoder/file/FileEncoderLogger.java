package encoder.file;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class FileEncoderLogger {
    
    private static Logger logger;
    
    public static Logger getLogger() throws IOException {
        if (Objects.nonNull(logger)) {
            return logger;
        }
        FileHandler handler = new FileHandler("file_encoder.log", true);
        logger = Logger.getLogger("File encoder/decoder");
        logger.addHandler(handler);
        return logger;
    }
    
}
