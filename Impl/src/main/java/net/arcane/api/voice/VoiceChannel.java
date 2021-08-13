package net.arcane.api.voice;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Braydon
 */
@AllArgsConstructor @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class VoiceChannel {
	@EqualsAndHashCode.Include @NonNull private final String name;
	private final List<VoiceUser> users = new ArrayList<>();
	
	/**
	 * Add the given user to the voice channel.
	 *
	 * @param user the user to add
	 * @see VoiceUser for user
	 * @throws IllegalStateException if the user is already in the voice channel
	 */
	public void addUser(@NonNull VoiceUser user) {
		if (hasUser(user.getUuid())) {
			throw new IllegalStateException("User is already in the voice channel");
		}
		users.add(user);
	}
	
	/**
	 * Checks if there is a user in the voice channel with the given uuid.
	 *
	 * @param uuid the uuid to check
	 * @return if the user is in the voice channel
	 */
	public boolean hasUser(@NonNull UUID uuid) {
		return getUser(uuid) != null;
	}
	
	/**
	 * Remove the user with the given uuid from the voice channel.
	 *
	 * @param uuid the uuid of the user to remove
	 * @see VoiceUser for user
	 * @throws IllegalStateException if the user is not in the voice channel
	 */
	public void removeUser(@NonNull UUID uuid) {
		VoiceUser user = getUser(uuid);
		if (user == null) {
			throw new NullPointerException();
		}
		removeUser(user);
	}
	
	/**
	 * Remove the given user from the voice channel.
	 *
	 * @param user the user to remove
	 * @see VoiceUser for user
	 * @throws IllegalStateException if the user is not in the voice channel
	 */
	public void removeUser(@NonNull VoiceUser user) {
		if (!hasUser(user.getUuid())) {
			throw new IllegalStateException("User is not in the voice channel");
		}
		users.remove(user);
	}
	
	/**
	 * Get the voice user by the given uuid.
	 *
	 * @param uuid the uuid of the user to get
	 * @return the found user, null if none
	 */
	public VoiceUser getUser(@NonNull UUID uuid) {
		for (VoiceUser user : users) {
			if (user.getUuid().equals(uuid)) {
				return user;
			}
		}
		return null;
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