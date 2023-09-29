package net.kinchanramen.github.itempickupchoice.iroiro;

import fi.dy.masa.malilib.util.restrictions.ItemRestriction;
import net.minecraft.item.ItemStack;

public class canUseItem {
    public static boolean canUseItemWithRestriction(ItemRestriction restriction, ItemStack stack)
    {
        return stack.isEmpty() || restriction.isAllowed(stack.getItem());
    }
}
