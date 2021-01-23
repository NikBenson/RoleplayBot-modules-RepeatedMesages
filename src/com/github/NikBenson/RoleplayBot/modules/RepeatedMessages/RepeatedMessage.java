package com.github.nikbenson.roleplaybot.modules.RepeatedMessages;

import com.github.nikbenson.roleplaybot.commands.context.GuildContext;
import com.github.nikbenson.roleplaybot.messages.MessageFormatter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class RepeatedMessage extends TimerTask {
	private final Guild GUILD;
	private final TextChannel channel;
	private final MessageFormatter<GuildContext> message;

	private final Date startingTime;
	private final long period;

	private final Timer timer;

	public RepeatedMessage(@NotNull TextChannel channel, @NotNull MessageFormatter<GuildContext> message, @NotNull Date startingTime, @NotNull long period) {
		GUILD = channel.getGuild();
		this.channel = channel;
		this.message = message;
		this.startingTime = startingTime;
		this.period = period;
		timer = new Timer();

		startSchedule();
	}

	private void startSchedule() {
		timer.schedule(
				this,
				startingTime,
				period
		);
	}

	public void stop() {
		timer.cancel();
	}

	@Override
	public void run() {
		channel.sendMessage(message.createMessage(new GuildContext(GUILD))).queue();
	}
}
