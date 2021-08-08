package net.arcane.api.adapter;

import net.arcane.api.network.ArcanePacket;
import net.arcane.api.network.PacketResult;

import java.util.UUID;

/**
 * @author Braydon
 */
public interface ArcaneAdapter {
	/**
	 * Check if the player with the given {@link UUID} is being verified.
	 * <p>
	 * This returns whether the status of the player with the given uuid is
	 * known to the server.
	 *
	 * @param uuid the uuid of the player to check
	 * @return the verifying status
	 */
	boolean isVerifying(UUID uuid);
	
	/**
	 * Check if the player with the given {@link UUID} is currently on Arcane Client.
	 *
	 * @param uuid the uuid of the player to check
	 * @return the client status
	 */
	boolean isOnArcane(UUID uuid);
	
	/**
	 * Send the given packet to the player with the given uuid.
	 *
	 * @param uuid the uuid of the player to send the packet to
	 * @param packet the packet to send
	 * @return the result of the packet
	 */
	PacketResult sendPacket(UUID uuid, ArcanePacket packet);
	
	class Properties {
		/**
		 * The messaging channel to use when in communication with the client.
		 */
		public static final String CHANNEL = "AC-Client";
	}
}