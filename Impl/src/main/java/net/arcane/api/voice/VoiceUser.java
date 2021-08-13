package net.arcane.api.voice;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

/**
 * @author Braydon
 */
@AllArgsConstructor @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class VoiceUser {
	@EqualsAndHashCode.Include private final UUID uuid;
	private final String name;
	private final boolean muted, deafened;
}