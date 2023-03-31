package me.panda_studios.mcmod.core.item;

import me.panda_studios.mcmod.core.block.IBlock;
import me.panda_studios.mcmod.core.block.WorldBlock;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.inventory.ItemStack;

public class ItemBlock extends IItem {
    private final IBlock iBlock;

    public ItemBlock(IBlock iBlock, Properties properties) {
        super(properties);
        this.iBlock = iBlock;
    }

    @Override
    public void useOn(Player player, ItemStack itemStack, Block block, BlockFace blockFace) {
        WorldBlock.PlaceBlock(iBlock, block.getLocation().add(blockFace.getDirection()), false);
    }
}
