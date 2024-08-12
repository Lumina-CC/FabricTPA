package de.hallotheengineer.fabrictpa;

import com.mojang.brigadier.context.CommandContext;
import de.hallotheengineer.fabrictpa.utils.TeleportRequest;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

public class TeleportHandler {
    private static final List<TeleportRequest> tpaRequests = new ArrayList<>();
    private static final List<TeleportRequest> tpaHereRequests = new ArrayList<>();
    public static void newAskReqest(ServerPlayerEntity source, ServerPlayerEntity target, CommandContext<ServerCommandSource> context) {
        TeleportRequest request = new TeleportRequest(source, target, context);
        tpaRequests.add(request);
        target.sendMessage(Text.literal(source.getName().getString()+" wants to teleport to you\n")
                .formatted(Formatting.GREEN)
                .append(Text.literal("[Accept]")
                        .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tpaccept "+ source.getUuidAsString())))
                        .formatted(Formatting.GREEN))
                .append("     ")
                .append(Text.literal("[Deny]")
                        .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tpdeny "+ source.getUuidAsString())))
                .formatted(Formatting.RED)));
    }
    public static void newAskHereReqest(ServerPlayerEntity source, ServerPlayerEntity target, CommandContext<ServerCommandSource> context) {
        TeleportRequest request = new TeleportRequest(target, source, context);
        tpaHereRequests.add(request);
        target.sendMessage(Text.literal(source.getName().getString()+" wants you to teleport to them\n")
                .formatted(Formatting.GREEN)
                .append(Text.literal("[Accept]")
                        .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tpaccept " + source.getUuidAsString())))
                        .formatted(Formatting.GREEN))
                .append("     ")
                .append(Text.literal("[Deny]")
                        .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tpdeny " + source.getUuidAsString())))
                        .formatted(Formatting.RED)));
    }

    public static List<TeleportRequest> getTpaRequests() {
        return tpaRequests;
    }
    public static List<TeleportRequest> getTpaHereRequests() {
        return tpaHereRequests;
    }
    public static void removeTPARequest(TeleportRequest request) {
        tpaRequests.remove(request);
    }
    public static void removeTPAHereRequest(TeleportRequest request) {
        tpaHereRequests.remove(request);
    }
}
