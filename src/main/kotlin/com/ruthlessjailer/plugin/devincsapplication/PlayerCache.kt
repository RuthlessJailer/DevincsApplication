package com.ruthlessjailer.plugin.devincsapplication

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.util.*

/**
 * Object to store values per-player.
 *
 * @author RuthlessJailer
 */
class PlayerCache(private val player: UUID) {

	companion object {
		private val CACHE_MAP = mutableMapOf<UUID, PlayerCache>()
		private val MODIFIED = mutableSetOf<UUID>()

		fun getCache(player: UUID): PlayerCache = CACHE_MAP.getOrPut(player) { PlayerCache(player) }

		fun OfflinePlayer.getCache(): PlayerCache = getCache(uniqueId)

		fun save() {
//			val con = DatabaseConnection()
//
//			for (player in MODIFIED) {
//				con.saveSettings(player)
//			}
//
//			MODIFIED.clear()
//
//			con.connection.close()
		}

		fun load() {
//			val con = DatabaseConnection()
//
//			CACHE_MAP.clear()
//			MODIFIED.clear()
//
//			con.loadAllSettings()
//
//			con.connection.close()
		}
	}

	fun getOwner(): OfflinePlayer = Bukkit.getOfflinePlayer(player)

	var mode: Mode = Mode.NONE

}