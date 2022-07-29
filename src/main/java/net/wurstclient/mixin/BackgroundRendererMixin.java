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
import net.minecraft.client.render.BackgroundRenderer.StatusEffectFogModifier;
import net.minecraft.entity.Entity;
import net.wurstclient.WurstClient;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin
{
	@Inject(at = {@At("HEAD")},
		method = {
			"getFogModifier(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/client/render/BackgroundRenderer$StatusEffectFogModifier;"},
		cancellable = true)
	private static void onGetFogModifier(Entity entity, float tickDelta,
		CallbackInfoReturnable<StatusEffectFogModifier> ci)
	{
		if(effect == StatusEffects.BLINDNESS
			&& WurstClient.INSTANCE.getHax().antiBlindHack.isEnabled())
			cir.setReturnValue(false);
		
		return entity.hasStatusEffect(effect);
	}
	

	@Inject(method = "getFogModifier", at = @At("HEAD"), cancellable = true)
	private static void getForModified(Entity entity, float tickDelta, CallbackInfoReturnable cir) {
		if (WurstClient.INSTANCE.getHax().antiBlindHack.isEnabled()) {
			cir.setReturnValue(null);
		}
	}
}
