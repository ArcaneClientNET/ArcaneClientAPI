package net.arcane.api.voice;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Braydon
 */
@AllArgsConstructor @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class VoiceChannel {
	@EqualsAndHashCode.Include private final String name;
	private final List<VoiceUser> users = new ArrayList<>();
	
	/**
	 * Add the given user to the voice channel.
	 *
	 * @param user the user to add
	 * @see VoiceUser for user
	 * @throws IllegalStateException if the user is already in the voice channel
	 */
	public void addUser(VoiceUser user) {
		if (hasUser(user.getUuid())) {
			throw new IllegalStateException("User is already in voice channel");
		}
		users.add(user);
	}
	
	/**
	 * Checks if there is a user in the voice channel with the given uuid.
	 *
	 * @param uuid the uuid to check
	 * @return if the user is in the voice channel
	 */
	public boolean hasUser(UUID uuid) {
		for (VoiceUser user : users) {
			if (user.getUuid().equals(uuid)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get the users in the voice channel.
	 *
	 * @return the users
	 */
	public List<VoiceUser> getUsers() {
		return Collections.unmodifiableList(users);
	}
}