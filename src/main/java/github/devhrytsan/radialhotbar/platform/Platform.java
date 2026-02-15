package github.devhrytsan.radialhotbar.platform;

public interface Platform {
	boolean isModLoaded(String modId);

	ModLoader loader();

	String mcVersion();

	boolean isDevelopmentEnvironment();

	default boolean isDebug() {
		return isDevelopmentEnvironment();
	}

	java.nio.file.Path getConfigDirectory();

	enum ModLoader {
		FABRIC, NEOFORGE, FORGE, QUILT
	}
}
