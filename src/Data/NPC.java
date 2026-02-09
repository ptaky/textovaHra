package Data;

import java.util.List;
import java.util.Map;

public class NPC {
    private String id;
    private String name;
    private String nickname;
    private String type;
    private String location;
    private String description;

    // stavové proměnné
    private boolean isRepaired;
    private boolean plantNeedsLight;
    private boolean hostile;

    // quest / item logika
    private String requiresItem;
    private String givesItem;

    // kolekce předmětů
    private List<String> givesItems;

    // dialogy a chování
    private Map<String, List<String>> dialogues;
    private List<String> functions;

    // hinty pro hint_command (keys jsou v JSONu jako "0","1","2","3","4","default")
    private Map<String, List<String>> hints;

    // podmínky a speciální mechaniky
    private Map<String, Object> conditions;
    private Map<String, Object> bypassMethods;

    public NPC() {
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

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRepaired() {
        return isRepaired;
    }
    public void setIsRepaired(boolean isRepaired) {
        this.isRepaired = isRepaired;
    }

    public boolean isPlantNeedsLight() {
        return plantNeedsLight;
    }
    public void setPlantNeedsLight(boolean plantNeedsLight) {
        this.plantNeedsLight = plantNeedsLight;
    }

    public boolean isHostile() {
        return hostile;
    }
    public void setHostile(boolean hostile) {
        this.hostile = hostile;
    }

    public String getRequiresItem() {
        return requiresItem;
    }
    public void setRequiresItem(String requiresItem) {
        this.requiresItem = requiresItem;
    }

    public String getGivesItem() {
        return givesItem;
    }
    public void setGivesItem(String givesItem) {
        this.givesItem = givesItem;
    }

    public List<String> getGivesItems() {
        return givesItems;
    }
    public void setGivesItems(List<String> givesItems) {
        this.givesItems = givesItems;
    }

    public Map<String, List<String>> getDialogues() {
        return dialogues;
    }
    public void setDialogues(Map<String, List<String>> dialogues) {
        this.dialogues = dialogues;
    }

    public List<String> getFunctions() {
        return functions;
    }
    public void setFunctions(List<String> functions) {
        this.functions = functions;
    }

    public Map<String, List<String>> getHints() {
        return hints;
    }
    public void setHints(Map<String, List<String>> hints) {
        this.hints = hints;
    }

    public Map<String, Object> getConditions() {
        return conditions;
    }
    public void setConditions(Map<String, Object> conditions) {
        this.conditions = conditions;
    }

    public Map<String, Object> getBypassMethods() {
        return bypassMethods;
    }
    public void setBypassMethods(Map<String, Object> bypassMethods) {
        this.bypassMethods = bypassMethods;
    }

    @Override
    public String toString() {
        return "Engine.NPC{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", isRepaired=" + isRepaired +
                ", plantNeedsLight=" + plantNeedsLight +
                ", hostile=" + hostile +
                ", requiresItem='" + requiresItem + '\'' +
                ", givesItem='" + givesItem + '\'' +
                ", givesItems=" + givesItems +
                ", dialogues=" + dialogues +
                ", functions=" + functions +
                ", hintsByCheckpoint=" + hints +
                ", conditions=" + conditions +
                ", bypassMethods=" + bypassMethods +
                '}';
    }
}
