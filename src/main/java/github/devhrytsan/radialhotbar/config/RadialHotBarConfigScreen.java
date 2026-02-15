package github.devhrytsan.radialhotbar.config;

import github.devhrytsan.radialhotbar.Constants;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class RadialHotBarConfigScreen {

    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("main.radialhotbar.title"));
        builder.setSavingRunnable(FileConfigHandler::saveConfig);

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("config.radialhotbar.category.general"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();


        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.radialhotbar.option.enabled"), FileConfigHandler.CONFIG_INSTANCE.modEnabled)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.radialhotbar.option.enabled.tooltip"))
                .setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.modEnabled = newValue)
                .build());

        general.addEntry(entryBuilder.startIntSlider(
						Component.translatable("config.radialhotbar.option.scaleFactor"),
                        FileConfigHandler.CONFIG_INSTANCE.scaleFactor,
                        Constants.MIN_SCALE_FACTOR,
                        Constants.MAX_SCALE_FACTOR
                ).setDefaultValue(Constants.DEFAULT_SCALE_FACTOR)
                .setTooltip(Component.translatable("config.radialhotbar.option.scaleFactor.tooltip"))
                .setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.scaleFactor = newValue) // Save action
                .build());

        //general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.radialhotbar.option.shownames"), FileConfigHandler.CONFIG_INSTANCE.showItemNames)
            //    .setDefaultValue(true)
             //   .setTooltip(Component.translatable("config.radialhotbar.option.shownames.tooltip"))
              //  .setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.showItemNames = newValue)
              //  .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.radialhotbar.option.hideEmptySlots"), FileConfigHandler.CONFIG_INSTANCE.hideEmptySlots)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("config.radialhotbar.option.hideEmptySlots.tooltip"))
                .setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.hideEmptySlots = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.radialhotbar.option.useCenterItemPreview"), FileConfigHandler.CONFIG_INSTANCE.useCenterItemPreview)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.radialhotbar.option.useCenterItemPreview.tooltip"))
                .setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.useCenterItemPreview = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.radialhotbar.option.allowMovementWhileOpen"), FileConfigHandler.CONFIG_INSTANCE.allowMovementWhileOpen)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.radialhotbar.option.allowMovementWhileOpen.tooltip"))
                .setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.allowMovementWhileOpen = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.radialhotbar.option.autoSort"), FileConfigHandler.CONFIG_INSTANCE.useAutoSortSlots)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("config.radialhotbar.option.autoSort.tooltip"))
                .setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.useAutoSortSlots = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.radialhotbar.option.autoEquipArmor"), FileConfigHandler.CONFIG_INSTANCE.useAutoEquipArmor)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.radialhotbar.option.autoEquipArmor.tooltip"))
                .setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.useAutoEquipArmor = newValue)
                .build());

		general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.radialhotbar.option.swapToRecentOnNoSelect"), FileConfigHandler.CONFIG_INSTANCE.useSwapToRecentOnNoSelect)
				.setDefaultValue(false)
				.setTooltip(Component.translatable("config.radialhotbar.option.swapToRecentOnNoSelect.tooltip"))
				.setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.useSwapToRecentOnNoSelect = newValue)
				.build());

        return builder.build();
    }
}
