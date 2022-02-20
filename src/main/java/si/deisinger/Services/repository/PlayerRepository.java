package si.deisinger.Services.repository;

import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;
import si.deisinger.Services.model.Player;

public interface PlayerRepository extends DatastoreRepository<Player, Long> {
}
