package at.aems.apilib;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import at.aems.apilib.crypto.EncryptionType;

public class AemsDeleteAction extends AbstractAemsAction {

    private String idColumn;
    private Object idValue;
    
    public AemsDeleteAction(AemsUser user, EncryptionType encryption) {
        super(user, "DELETE", encryption);
    }
    
    /**
     * This method is used to specify the selection of records in the
     * database to be updated by this action. 
     * @param columnName The name of the column to be used for the WHERE clause
     * @param value The value that the column must have
     */
    public void setIdColumn(String columnName, Object value) {
            this.idColumn = columnName;
            this.idValue = value;
    }

    @Override
    public JsonElement serializeData() {
        JsonObject data = new JsonObject();
        if(idValue instanceof Number) {
                data.addProperty(idColumn, (Number) idValue);
        } else if(idValue instanceof Boolean) {
                data.addProperty(idColumn, (Boolean) idValue);
        } else {
                data.addProperty(idColumn, (String) idValue);
        }
        return data;
    }

    @Override
    public String getHttpVerb() {
        return "DELETE";
    }

}
