import java.io.File;

public class Launcher {
    public static void main(String[] args) {
        final String os = System.getProperty("os.name");

        String javaPath = os.contains("Mac")? "/Library/Java/JavaVirtualMachines/":"C:\\Program Files\\Java\\";
//        System.out.println();

        File[] files = new File(javaPath).listFiles();

        String name = files[0].getAbsolutePath();

        for (File file : files) {
//            System.out.println(file);
        }

//        System.out.println(name);

        System.getProperties().list(System.out);
        System.out.println(System.getProperty("sun.boot.library.path"));
    }
}
