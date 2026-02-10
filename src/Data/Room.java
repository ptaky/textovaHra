package Data;

import java.util.HashMap;
import java.util.List;

public class Room {
    private String id;
    private String name;
    private boolean isLocked;
    private boolean isExplored = false;
    private String description;
    private String advancedDescription;
    private List<String> nextRooms;
    private List<String> items;
    private List<String> npcs;

    public Room() {}

    // Gettery a settery
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
        this.isLocked = locked;
    }

    public boolean isExplored() {
        return isExplored;
    }
    public void setExplored(boolean explored) {
        isExplored = explored;
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

    public List<String> getItems() {
        return items;
    }
    public void setItems(List<String> items) {
        this.items = items;
    }
    public void addItem(String item) {
        items.add(item);
    }
    public void removeItem(String item) {
        items.remove(item);
    }

    public List<String> getNpcs() {
        return npcs;
    }
    public void setNpcs(List<String> npcs) {
        this.npcs = npcs;
    }

    @Override
    public String toString() {
                String txt = description;
                if (!items.isEmpty() && isExplored) {
                    txt += "\n  - itemy v teto mistnosti: " + items;
                }
                if (!npcs.isEmpty() && isExplored) {
                    txt += "\n  - npc v teto mistnosti: " + npcs;
                }
            return txt;
    }
}
