package net.arcane.api.adapter.impl;

import net.arcane.api.ArcaneClientAPI;
import net.arcane.api.adapter.ArcaneAdapter;
import net.arcane.api.network.ArcanePacket;
import net.arcane.api.network.PacketContainer;
import net.arcane.api.network.PacketResult;
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

import java.util.UUID;

/**
 * @author Braydon
 */
public final class BukkitAdapter extends ArcaneAdapter implements Listener {
	private final Plugin plugin;
	
	public BukkitAdapter(ArcaneClientAPI api, Plugin plugin) {
		super(api);
		this.plugin = plugin;
		Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, Properties.CHANNEL);
		Bukkit.getMessenger().registerIncomingPluginChannel(plugin, Properties.CHANNEL, (channel, player, bytes) -> {
			Bukkit.broadcastMessage("client packet from " + player.getName());
		});
		Bukkit.getPluginManager().registerEvents(this, plugin);
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
			handleClientJoin(event.getPlayer().getUniqueId());
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
}