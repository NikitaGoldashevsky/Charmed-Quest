import java.util.Map;

public class LocationEntranceText {

    public final static Map<Main.GameLocation.Location, String> texts = Map.of(
            Main.GameLocation.Location.START, """
                            You wake up in a dense forest.
                            What are you doing here? Why do you have a heavy stick in your hand?
                            And why does your back hurts so much?
                            There is no one around to answer your questions.
                            Your improvised weapon makes you believe you can get out of here alive.
                            Type 'go' to try to get out of here.""",
            Main.GameLocation.Location.FOREST, """
                            You slowly make your way through the woods.
                            Sooner or later the endless trees all around will drive you crazy.
                            Suddenly, you see something in the shadows.
                            You grip the stick tighter than ever before.""",
            Main.GameLocation.Location.CAVE, """
                            You are in a dangerous cave!
                            Who knows what horrors you are going to face here?""",
            Main.GameLocation.Location.TOWER, """
                            You have come to entrance of the tallest tower you have ever seen!
                            The legends say that terrible hazards awaits those dare to get atop.
                            And who is there right now? A vampire? A powerful mage?
                            It may be even something worse. Something flying and fire-breathing.
                            It's scary to even think about it.""",
            Main.GameLocation.Location.END, """
                            Sweaty and tired, you reach the top of the tower.
                            Your journey may end right here, but you've already gone too far.
                            """,
            Main.GameLocation.Location.VILLAGE, """
                            Your path has led you to a peaceful village!
                            Friendly human faces seem like a miracle after the hardship you have gone through.
                            The village elder is happy to offer you some food and a bed to rest for a while."""
    );

    public static String get(Main.GameLocation location) {
        return texts.get(location.location);
    }
}
