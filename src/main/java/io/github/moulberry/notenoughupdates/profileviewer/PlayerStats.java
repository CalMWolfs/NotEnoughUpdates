/*
 * Copyright (C) 2022-2024 NotEnoughUpdates contributors
 *
 * This file is part of NotEnoughUpdates.
 *
 * NotEnoughUpdates is free software: you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * NotEnoughUpdates is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with NotEnoughUpdates. If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.moulberry.notenoughupdates.profileviewer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.moulberry.notenoughupdates.NotEnoughUpdates;
import io.github.moulberry.notenoughupdates.miscfeatures.PetInfoOverlay;
import io.github.moulberry.notenoughupdates.profileviewer.info.QuiverInfo;
import io.github.moulberry.notenoughupdates.util.Constants;
import io.github.moulberry.notenoughupdates.util.PetLeveling;
import io.github.moulberry.notenoughupdates.util.Utils;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerStats {

	// Combat stats
	public static final String HEALTH = "health";
	public static final String DEFENCE = "defence";
	public static final String STRENGTH = "strength";
	public static final String INTELLIGENCE = "intelligence";
	public static final String CRIT_CHANCE = "crit_chance";
	public static final String CRIT_DAMAGE = "crit_damage";
	public static final String BONUS_ATTACK_SPEED = "bonus_attack_speed";
	public static final String ABILITY_DAMAGE = "ability_damage";
	public static final String TRUE_DEFENSE = "true_defense";
	public static final String FEROCITY = "ferocity";
	public static final String HEALTH_REGEN = "health_regen";
	public static final String VITALITY = "vitality";
	public static final String MENDING = "mending";
	public static final String SWING_RANGE = "swing_range";

	// Gathering stats
	public static final String MINING_SPEED = "mining_speed";
	public static final String MINING_FORTUNE = "mining_fortune";
	public static final String FARMING_FORTUNE = "farming_fortune";
	public static final String FORAGING_FORTUNE = "foraging_fortune";
	public static final String BREAKING_POWER = "breaking_power";
	public static final String PRISTINE = "pristine";
	public static final String WHEAT_FORTUNE = "wheat_fortune";
	public static final String CARROT_FORTUNE = "carrot_fortune";
	public static final String POTATO_FORTUNE = "potato_fortune";
	public static final String PUMPKIN_FORTUNE = "pumpkin_fortune";
	public static final String MELON_FORTUNE = "melon_fortune";
	public static final String MUSHROOM_FORTUNE = "mushroom_fortune";
	public static final String CACTUS_FORTUNE = "cactus_fortune";
	public static final String SUGAR_CANE_FORTUNE = "sugar_cane_fortune";
	public static final String NETHER_WART_FORTUNE = "nether_wart_fortune";
	public static final String COCOA_BEANS_FORTUNE = "cocoa_beans_fortune";

	// Wisdom stats
	public static final String COMBAT_WISDOM = "combat_wisdom";
	public static final String MINING_WISDOM = "mining_wisdom";
	public static final String FARMING_WISDOM = "farming_wisdom";
	public static final String FORAGING_WISDOM = "foraging_wisdom";
	public static final String FISHING_WISDOM = "fishing_wisdom";
	public static final String ENCHANTING_WISDOM = "enchanting_wisdom";
	public static final String ALCHEMY_WISDOM = "alchemy_wisdom";
	public static final String CARPENTRY_WISDOM = "carpentry_wisdom";
	public static final String RUNECRAFTING_WISDOM = "runecrafting_wisdom";
	public static final String SOCIAL_WISDOM = "social_wisdom";
	public static final String TAMING_WISDOM = "taming_wisdom";

	// Misc stats
	public static final String SPEED = "speed";
	public static final String MAGIC_FIND = "magic_find";
	public static final String PET_LUCK = "pet_luck";
	public static final String SEA_CREATURE_CHANCE = "sea_creature_chance";
	public static final String DOUBLE_HOOK_CHANCE = "double_hook_chance";
	public static final String FISHING_SPEED = "fishing_speed";
	public static final String COLD_RESISTANCE = "cold_resistance";
	public static final String BONUS_PEST_CHANCE = "bonus_pest_chance";

	public static final String[] defaultStatNames = new String[]{
		HEALTH,
		DEFENCE,
		STRENGTH,
		INTELLIGENCE,
		CRIT_CHANCE,
		CRIT_DAMAGE,
		BONUS_ATTACK_SPEED,
		ABILITY_DAMAGE,
		TRUE_DEFENSE,
		FEROCITY,
		HEALTH_REGEN,
		VITALITY,
		MENDING,
		SWING_RANGE,

		MINING_SPEED,
		MINING_FORTUNE,
		FARMING_FORTUNE,
		FORAGING_FORTUNE,
		BREAKING_POWER,
		PRISTINE,
		WHEAT_FORTUNE,
		CARROT_FORTUNE,
		POTATO_FORTUNE,
		PUMPKIN_FORTUNE,
		MELON_FORTUNE,
		MUSHROOM_FORTUNE,
		CACTUS_FORTUNE,
		SUGAR_CANE_FORTUNE,
		NETHER_WART_FORTUNE,
		COCOA_BEANS_FORTUNE,

		COMBAT_WISDOM,
		MINING_WISDOM,
		FARMING_WISDOM,
		FORAGING_WISDOM,
		FISHING_WISDOM,
		ENCHANTING_WISDOM,
		ALCHEMY_WISDOM,
		CARPENTRY_WISDOM,
		RUNECRAFTING_WISDOM,
		SOCIAL_WISDOM,
		TAMING_WISDOM,

		SPEED,
		MAGIC_FIND,
		PET_LUCK,
		SEA_CREATURE_CHANCE,
		DOUBLE_HOOK_CHANCE,
		FISHING_SPEED,
		COLD_RESISTANCE,
		BONUS_PEST_CHANCE,
	};
	public static final String[] defaultStatNamesPretty = new String[]{
		EnumChatFormatting.RED + "❤ Health",
		EnumChatFormatting.GREEN + "❈ Defence",
		EnumChatFormatting.RED + "❁ Strength",
		EnumChatFormatting.AQUA + "✎ Intelligence",
		EnumChatFormatting.BLUE + "☣ Crit Chance",
		EnumChatFormatting.BLUE + "☠ Crit Damage",
		EnumChatFormatting.YELLOW + "⚔ Bonus Attack Speed",
		EnumChatFormatting.RED + "๑ Ability Damage",
		EnumChatFormatting.WHITE + "❂ True Defense",
		EnumChatFormatting.RED + "⫽ Ferocity",
		EnumChatFormatting.RED + "❣ Health Regen",
		EnumChatFormatting.DARK_RED + "♨ Vitality",
		EnumChatFormatting.GREEN + "☄ Mending",
		EnumChatFormatting.YELLOW + "Ⓢ Swing Range",

		EnumChatFormatting.GOLD + "⸕ Mining Speed",
		EnumChatFormatting.GOLD + "☘ Mining Fortune",
		EnumChatFormatting.GOLD + "☘ Farming Fortune",
		EnumChatFormatting.GOLD + "☘ Foraging Fortune",
		EnumChatFormatting.DARK_GREEN + "Ⓟ Breaking Power",
		EnumChatFormatting.DARK_PURPLE + "✧ Pristine",
		EnumChatFormatting.GOLD + "☘ Wheat Fortune",
		EnumChatFormatting.GOLD + "☘ Carrot Fortune",
		EnumChatFormatting.GOLD + "☘ Potato Fortune",
		EnumChatFormatting.GOLD + "☘ Pumpkin Fortune",
		EnumChatFormatting.GOLD + "☘ Melon Fortune",
		EnumChatFormatting.GOLD + "☘ Mushroom Fortune",
		EnumChatFormatting.GOLD + "☘ Cactus Fortune",
		EnumChatFormatting.GOLD + "☘ Sugar Cane Fortune",
		EnumChatFormatting.GOLD + "☘ Nether Wart Fortune",
		EnumChatFormatting.GOLD + "☘ Cocoa Beans Fortune",

		EnumChatFormatting.DARK_AQUA + "☯ Combat Wisdom",
		EnumChatFormatting.DARK_AQUA + "☯ Mining Wisdom",
		EnumChatFormatting.DARK_AQUA + "☯ Farming Wisdom",
		EnumChatFormatting.DARK_AQUA + "☯ Foraging Wisdom",
		EnumChatFormatting.DARK_AQUA + "☯ Fishing Wisdom",
		EnumChatFormatting.DARK_AQUA + "☯ Enchanting Wisdom",
		EnumChatFormatting.DARK_AQUA + "☯ Alchemy Wisdom",
		EnumChatFormatting.DARK_AQUA + "☯ Carpentry Wisdom",
		EnumChatFormatting.DARK_AQUA + "☯ Runecrafting Wisdom",
		EnumChatFormatting.DARK_AQUA + "☯ Social Wisdom",
		EnumChatFormatting.DARK_AQUA + "☯ Taming Wisdom",

		EnumChatFormatting.WHITE + "✦ Speed",
		EnumChatFormatting.AQUA + "✯ Magic Find",
		EnumChatFormatting.LIGHT_PURPLE + "♣ Pet Luck",
		EnumChatFormatting.DARK_AQUA + "α Sea Creature Chance",
		EnumChatFormatting.BLUE + "⚓ Double Hook Chance",
		EnumChatFormatting.AQUA + "☂ Fishing Speed",
		EnumChatFormatting.AQUA + "❄ Cold Resistance",
		EnumChatFormatting.DARK_GREEN + "ൠ Bonus Pest Chance"
	};

	public static final HashMap<String, Pattern> STAT_PATTERN_MAP = new HashMap<String, Pattern>() {
		{
			put(HEALTH, Pattern.compile("^Health" + STAT_PATTERN_END));
			put(DEFENCE, Pattern.compile("^Defence" + STAT_PATTERN_END));
			put(STRENGTH, Pattern.compile("^Strength" + STAT_PATTERN_END));
			put(INTELLIGENCE, Pattern.compile("^Intelligence" + STAT_PATTERN_END));
			put(CRIT_CHANCE, Pattern.compile("^Crit Chance" + STAT_PATTERN_END));
			put(CRIT_DAMAGE, Pattern.compile("^Crit Damage" + STAT_PATTERN_END));
			put(BONUS_ATTACK_SPEED, Pattern.compile("^Bonus Attack Speed" + STAT_PATTERN_END));
			put(ABILITY_DAMAGE, Pattern.compile("^Ability Damage" + STAT_PATTERN_END));
			put(TRUE_DEFENSE, Pattern.compile("^True Defense" + STAT_PATTERN_END));
			put(FEROCITY, Pattern.compile("^Ferocity" + STAT_PATTERN_END));
			put(HEALTH_REGEN, Pattern.compile("^Health Regen" + STAT_PATTERN_END));
			put(VITALITY, Pattern.compile("^Vitality" + STAT_PATTERN_END));
			put(MENDING, Pattern.compile("^Mending" + STAT_PATTERN_END));
			put(SWING_RANGE, Pattern.compile("^Swing Range" + STAT_PATTERN_END));

			put(MINING_SPEED, Pattern.compile("^Mining Speed" + STAT_PATTERN_END));
			put(MINING_FORTUNE, Pattern.compile("^Mining Fortune" + STAT_PATTERN_END));
			put(FARMING_FORTUNE, Pattern.compile("^Farming Fortune" + STAT_PATTERN_END));
			put(FORAGING_FORTUNE, Pattern.compile("^Foraging Fortune" + STAT_PATTERN_END));
			put(BREAKING_POWER, Pattern.compile("^Breaking Power" + STAT_PATTERN_END));
			put(PRISTINE, Pattern.compile("^Pristine" + STAT_PATTERN_END));
			put(WHEAT_FORTUNE, Pattern.compile("^Wheat Fortune" + STAT_PATTERN_END));
			put(CARROT_FORTUNE, Pattern.compile("^Carrot Fortune" + STAT_PATTERN_END));
			put(POTATO_FORTUNE, Pattern.compile("^Potato Fortune" + STAT_PATTERN_END));
			put(PUMPKIN_FORTUNE, Pattern.compile("^Pumpkin Fortune" + STAT_PATTERN_END));
			put(MELON_FORTUNE, Pattern.compile("^Melon Fortune" + STAT_PATTERN_END));
			put(MUSHROOM_FORTUNE, Pattern.compile("^Mushroom Fortune" + STAT_PATTERN_END));
			put(CACTUS_FORTUNE, Pattern.compile("^Cactus Fortune" + STAT_PATTERN_END));
			put(SUGAR_CANE_FORTUNE, Pattern.compile("^Sugar Cane Fortune" + STAT_PATTERN_END));
			put(NETHER_WART_FORTUNE, Pattern.compile("^Nether Wart Fortune" + STAT_PATTERN_END));
			put(COCOA_BEANS_FORTUNE, Pattern.compile("^Cocoa Beans Fortune" + STAT_PATTERN_END));

			put(COMBAT_WISDOM, Pattern.compile("^Combat Wisdom" + STAT_PATTERN_END));
			put(MINING_WISDOM, Pattern.compile("^Mining Wisdom" + STAT_PATTERN_END));
			put(FARMING_WISDOM, Pattern.compile("^Farming Wisdom" + STAT_PATTERN_END));
			put(FORAGING_WISDOM, Pattern.compile("^Foraging Wisdom" + STAT_PATTERN_END));
			put(FISHING_WISDOM, Pattern.compile("^Fishing Wisdom" + STAT_PATTERN_END));
			put(ENCHANTING_WISDOM, Pattern.compile("^Enchanting Wisdom" + STAT_PATTERN_END));
			put(ALCHEMY_WISDOM, Pattern.compile("^Alchemy Wisdom" + STAT_PATTERN_END));
			put(CARPENTRY_WISDOM, Pattern.compile("^Carpentry Wisdom" + STAT_PATTERN_END));
			put(RUNECRAFTING_WISDOM, Pattern.compile("^Runecrafting Wisdom" + STAT_PATTERN_END));
			put(SOCIAL_WISDOM, Pattern.compile("^Social Wisdom" + STAT_PATTERN_END));
			put(TAMING_WISDOM, Pattern.compile("^Taming Wisdom" + STAT_PATTERN_END));

			put(SPEED, Pattern.compile("^Speed" + STAT_PATTERN_END));
			put(MAGIC_FIND, Pattern.compile("^Magic Find" + STAT_PATTERN_END));
			put(PET_LUCK, Pattern.compile("^Pet Luck" + STAT_PATTERN_END));
			put(SEA_CREATURE_CHANCE, Pattern.compile("^Sea Creature Chance" + STAT_PATTERN_END));
			put(DOUBLE_HOOK_CHANCE, Pattern.compile("^Double Hook Chance" + STAT_PATTERN_END));
			put(FISHING_SPEED, Pattern.compile("^Fishing Speed" + STAT_PATTERN_END));
			put(COLD_RESISTANCE, Pattern.compile("^Cold Resistance" + STAT_PATTERN_END));
			put(BONUS_PEST_CHANCE, Pattern.compile("^Bonus Pest Chance" + STAT_PATTERN_END));
		}
	};

	public static final String STAT_PATTERN_END = ": ((?:\\+|-)([0-9]+(\\.[0-9]+)?))%?";

	public static Stats getBaseStats() {
		JsonObject misc = Constants.MISC;
		if (misc == null) return null;

		Stats stats = new Stats();
		for (String statName : defaultStatNames) {
			stats.addStat(statName, Utils.getElementAsFloat(Utils.getElement(misc, "base_stats." + statName), 0));
		}
		return stats;
	}

	private static Stats getFairyBonus(int fairyExchanges) {
		Stats bonus = new Stats();

		bonus.addStat(SPEED, fairyExchanges / 10);

		for (int i = 0; i < fairyExchanges; i++) {
			bonus.addStat(STRENGTH, (i + 1) % 5 == 0 ? 2 : 1);
			bonus.addStat(DEFENCE, (i + 1) % 5 == 0 ? 2 : 1);
			bonus.addStat(HEALTH, 3 + i / 2);
		}

		return bonus;
	}

	private static Stats getSkillBonus(Map<String, ProfileViewer.Level> skyblockInfo) {
		JsonObject bonuses = Constants.BONUSES;
		if (bonuses == null) return null;

		Stats skillBonus = new Stats();

		for (Map.Entry<String, ProfileViewer.Level> entry : skyblockInfo.entrySet()) {
			JsonElement element = Utils.getElement(bonuses, "bonus_stats." + entry.getKey());
			if (element != null && element.isJsonObject()) {
				JsonObject skillStatMap = element.getAsJsonObject();

				Stats currentBonus = new Stats();
				for (int i = 1; i <= entry.getValue().level; i++) {
					if (skillStatMap.has("" + i)) {
						currentBonus = new Stats();
						for (Map.Entry<String, JsonElement> entry2 : skillStatMap.get("" + i).getAsJsonObject().entrySet()) {
							currentBonus.addStat(entry2.getKey(), entry2.getValue().getAsFloat());
						}
					}
					skillBonus.add(currentBonus);
				}
			}
		}

		return skillBonus;
	}

	public static int getPetScore(JsonObject profile) {
		JsonObject bonuses = Constants.BONUSES;
		if (bonuses == null) {
			Utils.showOutdatedRepoNotification("bonuses.json");
			return 0;
		}
		JsonElement petsElement = Utils.getElement(profile, "pets");
		if (petsElement == null) return 0;

		JsonArray pets = petsElement.getAsJsonArray();

		HashMap<String, String> highestRarityMap = new HashMap<>();

		for (int i = 0; i < pets.size(); i++) {
			JsonObject pet = pets.get(i).getAsJsonObject();
			highestRarityMap.put(pet.get("type").getAsString(), pet.get("tier").getAsString());
		}

		int petScore = 0;
		for (String value : highestRarityMap.values()) {
			petScore += Utils.getElementAsFloat(Utils.getElement(bonuses, "pet_value." + value.toUpperCase(Locale.ROOT)), 0);
		}
		return petScore;
	}

	private static Stats getTamingBonus(JsonObject profile) {
		JsonObject bonuses = Constants.BONUSES;
		if (bonuses == null) return null;

		JsonElement petRewardsElement = Utils.getElement(bonuses, "pet_rewards");
		if (petRewardsElement == null) return null;
		JsonObject petRewards = petRewardsElement.getAsJsonObject();

		int petScore = getPetScore(profile);

		Stats petBonus = new Stats();
		for (int i = 0; i <= petScore; i++) {
			if (petRewards.has("" + i)) {
				petBonus = new Stats();
				for (Map.Entry<String, JsonElement> entry : petRewards.get("" + i).getAsJsonObject().entrySet()) {
					petBonus.addStat(entry.getKey(), entry.getValue().getAsFloat());
				}
			}
		}
		return petBonus;
	}

	private static float harpBonus(JsonObject profile) {
		String talk_to_melody = Utils.getElementAsString(
			Utils.getElement(profile, "objectives.talk_to_melody.status"),
			"INCOMPLETE"
		);
		if (talk_to_melody.equalsIgnoreCase("COMPLETE")) {
			return 26;
		} else {
			return 0;
		}
	}

	private static float hotmFortune(JsonObject profile, Map<String, ProfileViewer.Level> skyblockInfo) {
		int miningLevelFortune = (int) (4 * (float) Math.floor(skyblockInfo.get("mining").level));
		int miningFortuneStat = ((Utils.getElementAsInt(Utils.getElement(profile, "mining_core.nodes.mining_fortune"), 0)) *
			5);
		int miningFortune2Stat = ((Utils.getElementAsInt(
			Utils.getElement(profile, "mining_core.nodes.mining_fortune_2"),
			0
		)) * 5);
		return miningFortuneStat + miningFortune2Stat + miningLevelFortune;
	}

	private static float hotmSpeed(JsonObject profile) {
		int miningSpeedStat = ((Utils.getElementAsInt(Utils.getElement(profile, "mining_core.nodes.mining_speed"), 0)) *
			20);
		int miningSpeed2Stat = ((Utils.getElementAsInt(Utils.getElement(profile, "mining_core.nodes.mining_speed_2"), 0)) *
			40);
		return miningSpeedStat + miningSpeed2Stat;
	}

	public static Stats getPassiveBonuses(Map<String, ProfileViewer.Level> skyblockInfo, JsonObject profile) {
		Stats passiveBonuses = new Stats();

		Stats fairyBonus = getFairyBonus((int) Utils.getElementAsFloat(Utils.getElement(
			profile,
			"fairy_soul.fairy_exchanges"
		), 0));
		Stats skillBonus = getSkillBonus(skyblockInfo);
		Stats petBonus = getTamingBonus(profile);

		if (skillBonus == null || petBonus == null) {
			return null;
		}

		passiveBonuses.add(fairyBonus);
		passiveBonuses.add(skillBonus);
		passiveBonuses.addStat(INTELLIGENCE, harpBonus(profile));
		passiveBonuses.add(petBonus);

		return passiveBonuses;
	}

	public static Stats getHOTMBonuses(Map<String, ProfileViewer.Level> skyblockInfo, JsonObject profile) {
		Stats hotmBonuses = new Stats();

		hotmBonuses.addStat(MINING_FORTUNE, hotmFortune(profile, skyblockInfo));
		hotmBonuses.addStat(MINING_SPEED, hotmSpeed(profile));

		return hotmBonuses;
	}

	private static String getFullset(JsonArray armor, int ignore) {
		String fullset = null;
		for (int i = 0; i < armor.size(); i++) {
			if (i == ignore) continue;

			JsonElement itemElement = armor.get(i);
			if (itemElement == null || !itemElement.isJsonObject()) {
				fullset = null;
				break;
			}
			JsonObject item = itemElement.getAsJsonObject();
			String internalname = item.get("internalname").getAsString();

			String[] split = internalname.split("_");
			split[split.length - 1] = "";
			String armorname = StringUtils.join(split, "_");

			if (fullset == null) {
				fullset = armorname;
			} else if (!fullset.equalsIgnoreCase(armorname)) {
				fullset = null;
				break;
			}
		}
		return fullset;
	}

	private static Stats getSetBonuses(
		Stats stats,
		Map<String, JsonArray> inventoryInfo,
		Map<String, ProfileViewer.Level> skyblockInfo,
		JsonObject profile
	) {
		JsonArray armor = inventoryInfo.get("inv_armor");

		Stats bonuses = new Stats();

		String fullset = getFullset(armor, -1);

		if (fullset != null) {
			// TODO @nea: repo based stat delivery? (with lisp)
			switch (fullset) {
				case "LAPIS_ARMOR_":
					bonuses.addStat(HEALTH, 60);
					break;
				case "FAIRY_":
					bonuses.addStat(HEALTH, Utils.getElementAsFloat(Utils.getElement(profile, "fairy_souls_collected"), 0));
					break;
				case "SPEEDSTER_":
					bonuses.addStat(SPEED, 20);
					break;
				case "YOUNG_DRAGON_":
					bonuses.addStat(SPEED, 70);
					break;
				case "MASTIFF_":
					bonuses.addStat(HEALTH, 50 * Math.round(stats.get(CRIT_DAMAGE)));
					break;
				case "ANGLER_":
					bonuses.addStat(HEALTH, 10 * (float) Math.floor(skyblockInfo.get("fishing").level));
					bonuses.addStat(SEA_CREATURE_CHANCE, 4);
					break;
				case "ARMOR_OF_MAGMA_":
					int bonus = (int) Math.min(
						200,
						Math.floor(Utils.getElementAsFloat(Utils.getElement(profile, "stats.kills_magma_cube"), 0) / 10)
					);
					bonuses.addStat(HEALTH, bonus);
					bonuses.addStat(INTELLIGENCE, bonus);
				case "OLD_DRAGON_":
					bonuses.addStat(HEALTH, 200);
					bonuses.addStat(DEFENCE, 40);
					break;
			}
		}

		JsonElement chestplateElement = armor.get(2);
		if (chestplateElement != null && chestplateElement.isJsonObject()) {
			JsonObject chestplate = chestplateElement.getAsJsonObject();
			if (chestplate.get("internalname").getAsString().equals("OBSIDIAN_CHESTPLATE")) {
				JsonArray inventory = inventoryInfo.get("inv_contents");
				for (int i = 0; i < inventory.size(); i++) {
					JsonElement itemElement = inventory.get(i);
					if (itemElement != null && itemElement.isJsonObject()) {
						JsonObject item = itemElement.getAsJsonObject();
						if (item.get("internalname").getAsString().equals("OBSIDIAN")) {
							int count = 1;
							if (item.has("count")) {
								count = item.get("count").getAsInt();
							}
							bonuses.addStat(SPEED, count / 20);
						}
					}
				}
			}
		}

		return bonuses;
	}

	private static Stats getStatForItem(String internalname, JsonObject item, JsonArray lore) {
		Stats stats = new Stats();
		for (int i = 0; i < lore.size(); i++) {
			String line = lore.get(i).getAsString();
			for (Map.Entry<String, Pattern> entry : STAT_PATTERN_MAP.entrySet()) {
				Matcher matcher = entry.getValue().matcher(Utils.cleanColour(line));
				if (matcher.find()) {
					int bonus = Integer.parseInt(matcher.group(1));
					stats.addStat(entry.getKey(), bonus);
				}
			}
		}
		if (internalname.equals("DAY_CRYSTAL") || internalname.equals("NIGHT_CRYSTAL")) {
			stats.addStat(STRENGTH, 2.5f);
			stats.addStat(DEFENCE, 2.5f);
		}
		if (internalname.equals("NEW_YEAR_CAKE_BAG") && item.has("item_contents")) {
			JsonArray bytesArr = item.get("item_contents").getAsJsonArray();
			byte[] bytes = new byte[bytesArr.size()];
			for (int i = 0; i < bytesArr.size(); i++) {
				bytes[i] = bytesArr.get(i).getAsByte();
			}
			try {
				NBTTagCompound contents_nbt = CompressedStreamTools.readCompressed(new ByteArrayInputStream(bytes));
				NBTTagList items = contents_nbt.getTagList("i", 10);
				HashSet<Integer> cakes = new HashSet<>();
				for (int j = 0; j < items.tagCount(); j++) {
					if (items.getCompoundTagAt(j).getKeySet().size() > 0) {
						NBTTagCompound nbt = items.getCompoundTagAt(j).getCompoundTag("tag");
						if (nbt != null && nbt.hasKey("ExtraAttributes", 10)) {
							NBTTagCompound ea = nbt.getCompoundTag("ExtraAttributes");
							if (ea.hasKey("new_years_cake")) {
								cakes.add(ea.getInteger("new_years_cake"));
							}
						}
					}
				}
				stats.addStat(HEALTH, cakes.size());
			} catch (IOException e) {
				e.printStackTrace();
				return stats;
			}
		}
		return stats;
	}

	private static Stats getItemBonuses(boolean talismanOnly, JsonArray... inventories) {
		JsonObject misc = Constants.MISC;
		if (misc == null) return null;
		JsonElement talisman_upgrades_element = misc.get("talisman_upgrades");
		if (talisman_upgrades_element == null) return null;
		JsonObject talisman_upgrades = talisman_upgrades_element.getAsJsonObject();

		HashMap<String, Stats> itemBonuses = new HashMap<>();
		for (JsonArray inventory : inventories) {
			for (int i = 0; i < inventory.size(); i++) {
				JsonElement itemElement = inventory.get(i);
				if (itemElement != null && itemElement.isJsonObject()) {
					JsonObject item = itemElement.getAsJsonObject();
					String internalname = item.get("internalname").getAsString();
					if (itemBonuses.containsKey(internalname)) {
						continue;
					}
					if (!talismanOnly || Utils.checkItemType(
						item.get("lore").getAsJsonArray(),
						true,
						"ACCESSORY",
						"HATCESSORY"
					) >= 0) {
						Stats itemBonus = getStatForItem(internalname, item, item.get("lore").getAsJsonArray());

						itemBonuses.put(internalname, itemBonus);

						for (Map.Entry<String, JsonElement> talisman_upgrades_item : talisman_upgrades.entrySet()) {
							JsonArray upgrades = talisman_upgrades_item.getValue().getAsJsonArray();
							for (int j = 0; j < upgrades.size(); j++) {
								String upgrade = upgrades.get(j).getAsString();
								if (upgrade.equals(internalname)) {
									itemBonuses.put(talisman_upgrades_item.getKey(), new Stats());
									break;
								}
							}
						}
					}
				}
			}
		}
		Stats itemBonusesStats = new Stats();
		for (Stats stats : itemBonuses.values()) {
			itemBonusesStats.add(stats);
		}

		return itemBonusesStats;
	}

	public static Stats getPetStatBonuses(JsonObject petsInfo) {
		JsonObject petsJson = Constants.PETS;
		JsonObject petnums = Constants.PETNUMS;
		if (petsJson == null || petnums == null) return new Stats();

		if (
			petsInfo != null &&
				petsInfo.has("active_pet") &&
				petsInfo.get("active_pet") != null &&
				petsInfo.get("active_pet").isJsonObject()
		) {
			JsonObject pet = petsInfo.get("active_pet").getAsJsonObject();
			if (
				pet.has("type") &&
					pet.get("type") != null &&
					pet.has("tier") &&
					pet.get("tier") != null &&
					pet.has("exp") &&
					pet.get("exp") != null
			) {
				String petname = pet.get("type").getAsString();
				String tier = pet.get("tier").getAsString();
				String heldItem = Utils.getElementAsString(pet.get("heldItem"), null);

				if (!petnums.has(petname)) {
					return new Stats();
				}

				String tierNum = GuiProfileViewer.RARITY_TO_NUM.get(tier);
				float exp = pet.get("exp").getAsFloat();
				if (tierNum == null) return new Stats();

				if (
					pet.has("heldItem") &&
						!pet.get("heldItem").isJsonNull() &&
						pet.get("heldItem").getAsString().equals("PET_ITEM_TIER_BOOST")
				) {
					tierNum = "" + (Integer.parseInt(tierNum) + 1);
				}

				PetLeveling.PetLevel levelObj = PetLeveling.getPetLevelingForPet(
					petname,
					PetInfoOverlay.Rarity.valueOf(tier)
				).getPetLevel(exp);
				float level = levelObj.getCurrentLevel();
				float currentLevelRequirement = levelObj.getExpRequiredForNextLevel();
				float maxXP = levelObj.getMaxLevel();
				pet.addProperty("level", level);
				pet.addProperty("currentLevelRequirement", currentLevelRequirement);
				pet.addProperty("maxXP", maxXP);

				JsonObject petItem = NotEnoughUpdates.INSTANCE.manager.getItemInformation().get(petname + ";" + tierNum);
				if (petItem == null) return new Stats();

				Stats stats = new Stats();

				JsonObject petInfo = petnums.get(petname).getAsJsonObject();
				if (petInfo.has(tier)) {
					JsonObject petInfoTier = petInfo.get(tier).getAsJsonObject();
					if (petInfoTier == null || !petInfoTier.has("1") || !petInfoTier.has("100")) {
						return new Stats();
					}

					JsonObject min = petInfoTier.get("1").getAsJsonObject();
					JsonObject max = petInfoTier.get("100").getAsJsonObject();

					float minMix = (100 - level) / 99f;
					float maxMix = (level - 1) / 99f;

					for (Map.Entry<String, JsonElement> entry : max.get("statNums").getAsJsonObject().entrySet()) {
						float statMax = entry.getValue().getAsFloat();
						float statMin = min.get("statNums").getAsJsonObject().get(entry.getKey()).getAsFloat();
						float val = statMin * minMix + statMax * maxMix;

						stats.addStat(entry.getKey().toLowerCase(Locale.ROOT), (int) Math.floor(val));
					}
				}

				if (heldItem != null) {
					HashMap<String, Float> petStatBoots = GuiProfileViewer.PET_STAT_BOOSTS.get(heldItem);
					HashMap<String, Float> petStatBootsMult = GuiProfileViewer.PET_STAT_BOOSTS_MULT.get(heldItem);
					if (petStatBoots != null) {
						for (Map.Entry<String, Float> entryBoost : petStatBoots.entrySet()) {
							String key = entryBoost.getKey().toLowerCase(Locale.ROOT);
							try {
								stats.addStat(key, entryBoost.getValue());
							} catch (Exception ignored) {
							}
						}
					}
					if (petStatBootsMult != null) {
						for (Map.Entry<String, Float> entryBoost : petStatBootsMult.entrySet()) {
							String key = entryBoost.getKey().toLowerCase(Locale.ROOT);
							try {
								stats.scale(key, entryBoost.getValue());
							} catch (Exception ignored) {
							}
						}
					}
				}

				return stats;
			}
		}
		return new Stats();
	}

	private static float getStatMult(Map<String, JsonArray> inventoryInfo) {
		float mult = 1f;

		JsonArray armor = inventoryInfo.get("inv_armor");

		String fullset = getFullset(armor, -1);

		if (fullset != null && fullset.equals("SUPERIOR_DRAGON_")) {
			mult *= 1.05f;
		}

		for (int i = 0; i < armor.size(); i++) {
			JsonElement itemElement = armor.get(i);
			if (itemElement == null || !itemElement.isJsonObject()) continue;

			JsonObject item = itemElement.getAsJsonObject();
			String reforge = Utils.getElementAsString(Utils.getElement(item, "ExtraAttributes.modifier"), "");

			if (reforge.equals("renowned")) {
				mult *= 1.01f;
			}
		}

		return mult;
	}

	private static void applyLimits(Stats stats, Map<String, JsonArray> inventoryInfo) {
		//>0
		JsonArray armor = inventoryInfo.get("inv_armor");

		String fullset = getFullset(armor, 3);

		if (fullset != null) {
			switch (fullset) {
				case "CHEAP_TUXEDO_":
					stats.statsJson.add(HEALTH, new JsonPrimitive(Math.min(75, stats.get(HEALTH))));
				case "FANCY_TUXEDO_":
					stats.statsJson.add(HEALTH, new JsonPrimitive(Math.min(150, stats.get(HEALTH))));
				case "ELEGANT_TUXEDO_":
					stats.statsJson.add(HEALTH, new JsonPrimitive(Math.min(250, stats.get(HEALTH))));
			}
		}

		for (Map.Entry<String, JsonElement> statEntry : stats.statsJson.entrySet()) {
			if (
				statEntry.getKey().equals(CRIT_DAMAGE) ||
					statEntry.getKey().equals(INTELLIGENCE) ||
					statEntry.getKey().equals(BONUS_ATTACK_SPEED)
			) continue;
			stats.statsJson.add(statEntry.getKey(), new JsonPrimitive(Math.max(0, statEntry.getValue().getAsFloat())));
		}
	}

	public static Stats getStats(
		Map<String, ProfileViewer.Level> levelingInfo,
		Map<String, JsonArray> inventoryInfo,
		JsonObject petsInfo,
		JsonObject profile
	) {
		if (levelingInfo == null || inventoryInfo == null || profile == null) return null;

		JsonArray armor = inventoryInfo.get("inv_armor");
		JsonArray inventory = inventoryInfo.get("inv_contents");
		JsonArray talisman_bag = inventoryInfo.get("talisman_bag");

		Stats passiveBonuses = getPassiveBonuses(levelingInfo, profile);
		Stats hotmBonuses = getHOTMBonuses(levelingInfo, profile);
		Stats armorBonuses = getItemBonuses(false, armor);
		Stats talismanBonuses = getItemBonuses(true, inventory, talisman_bag);

		if (passiveBonuses == null || armorBonuses == null || talismanBonuses == null) {
			return null;
		}

		Stats stats = getBaseStats();
		if (stats == null) {
			return null;
		}

		Stats petBonus = getPetStatBonuses(petsInfo);

		stats = stats.add(passiveBonuses).add(armorBonuses).add(talismanBonuses).add(petBonus).add(hotmBonuses);

		stats.add(getSetBonuses(stats, inventoryInfo, levelingInfo, profile));

		stats.scaleAll(getStatMult(inventoryInfo));

		applyLimits(stats, inventoryInfo);

		return stats;
	}

	/**
	 * Finds the Magical Power the player selected if applicable
	 *
	 * @param profileInfo profile information object
	 * @return selected magical power as a String or null
	 * @see SkyblockProfiles.SkyblockProfile#getLevelingInfo()
	 */
	public static @Nullable String getSelectedMagicalPower(JsonObject profileInfo) {
		String abs = "accessory_bag_storage";

		if (
			profileInfo == null ||
				!profileInfo.has(abs) ||
				!profileInfo.get(abs).isJsonObject() ||
				!profileInfo.get(abs).getAsJsonObject().has("selected_power")
		) {
			return null;
		}
		String selectedPower = profileInfo.get(abs).getAsJsonObject().get("selected_power").getAsString();
		return selectedPower.substring(0, 1).toUpperCase(Locale.ROOT) + selectedPower.substring(1);
	}

	public static @Nullable QuiverInfo getQuiverInfo(Map<String, JsonArray> inventoryInfo, JsonObject profileInfo) {
		if (inventoryInfo == null
			|| !inventoryInfo.containsKey("quiver")) {
			return null;
		}
		QuiverInfo quiverInfo = new QuiverInfo();
		quiverInfo.arrows = new HashMap<>();

		JsonArray quiver = inventoryInfo.get("quiver");
		for (JsonElement quiverEntry : quiver) {
			if (quiverEntry == null || quiverEntry.isJsonNull() || !quiverEntry.isJsonObject()) {
				continue;
			}
			JsonObject stack = quiverEntry.getAsJsonObject();
			if (!stack.has("internalname") || !stack.has("count")) {
				continue;
			}
			String internalName = stack.get("internalname").getAsString();
			int count = stack.get("count").getAsInt();

			quiverInfo.arrows.computeIfPresent(internalName, (key, existing) -> existing + count);
			quiverInfo.arrows.putIfAbsent(internalName, count);
		}

		quiverInfo.selectedArrow = Utils.getElementAsString(
			Utils.getElement(profileInfo, "item_data.favorite_arrow"),
			null
		);

		return quiverInfo;
	}

	public static class Stats {

		JsonObject statsJson = new JsonObject();

		/*public float health;
		public float defence;
		public float strength;
		public float speed;
		public float crit_chance;
		public float crit_damage;
		public float bonus_attack_speed;
		public float intelligence;
		public float sea_creature_chance;
		public float magic_find;
		public float pet_luck;*/

		public Stats(Stats... statses) {
			for (Stats stats : statses) {
				add(stats);
			}
		}

		/*@Override
		public String toString() {
			return String.format("{health=%s,defence=%s,strength=%s,speed=%s,crit_chance=%s,crit_damage=%s," +
					"bonus_attack_speed=%s,intelligence=%s,sea_creature_chance=%s,magic_find=%s,pet_luck=%s}",
				stats.get("health"), defence, strength, speed, crit_chance, crit_damage, bonus_attack_speed, intelligence,
				sea_creature_chance, magic_find, pet_luck
			);
		}*/

		public int size() {
			return statsJson.entrySet().size();
		}

		public float get(String statName) {
			if (statsJson.has(statName)) {
				return statsJson.get(statName).getAsFloat();
			} else {
				return 0;
			}
		}

		public Stats add(Stats stats) {
			for (Map.Entry<String, JsonElement> statEntry : stats.statsJson.entrySet()) {
				if (statEntry.getValue().isJsonPrimitive() && ((JsonPrimitive) statEntry.getValue()).isNumber()) {
					if (!statsJson.has(statEntry.getKey())) {
						statsJson.add(statEntry.getKey(), statEntry.getValue());
					} else {
						JsonPrimitive e = statsJson.get(statEntry.getKey()).getAsJsonPrimitive();
						float statNum = e.getAsFloat() + statEntry.getValue().getAsFloat();
						statsJson.add(statEntry.getKey(), new JsonPrimitive(statNum));
					}
				}
			}
			return this;
		}

		public void scale(String statName, float scale) {
			if (statsJson.has(statName)) {
				statsJson.add(statName, new JsonPrimitive(statsJson.get(statName).getAsFloat() * scale));
			}
		}

		public void scaleAll(float scale) {
			for (Map.Entry<String, JsonElement> statEntry : statsJson.entrySet()) {
				statsJson.add(statEntry.getKey(), new JsonPrimitive(statEntry.getValue().getAsFloat() * scale));
			}
		}

		public void addStat(String statName, float amount) {
			if (!statsJson.has(statName)) {
				statsJson.add(statName, new JsonPrimitive(amount));
			} else {
				JsonPrimitive e = statsJson.get(statName).getAsJsonPrimitive();
				statsJson.add(statName, new JsonPrimitive(e.getAsFloat() + amount));
			}
		}
	}
}
