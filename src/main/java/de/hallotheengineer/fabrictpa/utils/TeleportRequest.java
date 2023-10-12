package de.hallotheengineer.fabrictpa.utils;


import com.mojang.brigadier.context.CommandContext;
import de.hallotheengineer.fabrictpa.TeleportHandler;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class TeleportRequest {
    private final Timer timer;
    private final ServerPlayerEntity source;
    private final ServerPlayerEntity target;
    private BlockPos pos;
    private final CommandContext<ServerCommandSource> context;
    private int iterations = 5;
    public TeleportRequest(ServerPlayerEntity source, ServerPlayerEntity target, CommandContext<ServerCommandSource> context) {
        this.timer = new Timer();
        this.source = source;
        this.target = target;
        this.context = context;
        startCountdown();
    }
    public TeleportRequest(ServerPlayerEntity source, ServerPlayerEntity target, CommandContext<ServerCommandSource> context, int iterations) {
        this.timer = new Timer();
        this.source = source;
        this.target = target;
        this.context = context;
        this.iterations = iterations;
        startCountdown();
    }
    public void startCountdown() {
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                wipeRequest();
                timer.cancel();
                target.networkHandler.sendPacket(new OverlayMessageS2CPacket(Text.literal("Die Teleport-Anfrage von "+source.getName()+" ist abgelaufen!").formatted(Formatting.RED)));
                source.playSound(SoundEvents.ENTITY_PUFFER_FISH_DEATH, SoundCategory.MASTER, 1, 1);
            }
        }, 300000);
    }
    public void run() {
        pos = source.getBlockPos();
        source.networkHandler.sendPacket(new ParticleS2CPacket(ParticleTypes.PORTAL, false, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0, 0, 1));
        target.networkHandler.sendPacket(new OverlayMessageS2CPacket(Text.literal("Teleport wird durchgeführt").formatted(Formatting.GREEN)));
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!source.isDisconnected() && !target.isDisconnected()) {
                    if (pos != source.getBlockPos()) {
                        source.networkHandler.sendPacket(new OverlayMessageS2CPacket(Text.literal("Teleport abgebrochen!").formatted(Formatting.RED)));
                        target.networkHandler.sendPacket(new OverlayMessageS2CPacket(Text.literal("Teleport abgebrochen!").formatted(Formatting.RED)));
                        source.playSound(SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.MASTER, 1, 1);
                        wipeRequest();
                        this.cancel();
                        timer.cancel();
                    }
                    if (iterations == 0){
                        timer.cancel();
                        executeTeleport();
                    } else {
                        source.networkHandler.sendPacket(new OverlayMessageS2CPacket(Text.literal(String.valueOf(iterations)).formatted(Formatting.GREEN)));
                    }
                    source.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1, 1);
                    iterations--;
                } else {
                    this.cancel();
                }
            }
        }, 0, 1000);
    }
    private void executeTeleport() {
        source.teleport(target.getServerWorld(), target.getX(), target.getY(), target.getZ(), target.getYaw(), target.getPitch());
        source.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1, 1);
    }
    private void wipeRequest() {
        for (TeleportRequest request : TeleportHandler.getTpaRequests()) {
            if (request == getInstance()) TeleportHandler.removeTPARequest(getInstance());
        }
        for (TeleportRequest request : TeleportHandler.getTpaHereRequests()) {
            if (request == getInstance()) TeleportHandler.removeTPAHereRequest(getInstance());
        }
    }
    public void cancel() {
        timer.cancel();
    }

    public ServerPlayerEntity getSource() {
        return source;
    }

    public ServerPlayerEntity getTarget() {
        return target;
    }
    private TeleportRequest getInstance() {
        return this;
    }
}
