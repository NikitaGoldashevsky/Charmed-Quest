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

    private static void handleInput(Player player) {
        Scanner scanner = new Scanner(System.in);
        String userInput;

        do {
            System.out.print("\n> ");
            userInput = scanner.nextLine();

            if (userInput.matches("^use [1-9][0-9]*")) {
                int itemIndex = Integer.parseInt(userInput.substring(4)) - 1;
                if (player.getInventory().itemsCount() > itemIndex) {
                    Item item = player.getInventory().getItemAt(itemIndex);
                    if (item instanceof HealthRestorer) {
                        player.restoreHealth(((HealthRestorer) item).restoreAmount);
                        System.out.printf("You used %s! Your health is now %d/%d. \n", item.getName(), player.getHP(), player.getMaxHP());
                        player.getInventory().removeItemAt(itemIndex);
                    }
                }
                else {
                    System.out.printf("There is no item by index %d in your inventory\n", itemIndex);
                }
                continue;
            }

            switch (userInput) {
                case "me":
                    System.out.printf("You are at the %s.\n", currentLocation);
                    System.out.println(player);
                    break;
                case "inv":
                    System.out.println("Your weapon: " + player.getInventory().getWeapon());

                    if (player.getInventory().itemsCount() == 0) {
                        System.out.println("You have nothing in the inventory");
                    }
                    else {
                        System.out.println("Your inventory:");
                        for (int i = 0; i < player.getInventory().itemsCount(); i++) {
                            System.out.printf("\t%d - %s\n", i+1, player.getInventory().getItemAt(i));
                        }
                    }
                    break;
                case "help":
                    System.out.println("Commands:");
                    System.out.println("\t'me' - show your state and location");
                    System.out.println("\t'inv' - show inventory");
                    System.out.println("\t'use <1..n>' - use an item by index 'n' from your inventory");
                    System.out.println("\t'go' - move to the next location");
                    break;
                case "go":
                    break;
                default:
                    System.out.println("Unknown command! Type 'help' to view list of commands.");
                    break;
            }
        }
        while (!userInput.equals("go"));
    }
}