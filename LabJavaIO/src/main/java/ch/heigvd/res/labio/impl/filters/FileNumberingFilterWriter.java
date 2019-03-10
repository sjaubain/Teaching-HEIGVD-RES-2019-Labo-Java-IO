package ch.heigvd.res.labio.impl.filters;

import ch.heigvd.res.labio.impl.Utils;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 * <p>
 * Hello\n\World -> 1\tHello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

    /**
     * Number of lines already written
     */
    private int nbLines;

    /**
     * To know if we have to write the line number (true by default)
     */
    private boolean newLine;

    private boolean CRfound;

    private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());

    public FileNumberingFilterWriter(Writer out) {
        super(out);
        nbLines = 0;
        newLine = true;
        CRfound = false;
    }

    @Override
    public void write(String str, int off, int len) throws IOException {

        String[] lines = Utils.getNextLine(str.substring(off, off + len));
        String nextLine = lines[0];
        String remainingText = lines[1];

        String fileNumberingText = (newLine ? ++nbLines + "\t" : "") + nextLine;

        while(!nextLine.isEmpty()) {

            /**
             * Update next line
             */
            lines = Utils.getNextLine(remainingText);
            nextLine = lines[0];
            remainingText = lines[1];

            /**
             * We have to write the line number each time nextLine is not
             * empty. (getNextLine method returns a non empty first String
             * for each CRLF found int the input)
             */
            fileNumberingText += ++nbLines + "\t" + nextLine;

            newLine = !nextLine.isEmpty();
        }

        /**
         * In case input does not contain CRLF, loop above breaks
         * cause nextLine is empty but the remainingText contains
         * the last text to write (c.f getNextLine)
         */
        if(!remainingText.isEmpty()) {
            fileNumberingText += remainingText;
            newLine = false;
        }

        /**
         * Finally write the output
         */
        super.out.write(fileNumberingText);

    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        this.write(new String(cbuf), off, len);
    }

    @Override
    public void write(int c) throws IOException {
        if(c == '\r') {
            this.write("\r\n");
            CRfound = true;
        } else if(c == '\n' && CRfound) {
            CRfound = false;
        } else {
            this.write(String.valueOf((char) c));
        }
    }
}
