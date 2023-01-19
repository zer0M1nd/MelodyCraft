package hciu.pub.mcmod.hciusutils.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import hciu.pub.mcmod.hciusutils.gui.render.AbstractTextureDrawer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class SmartGuiScreen extends GuiScreen implements ISmartGuiComponent {

	private int sizeX, sizeY, relativeX, relativeY, actualX, actualY;
	private boolean visible = true;
	private List<ISmartGuiComponent> components;
	private SmartGuiScreen parent;
	protected ISmartGuiComponent holder;
	private ISmartGuiComponent focus = null;
	private AbstractTextureDrawer<SmartGuiScreen> texture;
	private boolean autoCenter = true;

	public SmartGuiScreen getParent() {
		return parent;
	}

	public void setTextureDrawer(AbstractTextureDrawer<SmartGuiScreen> drawer) {
		texture = drawer;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	protected void addComponent(ISmartGuiComponent comp) {
		components.add(comp);
	}

	public ISmartGuiComponent getFocus() {
		return focus;
	}

	public SmartGuiScreen(SmartGuiScreen parent, ISmartGuiComponent holder) {
		this.holder = holder;
		this.parent = parent;
		this.components = new ArrayList<ISmartGuiComponent>();
		texture = AbstractTextureDrawer.createEmpty(this);
	}

	@Override
	public void initGui() {
		this.onResizeAll();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (visible) {
			this.drawAll();
			List<String> tooltip = getTooltipAll();
			if (tooltip != null) {
				drawHoveringText(tooltip, mouseX, mouseY);
			}
		}
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
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		onKeyPressed(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		onMouseClicked(mouseX - actualX, mouseY - actualY, mouseButton);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		onMouseReleased(mouseX - actualX, mouseY - actualY, state);
	}

	@Override
	public void onKeyPressed(char typedChar, int keyCode) {
		if(getHolder() == null) {
			try {
				super.keyTyped(typedChar, keyCode);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (focus != null) {
			focus.onKeyPressed(typedChar, keyCode);
		}
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		focus = null;
		for (int i = components.size() - 1;i >= 0;i--) {
			ISmartGuiComponent c = components.get(i);
			if (c.checkMouse()) {
				focus = c;
				c.onMouseClicked(mouseX - c.getRelativeX(), mouseY - c.getRelativeY(), mouseButton);
				break;
			}
		}
	}

	@Override
	public void onMouseReleased(int mouseX, int mouseY, int state) {
		for (int i = components.size() - 1;i >= 0;i--) {
			ISmartGuiComponent c = components.get(i);
			if (c.checkMouse()) {
				c.onMouseReleased(mouseX - c.getRelativeX(), mouseY - c.getRelativeY(), state);
				break;
			}
		}
	}

	public void setAutoCenter(boolean autoCenter) {
		this.autoCenter = autoCenter;
	}

	@Override
	public ISmartGuiComponent getHolder() {
		return holder;
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

	private void updateActual() {
		if (getHolder() == null) {
			actualX = relativeX;
			actualY = relativeY;
		} else {
			actualX = relativeX + getHolder().getActualX();
			actualY = relativeY + getHolder().getActualY();
		}
	}

	public void setPos(int x, int y) {
		relativeX = x;
		relativeY = y;
		updateActual();
	}

	public void setX(int relativeX) {
		this.relativeX = relativeX;
		updateActual();
	}

	public void setY(int relativeY) {
		this.relativeY = relativeY;
		updateActual();
	}

	public void setSize(int x, int y) {
		this.sizeX = x;
		this.sizeY = y;
		if (autoCenter) {
			autoCenter();
		}
	}
	
	public void setCenter(int x,int y) {
		setPos(x - sizeX / 2, y - sizeY / 2);
	}
	
	public void setCenterSize(int x,int y,int sx,int sy) {
		setSize(sx, sy);
		setCenter(x, y);
	}

	public void setBounds(int x, int y, int sx, int sy) {
		setSize(sx, sy);
		setPos(x, y);
	}

	public void autoCenter() {
		int x = (width - sizeX) / 2;
		int y = (height - sizeY) / 2;
		setPos(x, y);
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
	public void handleKeyboardInput() throws IOException {
		super.handleKeyboardInput();
		char c0 = Keyboard.getEventCharacter();
		if (!Keyboard.getEventKeyState()) {
			this.onKeyReleased(c0, Keyboard.getEventKey());
		}
	}

	@Override
	public void onKeyReleased(char typedChar, int keyCode) {
		//System.out.println("Key released " + keyCode);
		if (focus != null) {
			focus.onKeyReleased(typedChar, keyCode);
		}
	}

	public static void drawScaledTexturedModelRect(int x, int y, int sx, int sy, int tx, int ty, int tsx, int tsy) {
		Minecraft mc = Minecraft.getMinecraft();
		double scaleX = (double) sx / (double) tsx;
		double scaleY = (double) sy / (double) tsy;
		GlStateManager.scale(scaleX, scaleY, 1.0);
		((SmartGuiScreen) mc.currentScreen).drawTexturedModalRect((float) (x / scaleX), (float) (y / scaleY),
				(float) tx, (float) ty, (float) tsx, (float) tsy);
		GlStateManager.scale(1 / scaleX, 1 / scaleY, 1.0);
	}

	public static void drawScaledTexturedModelRect(float x, float y, float sx, float sy, float tx, float ty, float tsx,
			float tsy) {
		Minecraft mc = Minecraft.getMinecraft();
		double scaleX = (double) sx / (double) tsx;
		double scaleY = (double) sy / (double) tsy;
		GlStateManager.scale(scaleX, scaleY, 1.0);
		((SmartGuiScreen) mc.currentScreen).drawTexturedModalRect((float) (x / scaleX), (float) (y / scaleY),
				(float) tx, (float) ty, (float) tsx, (float) tsy);
		GlStateManager.scale(1 / scaleX, 1 / scaleY, 1.0);
	}

	public void drawTexturedModalRect(float x, float y, float textureX, float textureY, float width, float height) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos((double) (x + 0), (double) (y + height), (double) this.zLevel)
				.tex((double) ((float) (textureX + 0) * 0.00390625F),
						(double) ((float) (textureY + height) * 0.00390625F))
				.endVertex();
		bufferbuilder.pos((double) (x + width), (double) (y + height), (double) this.zLevel)
				.tex((double) ((float) (textureX + width) * 0.00390625F),
						(double) ((float) (textureY + height) * 0.00390625F))
				.endVertex();
		bufferbuilder.pos((double) (x + width), (double) (y + 0), (double) this.zLevel)
				.tex((double) ((float) (textureX + width) * 0.00390625F),
						(double) ((float) (textureY + 0) * 0.00390625F))
				.endVertex();
		bufferbuilder.pos((double) (x + 0), (double) (y + 0), (double) this.zLevel)
				.tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F))
				.endVertex();
		tessellator.draw();
	}

	@Override
	public void setFocus(ISmartGuiComponent focus) {
		this.focus = focus;
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		for (ISmartGuiComponent c : components) {
			c.onGuiClosed();
		}
	}
}
