package github.devhrytsan.radialhotbar.platform.neoforge;

//? neoforge {

/*import github.devhrytsan.radialhotbar.platform.Platform;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.VersionInfo;
import net.neoforged.fml.loading.FMLPaths;

public class NeoforgePlatform implements Platform {

	@Override
	public boolean isModLoaded(String modId) {
		return ModList.get().isLoaded(modId);
	}

	@Override
	public ModLoader loader() {
		return ModLoader.NEOFORGE;
	}

	@Override
	public String mcVersion() {
		return "";
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return !FMLLoader/^? if > 1.21.7 {^/.getCurrent()/^?}^/.isProduction();
	}

	@Override
	public java.nio.file.Path getConfigDirectory() {
		return FMLPaths.CONFIGDIR.get();
	}
}
*///?}
