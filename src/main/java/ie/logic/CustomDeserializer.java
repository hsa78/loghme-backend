package ie.logic;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ie.logic.Food;
import ie.logic.OrdinaryFood;

import java.io.IOException;

public class CustomDeserializer extends StdDeserializer<Food> {

    protected CustomDeserializer() {
        super(Food.class);
    }

    @Override
    public Food deserialize(JsonParser p, DeserializationContext ctx) throws IOException, JsonProcessingException {
        TreeNode node = p.readValueAsTree();

        if(node.get("count") == null)
            return p.getCodec().treeToValue(node, OrdinaryFood.class);

        // Select the concrete class based on the existence of a property
//        if (node.get("count") != null) {
        return p.getCodec().treeToValue(node, DiscountFood.class);

    }
}