package core.util;

import java.io.File;
import static java.util.Objects.isNull;
import java.util.Optional;

public class FileUtils {

    /**
     * Verifica se a extensao de um arquivo corresponde a informada por parametro
     *
     * @param file Arquivo
     * @param extennsionName Extensao a ser verificada
     * @return {@code boolean}
     */
    public static boolean checkExtensionName(File file, String extennsionName) {
        if (isNull(file)) {
            return false;
        }
        Optional<String> extensionOpt = FileUtils.getFileExtension(file.getName());
        return extensionOpt.isPresent() && extensionOpt.get().equals(extennsionName);
    }

    /**
     * Retorna um novo arquivo com o mesmo nome do arquivo informado e com uma extensao diferente
     *
     * @param file Arquivo
     * @param extension Nova extensao
     * @return {@code File}
     */
    public static File changeExtension(File file, String extension) {
        String filename = _getFileNameWithoutExtension(file) + "." + extension;
        return new File(file.getParent(), filename);
    }
    
    /**
     * Retorna um novo arquivo com o mesmo nome do arquivo informado concatenando uma nova extensao
     *
     * @param file Arquivo
     * @param extension Extensao a ser concatenada
     * @return {@code File}
     */
    public static File appendExtension(File file, String extension) {
        return new File(file.getParent(), file.getName() + "." + extension);
    }

    /**
     * Retorna um novo arquivo com o mesmo nome do arquivo informado sem extensao
     *
     * @param file Arquivo
     * @return {@code File}
     */
    public static File removeExtension(File file) {
        return new File(file.getParent(), _getFileNameWithoutExtension(file));
    }

    /**
     * Retorna a extensao do arquivo a partir do seu nome
     *
     * @param filename Nome do arquivo
     * @return {@code Optional<String>}
     */
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
