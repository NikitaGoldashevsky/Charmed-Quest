import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;

public class Main {
    private static boolean gameIsRunning = true;

    private final static Map<String, Enemy> enemyPresets = Map.of(
        "rat", new Enemy("Rat", 2, 1, 1, 1),
        "goblin", new Enemy("Goblin", 3, 2, 1, 2),
        "spider", new Enemy("Spider", 4, 2, 2, 3),
        "ogre", new Enemy("Ogre", 8, 3, 2, 4),
        "wizard", new Enemy("Wizard", 5, 7, 3, 5),
        "dragon", new Enemy("Dragon", 10, 4, 3, 6)
    );

    private static class GameLocation {
        public enum Location {
            START, FOREST, CAVE, TOWER, VILLAGE, END;

            public Location next() {
                int nextIndex = (this.ordinal() + 1) % Location.values().length;
                return Location.values()[nextIndex];
            }
        }

        public Location location;
        public boolean firstEntrance = true;

        GameLocation(Location location) {
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

        GameLocation currentLocation = new GameLocation(GameLocation.Location.START);

        while (gameIsRunning) {
            switch (currentLocation.location) {
                case VILLAGE:
                    handleInput(player, currentLocation);
                    break;
                case END:
                    gameIsRunning = false;
                    break;
                default:
                    handleLocation(player, currentLocation);
                    break;
            }
        }
        System.out.println("The game ended!");
    }

    private static void handleLocation(Player player, GameLocation location) {
        if (location.firstEntrance) {
            switch (location.location) {
                case START:
                    System.out.println("""
                            You wake up in a dense forest.
                            What are you doing here? Why do you have a heavy stick in your hand?
                            And why does your back hurts so much?
                            There is no one around to answer your questions.
                            Your improvised weapon makes you believe you can get out of here alive.
                            Type 'go' to try to get out of here.""");
                    break;
                case FOREST:
                    System.out.println("""
                            You slowly make your way through the woods
                            Sooner or later the endless trees all around will drive you crazy.
                            Suddenly, you see something in the shadows.
                            You grip the stick tighter than ever before.""");
                    sleep(5);
                    break;
                case CAVE:
                    System.out.println("""
                            You are in a dangerous cave!
                            Who knows what horrors you are going to face here?""");
                    sleep(3);
                    break;
                case TOWER:
                    System.out.println("""
                            You have come to entrance of the tallest tower you have ever seen!
                            You see a giant creature sitting at the top of it.
                            May it be a dragon? It's scary to even think about it.""");
                    sleep(4);
                    break;
            }
        }

        if (location.location == GameLocation.Location.START) {
            handleInput(player, location);
            return;
        }

        int locationOrdinal = location.location.ordinal();
        ArrayList<Enemy> locationEnemies = new ArrayList<>();

        for (Enemy enemyPreset : enemyPresets.values()) {
            if (enemyPreset.getLocation() == locationOrdinal) {
                locationEnemies.add(new Enemy(enemyPreset));
            }
        }
        int enemyChance = 50 + locationOrdinal * 10;
        Enemy currentEnemy = new Enemy(locationEnemies.get( randomChance(enemyChance) ? 0 : 1));

        System.out.printf("The %s is approaching you!\n", currentEnemy.getName());
        handleFight(player, currentEnemy);

        if (gameIsRunning) {
            System.out.println("""
                    That was a tough fight!
                    You may go now or take your time around here.
                    Type 'help' to see what you can do.""");
            handleInput(player, location);
        }
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
        System.out.println();
    }

    private static void handleInput(Player player, GameLocation currentLocation) {
        Scanner scanner = new Scanner(System.in);
        String userInput;

        do {
            System.out.print("\n> ");
            userInput = scanner.nextLine();

            if (userInput.matches("^use [1-9][0-9]*")) {
                int itemIndex = Integer.parseInt(userInput.substring(4)) - 1;
                if (player.getInventory().itemsCount() > itemIndex) {
                    Item item = player.getInventory().getItemAt(itemIndex);
                    if (item instanceof Food) {
                        player.restoreHealth(((HealthRestorer) item).restoreAmount);
                        System.out.printf("You ate %s! Your health is now %d/%d. \n", item.getName(), player.getHP(), player.getMaxHP());
                        player.getInventory().removeItemAt(itemIndex);
                    }
                    else if (item instanceof Potion) {
                        player.restoreHealth(((HealthRestorer) item).getRestoreAmount());
                        player.increaseDamage(((Potion)item).getDamageIncrease());
                        System.out.printf("You drank %s! Your health is now %d/%d.\nAnd your power increased by %d!\n",
                                item.getName(), player.getHP(), player.getMaxHP(), ((Potion)item).getDamageIncrease());
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
                    System.out.println("""
                            Commands:
                            \t'me' - show your state and location
                            \t'inv' - show your inventory
                            \tuse <1..n>' - use an item by index 'n' from your inventory
                            \t'go' - move to the next location
                            \t'stay' - stay in the current location""");
                    break;
                case "go":
                    currentLocation.location = currentLocation.location.next();
                    System.out.printf("You moved to the %s.\n", currentLocation.location.toString().toLowerCase());
                    currentLocation.firstEntrance = true;
                    break;
                case "stay":
                    System.out.printf("You stayed at the %s.\n", currentLocation.location.toString().toLowerCase());
                    currentLocation.firstEntrance = false;
                    break;
                default:
                    System.out.println("Unknown command! Type 'help' to view the list of commands.");
                    break;
            }
        }
        while (!userInput.equals("go") && !userInput.equals("stay"));
    }

    private static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            System.out.println("The sleep was interrupted.");
            Thread.currentThread().interrupt();
        }
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