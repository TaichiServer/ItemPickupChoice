package net.kinchanramen.github.itempickupchoice.mixin;

import fi.dy.masa.malilib.util.restrictions.ItemRestriction;
import net.kinchanramen.github.itempickupchoice.config.Configs;
import net.kinchanramen.github.itempickupchoice.config.FeatureToggle;
import net.kinchanramen.github.itempickupchoice.tweaks.PlacementTweaks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

import static net.kinchanramen.github.itempickupchoice.iroiro.canUseItem.canUseItemWithRestriction;

@Mixin(ItemEntity.class)
public abstract class MixinItemPickupChoice {
    @Shadow public abstract ItemStack getStack();
    @Shadow
    private int pickupDelay;
    @Shadow
    private UUID owner;

    @Shadow private int itemAge;

    @Redirect(method = "Lnet/minecraft/entity/ItemEntity;onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z", ordinal = 0))
    private boolean itemPickupEvent(PlayerInventory instance, ItemStack stack,PlayerEntity player) {
        ItemEntity itemEntity = (ItemEntity)(Object) this;
        ItemStack itemStack = this.getStack();
        if (!player.getWorld().isClient) {
            if(FeatureToggle.ITEM_PICK_UP_CHOICE_TOGGLE.getBooleanValue()){
                if(canUseItemWithRestriction(PlacementTweaks.ITEM_PICK_UP_CHOICE_RESTRICTION,itemStack)==false) {
                    return false;
                }else {
                    pickup(itemEntity,stack,player);
                    return true;
                }
                }else{
                pickup(itemEntity,stack,player);
            }
        }
        return false;
    }
    private void pickup(ItemEntity itemEntity, ItemStack stack, PlayerEntity player){
        ItemStack itemStack = this.getStack();
        Item item = itemStack.getItem();
        int i = itemStack.getCount();
        if (this.pickupDelay == 0 && (this.owner == null || this.owner.equals(player.getUuid())) && player.getInventory().insertStack(itemStack) ) {
            player.sendPickup(itemEntity, i);
            if (stack.isEmpty()) {
                itemEntity.remove(Entity.RemovalReason.DISCARDED);
                itemStack.setCount(i);
            }

            player.increaseStat(Stats.PICKED_UP.getOrCreateStat(item), i);
            player.triggerItemPickedUpByEntityCriteria(itemEntity);
        }
    }
}


