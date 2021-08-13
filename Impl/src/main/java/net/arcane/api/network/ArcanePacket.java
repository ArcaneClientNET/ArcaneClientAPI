package net.arcane.api.network;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * @author Braydon
 */
@AllArgsConstructor
public class ArcanePacket {
	/**
	 * Write to the given packet container.
	 *
	 * @param container the packet container to write to
	 */
	public void write(@NonNull PacketContainer container) {}
	
	/**
	 * Read from the given packet container.
	 *
	 * @param container the packet container to read from
	 */
	public void read(@NonNull PacketContainer container) {}
}