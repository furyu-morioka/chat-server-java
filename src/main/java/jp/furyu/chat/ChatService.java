package jp.furyu.chat;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.hubspot.dropwizard.guice.GuiceBundle;

public class ChatService extends Application<ChatConfiguration> {
	
    public static void main(String[] args) throws Exception {
        new ChatService().run(args);
    }
    
    @Override
    public void initialize(Bootstrap<ChatConfiguration> bootstrap) {
    	
    	GuiceBundle<ChatConfiguration> guiceBundle = GuiceBundle.<ChatConfiguration>newBuilder()
    			.addModule(new ChatModule())
    			.addModule(new DBIModule())
    			.setConfigClass(ChatConfiguration.class)
				.enableAutoConfig(getClass().getPackage().getName())
				.build();
    	bootstrap.addBundle(guiceBundle);
    }
    
    @Override
    public String getName() {
        return "chat";
    }
    
    @Override
    public void run(ChatConfiguration configuration,
                    Environment environment) {
    }

}