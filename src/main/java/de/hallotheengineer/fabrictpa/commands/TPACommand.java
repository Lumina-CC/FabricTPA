package de.hallotheengineer.fabrictpa.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.hallotheengineer.fabrictpa.TeleportHandler;
import de.hallotheengineer.fabrictpa.utils.TeleportRequest;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.dimension.DimensionTypes;

public class TPACommand {
    public static int exec(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayerOrThrow();

        if (target == player) {
            source.sendFeedback(() -> Text.literal("You can't teleport to yourself").formatted(Formatting.RED), false);
            return -1;
        }
        for (TeleportRequest request : TeleportHandler.getTpaRequests()) {
            if (request.getSource() == player) {
                source.sendFeedback(() -> Text.literal("You already have ongoing requests").formatted(Formatting.RED), false);
                return -1;
            }
        }
        TeleportHandler.newAskReqest(player, target, context);
        context.getSource().sendFeedback(() -> Text.literal("Teleport request sent to "+target.getName().getString()).formatted(Formatting.GRAY), false);
        return 1;
    }
}
