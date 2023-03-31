package pl.wj.joboffers.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

public class RestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String message;
        if(response.getStatusCode().is5xxServerError()) {
            message = "Error occured during connecting to remote service";
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
        }
        if(response.getStatusCode().is4xxClientError()) {
            message = "Client application error occured";
            throw new ResponseStatusException(response.getStatusCode(), message);
        }
        throw new ResponseStatusException(response.getStatusCode());
    }
}
