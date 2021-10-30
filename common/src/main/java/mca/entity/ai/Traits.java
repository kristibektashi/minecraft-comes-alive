package mca.entity.ai;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import mca.Config;
import mca.entity.VillagerLike;
import mca.util.network.datasync.CDataManager;
import mca.util.network.datasync.CDataParameter;
import mca.util.network.datasync.CParameter;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class Traits {
    private static final CDataParameter<NbtCompound> TRAITS = CParameter.create("traits", new NbtCompound());

    public enum Trait {
        COLOR_BLIND(1.0f, 0.5f),
        HETEROCHROMIA(1.0f, 2.0f),
        LACTOSE_INTOLERANCE(1.0f, 1.0f),
        COELIAC_DISEASE(1.0f, 1.0f),
        DIABETES(1.0f, 1.0f),
        DWARFISM(1.0f, 1.0f),
        ALBINISM(1.0f, 1.0f),
        SIRBEN(0.1f, 1.0f);

        private final float chance;
        private final float inherit;

        Trait(float chance, float inherit) {
            this.chance = chance;
            this.inherit = inherit;
        }

        public Text getName() {
            return new TranslatableText("trait." + name().toLowerCase());
        }

        public Text getDescription() {
            return new TranslatableText("traitDescription." + name().toLowerCase());
        }
    }

    public static <E extends Entity> CDataManager.Builder<E> createTrackedData(CDataManager.Builder<E> builder) {
        return builder.addAll(TRAITS);
    }

    private Random random = new Random();

    private final VillagerLike<?> entity;

    public Traits(VillagerLike<?> entity) {
        this.entity = entity;
    }

    public Set<Trait> getTraits() {
        return entity.getTrackedValue(TRAITS).getKeys().stream().map(Trait::valueOf).collect(Collectors.toSet());
    }

    public Set<Trait> getInheritedTraits() {
        return getTraits().stream().filter(t -> t.inherit * Config.getInstance().traitInheritChance < random.nextFloat()).collect(Collectors.toSet());
    }

    public boolean hasTrait(Trait trait) {
        return entity.getTrackedValue(TRAITS).contains(trait.name());
    }

    public void addTrait(Trait trait) {
        entity.getTrackedValue(TRAITS).putBoolean(trait.name(), true);
    }

    public void removeTrait(Trait trait) {
        entity.getTrackedValue(TRAITS).remove(trait.name());
    }

    //initializes the genes with random numbers
    public void randomize(VillagerLike<?> entity) {
        for (Trait t : Trait.values()) {
            if (random.nextFloat() * t.chance < Config.getInstance().traitChance) {
                entity.getTraits().addTrait(t);
            }
        }
    }

    public void inherit(Traits from) {
        for (Trait t : from.getInheritedTraits()) {
            addTrait(t);
        }
    }

    public void inherit(Traits from, long seed) {
        Random old = random;
        random = new Random(seed);
        inherit(from);
        random = old;
    }

    public float getVerticalScaleFactor() {
        return hasTrait(Trait.DWARFISM) ? 0.65f : 1.0f;
    }

    public float getHorizontalScaleFactor() {
        return hasTrait(Trait.DWARFISM) ? 0.85f : 1.0f;
    }
}