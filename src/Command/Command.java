package Command;

/**
 * Interface for all commands.
 * @author Ondřej Ptáček
 */
public interface Command {
    String execute(String command);
    boolean exit();
}
