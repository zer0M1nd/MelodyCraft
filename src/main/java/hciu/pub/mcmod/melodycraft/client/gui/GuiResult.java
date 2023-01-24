package hciu.pub.mcmod.melodycraft.client.gui;

import hciu.pub.mcmod.hciusutils.gui.SmartGuiScreen;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiTextLabel;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftButton;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftPictureBox;
import hciu.pub.mcmod.melodycraft.mug.MelodyCraftGame;
import hciu.pub.mcmod.melodycraft.mug.saves.PlayResult;
import hciu.pub.mcmod.melodycraft.mug.saves.ResultManager;
import hciu.pub.mcmod.melodycraft.utils.MiscsHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiResult extends GuiMelodyCraftBase {

	private MelodyCraftGame game;

	public GuiResult(GuiGame parent, MelodyCraftGame game) {
		super(parent);
		this.game = game;

		addComponent(new GuiMelodyCraftPictureBox(this) {
			{
				if (game.getSong().getBgfile() == null) {
					setTexture(GuiMelodyCraftConstants.MISCS, 0, 128, 128, 128);
				} else {
					setTexture(game.getSong().getBgfile(), 0, 0, 256, 256);
				}
			}
		}.setResizeAction(x -> {
			int bgsz = Math.min(getSizeX() / 2 - 40, getSizeY() - 45);
			x.setBounds(30, 35, bgsz, bgsz);
		}));

		addComponent(new SmartGuiTextLabel(this, I18n.format("melodycraft.gui.result.title")).setCentered(true)
				.setResizeAction(x -> x.setCenterSize(ratioX(0.5), 12, 100, 16)));
		
		PlayResult res = ResultManager.getInstance().getBestFor(game.getChart());
		int history = res == null ? 0 : res.getScore();
		
		addComponent(new SmartGuiTextLabel(this) {
			{
				String s = "";
				s += game.getSong().getName() + "\n";
				s += game.getSong().getArtist() + "\n";
				s += game.getChart().getInfoDifficulty() + "\n\n";
				s += I18n.format("melodycraft.gui.result.maxcombo") + "\n";
				s += I18n.format("melodycraft.gui.result.score") + "\n";
				s += (game.getScore() > history ? I18n.format("melodycraft.gui.result.newrecord") : I18n.format("melodycraft.gui.result.record")) + "\n";
				s += "\n";
				s += I18n.format("melodycraft.gui.result.acc") + "\n";
				s += "\n\n\n\n";
				s += I18n.format("melodycraft.gui.judge.perfect") + "\n";
				s += I18n.format("melodycraft.gui.judge.great") + "\n";
				s += I18n.format("melodycraft.gui.judge.good") + "\n";
				s += I18n.format("melodycraft.gui.judge.miss") + "\n";
				setText(s);
			}
		}.setResizeAction(x -> x.setBounds(ratioX(0.5) + 10, 35, ratioX(0.5) - 10, getSizeY() - 45)));
		addComponent(new SmartGuiTextLabel(this) {
			{
				String s = "\n\n\n\n";
				s += game.getMaxCombo() + "\n";
				s += game.getScore() + "\n";
				s += history + "\n";
				s += "\n";
				s += ((int) (game.getAcc() * 100) + "." + String.format("%02d", ((int) (game.getAcc() * 10000) % 100))
						+ "%") + "\n";
				s += "\n\n\n\n";
				s += game.getJudge()[0] + "\n";
				s += game.getJudge()[1] + "\n";
				s += game.getJudge()[2] + "\n";
				s += game.getJudge()[3] + "\n";
				setText(s);
			}
		}.setResizeAction(x -> x.setBounds(ratioX(0.5) + 10 + 50, 35, ratioX(0.5) - 10 - 50, getSizeY() - 45)));

		boolean fc = game.getJudge()[3] == 0;
		boolean ap = game.getJudge()[1] + game.getJudge()[2] + game.getJudge()[3] == 0;

		addComponent(new SmartGuiTextLabel(this) {

			public void drawSelf() {
				int color = 0;
				String text = "";
				if (ap) {
					text = I18n.format("melodycraft.gui.result.ap");
					int h = (int) (Minecraft.getSystemTime() / 1000.0 * 360.0) % 360;
					color = GuiMelodyCraftConstants.getColorByHSV(h, 20, 100);
				} else if (fc) {
					text = I18n.format("melodycraft.gui.result.fc");
					color = GuiMelodyCraftConstants.getColorByRGB(255, 242, 0);
				}
				Minecraft mc = Minecraft.getMinecraft();
				mc.fontRenderer.drawSplitString(text, actualX, actualY, sizeX, color);
			}
		}.setResizeAction(x -> x.setBounds(ratioX(0.5) + 10, 35 + 9 * 10, ratioX(0.5) - 10, getSizeY() - 45)));

		addComponent(new GuiMelodyCraftButton(this, I18n.format("melodycraft.gui.result.button.next")) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				saveResult();
				SmartGuiScreen screen = null;
				try {
					screen = parent.getParent();
				} catch (Exception e) {
				}
				if (screen instanceof GuiSelectChart) {
					((GuiSelectChart) screen).updateText();
					Minecraft.getMinecraft().displayGuiScreen(screen);
				} else {
					Minecraft.getMinecraft().displayGuiScreen(null);
				}

			}
		}.setResizeAction(x -> x.setCenterSize(getSizeX() - 60, getSizeY() - 20, 80, 20)));
		addComponent(new GuiMelodyCraftButton(this, I18n.format("melodycraft.gui.result.button.retry")) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				saveResult();
				game.getTileEntity().setGame(game.newGame());
				Minecraft.getMinecraft().displayGuiScreen(
						new GuiGame(parent.getParent(), game.getTileEntity(), parent.getClientSettings()));
			}
		}.setResizeAction(x -> x.setCenterSize(getSizeX() - 160, getSizeY() - 20, 80, 20)));

	}

	public void saveResult() {
		PlayResult r = new PlayResult(game.getSong().getIdentifier(), game.getChart().getIdentifier(),
				game.getSettings().getJudge(), game.getScore(), game.getAcc(), game.getJudge(),
				MiscsHelper.currentTime());
		ResultManager.getInstance().add(r);

	}

}
