package avd;

import util.GlobalConstants;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Dale on 21/07/16.
 */
public class AndroidEmulator {
    private static final String ANDROID_DIRECTORY  = "android";
    private static final String ANDROID_PLATFORM_TOOLS_DIRECTORY = "platform-tools";
    private static final String ANDROID_TOOLS_DIRECTORY = "tools";
    private static final String EMULATOR_TOOL = "emulator";
    private static final String ADB_TOOL = "adb";
    private static final String ANDROID_SDK_ENVIROMENTAL_VARIABLE = "ANDROID_SDK_DIR";

    private final String androidSdkLocation;

    
    public AndroidEmulator(String sdkLocation) throws EmulatorException {
        if(!isValidSdkLocation(sdkLocation)){
            throw new EmulatorException("Invalid SDK location");
        }
        this.androidSdkLocation = sdkLocation;
    }

    public AndroidEmulator() throws EmulatorException {
        this.androidSdkLocation = tryLocateAndroidSDK();
    }

    public void start(){

    }

    public void waitFor(){

    }

    public void stop(){

    }

    public boolean createAVD(){
        return false;
    }

    private boolean isValidSdkLocation(String finalDir){
        Path toolsPath = Paths.get(finalDir + GlobalConstants.FILE_SEPARATOR +
                ANDROID_TOOLS_DIRECTORY + GlobalConstants.FILE_SEPARATOR + EMULATOR_TOOL);

        Path platformToolsPath = Paths.get(finalDir + GlobalConstants.FILE_SEPARATOR +
                ANDROID_PLATFORM_TOOLS_DIRECTORY + GlobalConstants.FILE_SEPARATOR + ADB_TOOL);

        return finalDir != null && Files.exists(toolsPath) && Files.exists(platformToolsPath);
    }

    protected final String tryLocateAndroidSDK() throws EmulatorException{
        Optional<String> androidSdkDir =  Stream.of(GlobalConstants.separatedSystemPath())
                .filter(path->path.contains(ANDROID_DIRECTORY)
                && (path.contains(ANDROID_PLATFORM_TOOLS_DIRECTORY)
                || path.contains(ANDROID_TOOLS_DIRECTORY))).findFirst();

        String finalDir;
        if(androidSdkDir.isPresent()){
            String dir = androidSdkDir.get();
            finalDir = dir.substring(0, dir.lastIndexOf(GlobalConstants.FILE_SEPARATOR));
        }else{
            finalDir = System.getenv(ANDROID_SDK_ENVIROMENTAL_VARIABLE);
        }

        if(!isValidSdkLocation(finalDir))
            throw new EmulatorException("Could not located android SDK");

        return finalDir;
    }
}
