package com.ruthlessjailer.plugin.devincsapplication

import net.minecraft.server.v1_16_R3.BlockPosition
import org.bukkit.Location
import org.bukkit.World
import java.nio.ByteBuffer
import java.util.*
import javax.xml.bind.DatatypeConverter

/**
 * @author RuthlessJailer
 */
object SerializeUtil {

	/**
	 * Pack a location's coordinates into a [Long].
	 * This does not pack [Location.pitch], [Location.yaw], or [World].
	 *
	 * @param location the [Location]
	 *
	 * @return a packed [Long]
	 */
	fun locToLong(location: Location): Long = locToLong(location.blockX, location.blockY, location.blockZ)

	/**
	 * Pack `x`, `y`, and `z` coordinates into a [Long].
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 *
	 * @return a packed [Long]
	 */
	fun locToLong(x: Int, y: Int, z: Int): Long = BlockPosition(x, y, z).asLong()

	/**
	 * Unpack a [Long] into a [Location].
	 *
	 * @param long the [Long] to unpack
	 * @param world the [World] of the [Location], nullable
	 *
	 * @return an unpacked [Location] object
	 */
	fun longToLoc(long: Long, world: World? = null): Location {
		val bp = BlockPosition.fromLong(long)
		return Location(world, bp.x.toDouble(), bp.y.toDouble(), bp.z.toDouble())
	}

	/**
	 * Convert a [UUID] into a [ByteArray].
	 *
	 * @param uuid the [UUID] to convert
	 *
	 * @return a [ByteArray] representation of the [UUID]
	 */
	fun uuidToBytes(uuid: UUID): ByteArray = DatatypeConverter.parseHexBinary(uuid.toString().replace("-", ""))

	/**
	 * Convert a [ByteArray] into a [UUID].
	 *
	 * @param bytes the [ByteArray] to convert
	 *
	 * @return a [UUID] representation of the [ByteArray]
	 *
	 * @throws IllegalStateException if the [ByteArray] is shorter than 2
	 */
	fun bytesToUuid(bytes: ByteArray): UUID {
		check(bytes.size >= 2) { "Too small of an array." }
		val bb = ByteBuffer.wrap(bytes)
		return UUID(bb.long, bb.long)
	}

}