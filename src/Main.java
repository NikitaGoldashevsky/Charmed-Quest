public class Main {
    public static void main(String[] args) {
        handleGame();
    }

    private static void handleGame() {
        boolean gameIsRunning = true;
        enum GameLocation {
            START, FOREST, CAVE, TOWER, VILLAGE, END
        };

        GameLocation currentLocation = GameLocation.START;
        Entity currentEnemy;

        while (gameIsRunning) {
            switch (currentLocation) {
                case START:
                    System.out.println("You are in the start location.");
                    currentLocation = GameLocation.FOREST;
                    break;
                case FOREST:
                    System.out.println("You are in a forest.");
                    currentEnemy = new Entity("Rat", 3, 1);
                    System.out.println("The rat is attacking you!");
                    break;
                case CAVE:
                    System.out.println("You are in a cave.");
                    currentEnemy = new Entity("Spider", 3, 1);
                    break;
                case TOWER:
                    System.out.println("You are in the Tower.");
                    currentEnemy = new Entity("Wizard", 3, 1);
                    break;
                case VILLAGE:
                    System.out.println("You are in a village.");
                    break;
                case END:
                    System.out.println("You are in the end.");
                    break;
            }
        }
    }
}