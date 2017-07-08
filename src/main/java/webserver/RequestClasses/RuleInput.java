package webserver.RequestClasses;

/**
 * Created by Tim on 12.06.2017.
 */

//class to convert a json object easily into a java class
//an original rule would be to complex
public class RuleInput {
    private int id;
    private String name;
    private boolean active;
    private int[] components;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int[] getComponents() {
        return components;
    }

    public void setComponents(int[] components) {
        this.components = components;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
