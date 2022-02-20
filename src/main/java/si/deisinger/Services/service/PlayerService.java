package si.deisinger.Services.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import si.deisinger.Services.model.Player;
import si.deisinger.Services.repository.PlayerRepository;

import java.util.Optional;

@Service
@Transactional
public class PlayerService {

	@Autowired
	private PlayerRepository playerRepository;

	public Iterable<Player> listAllPlayers() {
		return playerRepository.findAll();
	}

	public void savePlayer(Player player) {
		playerRepository.save(player);
	}

	public Optional<Player> getPlayer(Long id) {
		return playerRepository.findById(id);
	}

	public void deletePlayer(Long id) {
		playerRepository.deleteById(id);
	}

}