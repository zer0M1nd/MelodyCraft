package hciu.pub.mcmod.melodycraft.client.gui;

import hciu.pub.mcmod.hciusutils.gui.ISmartGuiComponent;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiButton;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiComponentBase;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiScreen;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiTextField;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiTextLabel;
import hciu.pub.mcmod.hciusutils.gui.render.FramedRectangleDrawer;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftButton;
import hciu.pub.mcmod.melodycraft.mug.EnumJudgeLevel;
import net.minecraft.client.resources.I18n;

public class SubGuiScreenSelectJudge extends SmartGuiComponentBase {

	private EnumJudgeLevel selected = EnumJudgeLevel.A;

	private SmartGuiTextLabel textSelected;
	private GuiMelodyCraftButton prev;
	private GuiMelodyCraftButton next;
	private GuiMelodyCraftButton finish;

	public SubGuiScreenSelectJudge(SmartGuiScreen parent, ISmartGuiComponent holder) {
		super(holder);
		setTextureDrawer(new FramedRectangleDrawer<SubGuiScreenSelectJudge>(this, GuiMelodyCraftConstants.COLOR_MAIN_FRAME,
				GuiMelodyCraftConstants.COLOR_MAIN_IN, 2));
		this.setVisible(false);
		addComponent(textSelected = new SmartGuiTextLabel(this));
		
		addComponent(prev = new GuiMelodyCraftButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				updateSelected(-1);
			}
		});
		addComponent(next = new GuiMelodyCraftButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				updateSelected(1);
			}
		});
		addComponent(finish = new GuiMelodyCraftButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				SubGuiScreenSelectJudge.this.setVisible(false);
			}
		});
		prev.setText(I18n.format("gui.button.left"));
		next.setText(I18n.format("gui.button.right"));
		finish.setText(I18n.format("gui.button.finish"));
		updateSelected(0);
	}

	public void updateSelected(int delta) {
		int x = selected.ordinal() + delta;
		if (x < 0) {
			x = 0;
		}
		if (x >= EnumJudgeLevel.values().length) {
			x = EnumJudgeLevel.values().length - 1;
		}
		selected = EnumJudgeLevel.values()[x];
		textSelected.setText(selected.name());
	}

	@Override
	public void onResizeSelf() {
		super.onResizeSelf();
		int xm = ratioX(0.5), ym = 14;
		textSelected.setCenterSize(xm, ym, 6, 10);
		prev.setCenterSize(xm - 15, ym, 16, 16);
		next.setCenterSize(xm + 15, ym, 16, 16);
		finish.setCenterSize(xm,ym + 24, 32,16);
	}

}
