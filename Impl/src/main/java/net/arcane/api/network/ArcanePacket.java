package net.arcane.api.network;

import lombok.AllArgsConstructor;

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
	public void write(PacketContainer container) {}
	
	/**
	 * Read from the given packet container.
	 *
	 * @param container the packet container to read from
	 */
	public void read(PacketContainer container) {}
}