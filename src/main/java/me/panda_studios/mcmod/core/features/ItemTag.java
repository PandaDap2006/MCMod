package me.panda_studios.mcmod.core.features;

import me.panda_studios.mcmod.core.utils.Behavior;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemTag extends Behavior implements Cloneable {
    public List<ItemStack> itemList = new ArrayList<>();

    public ItemTag(ItemStack... itemStacks) {
        itemList.addAll(Arrays.asList(itemStacks));
    }

    public boolean equals(ItemTag itemTag) {
        for (ItemStack itemA: itemList) {
            for (ItemStack itemB: itemTag.itemList) {
                if (itemA.isSimilar(itemB)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean contains(ItemStack itemStack) {
        for (ItemStack stack: itemList) {
            return itemStack.isSimilar(stack);
        }
        return false;
    }

    public ItemStack get(ItemStack itemStack) {
        for (ItemStack stack: itemList) {
            if (stack.getType() == itemStack.getType())
                return stack;
        }
        return null;
    }

    @Override
    public String toString() {
        return itemList.toString();
    }

    @Override
    public ItemTag clone() {
        try {
            ItemTag clone = (ItemTag) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
