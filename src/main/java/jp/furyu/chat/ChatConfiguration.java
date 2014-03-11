package jp.furyu.chat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotEmpty;

public class ChatConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    private String template;

    @NotEmpty
    @JsonProperty
    private String defaultName = "Stranger";
    
    @Valid
    @NotNull
    private DataSourceFactory dataSourceFactory = new DataSourceFactory();

    public String getTemplate() {
        return template;
    }

    public String getDefaultName() {
        return defaultName;
    }
    
    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory(){
    	return this.dataSourceFactory;
    }
    
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory){
    	this.dataSourceFactory = dataSourceFactory;
    }
}