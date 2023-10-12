package de.hallotheengineer.fabrictpa;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class FabricTPA implements DedicatedServerModInitializer, ModInitializer {
    @Override
    public void onInitializeServer() {
        CommandRegistrationCallback.EVENT.register(Commands::registerCommands);
    }

    @Override
    public void onInitialize() {

    }
}
