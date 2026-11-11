package com.example.mixin;

import com.example.RoeiMod;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.MaceItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MaceItem.class)
public class MaceItemMixin {

	@Inject(method = "canSmashAttack", at = @At("HEAD"), cancellable = true)
	private static void forceSmashAttack(LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
		if (RoeiMod.isEnabled && attacker instanceof Player player && player.isCreative()) {
			cir.setReturnValue(true);
		}
	}
}
