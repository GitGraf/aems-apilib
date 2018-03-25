package at.aems.apilib;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import at.aems.apilib.crypto.EncryptionType;

public class AemsStatisticAction extends AbstractAemsAction {

    private Integer statisticId;
    
    public AemsStatisticAction(AemsUser user, EncryptionType encryption) {
        super(user, "STATISTIC", encryption);
    }
    
    public void setStatisticId(Integer id) {
        statisticId = id;
    }
    
    public Integer getStatisticId() {
        return this.statisticId;
    }

    @Override
    public JsonElement serializeData() {
        if(statisticId == null) {
            throw new IllegalArgumentException("Statistc id cannot be null!");
        }
        return new JsonPrimitive(String.valueOf(statisticId));
    }

    @Override
    public String getHttpVerb() {
        return "POST";
    }

}
