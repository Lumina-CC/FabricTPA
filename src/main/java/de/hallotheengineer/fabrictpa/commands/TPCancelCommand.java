package de.hallotheengineer.fabrictpa.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.hallotheengineer.fabrictpa.TeleportHandler;
import de.hallotheengineer.fabrictpa.utils.TeleportRequest;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TPCancelCommand {
    public static int exec(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (!checkRequests(context)) context.getSource().sendFeedback(() -> Text.literal("Du hast keine Teleport-Anfragen!").formatted(Formatting.RED), false);
        return 1;
    }
    private static boolean checkRequests(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        for (TeleportRequest request : TeleportHandler.getTpaRequests()) {
            if (request.getSource() == context.getSource().getPlayerOrThrow()) {
                request.cancel();
                TeleportHandler.removeTPARequest(request);
                context.getSource().sendFeedback(() -> Text.literal("Deine Teleport-Anfrage an "+request.getSource().getName().getString()+" wurde abgebrochen").formatted(Formatting.GRAY), false);
                return true;
            }
        }
        for (TeleportRequest request : TeleportHandler.getTpaHereRequests()) {
            if (request.getTarget() == context.getSource().getPlayerOrThrow()) {
                request.cancel();
                TeleportHandler.removeTPAHereRequest(request);
                context.getSource().sendFeedback(() -> Text.literal("Deine Teleport-Here-Anfrage an "+request.getSource().getName().getString()+" wurde abgebrochen").formatted(Formatting.GRAY), false);
                return true;
            }
        }
        return false;
    }

}
