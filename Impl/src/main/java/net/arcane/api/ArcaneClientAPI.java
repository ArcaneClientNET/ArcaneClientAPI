package net.arcane.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.arcane.api.adapter.ArcaneAdapter;
import net.arcane.api.version.ClientVersion;

/**
 * @author Braydon
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE) @Getter
public final class ArcaneClientAPI {
	private ArcaneAdapter adapter;
	private final ClientVersion[] allowedVersions;
	private final ClientProperty[] properties;
	
	/**
	 * Set the adapter of the api.
	 *
	 * @param adapter the adapter
	 */
	public void withAdapter(ArcaneAdapter adapter) {
		if (this.adapter != null)
			throw new IllegalStateException("The adapter has already been set");
		this.adapter = adapter;
	}
	
	public boolean hasProperty(ClientProperty property) {
		for (ClientProperty clientProperty : properties) {
			if (clientProperty == property) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Create a new API builder.
	 *
	 * @param adapter the adapter to use for the api
	 * @return the builder
	 */
	public static APIBuilder builder() {
		return new APIBuilder();
	}
	
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class APIBuilder {
		private ClientVersion[] allowedVersions = ClientVersion.values();
		private ClientProperty[] properties;
		
		/**
		 * Only allow the given client versions to connect to the
		 * server.
		 *
		 * @param allowedVersions the array of versions that are allowed to join
		 */
		public APIBuilder withAllowedVersions(ClientVersion[] allowedVersions) {
			this.allowedVersions = allowedVersions;
			return this;
		}
		
		/**
		 * Set the given properties for the API.
		 *
		 * @param properties the properties to set
		 */
		public APIBuilder withProperties(ClientProperty[] properties) {
			this.properties = properties;
			return this;
		}
		
		/**
		 * Build the API.
		 *
		 * @return the built api
		 */
		public ArcaneClientAPI build() {
			return new ArcaneClientAPI(allowedVersions, properties);
		}
	}
	
	public enum ClientProperty {
		ARCANE_ONLY, VOICE_CHAT
	}
}