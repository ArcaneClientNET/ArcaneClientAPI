package net.arcane.api.network;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author Braydon
 */
@Getter
public final class PacketContainer {
	private final ByteBuf byteBuf;
	
	public PacketContainer(@NonNull ArcanePacket packet) {
		byteBuf = Unpooled.buffer();
		writeInt(PacketRegistry.lookupId(packet.getClass()));
		packet.write(this);
	}
	
	/**
	 * Write a String to the container.
	 *
	 * @param string the string
	 */
	public void writeString(@NonNull String string) {
		byte[] bytes = string.getBytes(Charsets.UTF_8);
		writeInt(bytes.length);
		byteBuf.writeBytes(bytes);
	}
	
	/**
	 * Read a string from the container.
	 *
	 * @return the read string
	 */
	public @NonNull String readString() {
		byte[] buffer = new byte[readInt()];
		byteBuf.readBytes(buffer);
		return new String(buffer, Charsets.UTF_8);
	}
	
	/**
	 * Write a boolean to the container.
	 *
	 * @param value the boolean
	 */
	public void writeBoolean(boolean value) {
		writeInt(value ? 1 : 0);
	}
	
	/**
	 * Read a boolean from the container.
	 *
	 * @return the read boolean
	 */
	public boolean readBoolean() {
		return readInt() == 1;
	}
	
	/**
	 * Write an int to the container.
	 *
	 * @param value the int
	 */
	public void writeInt(int value) {
		while ((value & 0xFFFFFF80) != 0x0) {
			byteBuf.writeByte((value & 0x7F) | 0x80);
			value >>>= 7;
		}
		byteBuf.writeByte(value);
	}
	
	/**
	 * Read an int from the container.
	 *
	 * @return the read int
	 */
	public int readInt() {
		byte valueBytes;
		int value = 0;
		int chunk = 0;
		do {
			valueBytes = byteBuf.readByte();
			value |= (valueBytes & 0x7F) << chunk++ * 7;
			if (chunk > 5) {
				throw new RuntimeException("Too big");
			}
		} while ((valueBytes & 0x80) == 0x80);
		return value;
	}
}