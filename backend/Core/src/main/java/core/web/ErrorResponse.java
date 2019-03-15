package core.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorResponse {
    private Map<String, List<String>> errors;

    public ErrorResponse(Map<String, List<String>> errors) {
        this.errors = errors;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }
}
