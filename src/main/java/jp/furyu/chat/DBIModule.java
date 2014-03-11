package jp.furyu.chat;

import io.dropwizard.db.DataSourceFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

@Singleton
public class DBIModule extends AbstractModule {
	@Override
	protected void configure() {
		System.out.println("config...");
	}

	@Provides
	public DataSourceFactory provideDataSourceFactory(ChatConfiguration configuration) {
		System.out.println("get data source : " + configuration.getDataSourceFactory());
		return configuration.getDataSourceFactory();
	}
}
