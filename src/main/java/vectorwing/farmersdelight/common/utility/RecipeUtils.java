package vectorwing.farmersdelight.common.utility;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.DynamicRegistryManager;

public class RecipeUtils
{
	// Copyright (c) 2014-2015 mezz
	public static ItemStack getResultItem(Recipe<?> recipe) {
		MinecraftClient minecraft = MinecraftClient.getInstance();
		ClientWorld level = minecraft.world;
		if (level == null) {
			throw new NullPointerException("level must not be null.");
		}
		DynamicRegistryManager registryAccess = level.getRegistryManager();
		return recipe.getResultItem(registryAccess);
	}
}
