package jp.furyu.chat.jdbi;

import org.skife.jdbi.v2.DBI;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;

@Singleton
public class DAOFactory {
	private DBI jdbi;
	
	@Inject
	public  DAOFactory(Environment environment, DataSourceFactory dataSourceFactory){
		DBIFactory factory = new DBIFactory();
    	try {
			jdbi = factory.build(environment, dataSourceFactory, "mysql");
		} catch (ClassNotFoundException e) {
			throw new  IllegalArgumentException("db is error");
		}		
	}
	public <T> T createDAO(Class<T> clazz){
		return jdbi.onDemand(clazz);
	}
	
}
