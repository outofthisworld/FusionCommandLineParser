package avd;

import static util.AndroidSdkUtils.isValidSdkLocation;
import static util.AndroidSdkUtils.tryLocateAndroidSDK;

/**
 * Created by Dale on 21/07/16.
 */
public class AndroidEmulator {
    private final String androidSdkLocation;
    private final String name;

    public AndroidEmulator(String name,String sdkLocation) throws EmulatorException {
        if(!isValidSdkLocation(sdkLocation)){
            throw new EmulatorException("Invalid SDK location");
        }
        this.androidSdkLocation = sdkLocation;
        this.name = name;
    }

    public AndroidEmulator(String name) throws EmulatorException {
        this.androidSdkLocation = tryLocateAndroidSDK();
        this.name = name;
    }

    public void start(){

    }

    public void waitFor(){

    }

    public void stop(){

    }
}
