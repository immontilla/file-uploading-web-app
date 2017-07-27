package eu.immontilla.poc.secureupload;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import eu.immontilla.poc.secureupload.controller.MainController;

@RunWith(SpringRunner.class)
@WebMvcTest(MainController.class)
public class MainControllerTests {
    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldReturnHTML() throws Exception {
        this.mvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(forwardedUrl("index.html"));
    }

    @Test
    public void shouldReturnBadRequestRequiredPropertyMissing() throws Exception {
        MockMultipartHttpServletRequestBuilder request = fileUpload("/upload");
        mvc.perform(request).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void shouldReturnBadRequestInvalidFileExtension() throws Exception {
        MockMultipartFile csvFile = new MockMultipartFile("file", "test", "text/csv", "a,b,c,d,e,f".getBytes());
        this.mvc.perform(fileUpload("/upload").file(csvFile)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validFileExtension").value("false"));
    }

    @Test
    public void shouldReturnOK() throws Exception {
        MockMultipartFile csvFile = new MockMultipartFile("file", "file.csv", "text/csv", "a,b,c,d,e,f".getBytes());
        this.mvc.perform(fileUpload("/upload").file(csvFile)).andExpect(status().isOk())
                .andExpect(jsonPath("$.failed").value("false"));
    }

}
