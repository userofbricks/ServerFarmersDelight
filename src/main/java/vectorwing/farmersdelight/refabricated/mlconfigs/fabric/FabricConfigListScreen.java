package vectorwing.farmersdelight.refabricated.mlconfigs.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.refabricated.mlconfigs.ModConfigHolder;

import java.util.List;

/**
 * Main config screen
 */
public class FabricConfigListScreen extends Screen {

    protected final Screen parent;
    protected final ModConfigHolder[] configs;
    @Nullable
    protected final Identifier background;
    private final ItemStack mainIcon;
    private final String modId;
    private final String modURL;

    protected ConfigList list;

    public FabricConfigListScreen(String modId, ItemStack mainIcon, Text displayName, @Nullable Identifier background,
                                  Screen parent,
                                  ModConfigHolder... specs) {
        super(displayName);
        this.parent = parent;
        this.configs = specs;
        this.background = background;
        this.mainIcon = mainIcon;
        this.modId = modId;
        this.modURL = FabricLoader.getInstance().getModContainer(modId).get().getMetadata().getContact().get("homepage").orElse(null);
    }

    @Override
    protected void init() {
        this.list = new ConfigList(this.client, this.width, this.height, 32, 40,
                this.configs);
        this.addDrawableChild(this.list);

        this.addExtraButtons();
    }

    protected void addExtraButtons() {
        this.addDrawableChild(ButtonWidget.builder(
                        ScreenTexts.DONE, button -> this.client.setScreen(this.parent))
                .dimensions(this.width / 2 - 155 + 160, this.height - 29, 150, 20).build());
    }

    @Override
    public void removed() {
    }

    @Override
    public void render(DrawContext graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 16777215);

        if (modURL != null && isMouseWithin((this.width / 2) - 90, 2 + 6, 180, 16 + 2, mouseX, mouseY)) {
            graphics.drawOrderedTooltip(this.textRenderer, this.textRenderer.wrapLines(Text.translatable("gui.moonlight.open_mod_page", this.modId), 200), mouseX, mouseY);
        }
        int titleWidth = this.textRenderer.getWidth(this.title) + 35;
        graphics.drawItemWithoutEntity(this.mainIcon, (this.width / 2) + titleWidth / 2 - 17, 2 + 8);
        graphics.drawItemWithoutEntity(this.mainIcon, (this.width / 2) - titleWidth / 2, 2 + 8);
    }

    private boolean isMouseWithin(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (modURL != null && isMouseWithin((this.width / 2) - 90, 2 + 6, 180, 16 + 2, (int) mouseX, (int) mouseY)) {
            Style style = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, modURL));
            this.handleTextClick(style);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    protected class ConfigList extends ElementListWidget<ConfigButton> {

        public ConfigList(MinecraftClient minecraft, int width, int height, int y0, int itemHeight, ModConfigHolder... specs) {
            super(minecraft, width, height, y0,  itemHeight);
            this.centerListVertically = true;
            for (var s : specs) {
                this.addEntry(new ConfigButton(s, this.width, this.getRowWidth()));
            }
        }

        @Override
        public int getRowWidth() {
            return 260;
        }

        @Override
        protected int getScrollbarPosition() {
            return super.getScrollbarPosition() + 32;
        }
    }

    protected class ConfigButton extends ElementListWidget.Entry<ConfigButton> {

        private final List<ClickableWidget> children;

        private ConfigButton(ClickableWidget widget) {
            this.children = List.of(widget);
        }

        protected ConfigButton(ModConfigHolder spec, int width, int buttonWidth) {
            this(ButtonWidget.builder(Text.literal(spec.getFileName()), b -> {}).dimensions(width / 2 - buttonWidth / 2, 0, buttonWidth, 20).build());
        }

        @Override
        public void render(DrawContext graphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            this.children.forEach((button) -> {
                button.setY(top);
                button.render(graphics, mouseX, mouseY, partialTick);
            });
        }

        @Override
        public List<? extends Element> children() {
            return this.children;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return this.children;
        }
    }

}