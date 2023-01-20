package hciu.pub.mcmod.melodycraft.command;

import java.io.File;
import java.net.MalformedURLException;

import javax.swing.JFileChooser;

import hciu.pub.mcmod.melodycraft.client.sound.ExternalSoundHandler;
import hciu.pub.mcmod.melodycraft.mug.data.MalodyChartConvertor;
import hciu.pub.mcmod.melodycraft.mug.data.SongList;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandMelodyCraft extends CommandBase {

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getName() {
		return "mec";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/mec";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args[0].equals("test")) {
			File file = new File("test");
			if (!file.exists()) {
				file.mkdir();
			}
			File file2 = new File(file, args[1]);
			EntityPlayer player = Minecraft.getMinecraft().player;
			try {
				ExternalSoundHandler.getInstance().playSound(file2.toURI().toURL(), args[1], (float) player.posX,
						(float) player.posY, (float) player.posZ);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else if (args[0].equals("convert")) {
			File file;
			if (args.length > 1) {
				String x = "";
				x = args[1];
				for (int i = 2; i < args.length; i++) {
					x += " " + args[i];
				}
				file = new File(x);
			} else {
				JFileChooser choose = new JFileChooser(new File("."));
				choose.setMultiSelectionEnabled(false);
				choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				choose.showDialog(null, "Open");
				file = choose.getSelectedFile();
			}
			String to = args.length > 2 ? args[2] : "melodycraft";
			MalodyChartConvertor.convertRecursively(file, new File(to));
			SongList.loadAllSongs();
			sender.sendMessage(new TextComponentTranslation("command.convert.success"));
		} else if (args[0].equals("reload")) {
			SongList.loadAllSongs();
			sender.sendMessage(new TextComponentTranslation("command.reload"));
		}

	}
}
