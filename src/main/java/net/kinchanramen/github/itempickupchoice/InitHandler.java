package net.kinchanramen.github.itempickupchoice;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.event.WorldLoadHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import net.kinchanramen.github.itempickupchoice.config.Callbacks;
import net.kinchanramen.github.itempickupchoice.config.Configs;
import net.kinchanramen.github.itempickupchoice.event.InputHandler;
import net.kinchanramen.github.itempickupchoice.event.WorldLoadListener;
import net.minecraft.client.MinecraftClient;

public class InitHandler implements IInitializationHandler
{
    @Override
    public void registerModHandlers()
    {
        ConfigManager.getInstance().registerConfigHandler(Reference.MOD_ID, new Configs());

        InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerMouseInputHandler(InputHandler.getInstance());
        WorldLoadHandler.getInstance().registerWorldLoadPreHandler(new WorldLoadListener());
        Callbacks.init(MinecraftClient.getInstance());
    }
}
