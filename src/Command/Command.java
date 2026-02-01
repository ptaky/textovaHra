package Command;

public interface Command {
    String execute(String command);
    boolean exit();
}
