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

    public Long flightDuration(Airport airport, String type, Double distance) {
        if (type.equals("Carreidas 160")) {
            if (distance <= 175)
                return Long.valueOf(6 * 60 * 60);
            else if (distance <= 350)
                return Long.valueOf(12 * 60 * 60);
            else
                return Long.valueOf(18 * 60 * 60);
        } else if (type.equals("Orion III")) {
            if (distance <= 1500)
                return Long.valueOf(6 * 60 * 60);
            else if (distance <= 3000)
                return Long.valueOf(12 * 60 * 60);
            else
                return Long.valueOf(18 * 60 * 60);
        } else if (type.equals("Skyfleet S570")) {
            if (distance <= 500)
                return Long.valueOf(6 * 60 * 60);
            else if (distance <= 1000)
                return Long.valueOf(12 * 60 * 60);
            else
                return Long.valueOf(18 * 60 * 60);
        } else if (type.equals("T-16 Skyhopper")) {
            if (distance <= 2500)
                return Long.valueOf(6 * 60 * 60);
            else if (distance <= 5000)
                return Long.valueOf(12 * 60 * 60);
            else
                return Long.valueOf(18 * 60 * 60);
        } else {
            return Long.valueOf(0);
        }
    }
}