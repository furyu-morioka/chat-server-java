package jp.furyu.chat;

import javax.inject.Named;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class ChatModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	@Provides
	@Named("template")
	public String provideTemplate(ChatConfiguration configuration) {
		return configuration.getTemplate();
	}

	@Provides
	@Named("defaultName")
	public String provideDefaultName(ChatConfiguration configuration) {
		return configuration.getDefaultName();
	}

}