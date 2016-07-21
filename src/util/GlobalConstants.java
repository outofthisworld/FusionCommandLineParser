package util;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Dale on 21/07/16.
 */
public final class GlobalConstants {

    /* Os specific line separator E.G '\n' */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    /* Os specific character used to separate file paths E.G ':' || ';' */
    public static final String PATH_SERARATOR = File.pathSeparator;
    /* Os specific character used to separate directories & files E.G '/' || '\' */
    public static final String FILE_SEPARATOR = File.separator;
    public static final String EMPTY_STRING = "";
    public static final char NULL_CHAR = '\0';
    /* Path to the users home directory */
    private static final String USER_HOME = System.getProperty("user.home");
    /* The SHELL environmental variable, if it is set */
    private static final String SYSTEM_SHELL = System.getenv("SHELL");
    /* The system PATH variable */
    private static final String SYSTEM_PATH = System.getenv("PATH");


    private GlobalConstants() {
    }

    public static String userHome() {
        return USER_HOME != null ? USER_HOME : EMPTY_STRING;
    }

    public static String systemShell() {
        return SYSTEM_SHELL != null ? SYSTEM_SHELL : EMPTY_STRING;
    }

    public static String systemPath() {
        return SYSTEM_PATH != null ? SYSTEM_PATH : EMPTY_STRING;
    }

    public static final class OS {

        private static final String OS_NAME;
        private static final String OS_ARCH;
        private static final String OS_VERS;
        private static volatile OSFamily osFamily = null;

        static {
            OS_NAME = System.getProperty("os.name");
            OS_ARCH = System.getProperty("os.arch");
            OS_VERS = System.getProperty("os.vers");
        }

        private OS() {
        }

        public static boolean isOS(String name, String arch, String vers) {
            Objects.requireNonNull(name);

            boolean isOS;

            if (arch == null && vers == null) {
                isOS = osNameContains(name());
            } else if (arch == null) {
                isOS = osNameContains(name()) && isOSVers(vers());
            } else if (vers == null) {
                isOS = osNameContains(vers()) && isOSArch(arch());
            } else {
                isOS = osNameContains(name()) && isOSVers(vers()) && isOSArch(arch());
            }

            return isOS;
        }

        private static boolean osNameContains(String name) {
            return OS_NAME.toLowerCase().contains(name.toLowerCase());
        }

        public static boolean isOS(String name, String arch) {
            return isOS(name, arch, null);
        }

        public static boolean isOS(String name, int vers) {
            return isOS(name, null, String.valueOf(vers));
        }

        public static boolean isOS(OSFamily family, String arch) {
            return isOS(family.family, arch, null);
        }

        public static boolean isOS(OSFamily family) {
            return isOS(family.family, null, null);
        }

        public static boolean isOS(OSFamily family, int vers) {
            return isOS(family.family, null, String.valueOf(vers));
        }

        public static boolean isOS(OSFamily family, String arch, String vers) {
            return isOS(family.family, arch, vers);
        }

        public static boolean isOSArch(String arch) {
            return OS_ARCH.equals(arch);
        }

        public static boolean isOSVers(String vers) {
            return OS_VERS.equals(vers);
        }

        public static String name() {
            return OS_NAME;
        }

        public static String vers() {
            return OS_VERS;
        }

        public static String arch() {
            return OS_ARCH;
        }

        public static OSFamily family() {
            if (osFamily != null)
                return osFamily;

            OSFamily osFams[] = OSFamily.values();

            Stream<OSFamily> stream = Arrays.stream(osFams);

            Optional<OSFamily> fam = stream.filter(e -> osNameContains(e.family)).findFirst();

            if (!fam.isPresent())
                return OSFamily.FAMILY_NOT_SUPPORTED;
            else
                osFamily = fam.get();

            switch (osFamily) {
                case FAMILY_WINDOWS:
                    boolean is9X = osNameContains("95") || osNameContains("98") || osNameContains("me") || osNameContains("ce");
                    if (is9X)
                        osFamily = OSFamily.FAMILY_9X;
                    else
                        osFamily = OSFamily.FAMILY_NT;
                    break;
            }
            return osFamily;
        }

        public enum OSFamily {
            FAMILY_WINDOWS("windows"),
            FAMILY_9X("win9x"),
            FAMILY_DOS("dos"),
            FAMILY_MAC("mac"),
            FAMILY_NETWARE("netware"),
            FAMILY_NT("winnt"),
            FAMILY_OS2("os/2"),
            FAMILY_OS400("os/400"),
            FAMILY_TANDEM("tandem"),
            FAMILY_UNIX("unix"),
            FAMILY_VMS("openvms"),
            FAMILY_ZOS("z/os"),
            FAMILY_DARWIN("darwin"),
            FAMILY_NOT_SUPPORTED("");

            public final String family;

            OSFamily(String name) {
                this.family = name;
            }
        }
    }
}
