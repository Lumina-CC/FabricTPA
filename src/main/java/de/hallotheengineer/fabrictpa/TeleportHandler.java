package de.hallotheengineer.fabrictpa;

import com.mojang.brigadier.context.CommandContext;
import de.hallotheengineer.fabrictpa.utils.TeleportRequest;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

public class TeleportHandler {
    private static final List<TeleportRequest> tpaRequests = new ArrayList<>();
    private static final List<TeleportRequest> tpaHereRequests = new ArrayList<>();
    public static void newAskReqest(ServerPlayerEntity source, ServerPlayerEntity target, CommandContext<ServerCommandSource> context) {
        TeleportRequest request = new TeleportRequest(source, target, context);
        tpaRequests.add(request);
        target.sendMessage(Text.literal(source.getName().getString()+" sent you a teleport request!").formatted(Formatting.GREEN));
    }
    public static void newAskHereReqest(ServerPlayerEntity source, ServerPlayerEntity target, CommandContext<ServerCommandSource> context) {
        TeleportRequest request = new TeleportRequest(target, source, context);
        tpaHereRequests.add(request);
        target.sendMessage(Text.literal(source.getName().getString()+" wants you to teleport to them").formatted(Formatting.GREEN));
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
