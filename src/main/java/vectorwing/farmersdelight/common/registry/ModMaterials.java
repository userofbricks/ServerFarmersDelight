package vectorwing.farmersdelight.common.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.item.Tier;
import org.jetbrains.annotations.NotNull;

public class ModMaterials
{
	public static final Tier FLINT = new Tier()
	{
		@Override
		public int getUses() {
			return 131;
		}

		@Override
		public float getSpeed() {
			return 4.0F;
		}

		@Override
		public float getAttackDamageBonus() {
			return 1.0F;
		}

		@Override
		public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
			return BlockTags.INCORRECT_FOR_WOODEN_TOOL;
		}

		@Override
		public int getEnchantmentValue() {
			return 5;
		}

		@Override
		public @NotNull Ingredient getRepairIngredient() {
			return Ingredient.ofItem(Items.FLINT);
		}
	};
}
