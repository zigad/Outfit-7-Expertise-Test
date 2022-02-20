package DemoApplication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import si.deisinger.Services.Fun7Application;
import si.deisinger.Services.controller.FeaturesController;
import si.deisinger.Services.model.Features;
import si.deisinger.Services.model.Player;
import si.deisinger.Services.service.PlayerService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Fun7Application.class)
public class PlayerTests {

	//Simple tests! Much more would be needed!

	@Autowired
	PlayerService playerService;

	@Autowired
	FeaturesController featuresController;

	@Test
	public void findById() {
		Optional<Player> player = playerService.getPlayer(5634161670881280L);
		assertEquals(5634161670881280L, player.get().getId());
		assertEquals(100, player.get().getUsage());
	}

	@Test
	public void checkMultiplayerNotUS() {
		Long id = 5642368648740864L;
		String timeZone = "Europe/London";
		String cc = "LDN";

		int usageBefore = playerService.getPlayer(id).get().getUsage();

		Features features = featuresController.checkFeatures(timeZone, id, cc);
		assertEquals("disabled", features.getMultiplayer());
		assertEquals(1,  playerService.getPlayer(id).get().getUsage() - usageBefore);
	}

	@Test
	public void checkMultiplayerUS() {
		Long id = 5642368648740864L;
		String timeZone = "Europe/London";
		String cc = "US";

		int usageBefore = playerService.getPlayer(id).get().getUsage();

		Features features = featuresController.checkFeatures(timeZone, id, cc);
		assertEquals("enabled", features.getMultiplayer());
		assertEquals(1,  playerService.getPlayer(id).get().getUsage() - usageBefore);
	}
}
