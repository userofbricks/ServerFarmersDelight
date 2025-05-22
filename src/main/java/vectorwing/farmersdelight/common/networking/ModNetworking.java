package vectorwing.farmersdelight.common.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.item.SkilletItem;
import vectorwing.farmersdelight.common.registry.ModDataComponents;
import vectorwing.farmersdelight.refabricated.FDRecipeBookTypes;

public class ModNetworking {

    public static void init() {
        PayloadTypeRegistry.playS2C().register(SendRecipeBookValuesMessage.TYPE, SendRecipeBookValuesMessage.STREAM_CODEC);

        PayloadTypeRegistry.playC2S().register(FlipSkilletMessage.TYPE, FlipSkilletMessage.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(FlipSkilletMessage.TYPE, (payload, context) -> payload.handle(context.server(), context.player()));

    }

    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(SendRecipeBookValuesMessage.TYPE, (payload, context) -> payload.handle());
    }

    public static class FlipSkilletMessage implements CustomPayload {
        public static final Identifier ID = FarmersDelight.res("flip_skillet");
        public static final FlipSkilletMessage INSTANCE = new FlipSkilletMessage();
        public static final Id<FlipSkilletMessage> TYPE = new Id<>(ID);
        public static final PacketCodec<RegistryByteBuf, FlipSkilletMessage> STREAM_CODEC = PacketCodec.unit(INSTANCE);

        public FlipSkilletMessage() {
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return TYPE;
        }

        public void handle(MinecraftServer server, ServerPlayerEntity player) {
            ItemStack stack = player.getActiveItem();
            if (stack.getItem() instanceof SkilletItem) {
                stack.set(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get(), player.getWorld().getTime());
            }
        }
    }

    public record SendRecipeBookValuesMessage(boolean open, boolean filtering) implements CustomPayload {
        public static final Identifier ID = FarmersDelight.res("send_recipe_book_values");
        public static final Id<SendRecipeBookValuesMessage> TYPE = new Id<>(ID);
        public static final PacketCodec<RegistryByteBuf, SendRecipeBookValuesMessage> STREAM_CODEC = PacketCodec.ofStatic(SendRecipeBookValuesMessage::write, SendRecipeBookValuesMessage::new);

        public SendRecipeBookValuesMessage(PacketByteBuf buf) {
            this(buf.readBoolean(), buf.readBoolean());
        }

        public static void write(RegistryByteBuf buf, SendRecipeBookValuesMessage message) {
            buf.writeBoolean(message.open);
            buf.writeBoolean(message.filtering);
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return TYPE;
        }

        public void handle() {
            MinecraftClient.getInstance().execute(() -> {
                ClientRecipeBook recipeBook = MinecraftClient.getInstance().player.getRecipeBook();
                recipeBook.setGuiOpen(FDRecipeBookTypes.COOKING, open);
                recipeBook.setFilteringCraftable(FDRecipeBookTypes.COOKING, filtering);
            });
        }
    }
}
