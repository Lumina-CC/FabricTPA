package de.hallotheengineer.fabrictpa.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.hallotheengineer.fabrictpa.TeleportHandler;
import de.hallotheengineer.fabrictpa.utils.TeleportRequest;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TPAcceptCommand {
    public static int exec(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (!checkRequests(context)) context.getSource().sendFeedback(() -> Text.literal("You have no teleport requests!").formatted(Formatting.RED), false);
        return 1;
    }
    private static boolean checkRequests(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        for (TeleportRequest request : TeleportHandler.getTpaRequests()) {
            if (request.getTarget() == context.getSource().getPlayerOrThrow()) {
                request.run();
                TeleportHandler.removeTPARequest(request);
                return true;
            }
        }
        for (TeleportRequest request : TeleportHandler.getTpaHereRequests()) {
            if (request.getSource() == context.getSource().getPlayerOrThrow()) {
                request.run();
                TeleportHandler.removeTPAHereRequest(request);
                return true;
            }
        }
        return false;
    }
}
