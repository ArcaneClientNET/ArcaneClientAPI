package net.arcane.example.command;

import lombok.AllArgsConstructor;
import net.arcane.api.ArcaneClientAPI;
import net.arcane.api.voice.VoiceChannel;
import net.arcane.example.BukkitExample;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Braydon
 */
@AllArgsConstructor
public class VoiceChatCommand implements CommandExecutor {
	private final ArcaneClientAPI api;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cThe terminal cannot join voice channels.");
			return true;
		}
		VoiceChannel voiceChannel = api.getAdapter().getVoiceChannel(BukkitExample.VOICE_CHANNEL_NAME);
		if (voiceChannel == null) {
			sender.sendMessage("§cwell well well, something broke");
			return true;
		}
		UUID uuid = ((Player) sender).getUniqueId();
		if (voiceChannel.hasUser(uuid)) {
			api.getAdapter().leaveVoiceChannel(uuid);
			sender.sendMessage("§cLeft voice channel §f" + voiceChannel.getName());
		} else {
			try {
				api.getAdapter().joinVoiceChannel(((Player) sender).getUniqueId(), sender.getName(), voiceChannel);
				sender.sendMessage("§aJoined voice channel §f" + voiceChannel.getName());
			} catch (Exception ex) {
				sender.sendMessage("§cFailed to join voice channel: §f" + ex.getLocalizedMessage());
				ex.printStackTrace();
			}
		}
		return true;
	}
}