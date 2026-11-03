import jadx.api.JadxArgs;
import jadx.api.JadxDecompiler;
import jadx.api.JavaClass;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

/** Source extraction only. Decompiled text is saved unchanged; never executed. */
public class ExtractApkBlur {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) throw new IllegalArgumentException("APK output-directory class-regex");
        Path output = Path.of(args[1]).toAbsolutePath().normalize();
        Files.createDirectories(output);
        Pattern selection = Pattern.compile(args[2]);
        JadxArgs config = new JadxArgs();
        config.setInputFile(new File(args[0]));
        config.setSkipResources(true);
        config.setThreadsCount(4);
        config.setInlineMethods(false);
        config.setInlineAnonymousClasses(false);
        config.setOutDir(output.toFile());
        try (JadxDecompiler jadx = new JadxDecompiler(config)) {
            jadx.load();
            int count = 0;
            for (JavaClass cls : jadx.getClasses()) {
                if (!selection.matcher(cls.getRawName()).find()) continue;
                Path file = output.resolve(cls.getFullName().replace('.', '/') + ".java").normalize();
                if (!file.startsWith(output)) throw new SecurityException("Invalid class output path");
                if (Files.exists(file)) throw new IllegalStateException("Refusing to overwrite " + file);
                Files.createDirectories(file.getParent());
                Files.writeString(file, cls.getCode(), StandardCharsets.UTF_8);
                System.out.println("EXTRACT " + cls.getRawName());
                count++;
            }
            System.out.println("EXTRACTED_CLASSES=" + count);
        }
    }
}
