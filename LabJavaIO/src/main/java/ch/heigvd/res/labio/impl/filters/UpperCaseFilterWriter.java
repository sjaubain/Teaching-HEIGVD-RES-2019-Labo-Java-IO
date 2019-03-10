package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author Olivier Liechti
 */
public class UpperCaseFilterWriter extends FilterWriter {

    private static final int toUpperCaseAsciiOffset = -32;

    public UpperCaseFilterWriter(Writer wrappedWriter) {
        super(wrappedWriter);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        super.write(str.toUpperCase(), off, len);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        String str = new String(cbuf);
        this.write(str, off, len);
    }

    @Override
    public void write(int c) throws IOException {
        // If c is a lower case letter
        if(c >= 97 && c <= 122)
            c += toUpperCaseAsciiOffset;

        super.write((char) c);
    }

}
