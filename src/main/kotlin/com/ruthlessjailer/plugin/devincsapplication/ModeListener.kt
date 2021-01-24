package com.ruthlessjailer.plugin.devincsapplication

import com.ruthlessjailer.api.poseidon.Chat
import com.ruthlessjailer.api.poseidon.task.manager.TaskManager
import com.ruthlessjailer.plugin.devincsapplication.DevincsApplication.Companion.getBlockOffset
import com.ruthlessjailer.plugin.devincsapplication.PlayerCache.Companion.getCache
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.math.roundToInt


object ModeListener : Listener {

	val old = mutableMapOf<Location, Material>()

	@EventHandler
	fun onBreak(event: BlockBreakEvent) {
		//check that the player is in block mode
		if (event.player.getCache().mode != Mode.BLOCK) return

		//check that the player is holding a pick
		if (event.player.inventory.itemInMainHand.type != Material.WOODEN_PICKAXE) return

		val rel = event.player.getBlockOffset(DevincsApplication.Direction.LEFT, 5)//get the block 5 to the left of the player
		old[rel.location] = rel.type//store the old type so that it can be reverted

		rel.type = Material.GLASS

		TaskManager.sync.delay({
								   rel.type = old[rel.location] ?: Material.AIR
								   old.remove(rel.location)
							   }, 20 * 10)//revert the block 10 seconds later
	}

	@EventHandler
	fun onClick(event: PlayerInteractEvent) {
		val block = event.clickedBlock ?: return

		//check that the player is in door mode
		if (event.player.getCache().mode != Mode.DOOR) return

		//check that they are right clicking
		if (event.action != Action.RIGHT_CLICK_BLOCK) return

		//check that the material is a door
		if (!block.type.name.endsWith("_DOOR")) return

		//check that the door is a wooden door
		if (block.type.name.startsWith("IRON")) return

		//give player random amount of money from $0 to $1000
		val response = DevincsApplication.economy.depositPlayer(event.player, (Math.random() * 1000).roundToInt().toDouble())
		Chat.send(event.player, "&1Granted you &6$${response.amount} &1!")
	}

}