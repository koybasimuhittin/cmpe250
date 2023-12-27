
/**
 * Main
 */

import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Main {

    public static HashMap<String, Airport> airports = new HashMap<String, Airport>();
    public static HashMap<String, Airfield> airfields = new HashMap<String, Airfield>();

    public static void main(String[] args) throws Exception {
        File file = new File("airports/AS-0.csv");
        Scanner scanner = new Scanner(file);
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] values = line.split(",");
            Airport airport = new Airport(values[0], values[1], Double.parseDouble(values[2]),
                    Double.parseDouble(values[3]), Integer.parseInt(values[4]));
            airports.put(airport.airportCode, airport);
            if (airfields.containsKey(airport.airfieldName)) {
                airfields.get(airport.airfieldName).airports.put(airport.airportCode, airport);
            } else {
                Airfield airfield = new Airfield(airport.airfieldName);
                airfield.airports.put(airport.airportCode, airport);
                airfields.put(airport.airfieldName, airfield);
            }
        }

        file = new File("directions/AS-0.csv");
        scanner = new Scanner(file);
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] values = line.split(",");
            Airport airport = airports.get(values[0]);
            airport.directions.add(values[1]);
        }

        file = new File("weather.csv");
        scanner = new Scanner(file);
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] values = line.split(",");
            Airfield airfield = airfields.get(values[0]);
            if (airfield == null) {
                continue;
            }
            airfield.weatherCodeToTimeStamps[Integer.parseInt(values[2])].add(Long.parseLong(values[1]));
            airfield.timeStampToWeatherCode.put(Long.parseLong(values[1]), Integer.parseInt(values[2]));
        }

        file = new File("missions/AS-0.in");
        scanner = new Scanner(file);
        String type = scanner.nextLine();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] values = line.split(" ");
            Airport from = airports.get(values[0]);
            Airport to = airports.get(values[1]);
            Long timeOrigin = Long.parseLong(values[2]);
            Long timeDestination = Long.parseLong(values[3]);

            HashMap<String, Double> costs = new HashMap<String, Double>();
            HashMap<String, String> fromWhere = new HashMap<String, String>();
            costs.put(from.airportCode, 0.0);
            PriorityQueue<Node> queue = new PriorityQueue<Node>();
            queue.add(new Node(from.airportCode, null, 0.0, timeOrigin));
            while (!queue.isEmpty()) {
                Node node = queue.poll();
                if (node.airportCode.equals(to.airportCode)) {
                    break;
                }
                Airfield airfield = airfields.get(airports.get(node.airportCode).airfieldName);
                Airport airport = airports.get(node.airportCode);
                for (String direction : airport.directions) {
                    Airport nextAirport = airports.get(direction);
                    Double distance = airport.distanceTo(nextAirport);
                    Airfield nextAirfield = airfields.get(nextAirport.airfieldName);
                    Double cost = node.cost + airfield.weatherCoefficient(nextAirfield, timeOrigin) * 300 + distance;
                    if (costs.containsKey(nextAirport.airportCode)) {
                        if (cost < costs.get(nextAirport.airportCode)) {
                            costs.put(nextAirport.airportCode, cost);
                            queue.add(new Node(nextAirport.airportCode, node.airportCode, cost, node.time));
                            fromWhere.put(nextAirport.airportCode, node.airportCode);
                        }
                    } else {
                        costs.put(nextAirport.airportCode, cost);
                        queue.add(new Node(nextAirport.airportCode, node.airportCode, cost, node.time));
                        fromWhere.put(nextAirport.airportCode, node.airportCode);
                    }
                }
            }
            String airportCode = to.airportCode;
            ArrayList<String> path = new ArrayList<String>();
            while (airportCode != null) {
                path.add(airportCode);
                airportCode = fromWhere.get(airportCode);
            }
            for (int i = path.size() - 2; i >= 0; i--) {
                System.out.print(path.get(i) + " ");
                System.out.print(costs.get(path.get(i)) + " ");
                Airfield airfield = airfields.get(airports.get(path.get(i)).airfieldName);
                Airfield previousAirfield = airfields.get(airports.get(path.get(i + 1)).airfieldName);
                System.out.println(previousAirfield.weatherCoefficient(airfield, timeOrigin));
                System.out.println(previousAirfield.name + " " + previousAirfield.timeStampToWeatherCode.get(timeOrigin)
                        + " " + airfield.name + " "
                        + airfield.timeStampToWeatherCode.get(timeOrigin));
            }
            System.out.println(costs.get(to.airportCode));
        }
    }
}
