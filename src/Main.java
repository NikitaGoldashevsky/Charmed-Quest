public class Main {
    static boolean gameIsRunning = true;

    enum GameLocation {
        START, FOREST, CAVE, TOWER, VILLAGE, END
    };

    public static void main(String[] args) {
        handleGame();
    }

    private static void handleGame() {
        Player player = new Player(4, 10, 3);
        Player.Inventory inventory = player.getInventory();

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
                    System.out.println("The rat is approaching you!");
                    handleFight(player, currentEnemy);
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
                    gameIsRunning = false;
                    break;
            }
        }
        System.out.println("The game ended!");
    }

    private static void handleFight(Player player, Entity enemy) {
        Scanner scanner = new Scanner(System.in);
        System.out.println();

        System.out.println("The fight begins!");
        System.out.println(player.toString());
        System.out.println(enemy.toString());

        while (true) {
            handleAttack(player, enemy);
            if (!enemy.isAlive()) break;
            handleAttack(enemy, player);
            if (!player.isAlive()) break;
        }

        if (!enemy.isAlive()) {
            System.out.printf("\n%s has been defeated!\n", enemy.getName());
        }
        else if (!player.isAlive()) {
            System.out.println("\nYou were defeated!");
            gameIsRunning = false;
        }
        return;
    }

    private static void handleAttack(Entity attacker, Entity target) {
        int attackDamage = attacker.getDamage();
        System.out.printf("\n%s attacks %s with %d damage!\n", attacker.getName(), target.getName(), attackDamage);
        target.looseHP(attackDamage);
        System.out.println(target.toString());
    }
}