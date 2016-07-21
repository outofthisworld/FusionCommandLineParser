/**
 * Created by Dale on 20/07/16.
 */

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.appleby.commandlineparser.command.Command;
import org.appleby.commandlineparser.command.ICommand;
import org.appleby.commandlineparser.command.option.Option;
import org.appleby.commandlineparser.command.option.OptionArgumentValidator;
import org.appleby.commandlineparser.parser.CommandLineParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Dale on 20/07/16.
 */
public final class AppCommandLineParser extends CommandLineParser<ICommand<Option>> {
    private static final AppCommandLineParser parser = new AppCommandLineParser();
    private final HashMap<String, ICommand<Option>> commands = new HashMap<String, ICommand<Option>>() {
        {
            try {
                put("file", new DownloadCommand());
                put("help", new HelpCommand());
            } catch (InvalidArgumentException e) {
                e.printStackTrace();
            }

        }
    };

    {
        commands.values().forEach(this::addCommand);
    }

    private AppCommandLineParser() {
    }

    public static final AppCommandLineParser parser() {
        return parser;
    }

    private class DownloadCommand extends Command<Option> implements OptionArgumentValidator {

        public DownloadCommand() throws InvalidArgumentException {
            super("Pokem", "-");
            addOption(new Option("f", "flush", true, false, this))
                    .addOption(new Option("en", "encode", false, true))
                    .addOption(new Option("e", "encode", false, true));
        }

        @Override
        public boolean execute(Map<String, String> optArgs, Set<String> commandArguments) {

            if(optArgs.containsKey("-en"))
                System.out.println("en option specified");

            if(optArgs.containsKey("-e"))
                System.out.println("e option specified");

            //No need to check, this option is required and error will be thrown if it doesnt exist
            String requiredOptionArg = optArgs.get("-f");
            System.out.println(requiredOptionArg);

            return true;
        }

        @Override
        public boolean validateCommandArg(int commandPos, String command) {
            return true;
        }

        @Override
        public boolean validateOptionArgument(String option, String argument) {
            return option.equals("f") && argument.startsWith("file://");

        }
    }

    private class HelpCommand extends Command<Option> implements OptionArgumentValidator {

        public HelpCommand() throws InvalidArgumentException {
            super("help", "-");
            addOption(new Option("display", "Help", false, true))
                    .addOption(new Option("show", "Shows help information about a command", true, true));
        }

        @Override
        public boolean validateCommandArg(int commandPos, String command) {
            return false;
        }

        @Override
        public boolean execute(Map<String, String> optArgs, Set<String> commandArguments) {

            if (optArgs.containsKey("-display")) {
                commands.values().forEach(e -> {
                    if (e != null) {
                        System.out.println(e.getUsage());
                    }
                });
            }

            if (optArgs.containsKey("-show"))
                System.out.println(commands.get(optArgs.get("-show")).getUsage());


            return true;
        }

        @Override
        public boolean validateOptionArgument(String option, String argument) {
            return option.equals("show") && commands.containsKey(argument);
        }
    }
}

