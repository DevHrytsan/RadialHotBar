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


        var modEnableToggle = entryBuilder.startBooleanToggle(Component.translatable("config.radialhotbar.option.enabled"), FileConfigHandler.CONFIG_INSTANCE.modEnabled)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.radialhotbar.option.enabled.tooltip"))
                .setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.modEnabled = newValue)
                .build();

        var scaleFactorSlider = entryBuilder.startIntSlider(
						Component.translatable("config.radialhotbar.option.scaleFactor"),
                        FileConfigHandler.CONFIG_INSTANCE.scaleFactor,
                        Constants.MIN_SCALE_FACTOR,
                        Constants.MAX_SCALE_FACTOR
                ).setDefaultValue(Constants.DEFAULT_SCALE_FACTOR)
                .setTooltip(Component.translatable("config.radialhotbar.option.scaleFactor.tooltip"))
                .setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.scaleFactor = newValue) // Save action
                .build();

		var toggleBooleanToggle = entryBuilder.startBooleanToggle(Component.translatable("config.radialhotbar.option.toggleMode"), FileConfigHandler.CONFIG_INSTANCE.toggleMode)
				.setDefaultValue(false)
				.setTooltip(Component.translatable("config.radialhotbar.option.toggleMode.tooltip"))
				.setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.toggleMode = newValue)
				.build();

        //general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.radialhotbar.option.shownames"), FileConfigHandler.CONFIG_INSTANCE.showItemNames)
            //    .setDefaultValue(true)
             //   .setTooltip(Component.translatable("config.radialhotbar.option.shownames.tooltip"))
              //  .setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.showItemNames = newValue)
              //  .build());

        var hideEmptySlotToggle = entryBuilder.startBooleanToggle(Component.translatable("config.radialhotbar.option.hideEmptySlots"), FileConfigHandler.CONFIG_INSTANCE.hideEmptySlots)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("config.radialhotbar.option.hideEmptySlots.tooltip"))
                .setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.hideEmptySlots = newValue)
                .build();

        var useCenterPreviewToggle = entryBuilder.startBooleanToggle(Component.translatable("config.radialhotbar.option.useCenterItemPreview"), FileConfigHandler.CONFIG_INSTANCE.useCenterItemPreview)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.radialhotbar.option.useCenterItemPreview.tooltip"))
                .setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.useCenterItemPreview = newValue)
                .build();
		var useCenterPreviewDescriptionToggle = entryBuilder.startBooleanToggle(Component.translatable("config.radialhotbar.option.useCenterPreviewDescription"), FileConfigHandler.CONFIG_INSTANCE.useCenterPreviewDescription)
				.setDefaultValue(true)
				.setTooltip(Component.translatable("config.radialhotbar.option.useCenterPreviewDescription.tooltip"))
				.setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.useCenterPreviewDescription = newValue)
				.setRequirement(() -> useCenterPreviewToggle.getValue() == true)
				.build();
        var allowMovementToggle = entryBuilder.startBooleanToggle(Component.translatable("config.radialhotbar.option.allowMovementWhileOpen"), FileConfigHandler.CONFIG_INSTANCE.allowMovementWhileOpen)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.radialhotbar.option.allowMovementWhileOpen.tooltip"))
                .setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.allowMovementWhileOpen = newValue)
                .build();

        var autoSortToggle = entryBuilder.startBooleanToggle(Component.translatable("config.radialhotbar.option.autoSort"), FileConfigHandler.CONFIG_INSTANCE.useAutoSortSlots)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("config.radialhotbar.option.autoSort.tooltip"))
                .setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.useAutoSortSlots = newValue)
                .build();

       var autoEquipArmor = entryBuilder.startBooleanToggle(Component.translatable("config.radialhotbar.option.autoEquipArmor"), FileConfigHandler.CONFIG_INSTANCE.useAutoEquipArmor)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.radialhotbar.option.autoEquipArmor.tooltip"))
                .setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.useAutoEquipArmor = newValue)
                .build();

		var swapToRecentNoSelectToggle = entryBuilder.startBooleanToggle(Component.translatable("config.radialhotbar.option.swapToRecentOnNoSelect"), FileConfigHandler.CONFIG_INSTANCE.useSwapToRecentOnNoSelect)
				.setDefaultValue(false)
				.setTooltip(Component.translatable("config.radialhotbar.option.swapToRecentOnNoSelect.tooltip"))
				.setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.useSwapToRecentOnNoSelect = newValue)
				.build();

		var showPreviousItemOnCenterToggle = entryBuilder.startBooleanToggle(Component.translatable("config.radialhotbar.option.previousItemInCenterOnNoSelect"), FileConfigHandler.CONFIG_INSTANCE.usePreviewPreviousItemOnNoSelect)
				.setDefaultValue(false)
				.setTooltip(Component.translatable("config.radialhotbar.option.previousItemInCenterOnNoSelect.tooltip"))
				.setSaveConsumer(newValue -> FileConfigHandler.CONFIG_INSTANCE.usePreviewPreviousItemOnNoSelect = newValue)
				.setRequirement(() -> swapToRecentNoSelectToggle.getValue() == true && useCenterPreviewToggle.getValue() == true)
				.build();

		general.addEntry(modEnableToggle);
		general.addEntry(scaleFactorSlider);
		general.addEntry(toggleBooleanToggle);
		general.addEntry(hideEmptySlotToggle);
		general.addEntry(useCenterPreviewToggle);
		general.addEntry(useCenterPreviewDescriptionToggle);
		general.addEntry(allowMovementToggle);
		general.addEntry(autoSortToggle);
		general.addEntry(autoEquipArmor);
		general.addEntry(swapToRecentNoSelectToggle);
		general.addEntry(showPreviousItemOnCenterToggle);

        return builder.build();
    }
}
