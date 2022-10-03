/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.wurstclient.WurstClient;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin
{
	@Redirect(at = @At(value = "INVOKE",
			target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z",
			ordinal = 0),
			method = "render(Lnet/minecraft/client/render/Camera;FLnet/minecraft/client/world/ClientWorld;IF)V")
	private static boolean hasStatusEffectRender(LivingEntity entity,
												 StatusEffect effect)
	{
		if(effect == StatusEffects.BLINDNESS
				&& WurstClient.INSTANCE.getHax().antiBlindHack.isEnabled())
			return false;

		return entity.hasStatusEffect(effect);
	}


	@Inject(method = "getFogModifier", at = @At("HEAD"), cancellable = true)
	private static void getForModified(Entity entity, float tickDelta, CallbackInfoReturnable cir) {
		if (WurstClient.INSTANCE.getHax().antiBlindHack.isEnabled()) {
			cir.setReturnValue(null);
		}
	}
}