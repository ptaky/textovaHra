package Command;

public class Help_Command implements Command {
    @Override
    public String execute(String command) {
        return "";
    }

    @Override
    public boolean exit() {
        return false;
    }
}
