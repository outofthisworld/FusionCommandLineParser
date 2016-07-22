package util;

import com.sun.istack.internal.Nullable;

import java.io.BufferedOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by Dale on 22/07/16.
 */
public final class ProcessUtils {

    public static final String LOG_FILE = "/Log.html";

    private ProcessUtils(){}

    public static final boolean executeCommand(String cmd, @Nullable String out) throws IOException {
        Process process = Runtime.getRuntime().exec(cmd);

        FileWriter bos = new FileWriter(AndroidSdkUtils.class.getResource(LOG_FILE).getPath());

        BufferedOutputStream writer = new BufferedOutputStream(process.getOutputStream());

        int data;

        InputStream in = process.getInputStream();

        try {
            while ((data = in.read()) != -1) {
                System.out.print((char)data);
                bos.write(data);
                bos.flush();
                if(out != null)
                writer.write(out.getBytes(Charset.forName("UTF-8")));
                writer.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            writer.close();
            bos.flush();
            bos.close();

            int exitValue = process.exitValue();

            process.getErrorStream().close();
            process.getInputStream().close();
            process.getOutputStream().close();
            process.destroy();

            return exitValue == 0;
        }
    }

}
