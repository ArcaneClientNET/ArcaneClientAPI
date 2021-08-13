package net.arcane.example;

import net.arcane.api.ArcaneClientAPI;
import net.arcane.api.adapter.impl.BukkitAdapter;
import net.arcane.api.version.ClientVersion;
import net.arcane.example.command.VoiceChatCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Braydon
 */
public final class BukkitExample extends JavaPlugin implements Listener {
	public static final String VOICE_CHANNEL_NAME = "Test";
	
	private ArcaneClientAPI api;
	
	@Override
	public void onEnable() {
		api = ArcaneClientAPI.builder()
				.withAllowedVersions(ClientVersion.values()) // An array of client versions that are allowed to join
				.withProperties(new ArcaneClientAPI.ClientProperty[] {
						ArcaneClientAPI.ClientProperty.VOICE_CHAT // Tells the client that voice chat is supported on this server
				}).build();
		api.withAdapter(new BukkitAdapter(api, this)); // Set the adapter for the api to use
		api.getAdapter().createVoiceChannel(VOICE_CHANNEL_NAME);
		Bukkit.getPluginManager().registerEvents(this, this);
		getCommand("voicechat").setExecutor(new VoiceChatCommand(api));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
			// Since we delay the task 15 ticks, it's possible for the user to go offline in
			// that time, so we need to check for that.
			if (!player.isOnline()) {
				return;
			}
			boolean onArcane = api.getAdapter().isOnArcane(player.getUniqueId());
			if (!onArcane) {
				player.sendMessage("§cYou are not on Arcane Client");
			} else {
				player.sendMessage("§aYou have connected with Arcane Client");
			}
		}, 15L); // We have to delay this to allow the client some time to send the registration messages over
				   // the messaging channel. We could theoretically make this 10 ticks instead of 15 but doing so
				   // will result in the player not seeing the message on the kick screen, therefore we have to delay
				   // it another 5 ticks.
	}
}