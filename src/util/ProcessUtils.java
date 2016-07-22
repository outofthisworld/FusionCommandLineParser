package util;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Created by Dale on 22/07/16.
 */
public final class ProcessUtils {

    private ProcessUtils() {
    }

    public static final boolean executeCommand(@NotNull String cmd, @NotNull Writer inWriter, @Nullable String output,
                                               @Nullable Charset outputEncoding) throws IOException {

        Objects.requireNonNull(cmd);
        Objects.requireNonNull(inWriter);

        Process process = Runtime.getRuntime().exec(cmd);
        BufferedOutputStream writer = null;

        boolean isOut = output!=null;

        if (isOut)
            writer = new BufferedOutputStream(process.getOutputStream());

        int data;

        try (
                InputStream in = process.getInputStream();
                Writer cmdInWriter = new BufferedWriter(inWriter)
        ) {

            while ((data = in.read()) != -1) {
                System.out.print((char) data);
                cmdInWriter.write(data);
                cmdInWriter.flush();

                if (isOut) {
                    writer.write(output.getBytes(outputEncoding));
                    writer.flush();
                }
            }

            if(isOut)
                writer.close();

            inWriter.close();

            int exitValue = process.exitValue();

            process.getErrorStream().close();
            process.getInputStream().close();
            process.getOutputStream().close();
            process.destroy();

            return exitValue == 0;
        }
    }
}
