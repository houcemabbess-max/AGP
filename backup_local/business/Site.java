package business;

public class Site {
    private int id;
    private String name;
    private double entryPrice;
    private double duration;
    private Coordinates coordinates;
    private TypeSite type;
    private String description;

    public Site(int id, String name, double entryPrice, double duration,
                Coordinates coordinates, TypeSite type, String description) {
        this.id = id;
        this.name = name;
        this.entryPrice = entryPrice;
        this.duration = duration;
        this.coordinates = coordinates;
        this.type = type;
        this.description = description;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getEntryPrice() { return entryPrice; }
    public double getDuration() { return duration; }
    public Coordinates getCoordinates() { return coordinates; }

    
    public TypeSite getType() { 
        return type; 
    }

    public String getDescription() { return description; }

    @Override
    public String toString() {
        return "Site{name='" + name + "', type=" + type + "}";
    }
}
