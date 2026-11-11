package com.example;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class RoeiMod implements ClientModInitializer {
	public static final String MOD_ID = "roei_mod";
	public static boolean isEnabled = false;
	private static KeyMapping toggleKey;

	@Override
	public void onInitializeClient() {
		// טעינה אוטומטית של ה-Resource Pack מתוך תיקיית resourcepacks/my_pack
		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> {
			ResourceManagerHelper.registerBuiltinResourcePack(
					Identifier.fromNamespaceAndPath(MOD_ID, "my_pack"),
					container,
					ResourcePackActivationType.DEFAULT_ENABLED
			);
		});

		// רישום מקש O בתפריט המקשים של המשחק
		toggleKey = KeyMappingHelper.registerKeyBinding(new KeyMapping(
				"key.roei_mod.toggle",
				InputConstants.Type.KEYBOARD,
				InputConstants.KEY_O,
				KeyMapping.Category.MISC
		));

		// מקש O יפעל אך ורק כשהשחקן במצב Creative
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (toggleKey.consumeClick()) {
				if (client.player != null && client.player.isCreative()) {
					isEnabled = !isEnabled;
					String status = isEnabled ? "§aמופעל (מכל גובה)" : "§cכבוי (רגיל)";
					client.player.sendOverlayMessage(Component.literal("Wind Burst: " + status));
				}
			}
		});
	}
}
