package com.example.mixin;

import com.example.RoeiMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MaceItem.class)
public class MaceItemMixin {

	@Inject(method = "canSmashAttack", at = @At("HEAD"), cancellable = true)
	private static void forceSmashAttack(LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
		if (RoeiMod.isEnabled && attacker instanceof Player player && player.isCreative()) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "hurtEnemy", at = @At("RETURN"))
	private void launchPlayerOnGround(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfo ci) {
		if (RoeiMod.isEnabled && attacker instanceof Player player && player.isCreative() && !player.getAbilities().flying) {
			Holder<Enchantment> windBurst = player.level().registryAccess()
					.lookupOrThrow(Registries.ENCHANTMENT)
					.getOrThrow(Enchantments.WIND_BURST);
			int level = EnchantmentHelper.getItemEnchantmentLevel(windBurst, stack);

			if (level > 0 && player.onGround()) {
				// ניתוק מהקרקע ומתן דחיפה כלפי מעלה לפי רמת ה-Wind Burst
				player.setOnGround(false);
				Vec3 vel = player.getDeltaMovement();
				double launchPower = 1.1 + (level * 0.45);
				player.setDeltaMovement(vel.x, launchPower, vel.z);
			}
		}
	}
}
