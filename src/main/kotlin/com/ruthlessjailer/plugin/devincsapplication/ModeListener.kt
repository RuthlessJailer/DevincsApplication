package com.ruthlessjailer.plugin.devincsapplication

import com.ruthlessjailer.api.poseidon.task.manager.TaskManager
import com.ruthlessjailer.plugin.devincsapplication.DevincsApplication.Companion.getBlockOffset
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent


object ModeListener : Listener {

	val old = mutableMapOf<Location, Material>()

	@EventHandler
	fun onBreak(event: BlockBreakEvent) {
		if (event.player.inventory.itemInMainHand.type != Material.WOODEN_PICKAXE) return//check that the player is holding a pick

		val rel = event.player.getBlockOffset(DevincsApplication.Direction.LEFT, 5)//get the block 5 to the left of the player
		old[rel.location] = rel.type//store the old type so that it can be reverted

		rel.type = Material.GLASS

		TaskManager.sync.delay({
								   rel.type = old[rel.location] ?: Material.AIR
								   old.remove(rel.location)
							   }, 20 * 10)//revert the block 10 seconds later
	}

}