package eu.immontilla.poc.secureupload.controller;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Strings;

import eu.immontilla.poc.secureupload.helper.Constants;
import eu.immontilla.poc.secureupload.helper.FileHelper;

@Controller
public class MainController {
    @Value("${temp.path}")
    private String tempPath;
    @Value("${file.path}")
    private String finalPath;
    @Value("${clam.av.server.host}")
    private String clamavHost;
    @Value("${clam.av.server.port}")
    private int clamavPort;
    @Value("${clam.av.server.platform}")
    private String clamavPlatform;
    private final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @RequestMapping("/")
    public String index() {
        return "index.html";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        FileHelper fileHelper = new FileHelper();
        Map<String, Boolean> out = new HashMap<>();
        String fileName = file.getOriginalFilename();
        if (fileHelper.validFileExtension(fileName)) {
            out.put(Constants.ACCEPTED_FILE_EXTENSION, true);
            String copyAs = fileHelper.save(file, tempPath);
            String tempFile = Paths.get(tempPath, copyAs).toString();
            if (!Strings.isNullOrEmpty(copyAs)) {
                fileHelper.setClamavHost(clamavHost);
                fileHelper.setClamavPort(clamavPort);
                fileHelper.setClamavPlatform(clamavPlatform);
                if (fileHelper.virusScan(tempFile)) {
                    out.put(Constants.VIRUS_FREE, true);
                    if (fileHelper.validContentType(tempFile)) {
                        out.put(Constants.VALID_CONTENT_TYPE, true);
                        copyAs = fileHelper.save(file, finalPath);
                        if (!Strings.isNullOrEmpty(copyAs)) {
                            fileHelper.delete(tempFile);
                            out.put(Constants.FAILED, false);
                            return ResponseEntity.ok().body(out);
                        } else {
                            fileHelper.delete(tempFile);
                            LOGGER.info(fileName + Constants.MSG_SAVE_ERROR + Paths.get(finalPath, copyAs).toString());
                            out.put(Constants.FAILED, true);
                            return ResponseEntity.badRequest().body(out);
                        }
                    } else {
                        fileHelper.delete(tempFile);
                        out.put(Constants.VALID_CONTENT_TYPE, false);
                        out.put(Constants.FAILED, true);
                        return ResponseEntity.badRequest().body(out);
                    }
                } else {
                    fileHelper.delete(tempFile);
                    out.put(Constants.VIRUS_FREE, false);
                    out.put(Constants.FAILED, true);
                    return ResponseEntity.badRequest().body(out);
                }
            } else {
                LOGGER.info(fileName + Constants.MSG_SAVE_ERROR + tempFile);
                out.put(Constants.FAILED, true);
                return ResponseEntity.badRequest().body(out);
            }
        } else {
            out.put(Constants.ACCEPTED_FILE_EXTENSION, false);
            out.put(Constants.FAILED, true);
            return ResponseEntity.badRequest().body(out);
        }
    }

}
