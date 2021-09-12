package core.bitstream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InputBitStreamTest {
    
    private InputStream reader;
    private InputBitStream bstream;
    
    @BeforeEach
    public void setUp() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 0000 0001 0000 0011 0000 0111 0000 1111 0001 1111
        baos.write(1);
        baos.write(3);
        baos.write(7);
        baos.write(15);
        baos.write(31);
        reader = new ByteArrayInputStream(baos.toByteArray());
        bstream = new InputBitStream(reader);
    }
    
    @Test
    public void countWhileTest() throws IOException {
        // 1
        assertEquals(7, bstream.countWhile(false));
        assertEquals(0, bstream.countWhile(false));
        assertEquals(1, bstream.countWhile(true));
        assertEquals(0, bstream.countWhile(true));
        // 3
        assertEquals(6, bstream.countWhile(false));
        assertEquals(0, bstream.countWhile(false));
        assertEquals(2, bstream.countWhile(true));
        assertEquals(0, bstream.countWhile(true));
        // 7
        assertEquals(5, bstream.countWhile(false));
        assertEquals(0, bstream.countWhile(false));
        assertEquals(3, bstream.countWhile(true));
        assertEquals(0, bstream.countWhile(true));
        // 15
        assertEquals(4, bstream.countWhile(false));
        assertEquals(0, bstream.countWhile(false));
        assertEquals(4, bstream.countWhile(true));
        assertEquals(0, bstream.countWhile(true));
        // 31
        assertEquals(3, bstream.countWhile(false));
        assertEquals(0, bstream.countWhile(false));
        assertEquals(5, bstream.countWhile(true));
        assertEquals(0, bstream.countWhile(true));
    }
    
    @Test
    public void nextTest() throws IOException {
        assertEquals(0, bstream.next(0));
        assertEquals(0, bstream.next(4));
        assertEquals(1, bstream.next(4));
        assertEquals(3, bstream.next(8));
        assertEquals(7, bstream.next(8));
        assertEquals(0, bstream.next(0));
    }
}
