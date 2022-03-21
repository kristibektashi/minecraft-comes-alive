package mca.advancement.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate.Extended;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class BabySmeltedCriterion extends AbstractCriterion<BabySmeltedCriterion.Conditions> {
    private static final Identifier ID = new Identifier("mca:baby_smelted");

    public static final Identifier BOY_RECIPE_ID = new Identifier("mca:baby_boy_smelting");
    public static final Identifier GIRL_RECIPE_ID = new Identifier("mca:baby_girl_smelting");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public Conditions conditionsFromJson(JsonObject json, Extended player, AdvancementEntityPredicateDeserializer deserializer) {
        NumberRange.IntRange c = NumberRange.IntRange.fromJson(json.get("count"));
        return new Conditions(player, c);
    }

    public void trigger(ServerPlayerEntity player, int c) {
        trigger(player, (conditions) -> conditions.test(c));
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final NumberRange.IntRange count;

        public Conditions(Extended player, NumberRange.IntRange count) {
            super(BabySmeltedCriterion.ID, player);
            this.count = count;
        }

        public boolean test(int c) {
            return count.test(c);
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer serializer) {
            JsonObject json = super.toJson(serializer);
            json.add("count", count.toJson());
            return json;
        }
    }
}
