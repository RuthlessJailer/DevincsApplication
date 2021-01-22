package com.ruthlessjailer.plugin.devincsapplication

import com.ruthlessjailer.api.poseidon.command.CommandBase
import com.ruthlessjailer.api.poseidon.command.SubCommand
import com.ruthlessjailer.api.poseidon.command.SuperiorCommand
import com.ruthlessjailer.plugin.devincsapplication.PlayerCache.Companion.getCache
import org.bukkit.command.CommandSender

/**
 * @author RuthlessJailer
 */
object ModeCommand : CommandBase("mode"), SuperiorCommand {

	/**
	 * Send the player a list of modes.
	 */
	@SubCommand("help")
	fun help(sender: CommandSender, args: Array<String>) {
		send(sender, "&6Available modes are &b${Mode.values().joinToString(", ")}&6.")
	}

	/**
	 * Tell the player to run the help command.
	 */
	override fun onExecute(sender: CommandSender, args: Array<out String>, commandLabel: String) {
		if (args.isNotEmpty()) return
		send(sender, "&4Run &b/mode help &4for a list of modes!")
	}

	/**
	 * Update the player's mode.
	 */
	@SubCommand("%enum")
	fun mode(sender: CommandSender, args: Array<String>, mode: Mode) {
		val player = getPlayer(sender)

		player.getCache().mode = mode

		send(sender, "&2Updated your mode to &b$mode&2.")
	}


}