package com.mudounet.utils.video.external;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A helper class for wrapping input and output streams returned from remote
 * processes. These can used for a quick and dirty form of RMI between the Java
 * VMs.
 * @author gmanciet
 */
public class StreamWrapper {

    private InputStream inputStream;
    private OutputStream outputStream;

    /**
     * Create a new stream wrapper.
     * @param inputStream the input stream to wrap.
     * @param outputStream the output stream to wrap.
     */
    StreamWrapper(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    /**
     * Get the input stream.
     * @return the input stream.
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Get the output stream.
     * @return the output stream.
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }
}
