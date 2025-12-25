package github.devhrytsan.radialhotbar.config;

public class RadialHotBarConfig {
    //public static RadialHotBarConfig INSTANCE = new RadialHotBarConfig();

    public boolean modEnabled = true;       // Mod switch
    public int scaleFactor = 3; // Scales up radial menu
    //public boolean showItemNames = true;    // Shows names of the items
    public boolean hideEmptySlots = false;    // Whether to hide empty hotbar slots in the radial menu, instead showing them as prelocated.
    public boolean useCenterItemPreview = true; // Shows on center of radial menu a item.
    public boolean allowMovementWhileOpen = true; // Allows the player to move while the radial menu is open.
    public boolean useAutoSortSlots = false; // Automatically sorts item by category: Tools, Weapons, Food, Blocks, Potions
    public boolean useAutoEquipArmor = true; // Whether the radial menu should automatically equip armor when selected.

}