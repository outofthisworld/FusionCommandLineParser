import org.appleby.commandlineparser.parser.CommandLineParserError;

/**
 * Created by Dale on 21/07/16.
 */
public class Test {

    public static void main(String[] args) {
        AppCommandLineParser appCommandLineParser = AppCommandLineParser.parser();
        try {
            appCommandLineParser.parseCommandLine(args);
        } catch (CommandLineParserError commandLineParserError) {
            System.out.println(commandLineParserError.getMessage());
            System.out.println("For more information on how to use a command try: help -show [command]");
            System.out.println("Alternatively try: help -display to see a list of commands and there usage.");
        }
    }
}
