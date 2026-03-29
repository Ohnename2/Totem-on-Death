package ohne.name.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import ohne.name.TotemPlayerHandle;

public class RespawnCommand {
    public static void initialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("respawn").requires(commandSourceStack -> commandSourceStack.permissions().hasPermission(Permissions.COMMANDS_ADMIN)).executes(context -> {
                ServerPlayer player = context.getSource().getPlayer();
                if(player == null) {
                    context.getSource().sendFailure(Component.translatable("text.totem-on-death.command.respawn.fail.noplayer"));
                    return 1;
                }
                if(!TotemPlayerHandle.IsTotemDead(player)) {
                    context.getSource().sendFailure(Component.translatable("text.totem-on-death.command.respawn.fail.notdead"));
                    return 1;
                }
                context.getSource().sendSuccess(() -> Component.translatable("text.totem-on-death.command.respawn.success"), false);
                TotemPlayerHandle.getPlayerHandle(player).Respawn();
                return 1;
            }));
        });
    }
}
