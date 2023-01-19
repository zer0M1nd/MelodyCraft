package hciu.pub.mcmod.hciusutils.gui;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import hciu.pub.mcmod.hciusutils.gui.render.FramedRectangleDrawer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;

import static hciu.pub.mcmod.hciusutils.gui.SmartGuiConstants.*;

public class SmartGuiTextField extends SmartGuiComponentBase {

	private String text = "", defaultText = "";
	private int maxLength = 32;
	private int cursorCounter = 0;
	private Predicate<String> validator = Predicates.<String>alwaysTrue();
	private int cursorPosition = 0;
	private int selectionEnd = 0;
	private boolean isEnabled = true;
	private int lineScrollOffset = 0;
	private int enabledColor = VANILLA_TEXT_COLOR_ENABLED;
	private int disabledColor = VANILLA_TEXT_COLOR_DISABLED;

	public SmartGuiTextField(ISmartGuiComponent holder) {
		super(holder);
		setTextureDrawer(new FramedRectangleDrawer<ISmartGuiComponent>(this, VANILLA_TEXTBOX_COLOR_FRAME,
				VANILLA_TEXTBOX_COLOR_INSIDE, 1));
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public String getText() {
		return text;
	}

	public String getDefaultText() {
		return defaultText;
	}

	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
	}

	public int getMaxLength() {
		return maxLength;
	}

	/**
	 * Increments the cursor counter
	 */
	public void updateCursorCounter() {
		++this.cursorCounter;
	}

	/**
	 * Sets the text of the textbox, and moves the cursor to the end.
	 */
	public void setText(String textIn) {
		if (this.validator.apply(textIn)) {
			if (textIn.length() > this.maxLength) {
				this.text = textIn.substring(0, this.maxLength);
			} else {
				this.text = textIn;
			}

			this.setCursorPositionEnd();
		}
	}

	/**
	 * returns the text between the cursor and selectionEnd
	 */
	public String getSelectedText() {
		int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
		int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
		return this.text.substring(i, j);
	}

	public void setValidator(Predicate<String> theValidator) {
		this.validator = theValidator;
	}

	/**
	 * Adds the given text after the cursor, or replaces the currently selected text
	 * if there is a selection.
	 */
	public void writeText(String textToWrite) {
		String s = "";
		String s1 = ChatAllowedCharacters.filterAllowedCharacters(textToWrite);
		int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
		int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
		int k = this.maxLength - this.text.length() - (i - j);

		if (!this.text.isEmpty()) {
			s = s + this.text.substring(0, i);
		}

		int l;

		if (k < s1.length()) {
			s = s + s1.substring(0, k);
			l = k;
		} else {
			s = s + s1;
			l = s1.length();
		}

		if (!this.text.isEmpty() && j < this.text.length()) {
			s = s + this.text.substring(j);
		}

		if (this.validator.apply(s)) {
			this.text = s;
			this.moveCursorBy(i - this.selectionEnd + l);
		}
	}

	/**
	 * Deletes the given number of words from the current cursor's position, unless
	 * there is currently a selection, in which case the selection is deleted
	 * instead.
	 */
	public void deleteWords(int num) {
		if (!this.text.isEmpty()) {
			if (this.selectionEnd != this.cursorPosition) {
				this.writeText("");
			} else {
				this.deleteFromCursor(this.getNthWordFromCursor(num) - this.cursorPosition);
			}
		}
	}

	/**
	 * Deletes the given number of characters from the current cursor's position,
	 * unless there is currently a selection, in which case the selection is deleted
	 * instead.
	 */
	public void deleteFromCursor(int num) {
		if (!this.text.isEmpty()) {
			if (this.selectionEnd != this.cursorPosition) {
				this.writeText("");
			} else {
				boolean flag = num < 0;
				int i = flag ? this.cursorPosition + num : this.cursorPosition;
				int j = flag ? this.cursorPosition : this.cursorPosition + num;
				String s = "";

				if (i >= 0) {
					s = this.text.substring(0, i);
				}

				if (j < this.text.length()) {
					s = s + this.text.substring(j);
				}

				if (this.validator.apply(s)) {
					this.text = s;

					if (flag) {
						this.moveCursorBy(num);
					}

				}
			}
		}
	}

	/**
	 * Gets the starting index of the word at the specified number of words away
	 * from the cursor position.
	 */
	public int getNthWordFromCursor(int numWords) {
		return this.getNthWordFromPos(numWords, this.getCursorPosition());
	}

	/**
	 * Gets the starting index of the word at a distance of the specified number of
	 * words away from the given position.
	 */
	public int getNthWordFromPos(int n, int pos) {
		return this.getNthWordFromPosWS(n, pos, true);
	}

