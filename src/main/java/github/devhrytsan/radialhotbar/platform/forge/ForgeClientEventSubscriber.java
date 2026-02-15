package github.devhrytsan.radialhotbar.platform.forge;

//? forge {

/*import github.devhrytsan.radialhotbar.RadialHotBarMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = RadialHotBarMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeClientEventSubscriber {

	@SubscribeEvent
	public static void onClientSetup(final FMLClientSetupEvent event) {
		RadialHotBarMod.onInitializeClient();
		RadialHotBarMod.InitializeModForge(event);
	}

	@SubscribeEvent
	public static void registerKeys(RegisterKeyMappingsEvent event) {
		RadialHotBarMod.RegisterKeysForge(event);
	}
}
*///?}
