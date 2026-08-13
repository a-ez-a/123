package com.example.belowzero.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityVoidMixin {

    @Shadow public abstract double getY();

    // Catch void damage check when entity Y coordinate is negative
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source == DamageSource.OUT_OF_WORLD && this.getY() < 0.0D && this.getY() >= -500.0D) {
            cir.setReturnValue(false);
        }
    }

    // Prevent tick kill below Y=0 down to -500
    @Inject(method = "tickInVoid", at = @At("HEAD"), cancellable = true)
    private void onTickInVoid(CallbackInfo ci) {
        if (this.getY() >= -500.0D) {
            ci.cancel();
        }
    }
}
