package eu.immontilla.poc.secureupload;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.mock.web.MockMultipartFile;

import com.google.common.base.Strings;

import eu.immontilla.poc.secureupload.helper.FileHelper;

public class FileHelperTests {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    FileHelper fileHelper = new FileHelper();

    @Test
    public void testValidFileExtension() {
        assertFalse(fileHelper.validFileExtension(null));
        assertFalse(fileHelper.validFileExtension(""));
        assertFalse(fileHelper.validFileExtension("file"));
        assertFalse(fileHelper.validFileExtension("file.txt"));
        assertTrue(fileHelper.validFileExtension("file.csv"));
    }

    @Test
    public void testValidContentType() throws Exception {
        URL url = this.getClass().getResource("/libre-office-csv-file.csv");
        File file = new File(url.getFile());
        assertTrue(file.exists());
        assertTrue(fileHelper.validContentType(url.getPath()));
        url = this.getClass().getResource("/fake-csv-it-is-a-png-file.csv");
        file = new File(url.getFile());
        assertTrue(file.exists());
        assertFalse(fileHelper.validContentType(url.getPath()));
        assertFalse(fileHelper.validContentType(url.getPath() + "break-it"));
    }

    @Test
    public void testSave() {
        MockMultipartFile tempCSV = new MockMultipartFile("test.csv", "test.csv", "text/csv", new byte[10]);
        String tempPath = testFolder.getRoot().getAbsolutePath() + "/test.csv";
        assertTrue(!Strings.isNullOrEmpty(fileHelper.save(tempCSV, tempPath)));
    }

    @Test
    public void testDelete() {
        fileHelper.delete(testFolder.getRoot().getAbsolutePath() + "/test.csv");
    }

}
