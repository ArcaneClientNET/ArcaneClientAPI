package net.arcane.api.network;

import net.arcane.api.network.impl.client.ACClientVoiceRulePacket;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Braydon
 */
public final class PacketRegistry {
	public static final Map<Integer, Class<? extends ArcanePacket>> REGISTRY = new HashMap<>();
	
	static {
		// Client
		register(0, ACClientVoiceRulePacket.class);
	}
	
	/**
	 * Register the given packet class with the corresponding packet id.
	 *
	 * @param id the id of the packet to register
	 * @param packetClass the class of the packet to register
	 */
	private static void register(int id, Class<? extends ArcanePacket> packetClass) {
		REGISTRY.put(id, packetClass);
	}
	
	/**
	 * Get the packet id by the packet class.
	 *
	 * @param packetClass the packet class to get the id for
	 * @return the id of the given packet class, -1 if there is a problem
	 */
	public static int lookupId(Class<? extends ArcanePacket> packetClass) {
		for (Map.Entry<Integer, Class<? extends ArcanePacket>> entry : REGISTRY.entrySet()) {
			if (entry.getValue().equals(packetClass)) {
				return entry.getKey();
			}
		}
		return -1;
	}
}