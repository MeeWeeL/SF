import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Weather {

    private static final String YANDEX_API_URL = "https://api.weather.yandex.ru/v2/forecast";
    private static final String YANDEX_API_KEY = "f86759f0-8a95-48cf-ad4d-752ccc14f341";
    private static final double lat = 55.45;
    private static final double lon = 37.36;
    private static final int period = 7; // Период, за который будет рассчитано среднее (7 дней)

    public static void main(String[] args) {

        try {
            String responseStr = getWeatherData();
            System.out.println("Полученные данные: ");
            System.out.println(responseStr);

            int temp = extractTemperature(responseStr);
            System.out.println("Температура: " + temp + "°C");

            double averageTemp = calculateAverageTemp(responseStr);
            System.out.println("Средняя температура за " + period + " дней: " + averageTemp + "°C");

            String cityName = extractCityName(responseStr);
            System.out.println("Город: " + cityName);

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static String getWeatherData() throws Exception {
        URL url = new URL(YANDEX_API_URL + "?lat=" + lat + "&lon=" + lon);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Yandex-API-Key", YANDEX_API_KEY);
        connection.setRequestProperty("Content-Type", "application/json");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private static int extractTemperature(String response) {
        int tempIndex = response.indexOf("\"temp\":") + period;
        int endIndex = response.indexOf(",", tempIndex);
        return Integer.parseInt(response.substring(tempIndex, endIndex));
    }

    private static double calculateAverageTemp(String response) {
        double totalTemp = 0;
        int count = 0;

        int forecastIndex = response.indexOf("\"forecasts\":") + 14;
        int endIndex = response.indexOf("]", forecastIndex);

        String forecasts = response.substring(forecastIndex, endIndex);
        String[] dailyForecasts = forecasts.split("\\},\\{");

        for (String dayForecast : dailyForecasts) {
            if (++count > period) break;
            int dayTemp = extractDailyTemperature(dayForecast);
            totalTemp += dayTemp;
        }

        return count > 0 ? totalTemp / count : 0;
    }

    private static int extractDailyTemperature(String dayForecast) {
        int dayTempIndex = dayForecast.indexOf("\"temp\":") + period;
        int endIndex = dayForecast.indexOf(",", dayTempIndex);
        return Integer.parseInt(dayForecast.substring(dayTempIndex, endIndex));
    }

    private static String extractCityName(String response) {
        String city;
        try {
            int geoIndex = response.indexOf("\"abbr\":\"") + 8;
            int endIndex = response.indexOf("\",\"dst\"", geoIndex);
            city = response.substring(geoIndex, endIndex);
        } catch (Exception e) {
            city = e.getMessage();
        }
        return city;
    }
}
