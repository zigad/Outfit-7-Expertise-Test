package si.deisinger.Services.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import si.deisinger.Services.model.Features;
import si.deisinger.Services.model.Player;
import si.deisinger.Services.service.PlayerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.TimeZone;

@RestController
public class FeaturesController {

	public static final String USER_NAME = "fun7user";
	public static final String PASSWORD = "fun7pass";

	static final String URL = "https://us-central1-o7tools.cloudfunctions.net/fun7-ad-partner?countryCode=";

	@Autowired
	PlayerService playerService;

	@GetMapping("/services")
	public Features checkFeatures(@RequestParam("timezone") String timezone, @RequestParam("userId") Long userId, @RequestParam("cc") String cc) {
		Features features = new Features("disabled", "disabled", "disabled");
		Optional<Player> player = playerService.getPlayer(userId);
		if (player.isPresent()) {
			// Increase Usage for user and save to database
			incrementUsage(player.get());
			//Checking Time in Ljubljana
			features.setCustomer_support(checkCustomerSupport(timezone));
			//Check if player has multiplayer enabled
			checkMultiplierSupport(cc, features, player.get());
			//Check if Ads are enabled
			features.setAds(checkForAds(cc));
		}
		return features;
	}

	private String checkForAds(String cc) {
		try {
			URL url = new URL(URL + cc);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((USER_NAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8)));
			connection.setDoOutput(true);

			int status = connection.getResponseCode();

			if (status == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuilder content = new StringBuilder();
				while ((inputLine = in.readLine()) != null) {
					content.append(inputLine);
				}
				in.close();
				JsonObject convertedObject = new Gson().fromJson(content.toString(), JsonObject.class);
				if (convertedObject.get("ads").getAsString().equals("sure, why not!")) {
					return "enabled";
				} else {
					return "disabled";
				}
			} else {
				return "Error while getting API check for ads: Code: " + status + " , message: " + connection.getResponseMessage();
			}
		} catch (IOException e) {
			throw new RuntimeException("Error: ", e);
		}
	}

	private void checkMultiplierSupport(String cc, Features features, Player player) {
		if (player.getUsage() > 5 && cc.equals("US")) {
			features.setMultiplayer("enabled");
		}
	}

	private void incrementUsage(Player player) {
		player.incrementUsage();
		playerService.savePlayer(player);
	}

	public String checkCustomerSupport(String timezone) {
		if (Arrays.asList(TimeZone.getAvailableIDs()).contains(timezone)) {
			LocalDateTime timeLjubljana = LocalDateTime.now(ZoneId.of("Europe/Ljubljana"));
			LocalDateTime timeUser = LocalDateTime.now(ZoneId.of(timezone));

			//We could write this data in database so no hardcoding is done
			LocalTime startHour = LocalTime.parse("09:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));
			LocalTime endHour = LocalTime.parse("15:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));

			int hourDifference = (int) Duration.between(timeLjubljana, timeUser).toHours();
			if (timeLjubljana.getDayOfWeek().getValue() < 6 && startHour.isBefore(LocalTime.from(timeUser.minusHours(hourDifference))) && endHour.isAfter(LocalTime.from(timeUser.minusHours(hourDifference)))) {
				return "enabled";
			} else {
				return "disabled";
			}
		}
		return "Wrong timezone provided: " + timezone;
	}

}
