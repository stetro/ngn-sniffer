package de.fhkoeln.ngn.data;


import java.util.List;

public class PointLocation {
    private String type;
    private List<Double> coordinates;

    public PointLocation(String type, List<Double> coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public double getLat() {
        return coordinates.get(0);
    }

    public double getLng() {
        return coordinates.get(1);
    }
}
