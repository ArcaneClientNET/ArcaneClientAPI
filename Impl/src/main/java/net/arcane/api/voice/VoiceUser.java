package net.arcane.api.voice;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

/**
 * @author Braydon
 */
@AllArgsConstructor @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class VoiceUser {
	@EqualsAndHashCode.Include @NonNull private final UUID uuid;
	@NonNull private final String name;
	private final boolean muted, deafened;
}