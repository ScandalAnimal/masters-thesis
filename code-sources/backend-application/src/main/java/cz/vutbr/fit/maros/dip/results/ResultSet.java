package cz.vutbr.fit.maros.dip.results;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.vutbr.fit.maros.dip.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.springframework.http.HttpStatus;


@Data
public class ResultSet<T> {
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private T data;

    private ResultSet() {
        this.timestamp = LocalDateTime.now();
    }

    public ResultSet(T o, HttpStatus status) throws ResourceNotFoundException {
        this();
        if (o == null || (o instanceof List && ((List) o).isEmpty())) {
            throw new ResourceNotFoundException("No Content Found");
        }
        this.status = status;
        this.data = o;
    }

}
