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
            source.sendFeedback(() -> Text.literal("Du kannst dich nicht zu dir selber teleportieren!").formatted(Formatting.RED), false);
            return -1;
        }
        for (TeleportRequest request : TeleportHandler.getTpaRequests()) {
            if (request.getSource() == player) {
                source.sendFeedback(() -> Text.literal("Du hast bereits eine laufende Anfrage!").formatted(Formatting.RED), false);
                return -1;
            }
        }
        if (player.getServerWorld().getDimensionKey().equals(DimensionTypes.OVERWORLD)) {
            if (player.experienceLevel >= 10) {
                player.addExperienceLevels(-10);
                TeleportHandler.newAskReqest(player, target, context);
                context.getSource().sendFeedback(() -> Text.literal("Du hast "+target.getName().getString()+" eine Teleport-Anfrage geschickt").formatted(Formatting.GRAY), false);
            } else {
                context.getSource().sendFeedback(() -> Text.literal("Du hast nicht genügend Level für ein Teleport! (mindestens 10)").formatted(Formatting.GRAY), false);
            }
        } else {
            if (player.experienceLevel >= 15) {
                player.addExperienceLevels(-15);
                TeleportHandler.newAskReqest(player, target, context);
                context.getSource().sendFeedback(() -> Text.literal("Du hast "+target.getName().getString()+" eine Teleport-Anfrage geschickt").formatted(Formatting.GRAY), false);
            } else {
                context.getSource().sendFeedback(() -> Text.literal("Du hast nicht genügend Level für ein Teleport! (mindestens 15)").formatted(Formatting.GRAY), false);
            }
        }


        return 1;
    }
}
