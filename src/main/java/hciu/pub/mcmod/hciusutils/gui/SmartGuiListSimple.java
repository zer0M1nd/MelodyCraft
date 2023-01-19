package hciu.pub.mcmod.hciusutils.gui;

import java.nio.channels.Selector;
import java.util.ArrayList;

import java.util.List;
import java.util.function.Function;

import javax.swing.text.StyleConstants.CharacterConstants;

import hciu.pub.mcmod.hciusutils.gui.render.FramedRectangleDrawer;
import hciu.pub.mcmod.hciusutils.gui.render.AbstractTextureDrawer;
import net.minecraft.client.Minecraft;

import static hciu.pub.mcmod.hciusutils.gui.SmartGuiConstants.*;

public class SmartGuiListSimple<T> extends SmartGuiComponentBase {

	private List<T> items;
	private int selected = -1;
	private int lineHeight = 14;
	private int buttonWidth = 14;
	private int boarderSize = 1;

	private SmartGuiButton buttonTop;
	private SmartGuiButton buttonUp;
	private SmartGuiButton buttonDown;
	private SmartGuiButton buttonBottom;

	private SmartGuiComponentBase selectionBackground;

	private Function<T, String> displayFunction = T::toString;

	public SmartGuiListSimple(ISmartGuiComponent holder) {
		super(holder);
		setTextureDrawer(new FramedRectangleDrawer<ISmartGuiComponent>(this, VANILLA_TEXTBOX_COLOR_FRAME,
				VANILLA_TEXTBOX_COLOR_INSIDE, 1));
		items = new ArrayList<>();
		addComponent(buttonTop = new SmartGuiButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				setSelected(Math.max(0, selected - (getHolder().getSizeY() - boarderSize * 2) / lineHeight));
				;
			}
		});
		addComponent(buttonUp = new SmartGuiButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				setSelected(Math.max(0, selected - 1));
				;
			}
		});
		addComponent(buttonDown = new SmartGuiButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				setSelected(Math.min(items.size() - 1, selected + 1));
				;
			}
		});
		addComponent(buttonBottom = new SmartGuiButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				setSelected(
						Math.min(items.size() - 1, selected + (getHolder().getSizeY() - boarderSize * 2) / lineHeight));
			}
		});
		addComponent(selectionBackground = new SmartGuiComponentBase(this));
		selectionBackground.setVisible(false);
		selectionBackground.setTextureDrawer(new FramedRectangleDrawer<ISmartGuiComponent>(this,
				VANILLA_TEXTBOX_COLOR_FRAME, VANILLA_TEXTBOX_COLOR_INSIDE, 1));
		setTextureDrawer(new FramedRectangleDrawer<ISmartGuiComponent>(this, VANILLA_TEXTBOX_COLOR_FRAME,
				VANILLA_TEXTBOX_COLOR_INSIDE, 1));
	}

	@Override
	public void onResizeSelf() {
		int y2 = sizeY / 2;
		int y1 = y2 / 2, y3 = sizeY - y1;
		buttonTop.setBounds(getSizeX() - buttonWidth, 0, buttonWidth, y1);
		buttonUp.setBounds(getSizeX() - buttonWidth, y1, buttonWidth, y2 - y1);
		buttonDown.setBounds(getSizeX() - buttonWidth, y2, buttonWidth, y3 - y2);
		buttonBottom.setBounds(getSizeX() - buttonWidth, y3, buttonWidth, sizeY - y3);
		selectionBackground.setBounds(boarderSize, boarderSize, sizeX - boarderSize - buttonWidth, lineHeight);
	}

	public void setButtonTextureDrawer(Function<SmartGuiButton, AbstractTextureDrawer<?>> drawers) {
		buttonTop.setTextureDrawer(drawers.apply(buttonTop));
		buttonUp.setTextureDrawer(drawers.apply(buttonUp));
		buttonDown.setTextureDrawer(drawers.apply(buttonDown));
		buttonBottom.setTextureDrawer(drawers.apply(buttonBottom));
	}

	public void setButtonTexts(String[] buttonTexts) {
		buttonTop.setText(buttonTexts[0]);
		buttonUp.setText(buttonTexts[1]);
		buttonDown.setText(buttonTexts[2]);
		buttonBottom.setText(buttonTexts[3]);
	}

	public void setBackgroundDrawer(Function<SmartGuiComponentBase, AbstractTextureDrawer<?>> drawer) {
		selectionBackground.setTextureDrawer(drawer.apply(selectionBackground));
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
		validateSelected();
		onSelectionChanged();
	}

	public void validateSelected() {
		int old = selected;
		if (selected > items.size() - 1) {
			selected = items.size() - 1;
		} else if (selected < 0) {
			selected = 0;
		}
	}

	@Override
	public void drawSelf() {
		super.drawSelf();
		if (items.size() == 0) {
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		int maxlines = (sizeY - boarderSize * 2) / lineHeight;
		int from = 0, to = -1;
		if (items.size() <= maxlines) {
			from = 0;
			to = items.size() - 1;
		} else {
			from = selected - (maxlines / 2);
			if (from < 0) {
				from = 0;
			} else if (from + maxlines - 1 > items.size() - 1) {
				from = items.size() - maxlines;
			}
			to = from + maxlines - 1;
		}
		selectionBackground.setVisible(true);
		selectionBackground.setY(boarderSize + (selected - from) * lineHeight);
		selectionBackground.drawAll();
		selectionBackground.setVisible(false);
		for (int i = from; i <= to; i++) {
			mc.fontRenderer.drawStringWithShadow(
					mc.fontRenderer.trimStringToWidth(
							displayFunction.apply(items.get(i)), sizeX - boarderSize * 2 - 4 - buttonWidth),
					actualX + boarderSize + 2,
					actualY + boarderSize + (i - from) * lineHeight + boarderSize
							+ (lineHeight - boarderSize * 2 - mc.fontRenderer.FONT_HEIGHT) / 2,
					SmartGuiConstants.VANILLA_TEXT_COLOR_ENABLED);
		}
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		int old = this.selected;
		this.selected = selected;
		validateSelected();
		if (old != selected) {
			onSelectionChanged();
		}
	}

	public int getLineHeight() {
		return lineHeight;
	}

	public int getButtonWidth() {
		return buttonWidth;
	}

	public int getBoarderSize() {
		return boarderSize;
	}

	public void setDisplayFunction(Function<T, String> displayFunction) {
		this.displayFunction = displayFunction;
	}

	public void setLineHeight(int lineHeight) {
		this.lineHeight = lineHeight;
	}

	public void setButtonWidth(int buttonWidth) {
		this.buttonWidth = buttonWidth;
	}

	public void setBoarderSize(int boarderSize) {
		this.boarderSize = boarderSize;
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.onMouseClicked(mouseX, mouseY, mouseButton);
		if (mouseX >= boarderSize && mouseX < sizeX - buttonWidth && mouseY >= boarderSize
				&& mouseY < sizeY - boarderSize) {
			int ydiff = (mouseY - selectionBackground.getRelativeY()) / lineHeight;
			if (mouseY < selectionBackground.getRelativeY()) {
				ydiff = -(selectionBackground.getRelativeY() - mouseY + lineHeight - 1) / lineHeight;
			}
			if (selected + ydiff >= 0 && selected + ydiff < items.size()) {
				setSelected(selected + ydiff);
			}
		}
	}

	public T getSelectedItem() {
		if (items.size() == 0) {
			return null;
		}
		return items.get(selected);
	}

	public void onSelectionChanged() {

	}
}
