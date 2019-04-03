package speedassistant.domain.models.speedlimit;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class Element {
    private String type;
    private Long id;
    private Map<String, String> tags = new HashMap<>();

    public Element() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonAnyGetter
    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    @JsonAnySetter
    public void setTagFields(String key, String value) {
        tags.put(key, value);
    }
}
