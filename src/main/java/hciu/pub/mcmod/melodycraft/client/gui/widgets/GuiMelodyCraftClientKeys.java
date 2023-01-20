package hciu.pub.mcmod.melodycraft.client.gui.widgets;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.lwjgl.input.Keyboard;
import hciu.pub.mcmod.hciusutils.gui.ISmartGuiComponent;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiConstants;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiScreen;
import hciu.pub.mcmod.melodycraft.client.gui.GuiMelodyCraftConstants;
import hciu.pub.mcmod.melodycraft.client.sound.ExternalSoundHandler;
import hciu.pub.mcmod.melodycraft.mug.EnumGameState;
import hciu.pub.mcmod.melodycraft.mug.EnumJudge;
import hciu.pub.mcmod.melodycraft.mug.EnumJudgeLevel;
import hciu.pub.mcmod.melodycraft.mug.LinearInterpolation;
import hciu.pub.mcmod.melodycraft.mug.MelodyCraftGameKeys;
import hciu.pub.mcmod.melodycraft.mug.MelodyCraftGameSettingsClient;
import hciu.pub.mcmod.melodycraft.mug.data.NoteInPlay;
import hciu.pub.mcmod.melodycraft.mug.data.NoteInPlay.NoteKeyModeInPlay;
import hciu.pub.mcmod.melodycraft.mug.data.NoteInPlay.NoteKeyModeLongInPlay;
import hciu.pub.mcmod.melodycraft.mug.data.NoteInPlay.NoteTimingBaseInPlay;
import hciu.pub.mcmod.melodycraft.mug.data.NoteInPlay.NoteTimingInPlay;
import hciu.pub.mcmod.melodycraft.mug.data.Note.NoteKeyMode;
import hciu.pub.mcmod.melodycraft.mug.data.Note.NoteKeyModeLong;
import hciu.pub.mcmod.melodycraft.mug.data.Note.NoteTiming;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;

public class GuiMelodyCraftClientKeys extends GuiMelodyCraftClient {

	private LinkedList<NoteInPlay<?>> notes;
	private LinearInterpolation timings = null;

	private String tempInfo = "";
	private long tempInfoStarted = 0;
	private long tempInfoRemaining = 0;

	private GuiMelodyCraftPictureBox pictureBg;
	
	private int[] keyBindings;

	public GuiMelodyCraftClientKeys(ISmartGuiComponent holder, MelodyCraftGameKeys game,
			MelodyCraftGameSettingsClient clientSettings) {
		super(holder, game, clientSettings);

		getGame().init();
		initialize();
		startGame();

		addComponent(pictureBg = new GuiMelodyCraftPictureBox(this));
		if (game.getSong().getBgfile() == null) {
			pictureBg.setTexture(GuiMelodyCraftConstants.MISCS, 0, 128, 128, 128);
		} else {
			pictureBg.setTexture(game.getSong().getBgfile(), 0, 0, 256, 256);
		}
		
		keyBindings = clientSettings.getKeyBindingForMode(this.getGame().getGameModeName());

	}

	private double getPlace(long l) {
		// System.out.println("time " + l + " place " + timings.get(l));
		return timings.get(l);
	}

	private void initialize() {
		if (currentlyPlaying != null) {
			ExternalSoundHandler.getInstance().stopSound(currentlyPlaying);
		}
		MelodyCraftGameKeys game = getGame();
		List<NoteTimingInPlay<NoteTiming>> notesTiming = new ArrayList<NoteInPlay.NoteTimingInPlay<NoteTiming>>();
		List<NoteInPlay<?>> byEndTime = new ArrayList<>();
		double bpmbase = 120;
		for (NoteInPlay<?> note : game.getNotes()) {
			if (note instanceof NoteTimingInPlay) {
				notesTiming.add((NoteTimingInPlay<NoteTiming>) note);
			} else if (note instanceof NoteTimingBaseInPlay) {
				bpmbase = ((NoteTimingBaseInPlay<?>) note).getNote().getBpm();
			}
		}
		double lasttime = 0, lastplace = 0, lastf = 1;
		double def = getGame().getSettings().speedFactor() * 50 / 1000;
		double[] x = new double[notesTiming.size() + 1], y = new double[notesTiming.size() + 1];
		int i = 0;
		for (NoteInPlay<?> note : notesTiming) {
			x[i] = note.getNote().getTime();
			y[i] = (lastplace += (x[i] - lasttime) * def * lastf);
			lasttime = x[i];
			lastf = ((NoteTimingInPlay<?>) note).getNote().getFactor() / bpmbase;
			i++;
		}
		x[i] = lasttime + 1000000;
		y[i] = lastplace + 1000000 * def * lastf;
		timings = new LinearInterpolation(x, y);
		for (NoteInPlay<?> note : game.getNotes()) {
			if ((note instanceof NoteKeyModeInPlay)) {
				byEndTime.add(note);
			}
		}
		byEndTime.sort((a, b) -> (int) (a.getNote().getTime() - b.getNote().getTime()));
		notes = new LinkedList<>(byEndTime);
	}

