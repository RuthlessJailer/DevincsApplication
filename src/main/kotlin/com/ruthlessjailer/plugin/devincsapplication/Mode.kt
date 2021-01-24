package com.ruthlessjailer.plugin.devincsapplication

/**
 * @author RuthlessJailer
 */
enum class Mode {

	/**
	 * Changes the block 5 to the left of where the player is facing for 10 seconds.
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