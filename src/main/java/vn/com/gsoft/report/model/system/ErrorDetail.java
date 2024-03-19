package vn.com.gsoft.report.model.system;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorDetail {
    private Date timestamp;

    private String message;

    private Object details;

    private String path;
}
