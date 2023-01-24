package hciu.pub.mcmod.melodycraft.client.gui;

import hciu.pub.mcmod.hciusutils.gui.SmartGuiComponentBase;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiScreen;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiTextLabel;
import hciu.pub.mcmod.hciusutils.gui.render.FramedRectangleDrawer;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftButton;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class SubGuiScreenPause extends SmartGuiComponentBase {


	private SmartGuiTextLabel paused;
	private GuiMelodyCraftButton resume;
	private GuiMelodyCraftButton restart;
	private GuiMelodyCraftButton quit;

	public GuiMelodyCraftButton getButtonResume() {
		return resume;
	}

	public GuiMelodyCraftButton getButtonRestart() {
		return restart;
	}

	public GuiMelodyCraftButton getButtonQuit() {
		return quit;
	}

	public SubGuiScreenPause(SmartGuiScreen parent, GuiGame holder0, GuiMelodyCraftClient game) {
		super(holder0);
		setTextureDrawer(new FramedRectangleDrawer<SubGuiScreenPause>(this, GuiMelodyCraftConstants.COLOR_MAIN_FRAME,
				GuiMelodyCraftConstants.COLOR_MAIN_IN, 2));
		this.setVisible(false);
		addComponent(paused = new SmartGuiTextLabel(this));
		paused.setCentered(true);

		addComponent(resume = new GuiMelodyCraftButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				SubGuiScreenPause.this.setVisible(false);
				game.resume();
			}
		});
		addComponent(restart = new GuiMelodyCraftButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				holder0.getTileEntity().setGame(holder0.getTileEntity().getGame().newGame());
				Minecraft.getMinecraft().displayGuiScreen(new GuiGame(holder0.getParent(), holder0.getTileEntity(), holder0.getClientSettings()));
			}
		});
		addComponent(quit = new GuiMelodyCraftButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				Minecraft.getMinecraft().displayGuiScreen(getSupreme().getParent());
			}
		});
		paused.setText(I18n.format("melodycraft.gui.paused"));
		resume.setText(I18n.format("melodycraft.gui.button.resume"));
		restart.setText(I18n.format("melodycraft.gui.button.restart"));
		quit.setText(I18n.format("melodycraft.gui.button.quit"));
	}

	@Override
	public void onResizeSelf() {
		super.onResizeSelf();
		int xm = ratioX(0.5), ym = 14;
		paused.setCenterSize(xm, ym, 64, 16);
		resume.setCenterSize(xm,ym + 24, 64,16);
		restart.setCenterSize(xm,ym + 48, 64,16);
		quit.setCenterSize(xm,ym + 72, 64,16);
	}

}
