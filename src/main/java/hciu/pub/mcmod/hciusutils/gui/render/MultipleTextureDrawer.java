package hciu.pub.mcmod.hciusutils.gui.render;

import hciu.pub.mcmod.hciusutils.gui.ISmartGuiComponent;
import net.minecraft.util.ResourceLocation;

public class MultipleTextureDrawer<T extends ISmartGuiComponent> extends AbstractTextureDrawer<T> {

	public MultipleTextureDrawer(T obj, AbstractTextureDrawer<T>[] subs) {
		super(obj);
		this.subs = subs;
	}

	protected int getModifier() {
		return 0;
	}

	private AbstractTextureDrawer<T>[] subs;

	@Override
	public void draw() {
		subs[getModifier()].draw();
	}

	public static <T extends ISmartGuiComponent> AbstractTextureDrawer<T>[] makeFlexibleSubs(T obj, int count,
			ResourceLocation texture, int u, int v, int sx, int sy, int shiftX, int shiftY) {
		AbstractTextureDrawer<?>[] subs = new AbstractTextureDrawer<?>[count];
		for (int i = 0; i < count; i++) {
			subs[i] = new FlexibleTextureDrawer<T>(obj, texture, u + shiftX * i, v + shiftY * i, sx, sy);
		}
		return (AbstractTextureDrawer<T>[]) subs;
	}

	public static <T extends ISmartGuiComponent> AbstractTextureDrawer<T>[] makeRectangleSubs(T obj, int count,
			int[] outer, int[] inner, int boarderWidth) {
		AbstractTextureDrawer<?>[] subs = new AbstractTextureDrawer<?>[count];
		for (int i = 0; i < count; i++) {
			subs[i] = new FramedRectangleDrawer<ISmartGuiComponent>(obj, outer[i], inner[i], boarderWidth);
		}
		return (AbstractTextureDrawer<T>[]) subs;
	}
}
