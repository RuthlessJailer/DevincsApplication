package com.ruthlessjailer.plugin.devincsapplication

/**
 * @author RuthlessJailer
 */
enum class Mode {

	/**
	 *
	 */
	BLOCK,

	/**
	 * Saves the player's location to a MySQL database every minute.
	 */
	LOCATION,

	/**
	 * Grants a random amount of money when the player opens a door.
	 */
	DOOR,

	/**
	 * No mode.
	 */
	NONE
}