package hciu.pub.mcmod.melodycraft.client;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventRender {

	private boolean renderOff = false;

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onRenderLiving(RenderLivingEvent.Pre<EntityLivingBase> event) {
		if (renderOff) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onRenderBlocks(RenderWorldLastEvent event) {
		if (renderOff) {
			event.setCanceled(true);
		}
	}

	public void setRenderOff() {
		this.renderOff = true;
	}

	public void setRenderOn() {
		this.renderOff = false;
	}
}
