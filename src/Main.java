import java.util.Map;
import java.util.Scanner;
import java.util.Random;

public class Main {
    private static boolean gameIsRunning = true;

    private final static Map<String, Enemy> enemyPresets = Map.of(
        "rat", new Enemy("Rat", 2, 1, 1),
        "goblin", new Enemy("Goblin", 3, 2, 2),
        "spider", new Enemy("Spider", 4, 2, 3),
        "ogre", new Enemy("Ogre", 8, 3, 4),
        "wizard", new Enemy("Wizard", 5, 7, 5),
        "dragon", new Enemy("Dragon", 10, 6, 6)
    );

    enum GameLocation {
        START, FOREST, CAVE, TOWER, VILLAGE, END;

        public GameLocation next() {
            int nextIndex = (this.ordinal() + 1) % GameLocation.values().length;
            return GameLocation.values()[nextIndex];
        }
    }

    private static class MutableGameLocation {
        public GameLocation location;

        MutableGameLocation(GameLocation location) {
            this.location = location;
        }
    }

    public static void main(String[] args) {
        handleGame();
    }

    private static void handleGame() {
        Player player = new Player(4, 10, 3);
        player.getInventory().addItem(new Weapon("Heavy stick", 2));
        player.getInventory().addItem(new Food("Apple", 3));

        MutableGameLocation currentLocation = new MutableGameLocation(GameLocation.START);
        Enemy currentEnemy;

            while (gameIsRunning) {
                switch (currentLocation.location) {
                    case START:
                        System.out.println("You are in the start location!");
                        System.out.println("Type 'go' to move forward.");

                        handleInput(player, currentLocation);
                        break;
                    case FOREST:
                        currentEnemy = (randomChance(60) ? enemyPresets.get("rat") : enemyPresets.get("goblin"));
                        System.out.printf("The %s is approaching you!\n", currentEnemy.getName());
                        handleFight(player, currentEnemy);

                        System.out.println("\nThat was a tough fight!\n" +
                                "You may go now or take your time around here.");
                        handleInput(player, currentLocation);
                        break;
                    case CAVE:
                        currentEnemy = (randomChance(70) ? enemyPresets.get("spider") : enemyPresets.get("ogre"));
                        System.out.printf("The %s is approaching you!\n", currentEnemy.getName());
                        handleFight(player, currentEnemy);

                        System.out.println("\nThat was a tough fight!\n" +
                                "You may go now or take your time around here.");
                        handleInput(player, currentLocation);
                        break;
                    case TOWER:
                        currentEnemy = (randomChance(80) ? enemyPresets.get("wizard") : enemyPresets.get("dragon"));
                        System.out.printf("The %s is approaching you!\n", currentEnemy.getName());
                        handleFight(player, currentEnemy);

                        System.out.println("\nThat was a tough fight!\n" +
                                "You may go now or take your time around here.");
                        handleInput(player, currentLocation);
                        break;
                    case VILLAGE:
                        handleInput(player, currentLocation);
                        break;
                    case END:
                        gameIsRunning = false;
                        break;
            }
        }
        System.out.println("The game ended!");
    }

    private static void handleFight(Player player, Enemy enemy) {
        sleep();

        System.out.println("\nThe fight begins!");
        System.out.println(player);
        System.out.println(enemy);
        sleep();

        while (true) {
            handleAttack(player, enemy);
            if (!enemy.isAlive()) break;
            sleep();
            handleAttack(enemy, player);
            if (!player.isAlive()) break;
            sleep();
        }

        if (!enemy.isAlive()) {
            System.out.printf("\n%s has been defeated!\n", enemy.getName());
            handleLoot(enemy, player);
        }
        else if (!player.isAlive()) {
            System.out.println("\nYou have been defeated!");
            gameIsRunning = false;
        }
        sleep();
        return;
    }

    private static void handleAttack(Entity attacker, Entity target) {
        int attackDamage = attacker.getDamage();
        System.out.printf("\n%s attacks %s with %d damage!\n", attacker.getName(), target.getName(), attackDamage);
        target.looseHP(attackDamage);
        System.out.println(target);
    }

    private static void handleLoot(Enemy enemy, Player player) {
        Item item = enemy.getLoot();
        if (item != null) {
            System.out.printf("You looted %s from the defeated %s!\n", item.getName(), enemy.getName());

            if (item instanceof Weapon) {
                if (item.getName().equals(player.getInventory().getWeapon().getName())) {
                    System.out.printf("You already have %s as your weapon.\n", item.getName());
                }
                else if (((Weapon) item).getDamage() > player.getInventory().getWeapon().getDamage()) {
                    player.getInventory().addItem(item);
                    System.out.printf("%s is your new weapon!\n", item);
                }
                else {
                    System.out.printf("%s is worse than your current weapon.\n", item);
                }
            }
            else {
                player.getInventory().addItem(item);
            }
        }
        else {
            System.out.printf("Unfortunately, the defeated %s was stingy with loot.\n", enemy.getName());
        }
    }

    private static void handleInput(Player player, MutableGameLocation currentLocation) {
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
                    System.out.printf("You are at the %s.\n", currentLocation.location.toString().toLowerCase());
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
                    System.out.println("\t'inv' - show your inventory");
                    System.out.println("\t'use <1..n>' - use an item by index 'n' from your inventory");
                    System.out.println("\t'go' - move to the next location");
                    System.out.println("\t'stay' - stay in the current location");
                    break;
                case "go":
                    currentLocation.location = currentLocation.location.next();
                    System.out.printf("You moved to the %s.\n", currentLocation.location.toString().toLowerCase());
                    break;
                case "stay":
                    System.out.printf("You stayed at the %s.\n", currentLocation.location.toString().toLowerCase());
                    break;
                default:
                    System.out.println("Unknown command! Type 'help' to view list of commands.");
                    break;
            }
        }
        while (!userInput.equals("go") && !userInput.equals("stay"));
    }

    private static void sleep() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            System.out.println("The sleep was interrupted.");
            Thread.currentThread().interrupt();
        }
    }

    public static boolean randomChance(int chance) {
        Random random = new Random();
        return random.nextInt(100) < chance;
    }
}