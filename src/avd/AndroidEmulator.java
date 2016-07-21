package avd;

import static util.AndroidSdkUtils.isValidSdkLocation;
import static util.AndroidSdkUtils.tryLocateAndroidSDK;

/**
 * Created by Dale on 21/07/16.
 */
public class AndroidEmulator {
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
}
