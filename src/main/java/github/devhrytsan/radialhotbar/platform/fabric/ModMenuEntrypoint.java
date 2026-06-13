package github.devhrytsan.radialhotbar.platform.fabric;
//? fabric {
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import github.devhrytsan.radialhotbar.config.RadialHotBarConfigScreen;

@Entrypoint("modmenu")
public class ModMenuEntrypoint implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return RadialHotBarConfigScreen::createConfigScreen;
    }
}
//?}
