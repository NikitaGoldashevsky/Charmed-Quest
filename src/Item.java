public abstract class Item implements IPrintable {
    protected String name;

    protected Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public abstract String toString();
}
