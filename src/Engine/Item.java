package Engine;

import java.util.Map;

public class Item {
    private String id;
    private String name;
    private String description;
    private String location;

    // obecné vlastnosti
    private boolean portable;
    private boolean consumable;
    private boolean singleUse;
    private boolean questItem;

    // použití itemu
    private boolean isUsable;
    private String useAction;

    // vazby na questy / Engine.NPC
    private String requiredByNpc;
    private String givenByNpc;

    // herní efekty
    private Map<String, Object> effects;
    private Map<String, Object> conditions;

    public Item() {
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

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isPortable() {
        return portable;
    }
    public void setPortable(boolean portable) {
        this.portable = portable;
    }

    public boolean isConsumable() {
        return consumable;
    }
    public void setConsumable(boolean consumable) {
        this.consumable = consumable;
    }

    public boolean isSingleUse() {
        return singleUse;
    }
    public void setSingleUse(boolean singleUse) {
        this.singleUse = singleUse;
    }

    public boolean isQuestItem() {
        return questItem;
    }
    public void setQuestItem(boolean questItem) {
        this.questItem = questItem;
    }

    public boolean isUsable() {
        return isUsable;
    }
    public void setIsUsable(boolean isUsable) {
        this.isUsable = isUsable;
    }

    public String getUseAction() {
        return useAction;
    }
    public void setUseAction(String useAction) {
        this.useAction = useAction;
    }

    public String getRequiredByNpc() {
        return requiredByNpc;
    }
    public void setRequiredByNpc(String requiredByNpc) {
        this.requiredByNpc = requiredByNpc;
    }

    public String getGivenByNpc() {
        return givenByNpc;
    }
    public void setGivenByNpc(String givenByNpc) {
        this.givenByNpc = givenByNpc;
    }

    public Map<String, Object> getEffects() {
        return effects;
    }
    public void setEffects(Map<String, Object> effects) {
        this.effects = effects;
    }

    public Map<String, Object> getConditions() {
        return conditions;
    }
    public void setConditions(Map<String, Object> conditions) {
        this.conditions = conditions;
    }
}
