package net.kinchanramen.github.itempickupchoice.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigOptionList;
import fi.dy.masa.malilib.config.options.ConfigStringList;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.restrictions.UsageRestriction;
import fi.dy.masa.malilib.util.restrictions.UsageRestriction.ListType;
import net.kinchanramen.github.itempickupchoice.ItemPickupChoice;
import net.kinchanramen.github.itempickupchoice.Reference;
import net.kinchanramen.github.itempickupchoice.tweaks.PlacementTweaks;

import java.io.File;

public class Configs implements IConfigHandler {
    private static final String CONFIG_FILE_NAME = Reference.MOD_ID + ".json";
    public static class Generic
    {
        public static final ConfigBoolean HELLO = new ConfigBoolean     ("Hello!!", true, "This isn't work!");
        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                HELLO
        );
    }
    public static class Lists{
        public static final ConfigOptionList ITEM_PICK_UP_CHOICE_LIST_TYPE = new ConfigOptionList("ItemPickupChoiceListType", UsageRestriction.ListType.NONE, "The restriction list type for the Item Pick up Choice toggle");
        public static final ConfigStringList ITEM_PICK_UP_CHOICE_WHITELIST = new ConfigStringList("ItemPickupChoiceWhiteList", ImmutableList.of(), "The items that are NOT allowed to be picked up while the ItemPickupChoice is enabled");

        public static final ConfigStringList ITEM_PICK_UP_CHOICE_BLACKLIST = new ConfigStringList("ItemPickupChoiceBlackList", ImmutableList.of(), "The items that are allowed to be picked up while the ItemPickupChoice is enabled");
        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                ITEM_PICK_UP_CHOICE_LIST_TYPE,
                ITEM_PICK_UP_CHOICE_BLACKLIST,
                ITEM_PICK_UP_CHOICE_WHITELIST
        );
    }
    public static void loadFromFile() {
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject())
            {
                JsonObject root = element.getAsJsonObject();

                ConfigUtils.readConfigBase(root, "Generic", Generic.OPTIONS);
                ConfigUtils.readConfigBase(root, "GenericHotkeys", Hotkeys.HOTKEY_LIST);
                ConfigUtils.readConfigBase(root, "Lists", Lists.OPTIONS);
                ConfigUtils.readHotkeyToggleOptions(root, "TweakHotkeys", "TweakToggles", FeatureToggle.VALUES);
            }
        }
        PlacementTweaks.ITEM_PICK_UP_CHOICE_RESTRICTION.setListType((ListType) Lists.ITEM_PICK_UP_CHOICE_LIST_TYPE.getOptionListValue());
        PlacementTweaks.ITEM_PICK_UP_CHOICE_RESTRICTION.setListContents(
                Lists.ITEM_PICK_UP_CHOICE_BLACKLIST.getStrings(),
                Lists.ITEM_PICK_UP_CHOICE_WHITELIST.getStrings());
    }

        public static void saveToFile()
        {
            File dir = FileUtils.getConfigDirectory();

            if ((dir.exists() && dir.isDirectory()) || dir.mkdirs())
            {
                JsonObject root = new JsonObject();

                ConfigUtils.writeConfigBase(root, "Generic", Configs.Generic.OPTIONS);
                ConfigUtils.writeConfigBase(root, "GenericHotkeys", Hotkeys.HOTKEY_LIST);
                ConfigUtils.writeHotkeyToggleOptions(root, "TweakHotkeys", "TweakToggles", FeatureToggle.VALUES);
                ConfigUtils.writeConfigBase(root, "Lists", Configs.Lists.OPTIONS);
                JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
            }
        }
    @Override
    public void load()
    {
        loadFromFile();
    }

    @Override
    public void save()
    {
        saveToFile();
    }
}
