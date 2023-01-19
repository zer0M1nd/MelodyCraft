package hciu.pub.mcmod.hciusutils.gui;

import java.util.List;

import org.lwjgl.input.Mouse;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;

/**
 * 
 * CZM的高级GUI组件系统的基类，用来避开MC复杂且蛋疼的GUI系统。<br>
 * 由于这套系统是建立在魔改MC的基础再加上java不支持多重继承，我不得已写了一个接口并在每个基类里实现它。<br>
 * 不要手动实现这个接口！！！！<br>
 * 如果你想要拓展一种GUI组件的功能（例如按钮，文本框），请直接继承对应的类<br>
 * 如果你想要添加一种新的GUI组件（例如音乐播放器），请继承{@link SmartGuiComponentBase}类<br>
 * 如果你想要写一个客户端GUI窗口（当然，你需要手动实现相关网络通信），请继承{@link SmartGuiScreen}类<br>
 * 如果你想要写一个具有默认服务端交互机制的GUI窗口（例如物品交互），请继承SmartGuiContainer类（还没写，咕~）
 * 
 * @author Herobrine_CZM
 *
 */
public interface ISmartGuiComponent {

	/**
	 * 渲染这个GUI组件及其所有的子组件。<br>
	 * 在每次MC渲染时由其容器组件调用，如果本身是最高级GUI则由MC调用。<br>
	 * 仅当 {@link ISmartGuiComponent#isVisible()}返回true时调用，否则不调用。<br>
	 */
	public void drawAll();

	/**
	 * 渲染这个GUI组件本身。<br>
	 * 由{@link ISmartGuiComponent#drawAll()}调用。<br>
	 */
	public void drawSelf();

	/**
	 * 当这个GUI组件被容器组件设置为焦点，并且有键盘按键按下时由容器组件调用，如果本身是最高级GUI则由MC调用。
	 */
	default public void onKeyPressed(char typedChar, int keyCode) {
	};

	default public void onKeyReleased(char typedChar, int keyCode) {
	};

	/**
	 * 当这个GUI组件被点击时由容器组件调用，如果本身是最高级GUI则由MC调用。
	 */
	default public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
	};

	/**
	 * 当这个GUI组件在鼠标点击并释放后时由容器组件调用，如果本身是最高级GUI则由MC调用。
	 */
	default public void onMouseReleased(int mouseX, int mouseY, int state) {
	};

	/**
	 * 获取该组件的容器组件，如果没有则返回null
	 */
	public ISmartGuiComponent getHolder();

	/**
	 * 获取目前的最高级GUI，即直接由MC渲染的GUI。该GUI必须存在且继承自{@link SmartGuiScreen}，否则返回null。
	 */
	default public SmartGuiScreen getSupreme() {
		if (Minecraft.getMinecraft().currentScreen instanceof SmartGuiScreen) {
			return (SmartGuiScreen) Minecraft.getMinecraft().currentScreen;
		}
		return null;
	}

	/**
	 * 获取该GUI组件左上角相对于MC窗口，经MC缩放之后的位置的X坐标。
	 */
	public int getActualX();

	/**
	 * 获取该GUI组件左上角相对于MC窗口，经MC缩放之后的位置的Y坐标。
	 */
	public int getActualY();

	/**
	 * 获取该GUI组件经MC缩放之后的X轴方向（横向）宽度。
	 */
	public int getSizeX();

	/**
	 * 获取该GUI组件经MC缩放之后的Y轴方向（纵向）宽度。
	 */
	public int getSizeY();

	/**
	 * 获取该GUI组件左上角相对于其容器组件（如果没有则是MC窗口），经MC缩放之后的位置的X坐标。
	 */
	public int getRelativeX();

	/**
	 * 获取该GUI组件左上角相对于其容器组件（如果没有则是MC窗口），经MC缩放之后的位置的Y坐标。
	 */
	public int getRelativeY();

	/**
	 * 获取当前鼠标的位置相对于该GUI组件，经MC缩放之后的X坐标。
	 */
	public default int getMouseX() {
		return Mouse.getX() * getSupreme().width / Minecraft.getMinecraft().displayWidth - getActualX();
	}

	/**
	 * 获取当前鼠标的位置相对于该GUI组件，经MC缩放之后的Y坐标。
	 */
	public default int getMouseY() {
		return getSupreme().height - Mouse.getY() * getSupreme().height / Minecraft.getMinecraft().displayHeight
				- getActualY();
	}

	/**
	 * 返回当前鼠标是否在该容器内。
	 */
	public default boolean checkMouse() {
		int x = getMouseX(), y = getMouseY();
		return x >= 0 && x < getSizeX() && y >= 0 && y < getSizeY();
	}

	/**
	 * 获取该GUI组件当前设置为焦点的子组件，如果没有则返回null。
	 */
	public ISmartGuiComponent getFocus();

	public void setFocus(ISmartGuiComponent focus);
	
	/**
	 * 获取该GUI组件当前是否被设置为可见。
	 */
	public boolean isVisible();

	/**
	 * 当MC窗口被重新调整大小之后调用。 特殊地，在窗口刚被创建的时候会被调用一次。
	 */
	public void onResizeAll();

	/**
	 * 当MC窗口被重新调整大小之后由{@link ISmartGuiComponent#onResizeAll()}调用。<br>
	 * 在这个方法里，你应该设置当前GUI组件及其子组件的位置和大小。<br>
	 */
	default public void onResizeSelf() {
	}

	/**
	 * 将当前窗口中以比例形式表示的X坐标转化为像素<br>
	 */
	public default int ratioX(double ratio) {
		return (int) (getSizeX() * ratio);
	}

	/**
	 * 将当前窗口中以比例形式表示的Y坐标转化为像素<br>
	 */
	public default int ratioY(double ratio) {
		return (int) (getSizeY() * ratio);
	}

	public default List<String> getTooltipSelf() {
		return null;
	}

	public List<String> getTooltipAll();
	
	public void onGuiClosed();
}
