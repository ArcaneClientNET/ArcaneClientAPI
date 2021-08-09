package net.arcane.api.network.impl.client;

import lombok.AllArgsConstructor;
import net.arcane.api.network.ArcanePacket;
import net.arcane.api.network.PacketContainer;

/**
 * This packet is sent to the client to notify it that voice chat is enabled
 * on this server.
 *
 * @author Braydon
 */
@AllArgsConstructor
public final class ACClientVoiceRulePacket extends ArcanePacket {
	private boolean allowed;
	
	@Override
	public void write(PacketContainer container) {
		container.writeBoolean(allowed);
	}
}