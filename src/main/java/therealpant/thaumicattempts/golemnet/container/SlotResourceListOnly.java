package therealpant.thaumicattempts.golemnet.container;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import therealpant.thaumicattempts.golemcraft.item.ItemResourceList;

/**
 * Slot that accepts only ItemResourceList stacks for requester patterns.
 */
public class SlotResourceListOnly extends SlotItemHandler {

    public SlotResourceListOnly(IItemHandler handler, int index, int xPosition, int yPosition) {
        super(handler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemResourceList;
    }
}