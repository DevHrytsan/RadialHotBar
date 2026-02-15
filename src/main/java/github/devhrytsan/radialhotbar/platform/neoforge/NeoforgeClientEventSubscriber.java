package github.devhrytsan.radialhotbar.platform.neoforge;

//? neoforge {

/*import github.devhrytsan.radialhotbar.RadialHotBarMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(modid = RadialHotBarMod.MOD_ID, value = Dist.CLIENT)
public class NeoforgeClientEventSubscriber {
	@SubscribeEvent
	public static void onClientSetup(final FMLClientSetupEvent event) {
		RadialHotBarMod.onInitializeClient();
		RadialHotBarMod.InitializeModNeoForge(event);
	}

	@SubscribeEvent
	public static void registerKeys(RegisterKeyMappingsEvent event) {
		RadialHotBarMod.RegisterKeysNeoForge(event);
	}
}
*///?}
