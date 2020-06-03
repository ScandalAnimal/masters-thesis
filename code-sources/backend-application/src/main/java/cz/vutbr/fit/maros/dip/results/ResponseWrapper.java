package cz.vutbr.fit.maros.dip.results;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SuppressWarnings("unchecked")
public class ResponseWrapper<T> extends ResponseEntity<T> {
    public ResponseWrapper(T t, HttpStatus status) {
        super((T) new ResultSet<>(t, status), status);
    }
}
