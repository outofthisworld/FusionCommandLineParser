package util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static util.GlobalConstants.*;

/**
 * Created by Dale on 21/07/16.
 */
public final class AndroidSdkUtils {
    public static final String ANDROID_DIRECTORY  = "android";
    public static final String SDK_DIR = "sdk";
    public static final String ANDROID_PLATFORM_TOOLS_DIRECTORY = "platform-tools";
    public static final String ANDROID_TOOLS_DIRECTORY = "tools";
    public static final String EMULATOR_TOOL = "emulator";
    public static final String ANDROID_TOOL = "android";
    public static final String ADB_TOOL = "adb";
    public static final String ANDROID_SDK_ENV_VARIABLE = "ANDROID_SDK_DIR";
    public static final String LOG_FILE = "AndroidSdkTools.txt";

    public static boolean createAVD(String sdkLocation, String name, String target) throws IOException {

        StringBuilder commandBuilder = new StringBuilder();

        commandBuilder.append(sdkLocation)
                .append(FILE_SEPARATOR)
                .append(ANDROID_TOOL)
                .append(SPACE).append("-s ")
                .append("-n ").append(name)
                .append("-t ").append(target);

        Process process = Runtime.getRuntime().exec(commandBuilder.toString());

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(
                new File(AndroidSdkUtils.class.getResource(LOG_FILE).getPath())));

        int data;

        try {
            while ((data = process.getErrorStream().read()) != -1) {
                bos.write(data);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            bos.flush();
            bos.close();
            process.getErrorStream().close();

            int exitValue = process.exitValue();
            try {
                process.waitFor(2000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            process.destroy();

            return exitValue == 0;
        }
    }

    public static void createAVD(String name, String target) throws Exception {
        String androidSdkLocation;
        if((androidSdkLocation = tryLocateAndroidSDK()) == null)
            throw new Exception("Cannot locate android sdk to create android virtual device");

        createAVD(androidSdkLocation,name,target);
    }

    public static boolean isValidSdkLocation(String path){
        Path emulatorTool = Paths.get(path + FILE_SEPARATOR +
                ANDROID_TOOLS_DIRECTORY + FILE_SEPARATOR + EMULATOR_TOOL);

        Path androidTool = Paths.get(path + FILE_SEPARATOR +
                ANDROID_TOOLS_DIRECTORY + FILE_SEPARATOR + ANDROID_TOOL);

        Path adbTool = Paths.get(path + FILE_SEPARATOR +
                ANDROID_PLATFORM_TOOLS_DIRECTORY + FILE_SEPARATOR + ADB_TOOL);

        return path != null && Files.exists(emulatorTool) && Files.exists(androidTool) && Files.exists(adbTool);
    }

    public static String tryLocateAndroidSDK() {
        Optional<String> androidSdkDir =  Stream.of(separatedSystemPath())
                .filter(path->(path.toLowerCase().contains(ANDROID_DIRECTORY) ||
                        path.toLowerCase().contains(SDK_DIR))
                        && (path.contains(ANDROID_PLATFORM_TOOLS_DIRECTORY)
                        || path.contains(ANDROID_TOOLS_DIRECTORY))).findFirst();

        String finalDir;
        if(androidSdkDir.isPresent()){
            String dir = androidSdkDir.get();
            finalDir = dir.substring(0, dir.endsWith(FILE_SEPARATOR)?
                    dir.lastIndexOf(FILE_SEPARATOR):dir.length());
        }else{
            String loc = System.getenv(ANDROID_SDK_ENV_VARIABLE);
            finalDir = loc.substring(0,loc.endsWith(FILE_SEPARATOR)?
                    loc.lastIndexOf(FILE_SEPARATOR):loc.length());
        }

        if(!isValidSdkLocation(finalDir))
            return null;

        return finalDir;
    }

    private AndroidSdkUtils(){}
}
