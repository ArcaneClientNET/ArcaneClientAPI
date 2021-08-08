package net.arcane.api.adapter.impl;

import net.arcane.api.ArcaneClientAPI;
import net.arcane.api.adapter.ArcaneAdapter;
import net.arcane.api.network.ArcanePacket;
import net.arcane.api.network.PacketContainer;
import net.arcane.api.network.PacketResult;
import net.arcane.api.network.impl.ACClientVoiceRulePacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Braydon
 */
public final class BukkitAdapter implements ArcaneAdapter, Listener {
	private final ArcaneClientAPI api;
	private final Plugin plugin;
	
	/**
	 * The array of player UUIDs that are currently being verified.
	 */
	private final List<UUID> usersVerifying = new ArrayList<>();
	
	/**
	 * The array of player UUIDs that are on Arcane Client.
	 */
	private final List<UUID> clientUsers = new ArrayList<>();
	
	public BukkitAdapter(ArcaneClientAPI api, Plugin plugin) {
		this.api = api;
		this.plugin = plugin;
		Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, Properties.CHANNEL);
		Bukkit.getMessenger().registerIncomingPluginChannel(plugin, Properties.CHANNEL, (channel, player, bytes) -> {
			Bukkit.broadcastMessage("client packet from " + player.getName());
		});
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	/**
	 * Check if the player with the given {@link UUID} is being verified.
	 * <p>
	 * This returns whether the status of the player with the given uuid is
	 * known to the server.
	 *
	 * @param uuid the uuid of the player to check
	 * @return the verifying status
	 */
	@Override
	public boolean isVerifying(UUID uuid) {
		return usersVerifying.contains(uuid);
	}
	
	/**
	 * Check if the player with the given {@link UUID} is currently on Arcane Client.
	 *
	 * @param uuid the uuid of the player to check
	 * @return the client status
	 */
	@Override
	public boolean isOnArcane(UUID uuid) {
		return clientUsers.contains(uuid);
	}
	
	/**
	 * Send the given packet to the player with the given uuid.
	 *
	 * @param uuid   the uuid of the player to send the packet to
	 * @param packet the packet to send
	 * @return the result of the packet
	 */
	@Override
	public PacketResult sendPacket(UUID uuid, ArcanePacket packet) {
		Player player = Bukkit.getPlayer(uuid);
		if (player != null && isOnArcane(uuid)) {
			player.sendPluginMessage(plugin, Properties.CHANNEL, new PacketContainer(packet).getByteBuf().array());
			return PacketResult.SUCCESS;
		}
		return PacketResult.FAILED;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onJoin(PlayerJoinEvent event) {
		usersVerifying.add(event.getPlayer().getUniqueId());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onChannelRegister(PlayerRegisterChannelEvent event) {
		if (event.getChannel().equals(Properties.CHANNEL)) {
			UUID uuid = event.getPlayer().getUniqueId();
			usersVerifying.remove(uuid);
			clientUsers.add(uuid);
			
			// If voice chat is enabled on this server, notify the client after they are verified.
			if (api.hasProperty(ArcaneClientAPI.ClientProperty.VOICE_CHAT)) {
				sendPacket(uuid, new ACClientVoiceRulePacket(true));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onChannelUnregister(PlayerUnregisterChannelEvent event) {
		if (event.getChannel().equals(Properties.CHANNEL)) {
			cleanup(event.getPlayer());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onQuit(PlayerQuitEvent event) {
		cleanup(event.getPlayer());
	}
	
	private void cleanup(Player player) {
		usersVerifying.remove(player.getUniqueId());
		clientUsers.remove(player.getUniqueId());
	}
}