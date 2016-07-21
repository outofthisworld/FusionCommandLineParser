package util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static util.GlobalConstants.FILE_SEPARATOR;
import static util.GlobalConstants.separatedSystemPath;

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
    public static final String LOG_FILE = "/AndroidSdkTools";

    public static final String CREATE_AVD = "create avd";

    public static boolean createAVD(String sdkLocation, String name, String target, String abi) throws IOException {

        Process process = Runtime.getRuntime().exec(buildCommand(sdkLocation, name, target, abi));

        FileWriter bos = new FileWriter(AndroidSdkUtils.class.getResource(LOG_FILE).getPath());

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

        int data;
        int data2;

        InputStream in = process.getInputStream();
        InputStream in2 = process.getErrorStream();
        try {
            while ((data = in.read()) != -1) {
                System.out.print((char)data);
                bos.write(data);

                if(in2 != null) {
                    if ((data2 = in2.read()) != -1)
                        bos.write(data2);

                    if (data2 != -1 && data == -1) {
                        in = in2;
                        in2 = null;
                    }
                }
            }
            writer.write("no");
            writer.newLine();
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            bos.flush();
            bos.close();
            process.getErrorStream().close();
            process.getInputStream().close();
            process.getOutputStream().close();
            try {
                process.waitFor(2000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int exitValue = process.exitValue();
            process.destroy();

            return exitValue == 0;
        }
    }

    private static final String buildCommand(String sdkLocation, String name,String target,String abi){
        StringBuilder commandBuilder = new StringBuilder();

        return commandBuilder.append(sdkLocation)
                .append(FILE_SEPARATOR)
                .append(ANDROID_TOOLS_DIRECTORY)
                .append(FILE_SEPARATOR)
                .append(ANDROID_TOOL)
                .append(" -s ")
                .append(CREATE_AVD)
                .append(" --name ").append(name)
                .append(" --target ").append(target)
                .append(" --abi ").append(abi).toString();
    }

    public static void createAVD(String name, String target, String abi) throws Exception {
        String androidSdkLocation;
        if((androidSdkLocation = tryLocateAndroidSDK()) == null)
            throw new Exception("Cannot locate android sdk to create android virtual device");

        createAVD(androidSdkLocation,name,target,abi);
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

        if(androidSdkDir.isPresent())
            finalDir = androidSdkDir.get();
        else
            finalDir = System.getenv(ANDROID_SDK_ENV_VARIABLE);

        if(isValidSdkLocation(finalDir)){
            finalDir = finalDir.substring(0,finalDir.endsWith(FILE_SEPARATOR)?
                    finalDir.lastIndexOf(FILE_SEPARATOR):finalDir.length());
        }

        return finalDir;
    }

    private AndroidSdkUtils(){}
}
