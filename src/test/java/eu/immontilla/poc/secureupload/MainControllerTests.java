package eu.immontilla.poc.secureupload;

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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import eu.immontilla.poc.secureupload.controller.MainController;

@RunWith(SpringRunner.class)
@WebMvcTest(MainController.class)
public class MainControllerTests {
    @Autowired
    private MockMvc mockMVC;
    
    /**
     * A GET call should return index.html
     * @throws Exception
     */
    @Test
    public void shouldReturnHTML() throws Exception {
        mockMVC.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(forwardedUrl("index.html"));
    }
    
    /**
     * Missing parameter file should return 400 Bad Request
     * @throws Exception
     */
    @Test
    public void shouldReturnBadRequestRequiredPropertyMissing() throws Exception {
        ResultMatcher badRequest = MockMvcResultMatchers.status().isBadRequest();
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/upload");
        mockMVC.perform(builder).andExpect(badRequest).andReturn();
    }
    
    /**
     * Trying to upload a file named test should return 400 Bad Request and validFileExtension equals to false
     * @throws Exception
     */
    @Test
    public void shouldReturnBadRequestInvalidFileExtension() throws Exception {
        ResultMatcher badRequest = MockMvcResultMatchers.status().isBadRequest();
        MockMultipartFile csvFile = new MockMultipartFile("file", "test", "text/csv", "a,b,c,d,e,f".getBytes());
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/upload").file(csvFile);
        mockMVC.perform(builder).andExpect(badRequest).andExpect(jsonPath("$.validFileExtension").value("false"));
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test
    public void shouldReturnOK() throws Exception {
        ResultMatcher ok = MockMvcResultMatchers.status().isOk();
        MockMultipartFile csvFile = new MockMultipartFile("file", "file.csv", "text/csv", "a,b,c,d,e,f".getBytes());
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/upload").file(csvFile);
        mockMVC.perform(builder).andExpect(ok).andExpect(jsonPath("$.failed").value("false"));
    }

}
