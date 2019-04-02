package speedassistant.domain.models.speedlimit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpeedLimit {
    private List<Element> elements;

    public SpeedLimit() {
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    /**
     * @return null if no speed limit was found
     */
    public Integer getMaxSpeed() {
        if (this.elements.size() == 0) {
            return null;
        }

        Element firstElement = this.elements.get(0);
        if (firstElement == null || !firstElement.getTags().containsKey("maxspeed")) {
            return null;
        }

        return Integer.parseInt(firstElement.getTags().get("maxspeed"));
    }
}
