package pandemic;

import java.io.*;
import java.util.logging.Logger;
import javax.servlet.http.*;
import com.fasterxml.jackson.core.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.*;

public class ScenarioServlet extends HttpServlet
{
	private static final Logger log = Logger.getLogger(ScenarioServlet.class.getName());

	void doGetScenario(String scenarioName, HttpServletRequest req, HttpServletResponse resp)
		throws IOException
	{
		try {

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey("Scenario", scenarioName);
		Entity ent = datastore.get(key);

		Text t = (Text) ent.getProperty("content");
		
		resp.setContentType("text/json;charset=UTF-8");
		Writer out = resp.getWriter();
		out.write(t.getValue());
		out.close();

		}
		catch (EntityNotFoundException e) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws IOException
	{
		String scenario_id = req.getParameter("id");

		if (scenario_id != null) {
			doGetScenario(scenario_id, req, resp);
			return;
		}

		doGetIndex(req, resp);
	}

	void doGetIndex(HttpServletRequest req, HttpServletResponse resp)
		throws IOException
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Scenario");
		PreparedQuery pq = datastore.prepare(q);

		resp.setContentType("text/json;charset=UTF-8");
		JsonGenerator out = new JsonFactory().
			createJsonGenerator(resp.getWriter()
			);
		out.writeStartObject();
		out.writeStringField("serverVersion", "1");

		UserService userService = UserServiceFactory.getUserService();
		if (req.getUserPrincipal() == null) {
			// not signed in

			String loginUrl = userService.createLoginURL(
				getBaseUrl(req));
			out.writeStringField("loginUrl", loginUrl);
		}
		else {
			out.writeStringField("userName",
				req.getUserPrincipal().getName());
			String logoutUrl = userService.createLogoutURL(
				getBaseUrl(req));
			out.writeStringField("logoutUrl", logoutUrl);
		}

		out.writeFieldName("scenarios");
		out.writeStartArray();

		for (Entity ent : pq.asIterable()) {
			out.writeStartObject();
			String id = ent.getKey().getName();
			out.writeStringField("id", id);
			String rules = (String) ent.getProperty("rules");
			out.writeStringField("rules", rules);
			out.writeEndObject();
		}

		out.writeEndArray();

		Query q_1 = new Query("Result");
		PreparedQuery pq_1 = datastore.prepare(q_1);

		out.writeFieldName("results");
		out.writeStartArray();

		for (Entity ent : pq_1.asIterable()) {
			out.writeStartObject();
			String deal_id = ent.getKey().getParent().getName();
			String result_id = ent.getKey().getName();
			out.writeStringField("id", result_id);
			out.writeStringField("scenario", deal_id);
			out.writeEndObject();
		}

		out.writeEndArray();

		out.writeEndObject();
		out.close();
	}

	static String getBaseUrl(HttpServletRequest req)
	{
		String scheme = req.getScheme();
		int port = req.getServerPort();
		int defaultPort = scheme.equals("https") ? 443 : 80;

		String myUrl = scheme + "://" + req.getServerName() + (port == defaultPort ? "" : ":"+port) + req.getContextPath();
		return myUrl;
	}
}
