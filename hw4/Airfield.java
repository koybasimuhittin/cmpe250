import java.util.ArrayList;
import java.util.HashMap;

public class Airfield {
    public String name;
    public HashMap<String, Airport> airports;
    public ArrayList<Long>[] weatherCodeToTimeStamps = new ArrayList[32];
    public HashMap<Long, Integer> timeStampToWeatherCode = new HashMap<Long, Integer>();

    public Airfield(String name) {
        this.name = name;
        this.airports = new HashMap<String, Airport>();
        for (int i = 0; i < 32; i++) {
            weatherCodeToTimeStamps[i] = new ArrayList<Long>();
        }
    }

    public Double weatherMultiplier(int weatherCode) {
        int Bw = (weatherCode & 16) >> 4; // Wind
        int Br = (weatherCode & 8) >> 3; // Rain
        int Bs = (weatherCode & 4) >> 2; // Snow
        int Bh = (weatherCode & 2) >> 1; // Hail
        int Bb = weatherCode & 1; // Bolt

        // Calculating the weather multiplier
        double W = (Bw * 1.05 + (1 - Bw)) *
                (Br * 1.05 + (1 - Br)) *
                (Bs * 1.10 + (1 - Bs)) *
                (Bh * 1.15 + (1 - Bh)) *
                (Bb * 1.20 + (1 - Bb));

        return W;
    }

    public Double weatherCoefficient(Airfield airfield) {
        Double coefficient = Double.MAX_VALUE;
        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 32; j++) {
                if (weatherCodeToTimeStamps[i].size() == 0 || airfield.weatherCodeToTimeStamps[j].size() == 0) {
                    continue;
                }
                coefficient = coefficient < weatherMultiplier(i) * weatherMultiplier(j) ? coefficient
                        : weatherMultiplier(i) * weatherMultiplier(j);
            }
        }
        System.out.println(coefficient);
        return coefficient;
    }

    public Double weatherCoefficient(Airfield airfield, Long time) {
        return weatherMultiplier(timeStampToWeatherCode.get(time))
                * airfield.weatherMultiplier(timeStampToWeatherCode.get(time));
    }
}
