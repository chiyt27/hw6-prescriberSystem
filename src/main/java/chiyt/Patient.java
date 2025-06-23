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

    // Constructors
    public Patient() {}

    public Patient(String id, String name, String gender, int age, float height, float weight) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public float getHeight() { return height; }
    public void setHeight(float height) { this.height = height; }

    public float getWeight() { return weight; }
    public void setWeight(float weight) { this.weight = weight; }

    public List<Case> getCases() { return cases; }
    public void setCases(List<Case> cases) { this.cases = cases; }

    // Helper methods
    public float getBMI() {
        if (height <= 0) return 0;
        return weight / ((height / 100) * (height / 100));
    }

    public void addCase(Case newCase) {
        this.cases.add(newCase);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", bmi=" + String.format("%.2f", getBMI()) +
                ", cases=" + cases.size() +
                '}';
    }
}
