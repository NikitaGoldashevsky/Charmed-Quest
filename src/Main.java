import java.util.*;

public class Main {
    private static boolean gameIsRunning = true;
    private static final int VILLAGE_LOCATION_CHANCE = 35;
    private static final List<Item> VILLAGE_GIFTS = List.of(
            new Food("Strawberry Pie", 4),
            new Food("Meatier Stew", 5),
            new Potion("Chamomile Beer", 1, 3 )
    );

    private final static Map<String, Enemy> ENEMY_PRESETS = Map.of(
            "rat", new Enemy("Rat", 2, 1, 1, 1),
            "goblin", new Enemy("Goblin", 3, 2, 1, 2),
            "spider", new Enemy("Spider", 5, 2, 2, 3),
            "ogre", new Enemy("Ogre", 9, 3, 2, 4),
            "wizard", new Enemy("Wizard", 5, 8, 3, 6),
            "golem", new Enemy("Golem", 12, 5, 3, 6)
    );

    private final static Map<String, Boss> BOSS_PRESETS = Map.of(
            "dragon", new Boss("The Elder Dragon", 50, 2, 5, 3)
    );

    private static class GameLocation {
        public enum Location {
            START, FOREST, CAVE, TOWER, END, VILLAGE
        }

        private Location lastLocation;

        public Location location;
        public boolean firstEntrance = true;

        public void moveNext() {
            if (location == Location.VILLAGE) {
                location = lastLocation;
                lastLocation = Location.VILLAGE;
                moveNext();
            }
            else {
                if (lastLocation != Location.VILLAGE && randomChance(VILLAGE_LOCATION_CHANCE)) {
                    lastLocation = location;
                    location = Location.VILLAGE;
                } else {
                    lastLocation = location;
                    int nextIndex = (location.ordinal() + 1) % Location.values().length;
                    location = Location.values()[nextIndex];
                }
            }
        }

        GameLocation(Location location) {
            this.location = location;
        }
    }

    public static void main(String[] args) {
        handleGame();
    }

    private static void handleGame() {
        Player player = new Player(4, 10, 0);
        player.getInventory().addItem(new Weapon("Heavy stick", 2));
        player.getInventory().addItem(new Food("Apple", 3));

        GameLocation currentLocation = new GameLocation(GameLocation.Location.START);

        while (gameIsRunning) {
            handleLocation(player, currentLocation);
        }
        System.out.println("The game ended.");
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
                            You slowly make your way through the woods.
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
                case END:
                    System.out.println("""
                            Sweaty and tired, you reach the top of the tower.
                            Your journey may end right here, as the giant dragon is in front of you.
                            It looks at you with despair. Do you even have any chances against it?
                            At least, you should try.""");
                    sleep(4);
                    break;
                case VILLAGE:
                    System.out.println("""
                            Your path has led you to a peaceful village!
                            Friendly human faces seem like a miracle after the hardship you have gone through.
                            The village elder is happy to offer you some food and a bed to rest for a while.""");
                    sleep(4);
                    break;
            }
        }

        if (location.location == GameLocation.Location.START) {
            handleInput(player, location);
            return;
        }
        else if (location.location == GameLocation.Location.VILLAGE) {
            Random random = new Random();
            int randomItemIndex = random.nextInt(VILLAGE_GIFTS.size());

            Item item = (VILLAGE_GIFTS.get(randomItemIndex));
            System.out.printf("\nYou have been given a %s!\n", item.getName());
            player.getInventory().addItem(item);

            System.out.print("You have a good rest and feel healthier than ever!\n");
            player.restoreHealth(player.getMaxHP() - player.getHP());
            System.out.printf("Your health is now %d/%d.\n", player.getHP(), player.getMaxHP());
            System.out.println("You leave the village.\n");

            location.moveNext();
            System.out.printf("You moved to the %s.\n", location.location.toString().toLowerCase());
            return;
        }
        else if (location.location == GameLocation.Location.END) {
            handleBossFight(player);
            return;
        }

        // Common location logic: Forest, Cave, Tower
        Enemy enemy = new Enemy(getLocationEnemy(location));

        System.out.printf("The %s is approaching you!\n", enemy.getName());
        handleFight(player, enemy);

        if (gameIsRunning) {
            System.out.println("""
                    That was a tough fight!
                    You may go now or take your time around here.
                    Type 'help' to see what you can do.""");
            handleInput(player, location);
        }
    }

    private static void handleBossFight(Player player) {
        Boss bossEnemy = BOSS_PRESETS.get("dragon");
        sleep();

        System.out.println("\nThe final fight begins!");
        System.out.println(player);
        System.out.println(bossEnemy);
        sleep();

        while (true) {
            handleAttack(player, bossEnemy);
            if (!bossEnemy.isAlive()) break;
            sleep();

            handleAttack(bossEnemy, player);
            if (!player.isAlive()) break;
            sleep();

            bossEnemy.healItself();
            System.out.printf("%s heals itself for a %d HP!\n", bossEnemy.getName(), bossEnemy.getHealingAmount());
            bossEnemy.weakenPlayer(player);
            System.out.printf("%s weakens your power by %d!\n", bossEnemy.getName(), bossEnemy.getWeakeningAmount());
        }

        if (!bossEnemy.isAlive()) {
            System.out.printf("\n%s has been defeated!\n", bossEnemy.getName());
            System.out.println("""
                    You have beat the game!
                    Thanks for playing, and have a nice day!
                    """);
        }
        else if (!player.isAlive()) {
            System.out.println("\nYou have been defeated!");
        }
        gameIsRunning = false;
    }

    private static Enemy getLocationEnemy(GameLocation location) {
        int locationOrdinal = location.location.ordinal();
        ArrayList<Enemy> locationEnemies = new ArrayList<>();

        for (Enemy enemyPreset : ENEMY_PRESETS.values()) {
            if (enemyPreset.getLocation() == locationOrdinal) {
                locationEnemies.add(new Enemy(enemyPreset));
            }
        }

        Random random = new Random();
        int randomIndex = random.nextInt(locationEnemies.size());
        return locationEnemies.get(randomIndex);
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
                    System.out.printf("There is no item by index %d in your inventory\n", itemIndex+1);
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
                            \t'use <1..n>' - use an item by index 'n' from your inventory
                            \t'go' - move to the next location
                            \t'stay' - stay in the current location""");
                    break;
                case "go":
                    currentLocation.moveNext();
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