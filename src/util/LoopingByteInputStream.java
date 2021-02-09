package util;

import java.io.ByteArrayInputStream;
import java.io.IOException;


/**
 * Class <code>LoopingByteInputStream</code> adalah extends dari class 
 * <code>ByteArrayInputStream</code> yang loop terus menerus. Loop akan berhenti
 * ketika method <code>stop()</code> dipanggil.
 */
public class LoopingByteInputStream extends ByteArrayInputStream {
    private boolean closed;

    /**
     * Membuat object baru.
     * @param buffer <code>byte[]</code> yang ditetapkan. Array tidak dicopy
     */
    public LoopingByteInputStream(byte[] buffer) {
        super(buffer);
        closed = false;
    }

    public int read(byte[] buffer, int offset, int length) {
        if (closed) {
            return -1;
        }
        int totalBytesRead = 0;

        while (totalBytesRead < length) {
            int numBytesRead = super.read(buffer,
                offset + totalBytesRead,
                length - totalBytesRead);

            if (numBytesRead > 0) {
                totalBytesRead += numBytesRead;
            }
            else {
                reset();
            }
        }
        return totalBytesRead;
    }

    /**
     * Perulangan akan berhenti jika method ini dipanggil
     */
    public void close() throws IOException {
        super.close();
        closed = true;
    }

}
