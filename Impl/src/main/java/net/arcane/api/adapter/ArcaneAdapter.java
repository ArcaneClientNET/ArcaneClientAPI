package net.arcane.api.adapter;

import lombok.RequiredArgsConstructor;
import net.arcane.api.ArcaneClientAPI;
import net.arcane.api.network.ArcanePacket;
import net.arcane.api.network.PacketResult;
import net.arcane.api.network.impl.client.ACClientVoiceChannelJoinPacket;
import net.arcane.api.network.impl.client.ACClientVoiceRulePacket;
import net.arcane.api.voice.VoiceChannel;
import net.arcane.api.voice.VoiceUser;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Braydon
 */
@RequiredArgsConstructor
public abstract class ArcaneAdapter {
	protected final ArcaneClientAPI api;
	
	/**
	 * The list of player UUIDs that are currently being verified.
	 */
	protected final List<UUID> usersVerifying = new ArrayList<>();
	
	/**
	 * The list of player UUIDs that are on Arcane Client.
	 */
	protected final List<UUID> clientUsers = new ArrayList<>();
	
	/**
	 * The list of voice channels.
	 */
	private final List<VoiceChannel> voiceChannels = new ArrayList<>();
	
	/**
	 * Check if the player with the given {@link UUID} is being verified.
	 * <p>
	 * This returns whether the status of the player with the given uuid is
	 * known to the server.
	 *
	 * @param uuid the uuid of the player to check
	 * @return the verifying status
	 */
	public final boolean isVerifying(UUID uuid) {
		return usersVerifying.contains(uuid);
	}
	
	/**
	 * Check if the player with the given {@link UUID} is currently on Arcane Client.
	 *
	 * @param uuid the uuid of the player to check
	 * @return the client status
	 */
	public final boolean isOnArcane(UUID uuid) {
		return clientUsers.contains(uuid);
	}
	
	/**
	 * Create a voice channel with the given name.
	 *
	 * @param name the name of the channel to create
	 * @see VoiceChannel for voice channel
	 * @throws IllegalStateException if voice chat isn't enabled or if there is already a channel with the given name
	 */
	public final void createVoiceChannel(String name) {
		if (!api.hasProperty(ArcaneClientAPI.ClientProperty.VOICE_CHAT)) {
			throw new IllegalStateException("Voice chat is not enabled");
		}
		if (getVoiceChannel(name) != null) {
			throw new IllegalStateException("Voice channel already exists");
		}
		voiceChannels.add(new VoiceChannel(name));
	}
	
	/**
	 * Set a player's voice channel.
	 *
	 * @param uuid the uuid of the player
	 * @param name the name of the player
	 * @param voiceChannel the voice channel
	 * @see VoiceChannel for voice channel
	 * @throws IllegalStateException if voice chat isn't enabled or if the player is already in a voice channel
	 */
	public final void setVoiceChannel(UUID uuid, String name, VoiceChannel voiceChannel) {
		if (!api.hasProperty(ArcaneClientAPI.ClientProperty.VOICE_CHAT)) {
			throw new IllegalStateException("Voice chat is not enabled");
		}
		for (VoiceChannel otherVoiceChannel : voiceChannels) {
			if (otherVoiceChannel.hasUser(uuid)) {
				throw new IllegalStateException("Player is already in a voice channel");
			}
		}
		VoiceUser user = new VoiceUser(uuid, name, false, false);
		voiceChannel.addUser(user); // Add the user to the voice channel
		// Send a channel join packet to all users in the channel to notify them that the above user has joined
		for (VoiceUser channelUser : voiceChannel.getUsers()) {
			sendPacket(channelUser.getUuid(), new ACClientVoiceChannelJoinPacket(user));
		}
	}
	
	/**
	 * Get the list of voice channels.
	 *
	 * @return the list of voice channels
	 * @see VoiceChannel for voice channel
	 * @throws IllegalStateException if voice chat isn't enabled
	 */
	public final List<VoiceChannel> getVoiceChannels() {
		if (!api.hasProperty(ArcaneClientAPI.ClientProperty.VOICE_CHAT)) {
			throw new IllegalStateException("Voice chat is not enabled");
		}
		return Collections.unmodifiableList(voiceChannels);
	}
	
	/**
	 * Get the voice channel by the given name.
	 *
	 * @param name the name of the voice channel to get
	 * @return the voice channel found, otherwise null
	 * @see VoiceChannel for voice channel
	 * @throws IllegalStateException if voice chat isn't enabled
	 */
	public final VoiceChannel getVoiceChannel(String name) {
		if (!api.hasProperty(ArcaneClientAPI.ClientProperty.VOICE_CHAT)) {
			throw new IllegalStateException("Voice chat is not enabled");
		}
		for (VoiceChannel voiceChannel : voiceChannels) {
			if (voiceChannel.getName().equalsIgnoreCase(name)) {
				return voiceChannel;
			}
		}
		return null;
	}
	
	/**
	 * Send the given packet to the player with the given uuid.
	 *
	 * @param uuid the uuid of the player to send the packet to
	 * @param packet the packet to send
	 * @return the result of the packet
	 * @see ArcanePacket for the packet
	 */
	public abstract PacketResult sendPacket(UUID uuid, ArcanePacket packet);
	
	/**
	 * Handle a player joining with the client.
	 * <p>
	 * This method is called in the respective adapter when the player has been
	 * verified and is on the client.
	 *
	 * @param uuid the uuid of the player that joined on Arcane Client
	 */
	protected final void handleClientJoin(UUID uuid) {
		usersVerifying.remove(uuid);
		clientUsers.add(uuid);
		
		// If voice chat is enabled on this server, notify the client after they are verified.
		if (api.hasProperty(ArcaneClientAPI.ClientProperty.VOICE_CHAT)) {
			sendPacket(uuid, new ACClientVoiceRulePacket(true));
		}
	}
	
	/**
	 * Clean up the given player.
	 *
	 * @param player the player to clean up
	 */
	public final void cleanup(Player player) {
		usersVerifying.remove(player.getUniqueId());
		clientUsers.remove(player.getUniqueId());
	}
	
	public static class Properties {
		/**
		 * The messaging channel to use when in communication with the client.
		 */
		public static final String CHANNEL = "AC-Client";
	}
}