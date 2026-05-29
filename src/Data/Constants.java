package Data;

public class Constants {

    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;

    }

    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING_RIGHT = 1;
        public static final int RUNNING_LEFT = 2;
        public static final int RUNNING_DOWN = 3;
        public static final int RUNNING_UP = 4;

        public static int getSpriteAmount(int playerAction) {
            switch (playerAction) {
                case IDLE:
                    return 14;
                case RUNNING_RIGHT:
                case RUNNING_LEFT:
                case RUNNING_UP:
                case RUNNING_DOWN:
                    return 2;
                default:
                    return 1;
            }

        }
    }

    public static class GameStates {
        public static final int RUNNING = 0;
        public static final int PAUSED = 1;
        public static final int LEAVE = 2;
        public static final int VICTORY = 2;
        public static final int DEFEATED = 3;
        public static final int INVENTORY = 4;
    }

    public static class Colors {
        public static final String ANSI_RESET = "\u001B[0m";
        public static final String ANSI_BLACK = "\u001B[30m";
        public static final String ANSI_RED = "\u001B[31m";
        public static final String ANSI_GREEN = "\u001B[32m";
        public static final String ANSI_YELLOW = "\u001B[33m";
        public static final String ANSI_BLUE = "\u001B[34m";
        public static final String ANSI_PURPLE = "\u001B[35m";
        public static final String ANSI_CYAN = "\u001B[36m";
        public static final String ANSI_WHITE = "\u001B[37m";
    }
}
