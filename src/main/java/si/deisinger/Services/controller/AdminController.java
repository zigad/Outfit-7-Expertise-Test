package si.deisinger.Services.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.deisinger.Services.model.Player;
import si.deisinger.Services.service.PlayerService;

import java.util.Optional;

@RestController
@RequestMapping("")
public class AdminController {

	@Autowired
	PlayerService playerService;

	/**
	 * GET Request will return all available players in the database with all available information
	 *
	 * @return all available players.
	 */
	@GetMapping("/admin/players")
	public Iterable<Player> listPlayers() {
		return playerService.listAllPlayers();
	}

	/**
	 * GET Request will return specific player with all information that's available in database.
	 *
	 * @param id Input is player ID
	 * @return All information available in database
	 */
	@GetMapping("/admin/players/{id}")
	public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
		Optional<Player> player = playerService.getPlayer(id);
		return player.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * POST Request for creating new player in the database.
	 *
	 * @param games POST request must be Content-Type: JSON
	 */
	@PostMapping("/admin/players")
	public void addPlayer(@RequestBody Player games) {
		playerService.savePlayer(games);
	}

	/**
	 * DELETE Request for deleting player from database
	 *
	 * @param id Parameter must be player ID.
	 */
	@DeleteMapping("/admin/players/{id}")
	public void deletePlayer(@PathVariable Long id) {
		playerService.deletePlayer(id);
	}

	/**
	 * PUT Request for modifying existing value in database
	 */
	@PutMapping("/admin/players/{id}")
	public void updatePlayer(@RequestBody Player newPlayer, @PathVariable Long id) {
		Optional<Player> updatedPlayer = playerService.getPlayer(id);
		if (updatedPlayer.isPresent()) {
			updatedPlayer.get().setUsage(newPlayer.getUsage());
			playerService.savePlayer(updatedPlayer.get());
			ResponseEntity.accepted();
		}
	}
}