	/**
	 * Like getNthWordFromPos (which wraps this), but adds option for skipping
	 * consecutive spaces
	 */
	public int getNthWordFromPosWS(int n, int pos, boolean skipWs) {
		int i = pos;
		boolean flag = n < 0;
		int j = Math.abs(n);

		for (int k = 0; k < j; ++k) {
			if (!flag) {
				int l = this.text.length();
				i = this.text.indexOf(32, i);

				if (i == -1) {
					i = l;
				} else {
					while (skipWs && i < l && this.text.charAt(i) == ' ') {
						++i;
					}
				}
			} else {
				while (skipWs && i > 0 && this.text.charAt(i - 1) == ' ') {
					--i;
				}

				while (i > 0 && this.text.charAt(i - 1) != ' ') {
					--i;
				}
			}
		}

		return i;
	}

	/**
	 * Moves the text cursor by a specified number of characters and clears the
	 * selection
	 */
	public void moveCursorBy(int num) {
		this.setCursorPosition(this.selectionEnd + num);
	}

	/**
	 * Sets the current position of the cursor.
	 */
	public void setCursorPosition(int pos) {
		this.cursorPosition = pos;
		int i = this.text.length();
		this.cursorPosition = MathHelper.clamp(this.cursorPosition, 0, i);
		this.setSelectionPos(this.cursorPosition);
	}

	/**
	 * Moves the cursor to the very start of this text box.
	 */
	public void setCursorPositionZero() {
		this.setCursorPosition(0);
	}

	/**
	 * Moves the cursor to the very end of this text box.
	 */
	public void setCursorPositionEnd() {
		this.setCursorPosition(this.text.length());
	}

