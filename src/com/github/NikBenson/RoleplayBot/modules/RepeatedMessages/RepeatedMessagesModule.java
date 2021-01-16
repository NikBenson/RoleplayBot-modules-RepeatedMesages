package com.github.NikBenson.RoleplayBot.modules.RepeatedMessages;

import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;
import com.github.NikBenson.RoleplayBot.modules.RoleplayBotModule;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class RepeatedMessagesModule implements RoleplayBotModule {
	private Map<Guild, RepeatedMessagesManager> managers = new HashMap<>();

	@Override
	public boolean isActive(Guild guild) {
		return managers.containsKey(guild);
	}

	@Override
	public void load(Guild guild) {
		if(!managers.containsKey(guild)) {
			RepeatedMessagesManager manager = new RepeatedMessagesManager(guild);
			ConfigurationManager configurationManager = ConfigurationManager.getInstance();

			try {
				configurationManager.registerConfiguration(manager);
				configurationManager.load(manager);
			} catch (Exception e) {
				System.out.printf("error while loading repeated messages config directory for guild: %s%n", guild.getId());
			}

			managers.put(guild, manager);
		}
	}

	@Override
	public void unload(Guild guild) {
		if(managers.containsKey(guild)) {
			RepeatedMessagesManager manager = managers.get(guild);

		}
	}
}
