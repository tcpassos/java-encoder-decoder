package core.util;

import java.io.File;
import static java.util.Objects.isNull;
import java.util.Optional;

public class FileUtils {

    public static boolean checkExtensionName(File file, String extennsionName) {
        if (isNull(file)) {
            return false;
        }
        Optional<String> extensionOpt = FileUtils.getFileExtension(file.getName());
        return extensionOpt.isPresent() && extensionOpt.get().equals(extennsionName);
    }

    public static File changeExtension(File file, String extension) {
        String filename = _getFileNameWithoutExtension(file) + "." + extension;
        return new File(file.getParent(), filename);
    }
    
    public static File appendExtension(File file, String extension) {
        return new File(file.getParent(), file.getName() + "." + extension);
    }

    public static File removeExtension(File file) {
        return new File(file.getParent(), _getFileNameWithoutExtension(file));
    }

    public static Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
    
    private static String _getFileNameWithoutExtension(File file) {
        String filename = file.getName();
        if (filename.contains(".")) {
            filename = filename.substring(0, filename.lastIndexOf('.'));
        }
        return filename;
    }

}
