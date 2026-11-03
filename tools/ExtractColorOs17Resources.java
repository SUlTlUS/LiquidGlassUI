import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.zip.ZipFile;

/** Mechanical extraction of original binary resources, not a recompiled COUI library. */
public class ExtractColorOs17Resources {
    private static final String EXPECTED = "e285088674907eedef89b4ba9862165dd070699cba4303c4b05b969f99c4261d";
    public static void main(String[] args) throws Exception {
        if (args.length != 2) throw new IllegalArgumentException("APK output-directory");
        Path apk = Path.of(args[0]).toAbsolutePath().normalize();
        Path root = Path.of(args[1]).toAbsolutePath().normalize();
        var digest = MessageDigest.getInstance("SHA-256");
        try (var stream = Files.newInputStream(apk)) {
            byte[] buffer = new byte[1024 * 1024];
            for (int length; (length = stream.read(buffer)) > 0;) digest.update(buffer, 0, length);
        }
        if (!HexFormat.of().formatHex(digest.digest()).equals(EXPECTED))
            throw new IllegalArgumentException("Not the reviewed Settings 17.0.0 APK");
        int count = 0;
        try (ZipFile zip = new ZipFile(apk.toFile())) {
            var entries = zip.entries();
            while (entries.hasMoreElements()) {
                var entry = entries.nextElement();
                String name = entry.getName();
                if (entry.isDirectory()) continue;
                if (!(name.equals("AndroidManifest.xml") || name.equals("resources.arsc") ||
                    name.startsWith("res/") && name.substring(name.lastIndexOf('/') + 1).startsWith("coui_"))) continue;
                Path target = root.resolve(name).normalize();
                if (!target.startsWith(root)) throw new SecurityException("Invalid ZIP path");
                byte[] bytes;
                try (var stream = zip.getInputStream(entry)) { bytes = stream.readAllBytes(); }
                if (Files.exists(target)) {
                    if (!java.util.Arrays.equals(Files.readAllBytes(target), bytes))
                        throw new IllegalStateException("Refusing to overwrite " + target);
                } else {
                    Files.createDirectories(target.getParent());
                    Files.write(target, bytes);
                }
                count++;
            }
        }
        System.out.println("ORIGINAL_RESOURCES=" + count);
    }
}
