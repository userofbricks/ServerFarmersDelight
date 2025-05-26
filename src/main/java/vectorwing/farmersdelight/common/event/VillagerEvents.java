package vectorwing.farmersdelight.common.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModItems;

public class VillagerEvents
{
	public static void init() {
		// As the config cannot be loaded on init, we must do this.
		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			onVillagerTrades();
			onWandererTrades();
		});
	}

	public static void onVillagerTrades() {
		TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 1, (trades) -> {
			trades.add(emeraldForItemsTrade(ModItems.ONION.get(), 26, 16, 2));
			trades.add(emeraldForItemsTrade(ModItems.TOMATO.get(), 26, 16, 2));
		});

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 2, (trades) -> {
			trades.add(emeraldForItemsTrade(ModItems.CABBAGE.get(), 16, 16, 5));
			trades.add(emeraldForItemsTrade(ModItems.RICE.get(), 20, 16, 5));
		});
	}

	public static void onWandererTrades() {
		TradeOfferHelper.registerWanderingTraderOffers((trades) -> {
			trades.pool(FarmersDelight.res("food"), 1,
					itemForEmeraldTrade(ModItems.CABBAGE_SEEDS.get(), 1, 12),
					itemForEmeraldTrade(ModItems.TOMATO_SEEDS.get(), 1, 12),
					itemForEmeraldTrade(ModItems.RICE.get(), 1, 12),
					itemForEmeraldTrade(ModItems.ONION.get(), 1, 12));
		});
	}

	public static TradeOffers.Factory emeraldForItemsTrade(ItemConvertible item, int count, int maxTrades, int xp) {
		return new TradeOffers.BuyItemFactory(item, count, maxTrades, xp);
	}

	public static TradeOffers.Factory itemForEmeraldTrade(ItemConvertible item, int maxTrades, int xp) {
		return new TradeOffers.SellItemFactory(new ItemStack(item), 1, 1, maxTrades, xp, 0.05F);
	}
}
