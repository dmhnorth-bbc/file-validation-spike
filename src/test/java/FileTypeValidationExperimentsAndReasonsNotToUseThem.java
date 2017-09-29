import org.apache.tika.Tika;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(Parameterized.class)
public class FileTypeValidationExperimentsAndReasonsNotToUseThem {
    @Parameters
    public static String[] data() {
        return new String[]{"dave-image", "dave-image.mp4", "dave-image.jpeg", "dog-mp4.mp4", "dog-gif.gif", "notes.txt", "RegexInJava-html.html", "Bojackhorse-jpg.mov"};
    }

    private Tika defaultTika = new Tika();

    private File constructFile(String endOfPath) {
        return Paths.get("./src/test/resources/" + endOfPath).toFile();
    }

    @Parameter
    public String filename;

    @Test
    public void shouldPrintResults() throws IOException {
        System.out.println((validateFileMethod1(constructFile(filename))));
        System.out.println(validateFileMethod2(constructFile(filename)));
        System.out.println(validateFileMethod3(constructFile(filename)));
        System.out.println(validateFileMethod4(constructFile(filename)));
        System.out.println(validateFileMethod5(constructFile(filename)));
    }

    @Test
    public void shouldValidateFileExtensionAndMimetypeAreMatch() throws IOException {
        Assert.assertTrue(validateFileMethod(constructFile(filename), filename));
    }

    private boolean validateFileMethod(File file, String filename) throws IOException {
        System.out.println(filename + " : " + defaultTika.detect(file));
        return filename.endsWith(defaultTika.detect(file).split("/")[1]);
    }

    private String validateFileMethod1(File file) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(file));
        String mimeType = URLConnection.guessContentTypeFromStream(is);
        System.out.println("Method 1: " + filename.split("/")[0] + " - mimetype = " + mimeType);
        return mimeType;
    }

    /**
     * Don't use this method because it is effectively useless and unsafe for Streams or byte array as it does no inspection
     * It does use a kind of system wide understanding of filetypes, so it may work if we can add more recognised mimetypes to the server we run,
     * but I think this is a risky option
     */
    private String validateFileMethod2(File file) throws IOException {
        String mimeType = Files.probeContentType(file.toPath());
        System.out.println("Method 2: " + filename.split("/")[0] + " - mimetype = " + mimeType);
        return mimeType;
    }

    /**
     * method 3 works but deprecated, so it looks a bit weird. Also, I believe it can cause locking of resources
     */
    private String validateFileMethod3(File file) throws IOException {
        String mimeType = file.toURI().toURL().openConnection().getContentType();
        System.out.println("Method 3: " + filename.split("/")[0] + " - mimetype = " + mimeType);
        return mimeType;
    }

    /**
     * method 4 not very safe, as it just guesses from the name, so liable to injection
     */
    private String validateFileMethod4(File file) throws IOException {
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        System.out.println("Method 4: " + filename.split("/")[0] + " - mimetype = " + mimeType);
        return mimeType;
    }

    /**
     * A likely winner. An outside library with a large library https://dzone.com/articles/determining-file-types-java
     */
    private String validateFileMethod5(File file) throws IOException {
        String mimeType = defaultTika.detect(file);
        System.out.println("Method 5: " + filename.split("/")[0] + " - mimetype = " + mimeType);
        return mimeType;
    }
}
