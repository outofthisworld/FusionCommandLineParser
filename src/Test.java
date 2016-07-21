import util.AndroidSdkUtils;

import java.io.IOException;

/**
 * Created by Dale on 21/07/16.
 */
public class Test {

    public static void main(String[] args) {

        System.getenv().entrySet().forEach(e -> System.out.println(e.getKey() + " " + e.getValue()));
        FusionCommandLineParser appCommandLineParser = FusionCommandLineParser.parser();

        try {
            System.out.println(AndroidSdkUtils.createAVD("/Users/Dale/Library/Android/sdk","DalesAVD","7","default/x86"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
