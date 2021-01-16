package com.github.NikBenson.RoleplayBot.modules.RepeatedMessages;

import com.github.NikBenson.RoleplayBot.commands.context.Context;
import com.github.NikBenson.RoleplayBot.commands.context.GuildContext;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationPaths;
import com.github.NikBenson.RoleplayBot.configurations.JSONConfigured;
import com.github.NikBenson.RoleplayBot.messages.MessageFormatter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RepeatedMessagesManager implements JSONConfigured {
	private final Guild GUILD;

	List<RepeatedMessage> repeatedMessages = new LinkedList<>();

	public RepeatedMessagesManager(Guild guild) {
		GUILD = guild;
	}

	@Override
	public JSONObject getJSON() {
		return null;
	}

	@Override
	public File getConfigPath() {
		return new File(ConfigurationManager.getInstance().getConfigurationRootPath(GUILD), ConfigurationPaths.REPEATED_MESSAGES_DIRECTORY);
	}

	@Override
	public void loadFromJSON(JSONObject json) {
		stopOld();

		for(Object current : json.keySet()) {
			try {
				loadRepeatedMessage((JSONObject) json.get(current));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	public void stopOld() {
		for(RepeatedMessage repeatedMessage : repeatedMessages) {
			repeatedMessage.stop();
		}

		repeatedMessages.clear();
	}
	private void loadRepeatedMessage(JSONObject json) throws java.text.ParseException {
		TextChannel channel = GUILD.getTextChannelById((Long) json.get("channel"));
		Date startAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String) json.get("startAt"));
		long timeDelta = (long) json.get("timeDelta");

		String message = (String) json.get("message");
		JSONArray valuesJSON = (JSONArray) json.get("values");
		String[] values = new String[valuesJSON.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = (String) valuesJSON.get(i);
		}

		MessageFormatter<GuildContext> messageFormatter = new MessageFormatter<>(message, values);
		repeatedMessages.add(new RepeatedMessage(channel, messageFormatter, startAt, timeDelta));
	}
}
