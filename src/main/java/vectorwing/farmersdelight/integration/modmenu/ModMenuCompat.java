package vectorwing.farmersdelight.integration.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.text.Text;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.refabricated.mlconfigs.fabric.FabricConfigListScreen;

public class ModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new FabricConfigListScreen(FarmersDelight.MODID, ModItems.STOVE.get().getDefaultStack(),
                Text.translatable(FarmersDelight.MODID), null,
                parent, Configuration.COMMON_CONFIG);
    }

}