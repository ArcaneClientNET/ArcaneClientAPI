package net.arcane.api.network.impl.client;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.arcane.api.network.ArcanePacket;
import net.arcane.api.network.PacketContainer;
import net.arcane.api.voice.VoiceUser;

/**
 * This packet is sent to the client when a user leaves a voice channel.
 * <p>
 * This is only sent if voice chat is enabled.
 *
 * @author Braydon
 */
@AllArgsConstructor
public final class ACClientVoiceChannelLeavePacket extends ArcanePacket {
	@NonNull private VoiceUser user;
	
	@Override
	public void write(@NonNull PacketContainer container) {
		container.writeUUID(user.getUuid());
		container.writeString(user.getName());
		container.writeBoolean(user.isMuted());
		container.writeBoolean(user.isDeafened());
	}
}