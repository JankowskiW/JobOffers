package pl.wj.joboffers.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

public class RestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatusCode httpStatusCode = response.getStatusCode();
        if(httpStatusCode.is5xxServerError()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Internal error occured while connecting to remote http server");
        } else if(httpStatusCode.value() == HttpStatus.NOT_FOUND.value()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else if(httpStatusCode.value() == HttpStatus.UNAUTHORIZED.value()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
