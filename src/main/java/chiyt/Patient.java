package chiyt;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Patient {
    @JsonProperty("id")
    String id;

    @JsonProperty("name")
    String name;

    @JsonProperty("gender")
    String gender;

    @JsonProperty("age")
    int age;

    @JsonProperty("height")
    float height;

    @JsonProperty("weight")
    float weight;

    @JsonProperty("cases")
    List<String> cases;
}
