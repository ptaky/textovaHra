public class NPC {
    private String name;
    private int age;

    // prazdny konstruktor je pro json nezbytny
    public NPC() {}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "NPC{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
