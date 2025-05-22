package vectorwing.farmersdelight.refabricated.mlconfigs.fabric;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;
import vectorwing.farmersdelight.refabricated.mlconfigs.ConfigBuilder;
import vectorwing.farmersdelight.refabricated.mlconfigs.ConfigType;
import vectorwing.farmersdelight.refabricated.mlconfigs.ModConfigHolder;

import java.io.*;
import java.nio.charset.StandardCharsets;

public final class FabricConfigHolder extends ModConfigHolder {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final ConfigSubCategory mainEntry;
    private final File file;
    private boolean initialized = false;

    public FabricConfigHolder(Identifier name, ConfigSubCategory mainEntry, ConfigType type, Runnable changeCallback) {
        super(name, "json", FabricLoader.getInstance().getConfigDir(), type, changeCallback);
        this.file = this.getFullPath().toFile();
        this.mainEntry = mainEntry;
        if (this.isSynced()) {
            ServerPlayConnectionEvents.JOIN.register(this::onPlayerLoggedIn);
        }
    }

    public ConfigSubCategory getMainEntry() {
        return mainEntry;
    }

    @Override
    public boolean isLoaded() {
        return initialized;
    }

    @Override
    public void forceLoad() {
        if (this.isLoaded()) return;

        try {
            JsonElement config = null;

            if (file.exists() && file.isFile()) {
                try (FileInputStream fileInputStream = new FileInputStream(file);
                     InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                     BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                    config = GSON.fromJson(bufferedReader, JsonElement.class);
                }
            }

            if (config instanceof JsonObject jo) {
                //don't call a load directly, so we skip the main category name
                mainEntry.getEntries().forEach(e -> e.loadFromJson(jo));
            }
            if (!initialized) {
                this.initialized = true;
                this.saveConfig();
                ConfigBuilder.LOGGER.info("Loaded config {}", this.getFileName());
            }
        } catch (Exception e) {
            throw new ConfigLoadingException(this, e);
        }
    }

    public void saveConfig() {
        try (FileOutputStream stream = new FileOutputStream(this.file);
             Writer writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8)) {

            JsonObject jo = new JsonObject();
            jo.addProperty("#README", "This config file does not support comments. To see them configure it in-game using YACL or Cloth Config (or just use Forge)");
            mainEntry.getEntries().forEach(e -> e.saveToJson(jo));

            GSON.toJson(jo, writer);
        } catch (IOException e) {
            ConfigBuilder.LOGGER.error("Failed to save config {}:", this.getReadableName(), e);
        }
        this.onRefresh();
    }

    private void onPlayerLoggedIn(ServerPlayNetworkHandler listener, PacketSender sender, MinecraftServer minecraftServer) {
        //send this configuration to connected clients
        syncConfigsToPlayer(listener.player);
    }

}
