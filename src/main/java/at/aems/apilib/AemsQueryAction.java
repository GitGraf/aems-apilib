package at.aems.apilib;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class AemsQueryAction extends AbstractAemsAction {

	private transient String graphQlQuery;
	
	public AemsQueryAction(AemsUser user) {
		super(user, "QUERY");
	}
	
	public void setQuery(String query) {
		this.graphQlQuery = query;
	}

	@Override
	public JsonElement serializeData() {
		return new JsonPrimitive(graphQlQuery);
	}

}
