import java.util.ArrayList;

class Airport {
    public String airportCode;
    public String airfieldName;
    double latitude;
    double longitude;
    int parkingCost;

    ArrayList<String> directions = new ArrayList<String>();

    public Airport(String code, String name, double latitude, double longitude, int parkingCost) {
        this.airportCode = code;
        this.airfieldName = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.parkingCost = parkingCost;
    }

    public Double distanceTo(Airport airport) {
        double lat1 = this.latitude;
        double lon1 = this.longitude;
        double lat2 = airport.latitude;
        double lon2 = airport.longitude;
        double R = 6371; // kilometres
        double φ1 = Math.toRadians(lat1);
        double φ2 = Math.toRadians(lat2);
        double Δφ = Math.toRadians(lat2 - lat1);
        double Δλ = Math.toRadians(lon2 - lon1);
        double a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) + Math.cos(φ1) * Math.cos(φ2) * Math.sin(Δλ / 2)
                * Math.sin(Δλ / 2);
        double c = Math.asin(Math.sqrt(a));
        double d = 2 * R * c;
        return d;
    }
}