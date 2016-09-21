package growthcraft.cellar.eventhandler;

import growthcraft.cellar.GrowthCraftCellar;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerLivingUpdateEventCellar
{
	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event)
	{
		final EntityLivingBase ent = event.entityLiving;

		if (ent.isPotionActive(GrowthCraftCellar.potionTipsy))
		{
			if (ent.getActivePotionEffect(GrowthCraftCellar.potionTipsy).getDuration() == 0)
			{
				ent.removePotionEffect(GrowthCraftCellar.potionTipsy);
				return;
			}

			final int lvl = ent.getActivePotionEffect(GrowthCraftCellar.potionTipsy).getAmplifier();

			if (lvl >= 3)
			{
				ent.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0));

				if (lvl >= 4)
				{
					ent.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 0));
				}
			}
		}
	}
}
