import java.util.List;

public class Room {
    private String id;
    private String name;
    private boolean isLocked;
    private String description;
    private String advancedDescription;
    private List<String> nextRooms;

    public Room() {}

    public boolean thereIsNextRoom(String roomId) {
        return nextRooms.contains(roomId);
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isLocked() {
        return isLocked;
    }
    public void setIsLocked(boolean locked) {
        isLocked = locked;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdvancedDescription() {
        return advancedDescription;
    }
    public void setAdvancedDescription(String advancedDescription) {
        this.advancedDescription = advancedDescription;
    }

    public List<String> getNextRooms() {
        return nextRooms;
    }
    public void setNextRooms(List<String> nextRooms) {
        this.nextRooms = nextRooms;
    }
}
