package jp.furyu.chat.resources;


import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jp.furyu.chat.api.Saying;
import jp.furyu.chat.jdbi.DAOFactory;
import jp.furyu.chat.jdbi.UserDAO;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;
    private UserDAO dao;

    @Inject
    public HelloWorldResource(@Named("template")String template, @Named("defaultName")String defaultName, DAOFactory factory) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
        
        this.dao = factory.createDAO(UserDAO.class);        
    }

    @GET
    @Timed
    public Response sayHello(@QueryParam("name") Optional<String> name) {
		System.out.println("say hello with dao : " + dao.findNameById(1));

		return Response.ok(new Saying(counter.incrementAndGet(),
                String.format(template, name.or(defaultName)))).header("Access-Control-Allow-Origin", "*").build();
    }
}