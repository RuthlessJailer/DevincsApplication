package com.ruthlessjailer.plugin.devincsapplication

import com.ruthlessjailer.api.poseidon.Chat
import com.ruthlessjailer.api.poseidon.PluginBase
import com.ruthlessjailer.api.poseidon.task.manager.TaskManager
import com.ruthlessjailer.plugin.devincsapplication.PlayerCache.Companion.getCache
import net.milkbowl.vault.economy.Economy
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

		lateinit var economy: Economy

		/**
		 * Gets a block relative to the player's direction.
		 *
		 * @param direction the direction, either left, right, forwards, or backwards
		 * @param distance how far away to get the block
		 *
		 * @return the block object `distance` blocks to the player's `direction`
		 */
		fun Player.getBlockOffset(direction: Direction, distance: Int): Block {
			val facing = location.direction.normalize()

			val offset = when (direction) {
				Direction.LEFT     -> Vector(facing.z, 0.0, -facing.x)
				Direction.RIGHT    -> Vector(-facing.z, 0.0, facing.x)
				Direction.FORWARD  -> facing
				Direction.BACKWARD -> facing.multiply(-1)
			}.normalize()

			return eyeLocation.add(offset.multiply(distance)).block
		}

		/**
		 * Save player locations if they are in location mode.
		 */
		fun savePlayerLocations() {
			val connection = DriverManager.getConnection("jdbc:mysql://localhost/spigot?autoReconnect=true&useSSL=false", Config.username, Config.password)

			var statement = connection.prepareStatement(
					"create table if not exists player_locations (" +
					"player_uid binary(16) primary key," +
					"location float(53)," +
					"world_uid binary(16)" +
					")")

			//create table
			statement.execute()

			for (player in Bukkit.getOnlinePlayers()) {
				//check that player is in location mode
				if (player.getCache().mode != Mode.LOCATION) continue

				val location = player.location

				statement = connection.prepareStatement("replace into player_locations (player_uid, location, world_uid) values (?, ?, ?)")

				//pack player's UUID as a byte array
				statement.setBytes(1, SerializeUtil.uuidToBytes(player.uniqueId))

				//pack the location's xyz coords to a long for ease of storage
				statement.setLong(2, SerializeUtil.locToLong(location))

				//pack world UUID as a byte array
				statement.setBytes(3, SerializeUtil.uuidToBytes(location.world?.uid ?: Bukkit.getWorlds()[0].uid))

				//insert player's location into table
				statement.execute()
			}

			connection.close()
		}

	}

	enum class Direction {
		LEFT,
		RIGHT,
		FORWARD,
		BACKWARD
	}

	/**
	 * Register commands, events, and repeating tasks.
	 */
	override fun onStart() {
		registerCommands(ModeCommand)
		registerEvents(ModeListener)

		//save economy variable or ease of access
		economy = Bukkit.getServicesManager().getRegistration(Economy::class.java)?.provider ?: run {
			Chat.severe("Vault not found.")
			throw RuntimeException("Lacking dependency Vault.")
		}

		TaskManager.async.repeat({//register async task to save player's location to MySQL database every minute
			savePlayerLocations()
								 }, 20 * 60)
	}
}
