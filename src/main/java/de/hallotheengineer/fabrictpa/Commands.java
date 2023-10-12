package de.hallotheengineer.fabrictpa;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.hallotheengineer.fabrictpa.commands.*;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Commands {
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        LiteralArgumentBuilder<ServerCommandSource> tpaCommand = literal("tpa")
                .then(argument("player", EntityArgumentType.player())
                        .executes(TPACommand::exec));
        LiteralArgumentBuilder<ServerCommandSource> tpCancelCommand = literal("tpcancel")
                .executes(TPCancelCommand::exec);
        LiteralArgumentBuilder<ServerCommandSource> tpAcceptCommand = literal("tpaccept")
                .executes(TPAcceptCommand::exec);
        LiteralArgumentBuilder<ServerCommandSource> tpDenyCommand = literal("tpdeny")
                .executes(TPDenyCommand::exec);
        LiteralArgumentBuilder<ServerCommandSource> tpaHereCommand = literal("tpahere")
                .then(argument("player", EntityArgumentType.player())
                        .executes(TPAHereCommand::exec));



        dispatcher.register(tpaCommand);
        dispatcher.register(tpCancelCommand);
        dispatcher.register(tpAcceptCommand);
        dispatcher.register(tpDenyCommand);
        dispatcher.register(tpaHereCommand);
    }

}
