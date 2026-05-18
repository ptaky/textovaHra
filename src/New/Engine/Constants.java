package New.Engine;

public class Constants {

    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;

    }

    public static class PlayerConstants {
        public static final int DELTA_MOVE_VALUE = 3;

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
}
