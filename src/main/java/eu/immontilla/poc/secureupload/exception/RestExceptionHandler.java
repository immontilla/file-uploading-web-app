package eu.immontilla.poc.secureupload.exception;

import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.FileUploadBase.SizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import eu.immontilla.poc.secureupload.helper.Constants;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * Return a 413 Payload Too Large when the user tried to upload a large sized file. The maximum size is configured
     * in the application.properties resource file
     * 
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler({ MultipartException.class, SizeLimitExceededException.class,
            FileSizeLimitExceededException.class, IllegalStateException.class })
    public ResponseEntity<Object> handleSizeExceededException(final WebRequest request, final MultipartException ex) {
        LOGGER.error(ex.getMessage());
        Map<String, Boolean> out = new HashMap<>();
        out.put(Constants.ACCEPTED_FILE_SIZE, false);
        out.put(Constants.FAILED, true);
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(out);
    }
}