	/**
	 * Call this method from your GuiScreen to process the keys into the textbox
	 */
	public void onKeyPressed(char typedChar, int keyCode) {
		if (GuiScreen.isKeyComboCtrlA(keyCode)) {
			this.setCursorPositionEnd();
			this.setSelectionPos(0);
		} else if (GuiScreen.isKeyComboCtrlC(keyCode)) {
			GuiScreen.setClipboardString(this.getSelectedText());
		} else if (GuiScreen.isKeyComboCtrlV(keyCode)) {
			if (this.isEnabled) {
				this.writeText(GuiScreen.getClipboardString());
			}

		} else if (GuiScreen.isKeyComboCtrlX(keyCode)) {
			GuiScreen.setClipboardString(this.getSelectedText());

			if (this.isEnabled) {
				this.writeText("");
			}

		} else {
			switch (keyCode) {
			case 14:

				if (GuiScreen.isCtrlKeyDown()) {
					if (this.isEnabled) {
						this.deleteWords(-1);
					}
				} else if (this.isEnabled) {
					this.deleteFromCursor(-1);
				}

				return;
			case 199:

				if (GuiScreen.isShiftKeyDown()) {
					this.setSelectionPos(0);
				} else {
					this.setCursorPositionZero();
				}

				return;
			case 203:

				if (GuiScreen.isShiftKeyDown()) {
					if (GuiScreen.isCtrlKeyDown()) {
						this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
					} else {
						this.setSelectionPos(this.getSelectionEnd() - 1);
					}
				} else if (GuiScreen.isCtrlKeyDown()) {
					this.setCursorPosition(this.getNthWordFromCursor(-1));
				} else {
					this.moveCursorBy(-1);
				}

				return;
			case 205:

				if (GuiScreen.isShiftKeyDown()) {
					if (GuiScreen.isCtrlKeyDown()) {
						this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
					} else {
						this.setSelectionPos(this.getSelectionEnd() + 1);
					}
				} else if (GuiScreen.isCtrlKeyDown()) {
					this.setCursorPosition(this.getNthWordFromCursor(1));
				} else {
					this.moveCursorBy(1);
				}

				return;
			case 207:

				if (GuiScreen.isShiftKeyDown()) {
					this.setSelectionPos(this.text.length());
				} else {
					this.setCursorPositionEnd();
				}

				return;
			case 211:

				if (GuiScreen.isCtrlKeyDown()) {
					if (this.isEnabled) {
						this.deleteWords(1);
					}
				} else if (this.isEnabled) {
					this.deleteFromCursor(1);
				}

				return;
			default:

				if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
					if (this.isEnabled) {
						this.writeText(Character.toString(typedChar));
					}

					return;
				}
			}
		}
	}

	/**
	 * Called when mouse is clicked, regardless as to whether it is over this button
	 * or not.
	 */
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0) {
			int i = mouseX;

			i -= 4;

			Minecraft mc = Minecraft.getMinecraft();
			String s = mc.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.sizeX - 8);
			this.setCursorPosition(mc.fontRenderer.trimStringToWidth(s, i).length() + this.lineScrollOffset);
		}
	}

	public void drawSelf() {
		super.drawSelf();

		int i = this.isEnabled ? enabledColor : disabledColor;

		String text = this.text;
		boolean nothing = false;

		if (text.length() == 0) {
			text = defaultText;
			i = disabledColor;
			nothing = true;
		}

		updateCursorCounter();

		Minecraft mc = Minecraft.getMinecraft();
		int j = this.cursorPosition - this.lineScrollOffset;
		int k = this.selectionEnd - this.lineScrollOffset;
		String s = mc.fontRenderer.trimStringToWidth(text.substring(this.lineScrollOffset), this.sizeX - 8);
		boolean flag = j >= 0 && j <= s.length();
		int l = this.actualX + 4;
		int i1 = this.actualY + (this.sizeY - 8) / 2;
		int j1 = l;

		if (k > s.length()) {
			k = s.length();
		}

		if (!s.isEmpty()) {
			String s1 = flag ? s.substring(0, j) : s;
			j1 = mc.fontRenderer.drawStringWithShadow(s1, (float) l, (float) i1, i);
		}

		boolean flag2 = this.cursorPosition < text.length() || text.length() >= this.getMaxStringLength();
		int k1 = j1;

		if (!flag) {
			k1 = j > 0 ? l + this.sizeY : l;
		} else if (flag2) {
			k1 = j1 - 1;
			--j1;
		}

		if (!s.isEmpty() && flag && j < s.length()) {
			j1 = mc.fontRenderer.drawStringWithShadow(s.substring(j), (float) j1, (float) i1, i);
		}

		if (getHolder().getFocus() == this) {
			if (flag2 && !nothing) {
				Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + mc.fontRenderer.FONT_HEIGHT, -3092272);
			} else {
				mc.fontRenderer.drawStringWithShadow("_", (float) k1, (float) i1, enabledColor);
			}
		}

		if (k != j) {
			int l1 = l + mc.fontRenderer.getStringWidth(s.substring(0, k));
			this.drawSelectionBox(k1, i1 - 1, l1 - 1, i1 + 1 + mc.fontRenderer.FONT_HEIGHT);
		}

	}

	/**
	 * Draws the blue selection box.
	 */
	private void drawSelectionBox(int startX, int startY, int endX, int endY) {
		if (startX < endX) {
			int i = startX;
			startX = endX;
			endX = i;
		}

		if (startY < endY) {
			int j = startY;
			startY = endY;
			endY = j;
		}

		if (endX > this.actualX + this.sizeX) {
			endX = this.actualX + this.sizeX;
		}

		if (startX > this.actualX + this.sizeX) {
			startX = this.actualX + this.sizeX;
		}

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.enableColorLogic();
		GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.pos((double) startX, (double) endY, 0.0D).endVertex();
		bufferbuilder.pos((double) endX, (double) endY, 0.0D).endVertex();
		bufferbuilder.pos((double) endX, (double) startY, 0.0D).endVertex();
		bufferbuilder.pos((double) startX, (double) startY, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.disableColorLogic();
		GlStateManager.enableTexture2D();
	}

	/**
	 * Sets the maximum length for the text in this text box. If the current text is
	 * longer than this length, the current text will be trimmed.
	 */
	public void setMaxStringLength(int length) {
		this.maxLength = length;

		if (this.text.length() > length) {
			this.text = this.text.substring(0, length);
		}
	}

	/**
	 * returns the maximum number of character that can be contained in this textbox
	 */
	public int getMaxStringLength() {
		return this.maxLength;
	}

	/**
	 * returns the current position of the cursor
	 */
	public int getCursorPosition() {
		return this.cursorPosition;
	}

	/**
	 * Sets the color to use when drawing this text box's text. A different color is
	 * used if this text box is disabled.
	 */
	public void setTextColor(int color) {
		this.enabledColor = color;
	}

	/**
	 * Sets the color to use for text in this text box when this text box is
	 * disabled.
	 */
	public void setDisabledTextColour(int color) {
		this.disabledColor = color;
	}

	public void setEnabled(boolean enabled) {
		this.isEnabled = enabled;
	}

	/**
	 * the side of the selection that is not the cursor, may be the same as the
	 * cursor
	 */
	public int getSelectionEnd() {
		return this.selectionEnd;
	}

	/**
	 * Sets the position of the selection anchor (the selection anchor and the
	 * cursor position mark the edges of the selection). If the anchor is set beyond
	 * the bounds of the current text, it will be put back inside.
	 */
	public void setSelectionPos(int position) {
		int i = this.text.length();

		if (position > i) {
			position = i;
		}

		if (position < 0) {
			position = 0;
		}

		this.selectionEnd = position;
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.fontRenderer != null) {
			if (this.lineScrollOffset > i) {
				this.lineScrollOffset = i;
			}

			int j = this.sizeX - 8;
			String s = mc.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), j);
			int k = s.length() + this.lineScrollOffset;

			if (position == this.lineScrollOffset) {
				this.lineScrollOffset -= mc.fontRenderer.trimStringToWidth(this.text, j, true).length();
			}

			if (position > k) {
				this.lineScrollOffset += position - k;
			} else if (position <= this.lineScrollOffset) {
				this.lineScrollOffset -= this.lineScrollOffset - position;
			}

			this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, i);
		}
	}
}
