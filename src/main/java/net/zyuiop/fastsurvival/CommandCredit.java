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

package net.zyuiop.fastsurvival;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.ArrayList;

/**
 * @author zyuiop
 */
public class CommandCredit implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender cs, Command command, String s, String[] strings) {
		PluginDescriptionFile desc = FastSurvival.instance.getDescription();
		cs.sendMessage(ChatColor.YELLOW + desc.getName() + ChatColor.WHITE + " version " + ChatColor.GREEN + desc.getVersion());
		cs.sendMessage(ChatColor.WHITE + "Developped by " + ChatColor.YELLOW + "zyuiop");
		ArrayList<String> authors = Lists.newArrayList(desc.getAuthors());
		authors.remove("zyuiop");
		String contributors = StringUtils.join(authors, ", ");
		cs.sendMessage(ChatColor.WHITE + "Contributors : " + ChatColor.YELLOW + contributors);
		return true;
	}
}
