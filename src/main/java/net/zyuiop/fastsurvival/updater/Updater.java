/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 zyuiop
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.zyuiop.fastsurvival.updater;

import net.zyuiop.fastsurvival.FastSurvival;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author zyuiop
 */
public class Updater implements Listener {
	private final URL versionUrl;
	private URL downloadUrl = null;
	private boolean updates = false;
	private String targetVersion = null;

	public Updater() throws MalformedURLException {
		versionUrl = new URL("http://archive.zyuiop.net/FastSurvival/LATEST.txt");
		checkForUpdates();
	}

	private void checkForUpdates() {
		Bukkit.getLogger().info("[Updater] Checking FastSurvival updates.");
		String version = FastSurvival.instance.getDescription().getVersion();
		String[] parts = StringUtils.split(version, ".");
		int major, minor, build = -1;
		try {
			major = Integer.decode(parts[0]);
			minor = Integer.decode(parts[1]);
			build = Integer.decode(parts[2]);
		} catch (Exception e) {
			Bukkit.getLogger().severe("Failed to check for FastSurvival updates : malformed version.");
			return;
		}

		Bukkit.getLogger().info("Current FastSurvival version is : MAJOR " + major + " MINOR " + minor + " BUILD " + build);

		try {
			InputStream stream = versionUrl.openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String upstreamVersion = reader.readLine();
			parts = StringUtils.split(upstreamVersion, ".");
			int _major, _minor, _build = -1;
			try {
				_major = Integer.decode(parts[0]);
				_minor = Integer.decode(parts[1]);
				_build = Integer.decode(parts[2]);
			} catch (Exception e) {
				Bukkit.getLogger().severe("Failed to check for FastSurvival updates : malformed upstream version.");
				return;
			}

			Bukkit.getLogger().info("Upstream FastSurvival version is : MAJOR " + _major + " MINOR " + _minor + " BUILD " + _build);

			if (_build > build) {
				Bukkit.getLogger().info("Update available ! Run /update to update the plugin.");
				this.targetVersion = upstreamVersion;
				updates = true;
				downloadUrl = new URL("http://archive.zyuiop.net/FastSurvival/fastsurvival-" + _build + ".jar");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void doUpdate(CommandSender sender) {
		checkForUpdates();
		if (!updates) {
			sender.sendMessage(ChatColor.GOLD + "The plugin is already up to date =)");
			return;
		}

		File plugin = FastSurvival.instance.getFile();
		String[] path = StringUtils.split(downloadUrl.getFile(), "/");

		Bukkit.getLogger().info("Current plugin : " + plugin.getAbsolutePath());

		File target = new File(plugin.getParentFile(), path[path.length - 1]);

		sender.sendMessage(ChatColor.YELLOW + "[Updater] Starting download of " + target.getName());
		try {
			FileUtils.copyURLToFile(downloadUrl, target);
		} catch (IOException e) {
			e.printStackTrace();
			sender.sendMessage(ChatColor.YELLOW + "[Updater] " + ChatColor.RED + "Failed to download the new version. Check the log for more information.");
			return;
		}

		sender.sendMessage(ChatColor.YELLOW + "[Updater] Download finished, applying update..");
		Plugin outdated = FastSurvival.instance;
		FastSurvival.instance = null;
		Bukkit.getServer().getPluginManager().disablePlugin(outdated);
		outdated.getPluginLoader().disablePlugin(outdated);

		/*try {
			Plugin pl = Bukkit.getServer().getPluginManager().loadPlugin(target);
			pl.getPluginLoader().enablePlugin(pl);
		} catch (InvalidPluginException | InvalidDescriptionException e) {
			e.printStackTrace();
			sender.sendMessage(ChatColor.YELLOW + "[Updater] " + ChatColor.RED + "Failed to launch new version. Check the log for more information.");
			return;
		}*/

		if (!plugin.delete()) {
			plugin.deleteOnExit();
			Bukkit.getLogger().info("Delete failed / Scheduled delete on exit.");
		}

		Bukkit.reload();
		sender.sendMessage(ChatColor.YELLOW + "[Updater] " + ChatColor.GREEN + "The plugin was updated successfully !");
		sender.sendMessage(ChatColor.YELLOW + "[Updater] " + ChatColor.YELLOW + "FastSurvival is now at version " + ChatColor.GREEN + targetVersion);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (event.getPlayer().isOp() && updates) {
			event.getPlayer().sendMessage(ChatColor.YELLOW + "[Updater] There is an update available for FastSurvival. Run /update to download and install.");
		}
	}
}
