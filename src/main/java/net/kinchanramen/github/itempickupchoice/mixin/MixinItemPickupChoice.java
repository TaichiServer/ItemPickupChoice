package net.kinchanramen.github.itempickupchoice.mixin;

import net.kinchanramen.github.itempickupchoice.config.Configs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import org.apache.commons.lang3.ObjectUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class ItemPickupChoice {
    @Shadow public abstract ItemStack getStack();
    @Shadow private int pickupDelay;
    @Shadow private UUID owner;

    @Redirect(method = "Lnet/minecraft/entity/ItemEntity;onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z", ordinal = 0))
    private boolean itemPickupEvent(PlayerInventory instance, ItemStack stack,PlayerEntity player) {
        ItemEntity itemEntity = (ItemEntity)(Object) this;
        if (!player.getWorld().isClient) {
            ItemStack itemStack = this.getStack();
            Item item = itemStack.getItem();
            int i = itemStack.getCount();
            if(Configs.Generic.ITEM_PICK_UP_CHOICE_BLACKLIST.getStrings().isEmpty()){
                player.sendMessage(Text.literal("なにもないやん！"));
                player.sendMessage(Text.literal(String.valueOf(Configs.Generic.ITEM_PICK_UP_CHOICE_BLACKLIST.getStrings())));
                if (this.pickupDelay == 0 && (this.owner == null || this.owner.equals(player.getUuid())) && player.getInventory().insertStack(itemStack) ) {
                    player.sendMessage(Text.literal("しね！"));
                    player.sendPickup(itemEntity, i);
                    if (stack.isEmpty()) {
                        itemEntity.remove(Entity.RemovalReason.DISCARDED);
                        itemStack.setCount(i);
                        return false;
                    }

                    player.increaseStat(Stats.PICKED_UP.getOrCreateStat(item), i);
                    player.triggerItemPickedUpByEntityCriteria(itemEntity);
                return true;
            }else if(Configs.Generic.ITEM_PICK_UP_CHOICE_BLACKLIST.getStrings().equals(item)){
                player.sendMessage(Text.literal(String.valueOf(Configs.Generic.ITEM_PICK_UP_CHOICE_BLACKLIST)));
                if (this.pickupDelay == 0 && (this.owner == null || this.owner.equals(player.getUuid())) && player.getInventory().insertStack(itemStack) ) {
                    player.sendMessage(Text.literal("しね！"));
                    player.sendPickup(itemEntity, i);
                    if (stack.isEmpty()) {
                        itemEntity.remove(Entity.RemovalReason.DISCARDED);
                        itemStack.setCount(i);
                        return false;
                    }

                    player.increaseStat(Stats.PICKED_UP.getOrCreateStat(item), i);
                    player.triggerItemPickedUpByEntityCriteria(itemEntity);
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }
        return false;
    }}


