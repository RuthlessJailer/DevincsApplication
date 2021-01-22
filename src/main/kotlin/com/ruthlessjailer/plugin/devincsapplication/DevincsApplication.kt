package com.ruthlessjailer.plugin.devincsapplication

import com.ruthlessjailer.api.poseidon.PluginBase
import com.ruthlessjailer.api.poseidon.task.manager.TaskManager
import com.ruthlessjailer.api.theseus.task.handler.FutureHandler
import com.ruthlessjailer.plugin.devincsapplication.PlayerCache.Companion.getCache
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.sql.DriverManager

/**
 * @author RuthlessJailer
 */
class DevincsApplication : PluginBase() {

	companion object {

		/**
		 * Gets a block relative to the player's direction.
		 *
		 * @param direction the direction, either left or right
		 * @param distance how far away to get the block
		 *
		 * @return the block object `distance` blocks to the player's `direction`
		 */
		fun Player.getBlockOffset(direction: Direction, distance: Int): Block {
			val facing = location.direction.normalize()

			val offset = when (direction) {
				Direction.LEFT  -> Vector(facing.z, 0.0, -facing.x)
				Direction.RIGHT -> Vector(-facing.z, 0.0, facing.x)
			}.normalize()

			return eyeLocation.add(offset.multiply(distance)).block
		}

	}

	enum class Direction {
		LEFT,
		RIGHT
	}

	/**
	 * Register commands, events, and repeating tasks.
	 */
	override fun onStart() {
		registerCommands(ModeCommand)
		registerEvents(ModeListener)

		TaskManager.async.repeat(//register async task to save player's location to MySQL database every minute
				{
					for (player in Bukkit.getOnlinePlayers()) {
						if (player.getCache().mode == Mode.LOCATION) {//check that player is in location mode
							val location = FutureHandler.sync.supply {
								player.location
							}.get()!!//block until fetched player's location

							val connection = DriverManager.getConnection("jdbc:mysql://localhost:33060/spigot")

							var statement = connection.prepareStatement(
									"create table if not exists player_locations (" +
									"player_uid binary(16) primary key," +
									"location float(53)," +
									"world_uid binary(16)" +
									")")

							statement.execute()//create table

							statement = connection.prepareStatement("replace into player_locations (player_uid, location, world_uid) values (?, ?, ?)")

							statement.setBytes(1, SerializeUtil.uuidToBytes(player.uniqueId))//pack player's UUID as a byte array
							statement.setLong(2, SerializeUtil.locToLong(location))//pack the location's xyz coords to a long for ease of storage
							statement.setBytes(3, SerializeUtil.uuidToBytes(location.world?.uid ?: Bukkit.getWorlds()[0].uid))//pack world UUID as a byte array

							statement.execute()//insert player's location into table
						}
					}


				}, 20 * 60)
	}
}