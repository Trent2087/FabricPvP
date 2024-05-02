package mod.trent2087.fabricpvp.mixin;

import mod.trent2087.fabricpvp.PlayerComponentInitializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "shouldDamagePlayer", at = @At("HEAD"), cancellable = true)
    private void testDamagePlayer(PlayerEntity player, CallbackInfoReturnable<Boolean> ci) {
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;

        ci.setReturnValue(PlayerComponentInitializer.PVP_DATA.get(self).isOn() && PlayerComponentInitializer.PVP_DATA.get(player).isOn());
    }
}
