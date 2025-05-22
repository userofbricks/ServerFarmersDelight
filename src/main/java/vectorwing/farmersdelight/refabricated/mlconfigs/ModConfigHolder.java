package vectorwing.farmersdelight.refabricated.mlconfigs;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class ModConfigHolder {

    private static final Map<ResourceLocation, ModConfigHolder> CONFIG_STORAGE = new ConcurrentHashMap<>(); //wack. multithreading mod loading

    public static void addTrackedSpec(ModConfigHolder spec) {
        var old = CONFIG_STORAGE.put(spec.getId(), spec);
        if (old != null) {
            throw new IllegalStateException("Duplicate config type for with id " + spec.getId());
        }
    }

    public static Collection<ModConfigHolder> getTrackedSpecs() {
        return CONFIG_STORAGE.values();
    }

    private final ResourceLocation configId;
    private final String fileName;
    private final Component readableName;
    private final Path filePath;
    private final ConfigType type;
    @Nullable
    private final Runnable changeCallback;

    protected ModConfigHolder(ResourceLocation id, String fileExtension, Path configDirectory, ConfigType type, @Nullable Runnable changeCallback) {
        this.configId = id;
        this.fileName = id.getNamespace() + "-" + id.getPath() + "." + fileExtension;
        this.filePath = configDirectory.resolve(fileName);
        this.type = type;
        this.changeCallback = changeCallback;
        this.readableName = Component.literal(getReadableName(id.toDebugFileName() + "_configs"));

        ModConfigHolder.addTrackedSpec(this);
    }

    public static String getReadableName(String name) {
        return Arrays.stream((name).replace(":", "_").split("_"))
                .map(StringUtils::capitalize).collect(Collectors.joining(" "));
    }

    public Component getReadableName() {
        return readableName;
    }

    protected void onRefresh() {
        if (this.changeCallback != null) {
            this.changeCallback.run();
        }
    }

    public boolean isLoaded() {
        return true;
    }

    public abstract void forceLoad();

    public ConfigType getConfigType() {
        return type;
    }

    public String getModId() {
        return configId.getNamespace();
    }

    public ResourceLocation getId() {
        return configId;
    }

    public boolean isSynced() {
        return this.type.isSynced();
    }

    public String getFileName() {
        return fileName;
    }

    public Path getFullPath() {
        return filePath;
    }

    //send configs from server -> client
    public void syncConfigsToPlayer(ServerPlayer player) {
        /*
        if (this.isSynced()) {
            try {
                final byte[] configData = Files.readAllBytes(this.getFullPath());
                NetworkHelper.sendToClientPlayer(player, new ClientBoundSyncConfigsMessage(configData, this.getId()));
            } catch (IOException e) {
                Moonlight.LOGGER.error("Failed to sync common configs {}", this.getFileName(), e);
            }
        } else throw new UnsupportedOperationException("Tried to sync a config of type " + this.getConfigType());
         */
    }


    public static class ConfigLoadingException extends RuntimeException {
        public ConfigLoadingException(ModConfigHolder config, Exception cause) {
            super("Failed to load config file " + config.getFileName() + " of type " + config.getConfigType() + " for mod " + config.getModId() + ". Try deleting it", cause);
        }
    }
}
