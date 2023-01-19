package hciu.pub.mcmod.hciusutils.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hciu.pub.mcmod.hciusutils.gui.render.AbstractTextureDrawer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class SmartGuiComponentBase extends Gui implements ISmartGuiComponent {

	protected int sizeX, sizeY, relativeX, relativeY, actualX, actualY;
	protected boolean visible = true;
	protected ISmartGuiComponent holder;
	protected AbstractTextureDrawer<?> texture;
	private ISmartGuiComponent focus = null;
	private List<ISmartGuiComponent> components;

	public SmartGuiComponentBase(ISmartGuiComponent holder) {
		this.holder = holder;
		texture = AbstractTextureDrawer.createEmpty(this);
		components = new ArrayList<>();
	}

	@Override
	public void setFocus(ISmartGuiComponent focus) {
		this.focus = focus;
	}

	protected void addComponent(ISmartGuiComponent comp) {
		components.add(comp);
	}

	public void setTextureDrawer(AbstractTextureDrawer<?> drawer) {
		texture = drawer;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public int getSizeX() {
		return sizeX;
	}

	@Override
	public int getSizeY() {
		return sizeY;
	}

	@Override
	public int getActualX() {
		return actualX;
	}

	@Override
	public int getActualY() {
		return actualY;
	}

	@Override
	public int getRelativeX() {
		return relativeX;
	}

	@Override
	public int getRelativeY() {
		return relativeY;
	}

	@Override
	public void drawSelf() {
		texture.draw();
	}

	@Override
	public void drawAll() {
		drawSelf();
		for (ISmartGuiComponent c : components) {
			if (c.isVisible()) {
				c.drawAll();
			}
		}
	}

	@Override
	public void onKeyPressed(char typedChar, int keyCode) {
		if (focus != null) {
			focus.onKeyPressed(typedChar, keyCode);
		}
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		focus = null;
		for (int i = components.size() - 1; i >= 0; i--) {
			ISmartGuiComponent c = components.get(i);
			if (c.checkMouse()) {
				focus = c;
				c.onMouseClicked(mouseX - c.getRelativeX(), mouseY - c.getRelativeY(), mouseButton);
				break;
			}
		}
	}

	@Override
	public ISmartGuiComponent getHolder() {
		return holder;
	}

	@Override
	public ISmartGuiComponent getFocus() {
		return focus;
	}

	private void updateActual() {
		if (getHolder() == null) {
			actualX = relativeX;
			actualY = relativeY;
		} else {
			actualX = relativeX + getHolder().getActualX();
			actualY = relativeY + getHolder().getActualY();
		}
	}

	public void setX(int relativeX) {
		this.relativeX = relativeX;
		updateActual();
	}

	public void setY(int relativeY) {
		this.relativeY = relativeY;
		updateActual();
	}

	public void setPos(int x, int y) {
		relativeX = x;
		relativeY = y;
		updateActual();
	}

	public void setSize(int x, int y) {
		this.sizeX = x;
		this.sizeY = y;
	}

	public void setBounds(int x, int y, int sx, int sy) {
		setSize(sx, sy);
		setPos(x, y);
	}

	public void setCorners(int x, int y, int x1, int y1) {
		setSize(x1 - x, y1 - y);
		setPos(x, y);
	}

	public void setCenter(int x, int y) {
		setPos(x - sizeX / 2, y - sizeY / 2);
	}

	public void setCenterSize(int x, int y, int sx, int sy) {
		setSize(sx, sy);
		setCenter(x, y);
	}

	@Override
	public void onResizeAll() {
		onResizeSelf();
		for (ISmartGuiComponent c : components) {
			c.onResizeAll();
		}
	}

	@Override
	public List<String> getTooltipAll() {
		for (ISmartGuiComponent c : components) {
			List<String> t = c.getTooltipAll();
			if (t != null) {
				return t;
			}
		}
		return getTooltipSelf();
	}

	@Override
	public void onKeyReleased(char typedChar, int keyCode) {
		if (focus != null) {
			focus.onKeyReleased(typedChar, keyCode);
		}
	}

	@Override
	public void onGuiClosed() {
		for (ISmartGuiComponent c : components) {
			c.onGuiClosed();
		}
	}

}
