import util.AndroidSdkUtils;
import util.GlobalConstants;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Dale on 21/07/16.
 */
public class Test {

    public static void main(String[] args) {


        System.out.println(Arrays.toString(GlobalConstants.separatedSystemPath()));

        try {
            System.out.println(AndroidSdkUtils.createAVD("/Users/Dale/Library/Android/sdk","DalesAVD","7","default/x86"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
