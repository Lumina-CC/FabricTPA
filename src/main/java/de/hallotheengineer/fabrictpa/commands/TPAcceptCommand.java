package de.hallotheengineer.fabrictpa.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.hallotheengineer.fabrictpa.TeleportHandler;
import de.hallotheengineer.fabrictpa.utils.TeleportRequest;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TPAcceptCommand {
    public static int exec(CommandContext<ServerCommandSource> context,String ownerUUID) throws CommandSyntaxException {
        if (!checkRequests(context,ownerUUID)) context.getSource().sendFeedback(() -> Text.literal("You have no teleport requests!").formatted(Formatting.RED), false);
        return 1;
    }
    private static boolean checkRequests(CommandContext<ServerCommandSource> context,String ownerUUID) throws CommandSyntaxException {
        for (TeleportRequest request : TeleportHandler.getTpaRequests()) {
            if (request.getTarget() == context.getSource().getPlayerOrThrow() && (ownerUUID == null || ownerUUID.equals(request.getOwner().getUuidAsString()))) {
                request.run();
                TeleportHandler.removeTPARequest(request);
                return true;
            }
        }
        for (TeleportRequest request : TeleportHandler.getTpaHereRequests()) {
            if (request.getSource() == context.getSource().getPlayerOrThrow() && (ownerUUID == null || ownerUUID.equals(request.getOwner().getUuidAsString()))) {
                request.run();
                TeleportHandler.removeTPAHereRequest(request);
                return true;
            }
        }
        return false;
    }
}
