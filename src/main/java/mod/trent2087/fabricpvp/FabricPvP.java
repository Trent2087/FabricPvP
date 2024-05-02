package mod.trent2087.fabricpvp;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import java.util.Collection;
import java.util.Collections;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class FabricPvP implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("pvp")
                    .executes(this::togglePvP)
                    .then(literal("on")
                            .executes(ctx -> this.setPvP(ctx, true))
                            .then(argument("players", EntityArgumentType.players()).requires(req -> req.hasPermissionLevel(2))
                                    .executes(ctx -> this.setPvP(ctx, true, EntityArgumentType.getPlayers(ctx, "players")))))
                    .then(literal("off")
                            .executes(ctx -> this.setPvP(ctx, false))
                            .then(argument("players", EntityArgumentType.players()).requires(req -> req.hasPermissionLevel(2))
                                    .executes(ctx -> this.setPvP(ctx, false, EntityArgumentType.getPlayers(ctx, "players"))))));
        });
    }

    private int setPvP(CommandContext<ServerCommandSource> ctx, boolean b) throws CommandSyntaxException {
        return setPvP(ctx, b, Collections.singletonList(ctx.getSource().getPlayerOrThrow()));
    }
    private int setPvP(CommandContext<ServerCommandSource> ctx, boolean b, Collection<ServerPlayerEntity> players) {
        players.forEach(v -> {
            PlayerComponentInitializer.PVP_DATA.get(v).set(b);
        });
        return 1;
    }

    private int togglePvP(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        PlayerComponentInitializer.IPvPComponent pvpComponent = PlayerComponentInitializer.PVP_DATA.get(context.getSource().getPlayerOrThrow());
        pvpComponent.set(!pvpComponent.isOn());
        context.getSource().sendMessage(Text.literal("").append(Text.literal("PVP status is - ")).append(pvpComponent.isOn() ? "enabled" : "disabled"));
        return 1;
    }
}
