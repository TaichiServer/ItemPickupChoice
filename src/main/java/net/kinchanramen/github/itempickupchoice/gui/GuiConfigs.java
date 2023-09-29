package net.kinchanramen.github.itempickupchoice.gui;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.BooleanHotkeyGuiWrapper;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.StringUtils;
import net.kinchanramen.github.itempickupchoice.Reference;
import net.kinchanramen.github.itempickupchoice.config.Configs;
import net.kinchanramen.github.itempickupchoice.config.FeatureToggle;
import net.kinchanramen.github.itempickupchoice.config.Hotkeys;

public class GuiConfigs extends GuiConfigsBase {
    public static ImmutableList<FeatureToggle> TWEAK_LIST = FeatureToggle.VALUES;
    private static ConfigGuiTab tab = ConfigGuiTab.TWEAKS;
    public GuiConfigs() {
        super(10, 50, Reference.MOD_ID, null, "tweakeroo.gui.title.configs", String.format("%s", Reference.MOD_VERSION));
    }
    @Override
    public void initGui()
    {
        super.initGui();
        this.clearOptions();
        Configs.saveToFile();
        Configs.loadFromFile();

        int x = 10;
        int y = 26;

        for (ConfigGuiTab tab : ConfigGuiTab.values())
        {
            x += this.createButton(x, y, -1, tab);
        }
    }
    private int createButton(int x, int y, int width, ConfigGuiTab tab)
    {
        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, tab.getDisplayName());
        button.setEnabled(GuiConfigs.tab != tab);
        this.addButton(button, new ButtonListener(tab, this));

        return button.getWidth() + 2;
    }
    @Override
    protected int getConfigWidth()
    {
        ConfigGuiTab tab = GuiConfigs.tab;

        if (tab == ConfigGuiTab.GENERIC)
        {
            return 120;
        }
        else if (tab == ConfigGuiTab.LISTS)
        {
            return 200;
        }

        return 260;
    }
    @Override
    protected boolean useKeybindSearch()
    {
        return GuiConfigs.tab == ConfigGuiTab.TWEAKS||
        GuiConfigs.tab == ConfigGuiTab.GENERIC_HOTKEYS ;
    }
    @Override
    public List<ConfigOptionWrapper> getConfigs()
    {
        List<? extends IConfigBase> configs;
        ConfigGuiTab tab = GuiConfigs.tab;

        if (tab == ConfigGuiTab.GENERIC)
        {
            configs = Configs.Generic.OPTIONS;
        }
        else if (tab == ConfigGuiTab.LISTS)
        {
            configs = Configs.Lists.OPTIONS;
        }
        else if (tab == ConfigGuiTab.TWEAKS)
        {
            return ConfigOptionWrapper.createFor(TWEAK_LIST.stream().map(this::wrapConfig).toList());
        }
        else if (tab == ConfigGuiTab.GENERIC_HOTKEYS)
        {
            configs = Hotkeys.HOTKEY_LIST;
        }
        else
        {
            return Collections.emptyList();
        }

        return ConfigOptionWrapper.createFor(configs);
    }
    protected BooleanHotkeyGuiWrapper wrapConfig(FeatureToggle config)
    {
        return new BooleanHotkeyGuiWrapper(config.getName(), config, config.getKeybind());
    }
    private static class ButtonListener implements IButtonActionListener
    {
        private final GuiConfigs parent;
        private final ConfigGuiTab tab;

        public ButtonListener(ConfigGuiTab tab, GuiConfigs parent)
        {
            this.tab = tab;
            this.parent = parent;
        }

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton)
        {
            GuiConfigs.tab = this.tab;
            this.parent.reCreateListWidget(); // apply the new config width
            this.parent.getListWidget().resetScrollbarPosition();
            this.parent.initGui();
        }
    }
    public enum ConfigGuiTab
    {
        GENERIC         ("tweakeroo.gui.button.config_gui.generic"),
        LISTS           ("tweakeroo.gui.button.config_gui.lists"),
        TWEAKS          ("tweakeroo.gui.button.config_gui.tweaks"),
        GENERIC_HOTKEYS ("tweakeroo.gui.button.config_gui.generic_hotkeys");

        private final String translationKey;

        ConfigGuiTab(String translationKey)
        {
            this.translationKey = translationKey;
        }

        public String getDisplayName()
        {
            return StringUtils.translate(this.translationKey);
        }
    }
}