	private long getGameTime() {
		return getGame().getGameTimeMillis();
	}

	private void setGameTime(long t) {
		getGame().setGameTimeMillis(t);
	}

	private double getGamePlace() {
		return getPlace((int) getGameTime());
	}

	@Override
	public void onGuiClosed() {
		endGame();
		super.onGuiClosed();
	}

	public MelodyCraftGameKeys getGame() {
		return (MelodyCraftGameKeys) game;
	}

	@Override
	public void drawSelf() {
		try {
			onTicking();
			drawLanes();
			drawScoreAndAcc();
			drawNotes();
			drawJudgeInfo();
			drawComboAndCurrentJudge();
			drawTempInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void drawLanes() {
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(GuiMelodyCraftConstants.MUSIC_GAME);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		// System.out.println("drawlanes called");
		int left = ratioX(0.35) - 16 * getGame().getKeys() - 6;
		for (int i = 0; i < getGame().getKeys(); i++) {
			int d = 16, x = 223;
			if (i == 0) {
				d = 18;
				x = 221;
			} else if (i == getGame().getKeys() - 1) {
				d = 17;
				x = 239;
			}
			SmartGuiScreen.drawScaledTexturedModelRect(getActualX() + left, getActualY(), d, getSizeY() - 22, x, 0, d,
					110);
			drawTexturedModalRect(getActualX() + left, getActualY() + getSizeY() - 22, x, 110, d, 22);
			if (getGame().getLaneState(i)) {
				SmartGuiScreen.drawScaledTexturedModelRect(getActualX() + left + (i == 0 ? 2 : 0), getActualY(), 15,
						getSizeY() - 22, 176, 0F, 13, 110);
			}
			left += d;
		}
	}

	public void drawScoreAndAcc() {
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(GuiMelodyCraftConstants.MUSIC_GAME);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GuiMelodyCraftConstants.CHARSET_MEDIUM_NUMBER.draw(Integer.toString(getGame().getScore()),
				getActualX() + ratioX(0.35) + 2 + 8 * 7, getActualY() + 5, true);
		double x = getGame().getAcc();
		GuiMelodyCraftConstants.CHARSET_SMALL_NUMBER.draw(
				(int) (x * 100) + "." + String.format("%02d", ((int) (x * 10000) % 100)) + "%",
				getActualX() + ratioX(0.35) + 2 + 8 * 7, getActualY() + 17, true);
	}

	public void drawNotes() {
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(GuiMelodyCraftConstants.MUSIC_GAME);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int firstlane = ratioX(0.35) - 16 * getGame().getKeys() - 4;
		double ysz = getSizeY() - 22;
		double place = getGamePlace();
		while (!notes.isEmpty() && getPlace(notes.getFirst().getNote().getEndtime()) < place - 5 && notes.getFirst()
				.getNote().getEndtime() < getGameTime() - getGame().getSettings().getJudge().getGoodTime()) {
			notes.removeFirst();
		}
		for (NoteInPlay<?> note : notes) {
			if (getPlace(note.getNote().getTime()) > place + ysz) {
				break;
			}
			if (note instanceof NoteKeyModeLongInPlay) {

				NoteKeyModeLongInPlay<NoteKeyModeLong> n = (NoteKeyModeLongInPlay<NoteKeyModeLong>) note;
				double ep = getPlace(note.getNote().getEndtime()), sp = getPlace(note.getNote().getTime());

				if (ep - place <= -5) {
					continue;
				}

				int xl = 206;
				if (n.isVisited()) {
					if (n.isMissed()) {
						xl = 191;
					} else {
						continue;
					}
				}
				float from = (float) (ysz - (ep + 5 - place));
				float to = (float) (ysz - (sp - place));
				if (from < 1) {
					from = 1;
				}
				if (to > ysz) {
					to = (float) ysz;
				}
				if (to - from >= 1) {
					SmartGuiScreen.drawScaledTexturedModelRect(
							((float) getActualX() + firstlane + 16 * n.getNote().getLane()), getActualY() + from, 15F,
							to - from, (float) xl, 0F, 15F, 100F);
				}
				// System.out.println("from = " + from + " to = " + to);

				sp = Math.max(sp, place);

				if (sp <= place + ysz - 5 && sp - place > -5) {
					getSupreme().drawTexturedModalRect(getActualX() + firstlane + 16 * n.getNote().getLane(),
							getActualY() + (float) (place + ysz - 5 - sp), xl, 100, 15,
							(float) Math.min(5.0, sp - place + 5));
				}
				if (ep <= place + ysz - 5 && ep - place > -5) {
					getSupreme().drawTexturedModalRect(getActualX() + firstlane + 16 * n.getNote().getLane(),
							getActualY() + (float) (place + ysz - 5 - ep), xl, 105, 15,
							(float) Math.min(5.0, ep - place + 5));
				}

			} else if (note instanceof NoteKeyModeInPlay) {
				NoteKeyModeInPlay<NoteKeyMode> n = (NoteKeyModeInPlay<NoteKeyMode>) note;
				if (!n.isVisited()) {
					double p = getPlace(note.getNote().getTime());
					if (p <= place + ysz - 5 && p - place > -5) {
						getSupreme().drawTexturedModalRect(getActualX() + firstlane + 16 * n.getNote().getLane(),
								getActualY() + (float) (place + ysz - 5 - p), 206, 110, 15,
								(float) Math.min(5.0, p - place + 5));
					}
				}
			}
		}

	}

	public void drawJudgeInfo() {
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(GuiMelodyCraftConstants.MUSIC_GAME);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.currentScreen.drawTexturedModalRect(getActualX() + ratioX(0.35) + 2, getActualY() + getSizeY() - 47, 95, 9,
				7, 37);
		for (int i = 0; i < 4; i++) {
			GuiMelodyCraftConstants.CHARSET_SMALL_NUMBER.draw(Integer.toString(getGame().getJudge()[i]),
					getActualX() + ratioX(0.35) + 2 + 9, getActualY() + getSizeY() - 47 + 10 * i, false);
		}

	}

	public void drawComboAndCurrentJudge() {
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(GuiMelodyCraftConstants.MUSIC_GAME);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		float mid = (float) (ratioX(0.35) - 16 * getGame().getKeys() - 6 + (16 * getGame().getKeys() + 3) / 2.0);
		float upper = ratioY(0.50) - 30;
		String combo = Integer.toString(getGame().getCombo());
		GuiMelodyCraftConstants.CHARSET_LARGE_NUMBER.draw(combo, getActualX() + mid - combo.length() * 9,
				getActualY() + upper, false);
		upper += 30;
		if (getGameTime() - getGame().getLastJudgeTime() < 2000) {
			double t = (1000 - (getGameTime() - getGame().getLastJudgeTime())) / 1000.0;
			float alpha = (float) Math.max(0.5, t);
			GlStateManager.enableBlend();
			GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
			GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
			if (getGame().getLastJudge() != EnumJudge.PERFECT) {
				getSupreme().drawTexturedModalRect(getActualX() + mid - 18F, getActualY() + upper, 0F,
						getGame().isLastJudgeLate() ? 135 : 126, 36, 9);
			}
			upper += 12;
			float midx = getActualX() + mid, midy = getActualY() + upper + 9;
			float size = (float) Math.max(1.0, t + 0.25);
			float sx = 107 * size, sy = 18 * size;
			SmartGuiScreen.drawScaledTexturedModelRect(midx - sx / 2, midy - sy / 2, sx, sy, 0F,
					51 + 18 * getGame().getLastJudge().ordinal(), 107, 18);
			GlStateManager.disableBlend();
		}
	}

	public void addTempInfo(String s, int ms) {
		tempInfo = s;
		tempInfoRemaining = ms;
		tempInfoStarted = getGameTime();
	}

	public void drawTempInfo() {
		Minecraft mc = Minecraft.getMinecraft();
		if (getGameTime() - tempInfoStarted < tempInfoRemaining) {
			mc.fontRenderer.drawSplitString(tempInfo, getActualX() + ratioX(0.5), getActualY() + sizeY - 25, sizeX,
					SmartGuiConstants.VANILLA_TEXT_COLOR_ENABLED);
		}

	}

	public int getLaneForKey(int keyCode) {
		for (int i = 0; i < keyBindings.length; i++) {
			if (keyCode == keyBindings[i]) {
				return i;
			}
		}
		return -1;
	}

	private boolean isNoteTooLate(NoteInPlay<?> note, long now) {
		return now - note.getNote().getTime() > getGame().getSettings().getJudge().getGoodTime();
	}

	public NoteInPlay<?> getNextNote(int lane) {
		long now = getGameTime();
		for (NoteInPlay<?> note : notes) {
			if (note instanceof NoteKeyModeInPlay<?>) {
				NoteKeyModeInPlay<?> nn = (NoteKeyModeInPlay<?>) note;
				if (nn.getNote().getLane() == lane && !isNoteTooLate(note, now)) {
					if (nn instanceof NoteKeyModeLongInPlay) {
						if (!((NoteKeyModeLongInPlay<?>) nn).isHeadVisited()) {
							return nn;
						}
					} else if (nn instanceof NoteKeyModeInPlay) {
						if (!nn.isVisited()) {
							return nn;
						}
					}
				}
			}
		}
		return null;
	}

	public NoteKeyModeLongInPlay<?> getCurrentLongNote(int lane) {
		long now = getGameTime();
		for (NoteInPlay<?> note : notes) {
			if (note instanceof NoteKeyModeLongInPlay<?>) {
				NoteKeyModeLongInPlay<?> nn = (NoteKeyModeLongInPlay<?>) note;
				if (nn.getNote().getLane() == lane && nn.isHeadVisited() && !nn.isVisited()) {
					return nn;
				} else if (nn.getNote().getTime() > now + getGame().getSettings().getJudge().getPreMissTime()) {
					return null;
				}
			}
		}
		return null;
	}

	public void updateMissingLongNoteJudge(NoteKeyModeLongInPlay<?> note, long now) {
		int desired = (int) ((now - note.getNote().getTime()) / NoteKeyModeLong.EXTRA_JUDGE_TIME);
		int has = note.getJudgedCount();
		if (has < desired) {
			getGame().judgeLongNoteExtra(note.getId(), desired - has);
		}
	}

	public void onTicking() {
		if (notes.isEmpty()) {
			//endGame();
			return;
		}
		long now = getGameTime();
		if (now > -clientSettings.getDelay() + getGame().getChart().getDelay() && currentlyPlaying == null) {
			// System.out.println(getGame().getChart().getDelay());
			currentlyPlaying = ExternalSoundHandler.getInstance().playSound(getGame().getSong());
			setGameTime(-clientSettings.getDelay() + getGame().getChart().getDelay());
		}
		while (notes.isEmpty()) {
			NoteKeyModeInPlay<?> nn = (NoteKeyModeInPlay<?>) notes.getFirst();
			if (now - nn.getNote().getEndtime() > getGame().getSettings().getJudge().getGoodTime()) {
				notes.removeFirst();
			} else {
				break;
			}
		}
		for (NoteInPlay<?> note : notes) {
			if (note != null) {
				NoteKeyModeInPlay<?> nn = (NoteKeyModeInPlay<?>) note;
				if (nn instanceof NoteKeyModeLongInPlay && ((NoteKeyModeLongInPlay<?>) nn).isHeadVisited()
						&& !(nn.isVisited())) {
					updateMissingLongNoteJudge(((NoteKeyModeLongInPlay<?>) nn), now);
					if (nn.getNote().getEndtime() <= now) {
						getGame().judgeNote(nn.getId(), EnumJudge.PERFECT, true);
					}
				} else if (isNoteTooLate(note, now)) {
					if (nn instanceof NoteKeyModeLongInPlay) {
						if (!((NoteKeyModeLongInPlay<?>) nn).isHeadVisited()) {
							getGame().judgeLongNoteHead(nn.getId(), EnumJudge.MISS, true);
							getGame().judgeNote(nn.getId(), EnumJudge.MISS, true);
						}
					} else if (nn instanceof NoteKeyModeInPlay) {
						if (!nn.isVisited()) {
							getGame().judgeNote(nn.getId(), EnumJudge.MISS, true);
						}
					}
				} else if (nn.getNote().getTime() > now + getGame().getSettings().getJudge().getPreMissTime()) {
					break;
				}
			} else {
				break;
			}
		}

	}

	@Override
	public void onKeyPressed(char typedChar, int keyCode) {
		if (game.getState() == EnumGameState.PAUSED) {
			return;
		}
		long now = getGameTime() + clientSettings.getHitdelay();
		if (Keyboard.isRepeatEvent()) {
			return;
		}
		if (keyCode == Keyboard.KEY_UP || keyCode == Keyboard.KEY_DOWN) {
			boolean plus = keyCode == Keyboard.KEY_UP;
			int p = plus ? 10 : -10;
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				p = p / 10;
			}
			int newDelay = clientSettings.getDelay() + p;
			setGameTime(getGameTime() - p);
			clientSettings.setDelay(newDelay);
			addTempInfo("Client delay changed to " + newDelay + " ms", 5000);
		}
		int l = getLaneForKey(keyCode);
		if (l == -1) {
			return;
		}
		getGame().updateLane(l, true);
		NoteInPlay<?> note = getNextNote(l);
		if (note == null) {
			return;
		}
		int timediff = (int) Math.abs(note.getNote().getTime() - now);
		boolean late = note.getNote().getTime() - now < 0;
		EnumJudgeLevel lv = getGame().getSettings().getJudge();
		if (timediff > lv.getPreMissTime()) {
			return;
		}
		EnumJudge result = EnumJudge.PERFECT;
		if (timediff > lv.getGoodTime()) {
			result = EnumJudge.MISS;
		} else if (timediff > lv.getGreatTime()) {
			result = EnumJudge.GOOD;
		} else if (timediff > lv.getPerfectTime()) {
			result = EnumJudge.GREAT;
		}
		if (note instanceof NoteKeyModeLongInPlay) {
			getGame().judgeLongNoteHead(note.getId(), result, late);
		} else if (note instanceof NoteKeyModeInPlay) {
			getGame().judgeNote(note.getId(), result, late);
		}
	}

	@Override
	public void onKeyReleased(char typedChar, int keyCode) {
		if (game.getState() == EnumGameState.PAUSED) {
			return;
		}
		long now = getGameTime() + clientSettings.getHitdelay();
		int l = getLaneForKey(keyCode);
		if (l == -1) {
			return;
		}
		getGame().updateLane(l, false);
		NoteKeyModeLongInPlay<?> note = getCurrentLongNote(l);
		if (note == null) {
			return;
		}
		int timediff = (int) (note.getNote().getEndtime() - now);
		EnumJudgeLevel lv = getGame().getSettings().getJudge();
		EnumJudge result = EnumJudge.PERFECT;
		if (timediff > lv.getGoodTime()) {
			result = EnumJudge.MISS;
		} else if (timediff > lv.getGreatTime()) {
			result = EnumJudge.GOOD;
		} else if (timediff > lv.getPerfectTime()) {
			result = EnumJudge.GREAT;
		}

		getGame().judgeNote(note.getId(), result, false);
	}

	@Override
	public void onResizeSelf() {
		int bgsz = Math.min(ratioX(0.5) - 40, getSizeY() - 45);
		pictureBg.setBounds(30 + ratioX(0.5), 35, bgsz, bgsz);
	}
}
