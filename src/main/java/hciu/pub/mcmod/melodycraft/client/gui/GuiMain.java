package hciu.pub.mcmod.melodycraft.client.gui;

import hciu.pub.mcmod.hciusutils.gui.SmartGuiButton;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiScreen;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftButton;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftPictureBox;
import hciu.pub.mcmod.melodycraft.tileentity.TileEntityArcade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiMain extends GuiMelodyCraftBase {

	private GuiMelodyCraftButton buttonSingleplayer;
	private GuiMelodyCraftPictureBox pictureTitle;
	private TileEntityArcade tileEntity;

	public GuiMain(SmartGuiScreen parent, TileEntityArcade tileEntity) {
		super(parent);
		addComponent(buttonSingleplayer = new GuiMelodyCraftButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiSelectSong(getSupreme(),GuiMain.this.tileEntity));
			}
		});
		buttonSingleplayer.setText(I18n.format("gui.singleplayer"));
		addComponent(pictureTitle = new GuiMelodyCraftPictureBox(this));
		pictureTitle.setTexture(GuiMelodyCraftConstants.MISCS, 0, 0, 186, 33);
		this.tileEntity = tileEntity;
	}

	@Override
	public void onResizeSelf() {
		super.onResizeSelf();
		buttonSingleplayer.setBounds(50, getSizeY() - 100, 80, 40);
		pictureTitle.setBounds((getSizeX() - 186) / 2, 30, 186, 33);
	}

}
