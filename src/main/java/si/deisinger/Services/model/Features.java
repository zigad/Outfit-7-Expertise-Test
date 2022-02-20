package si.deisinger.Services.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Features {

	private String multiplayer;
	private String customer_support;
	private String ads;

	public Features(String multiplayer, String customer_support, String ads) {
		this.multiplayer = multiplayer;
		this.customer_support = customer_support;
		this.ads = ads;
	}

}
