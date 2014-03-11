package jp.furyu.chat.jdbi;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

public interface UserDAO {
	  @SqlQuery("select name from Users where id = :id")
	  String findNameById(@Bind("id") int id);
}
