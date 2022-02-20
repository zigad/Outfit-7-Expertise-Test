package si.deisinger.Services.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Entity(name = "Player")
public class Player {
	@Id
	Long id;
	int usage;

	public Player(Long id, int usage) {
		this.id = id;
		this.usage = usage;
	}

	public void incrementUsage() {
		this.usage++;
	}

	@Override
	public String toString() {
		return "{" +
				"\"id\": " + "\"" + this.id + "\"" +
				", \"usage\": " + "\"" + this.usage + "\"" +
				"}";
	}
}