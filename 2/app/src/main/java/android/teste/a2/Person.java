package android.teste.a2;

import java.io.Serializable;

public class Person implements Serializable {
    private String name;
    private String age;
    private float weight;
    private float height;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }
    public void setAge(String age) { this.age = age; }

    public float getWeight() {
        return weight;
    }
    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() { return height; }
    public void setHeight(float height) { this.height = height; }

    public Person (){}
}
