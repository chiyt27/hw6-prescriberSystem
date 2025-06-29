package chiyt;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Patient {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("age")
    private int age;

    @JsonProperty("height")
    private float height;

    @JsonProperty("weight")
    private float weight;

    @JsonProperty("cases")
    private List<Case> cases = new ArrayList<>();

    public Patient() {}

    public Patient(String id, String name, String gender, int age, float height, float weight) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public int getAge() { return age; }
    public float getHeight() { return height; }
    public float getWeight() { return weight; }
    public List<Case> getCases() { return cases; }

    public void addCase(Case newCase) {
        this.cases.add(newCase);
    }

    // @Override
    // public String toString() {
    //     return "Patient{" +
    //             "id='" + id + '\'' +
    //             ", name='" + name + '\'' +
    //             ", gender='" + gender + '\'' +
    //             ", age=" + age +
    //             ", height=" + height +
    //             ", weight=" + weight +
    //             ", cases=" + cases.size() +
    //             '}';
    // }
}
