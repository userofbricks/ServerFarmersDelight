package vectorwing.farmersdelight.integration.jei.category;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ClientRenderUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;
import vectorwing.farmersdelight.integration.jei.FDRecipeTypes;
import vectorwing.farmersdelight.integration.jei.resource.DecompositionDummy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@MethodsReturnNonnullByDefault
public class DecompositionRecipeCategory implements IRecipeCategory<DecompositionDummy>
{
	public static final Identifier UID = Identifier.of(FarmersDelight.MODID, "decomposition");
	private static final int slotSize = 22;

	private final Text title;
	private final IDrawable background;
	private final IDrawable slotIcon;
	private final IDrawable icon;
	private final ItemStack organicCompost;
	private final ItemStack richSoil;

	public DecompositionRecipeCategory(IGuiHelper helper) {
		title = TextUtils.getTranslation("jei.decomposition");
		Identifier backgroundImage = Identifier.of(FarmersDelight.MODID, "textures/gui/jei/decomposition.png");
		background = helper.createDrawable(backgroundImage, 0, 0, 118, 80);
		organicCompost = new ItemStack(ModBlocks.ORGANIC_COMPOST.get());
		richSoil = new ItemStack(ModItems.RICH_SOIL.get());
		icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, richSoil);
		slotIcon = helper.createDrawable(backgroundImage, 119, 0, slotSize, slotSize);
	}

	@Override
	public RecipeType<DecompositionDummy> getRecipeType() {
		return FDRecipeTypes.DECOMPOSITION;
	}

	@Override
	public Text getTitle() {
		return this.title;
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, DecompositionDummy recipe, IFocusGroup focusGroup) {
		List<ItemStack> accelerators = new ArrayList<>();
		Registries.BLOCK.getTag(ModTags.COMPOST_ACTIVATORS).ifPresent(s -> s.forEach(f -> accelerators.add(new ItemStack(f.value()))));

		builder.addSlot(RecipeIngredientRole.INPUT, 9, 26).addItemStack(organicCompost);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 93, 26).addItemStack(richSoil);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 64, 54).addItemStacks(accelerators);
	}

	@Override
	public void draw(DecompositionDummy recipe, IRecipeSlotsView recipeSlotsView, DrawContext guiGraphics, double mouseX, double mouseY) {
		this.slotIcon.draw(guiGraphics, 63, 53);
	}

	@Override
	public List<Text> getTooltipStrings(DecompositionDummy recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if (ClientRenderUtils.isCursorInsideBounds(40, 38, 11, 11, mouseX, mouseY)) {
			return ImmutableList.of(translateKey(".light"));
		}
		if (ClientRenderUtils.isCursorInsideBounds(53, 38, 11, 11, mouseX, mouseY)) {
			return ImmutableList.of(translateKey(".fluid"));
		}
		if (ClientRenderUtils.isCursorInsideBounds(67, 38, 11, 11, mouseX, mouseY)) {
			return ImmutableList.of(translateKey(".accelerators"));
		}
		return Collections.emptyList();
	}

	private static MutableText translateKey(@NotNull String suffix) {
		return Text.translatable(FarmersDelight.MODID + ".jei.decomposition" + suffix);
	}
}
