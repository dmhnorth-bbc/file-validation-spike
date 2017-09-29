import org.apache.tika.Tika;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class FileTypeValidationTestWithApacheTika {

    @Parameterized.Parameter
    public String filename;

    private Tika defaultTika = new Tika();

    private static String defaultPath = "./src/test/resources/";

    @Parameterized.Parameters
    public static String[] data() {
        List<String> list = null;
        try {
            list = listFilesForFolder(Paths.get(defaultPath).toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert list != null;
        return list.toArray(new String[list.size()]);
    }


    // Very basic just to check nothing is coming back as null
    @Test
    public void shouldNotReturnNullForAnyAcceptedFileType() throws IOException {
        System.out.println(defaultTika.detect(constructPath(filename)) + " = " + filename);
        Assert.assertNotNull(defaultTika.detect(constructPath(filename).toFile()));
        Assert.assertTrue(defaultTika.detect(constructPath(filename).toFile()).matches("\\w+/\\w+"));
    }

    private Path constructPath(String endOfPath) {
        return Paths.get(defaultPath + endOfPath);
    }

    private static List<String> listFilesForFolder(final File folder) throws IOException {
        return Files.walk(Paths.get(defaultPath))
                .filter(Files::isRegularFile)
                .map(file -> file.toFile().getName())
                .collect(Collectors.toList());
    }
}
