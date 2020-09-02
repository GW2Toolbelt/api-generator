/*
 * Copyright (c) 2019-2020 Leon Linhart
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
@file:Suppress("DuplicatedCode", "FunctionName")
package com.gw2tb.apigen.internal.spec

import com.gw2tb.apigen.internal.dsl.*
import com.gw2tb.apigen.model.TokenScope.*
import com.gw2tb.apigen.model.v2.V2SchemaVersion.*
import kotlin.time.*

internal val GW2v2 = GW2APIVersion {
    "/Account" {
        summary = "Returns information about a player's account."
        security(ACCOUNT)

        schema(record(description = "Information about a player's account.") {
            CamelCase("id").."ID"(STRING, "the unique persistent account GUID")
            "Age"(INTEGER, "the age of the account in seconds")
            "Name"(STRING, "the unique account name")
            "World"(INTEGER, "the ID of the home world the account is assigned to")
            "Guilds"(array(STRING), "an array containing the IDs of all guilds the account is a member in")
            optional(GUILDS)..SerialName("guild_leader").."GuildLeader"(array(STRING), "an array containing the IDs of all guilds the account is a leader of")
            "Created"(STRING, "the ISO-8601 standard timestamp of when the account was created")
            "Access"(array(STRING), "an array of what content this account has access to")
            "Commander"(BOOLEAN, "a flag indicating whether or not the commander tag is unlocked for the account")
            optional(PROGRESSION)..SerialName("fractal_level").."FractalLevel"(INTEGER, "the account's personal fractal level")
            optional(PROGRESSION)..SerialName("daily_ap").."DailyAP"(INTEGER, "the daily AP the account has")
            optional(PROGRESSION)..SerialName("monthly_ap").."MonthlyAP"(INTEGER, "the monthly AP the account has")
            optional(PROGRESSION)..CamelCase("wvwRank")..SerialName("wvw_rank").."WvWRank"(INTEGER, "the account's personal wvw rank")
            since(V2_SCHEMA_2019_02_21T00_00_00_000Z)..SerialName("last_modified").."LastModified"(STRING, "the ISO-8601 standard timestamp of when the account information last changed (as perceived by the API)")
        })
    }
    "/Account/Achievements" {
        summary = "Returns a player's progress towards all their achievements."
        security = setOf(ACCOUNT, PROGRESSION)

        schema(array(
            description = "A list of progress information towards all achievements the account has made progress.",
            items = record(description = "Information about a player's progress towards an achievement.") {
                CamelCase("id").."ID"(INTEGER, "the achievement's ID")
                "Done"(BOOLEAN, "a flag indicating whether or not the account has completed the achievement")
                optional.."Bits"(array(INTEGER), "an array of numbers (whose exact meaning differs) giving information about the progress towards an achievement")
                optional.."Current"(INTEGER, "the account's current progress towards the achievement")
                optional.."Max"(INTEGER, "the amount of progress required to complete the achievement")
                optional.."Repeated"(INTEGER, "the number of times the achievement has been completed (if the achievement is repeatable)")
                optional.."Unlocked"(BOOLEAN, "a flag indicating whether or not the achievement is unlocked (if the achievement can be unlocked)")
            }
        ))
    }
    "/Account/DailyCrafting" {
        summary = "Returns which items that can be crafted once per day a player crafted since the most recent daily reset."
        security = setOf(ACCOUNT, PROGRESSION, UNLOCKS)

        schema(array(STRING, "a list of dailycrafting IDs of the items that can be crafted once per day which the player has crafted since the most recent daily reset"))
    }
    "/Account/Dungeons" {
        summary = "Returns which dungeons paths a player has completed since the most recent daily reset."
        security = setOf(ACCOUNT, PROGRESSION)

        schema(array(STRING, "an array of IDs containing an ID for each dungeon path that the player has completed since the most recent daily reset"))
    }
    "/Account/Dyes" {
        summary = "Returns information about a player's unlocked dyes."
        security = setOf(ACCOUNT, UNLOCKS)

        schema(array(INTEGER, "a list of the dye IDs of the account's unlocked dyes"))
    }
    "/Account/Emotes" {
        summary = "Returns information about a player's unlocked emotes."
        security = setOf(ACCOUNT)

        schema(array(INTEGER, "an array of IDs containing the ID of each emote unlocked by the player"))
    }
    "/Account/Finishers" {
        summary = "Returns information about a player's unlocked finishers."
        security = setOf(ACCOUNT, UNLOCKS)

        schema(array(INTEGER, "an array of IDs containing the ID of each finisher unlocked by the player"))
    }
    "/Account/Gliders" {
        summary = "Returns information about a player's unlocked gliders."
        security = setOf(ACCOUNT, UNLOCKS)

        schema(array(INTEGER, "an array of IDs containing the ID of each glider unlocked by the player"))
    }
    "/Account/Home/Nodes" {
        summary = "Returns information about a player's unlocked home instance nodes."
        security = setOf(ACCOUNT, PROGRESSION)

        schema(array(STRING, "an array of IDs containing th ID of each home instance node unlocked by the player"))
    }
    "/Account/Inventory" {
        summary = "Returns information about a player's shared inventory slots."
        security = setOf(ACCOUNT, INVENTORIES)

        schema(array(
            description = "A list of stacks of items in an account's shared inventory.",
            items = record(description = "Information about a stack of items in a player's shared inventory.") {
                CamelCase("id").."ID"(INTEGER, "the item's ID")
                "Count"(INTEGER, "the amount of items in the stack")
                optional.."Charges"(INTEGER, "the amount of charges remaining on the item")
                optional.."Skin"(INTEGER, "the ID of the skin applied to the item")
                optional.."Upgrades"(array(INTEGER), "the array of item IDs of runes or sigils applied to the item")
                optional.."Infusions"(array(INTEGER), "the array of item IDs of infusions applied to the item")
                optional.."Stats"(
                    description = "information about the stats chosen for the item (if the item offers the option to select stats/prefix)",
                    type = record("Information about an item's stats.") {
                        CamelCase("id").."ID"(INTEGER, "the itemstat ID")
                        optional..SerialName("Power").."Power"(INTEGER, "the amount of power given by the item")
                        optional..SerialName("Precision").."Precision"(INTEGER, "the amount of precision given by the item")
                        optional..SerialName("Toughness").."Toughness"(INTEGER, "the amount of toughness given by the item")
                        optional..SerialName("Vitality").."Vitality"(INTEGER, "the amount of vitality given by the item")
                        optional..SerialName("ConditionDamage").."ConditionDamage"(INTEGER, "the amount of condition damage given by the item")
                        optional..SerialName("ConditionDuration").."ConditionDuration"(INTEGER, "the amount of condition duration given by the item")
                        optional..SerialName("Healing").."Healing"(INTEGER, "the amount of healing given by the item")
                        optional..SerialName("BoonDuration").."BoonDuration"(INTEGER, "the amount of boon duration given by the item")
                    }
                )
                optional.."Binding"(STRING, "the binding of the item")
            }
        ))
    }
    "/Account/Mailcarriers" {
        summary = "Returns information about a player's unlocked mail carriers."
        security = setOf(ACCOUNT, UNLOCKS)

        schema(array(INTEGER, "an array of IDs containing the ID of each mail carrier unlocked by the player"))
    }
    "/Account/MapChests" {
        summary = "Returns which Hero's Choice Chests a player has acquired since the most recent daily reset."
        security = setOf(ACCOUNT, PROGRESSION)

        schema(array(STRING, "an array of IDs for Hero's Choice Chests that the player has acquired since the most recent daily reset"))
    }
    "/Account/Masteries" {
        summary = "Returns information about a player's unlocked masteries."
        security = setOf(ACCOUNT, PROGRESSION)

        schema(array(
            description = "A list of all masteries unlocked by an account.",
            items = record(description = "Information about a player's unlocked mastery.") {
                CamelCase("id").."ID"(INTEGER, "the mastery's ID")
                optional.."Level"(INTEGER, "the index of the unlocked mastery level")
            }
        ))
    }
    "/Account/Mastery/Points" {
        summary = "Returns information about a player's unlocked mastery points."
        security = setOf(ACCOUNT, PROGRESSION)

        schema(record(description = "Information about a player's unlocked mastery points for a region.") {
            "Totals"(
                description = "information about the total mastery points for a region",
                type = array(record(description = "Information about the mastery points for a region.") {
                    "Region"(STRING, "the mastery region")
                    "Spent"(INTEGER, "the amount of mastery points of this region spent in mastery tracks")
                    "Earned"(INTEGER, "the amount of mastery points of this region earned for the account")
                })
            )
            "Unlocked"(array(INTEGER), "the list of IDs of unlocked mastery points")
        })
    }
    "/Account/Materials" {
        summary = "Returns information about the materials stored in a player's vault."
        security = setOf(ACCOUNT, INVENTORIES)

        schema(array(
            description = "A list of all materials in an account's vault.",
            items = record(description = "Information about a stack of materials in a player's vault.") {
                CamelCase("id").."ID"(INTEGER, "the material's item ID")
                "Category"(INTEGER, "the material category the item belongs to")
                "Count"(INTEGER, "the number of the material that is stored in the player's vault")
                optional.."Binding"(STRING, "the binding of the material")
            }
        ))
    }
    "/Account/Minis" {
        summary = "Returns information about a player's unlocked miniatures."
        security = setOf(ACCOUNT, UNLOCKS)

        schema(array(INTEGER, "an array of IDs containing the ID of each miniature unlocked by the player"))
    }
    "/Account/Mounts/Skins" {
        summary = "Returns information about a player's unlocked mount skins."
        security = setOf(ACCOUNT, UNLOCKS)

        schema(array(STRING, "an array of IDs containing the ID of each mount skin unlocked by the player"))
    }
    "/Account/Mounts/Types" {
        summary = "Returns information about a player's unlocked mounts."
        security = setOf(ACCOUNT, UNLOCKS)

        schema(array(STRING, "an array of IDs containing the ID of each mount unlocked by the player"))
    }
    "/Account/Novelties" {
        summary = "Returns information about a player's unlocked novelties."
        security = setOf(ACCOUNT, UNLOCKS)

        schema(array(INTEGER, "an array of IDs containing the ID of each novelty unlocked by the player"))
    }
    "/Account/Outfits" {
        summary = "Returns information about a player's unlocked outfits."
        security = setOf(ACCOUNT, UNLOCKS)

        schema(array(INTEGER, "an array of IDs containing the ID of each outfit unlocked by the player"))
    }
    "/Account/PvP/Heroes" {
        summary = "Returns information about a player's unlocked PvP heroes."
        security = setOf(ACCOUNT, UNLOCKS)

        schema(array(INTEGER, "an array of IDs containing the ID of each PvP hero unlocked by the player"))
    }
    "/Account/Raids" {
        summary = "Returns which raid encounter a player has cleared since the most recent raid reset."
        security = setOf(ACCOUNT, PROGRESSION)

        schema(array(STRING, "an array of IDs containing the ID of each raid encounter that the player has cleared since the most recent raid reset"))
    }
    "/Account/Recipes" {
        summary = "Returns information about a player's unlocked recipes."
        security = setOf(ACCOUNT, UNLOCKS)

        schema(array(INTEGER, "an array of IDs containing the ID of each recipe unlocked by the player"))
    }
    "/Account/Skins" {
        summary = "Returns information about a player's unlocked skins."
        security = setOf(ACCOUNT, UNLOCKS)

        schema(array(INTEGER, "an array of IDs containing the ID of each skin unlocked by the player"))
    }
    "/Account/Titles" {
        summary = "Returns information about a player's unlocked titles."
        security = setOf(ACCOUNT, UNLOCKS)

        schema(array(INTEGER, "an array of IDs containing the ID of each title unlocked by the player"))
    }
    "/Account/Wallet" {
        summary = "Returns information about a player's wallet."
        security = setOf(ACCOUNT, WALLET)

        schema(array(
            description = "A list of all currencies in an account's wallet.",
            items = record(description = "Information about a currency in a player's wallet.") {
                CamelCase("id").."ID"(INTEGER, "the currency ID that can be resolved against /v2/currencies")
                "Value"(INTEGER, "the amount of this currency in the player's wallet")
            }
        ))
    }
    "/Account/WorldBosses" {
        summary = "Returns which world bosses that can be looted once per day a player has defeated since the most recent daily reset."
        security = setOf(ACCOUNT, PROGRESSION)

        schema(array(STRING, "an array of IDs for each world boss that can be looted once per day that the player has defeated since the most recent daily reset"))
    }
    "/Build" {
        summary = "Returns the current build ID."

        schema(record(description = "Information about the current game build.") {
            CamelCase("id").."ID"(INTEGER, "the current build ID")
        })
    }
//    "/Characters" {
//        summary = ""
//        security = setOf(ACCOUNT, CHARACTERS)
//
//        supportedQueries(BY_ID, BY_IDS, BY_PAGE)
//        schema(map {
//            "Name"(STRING, "the character's name")
//            "Race"(STRING, "the ID of the character's race")
//            "Gender"(STRING, "the character's gender")
//            "Flags"(INTEGER, "") // TODO array of something ?
//            "Profession"(STRING, "the ID of the character's profession")
//            "Level"(INTEGER, "the character's level")
//            "Guild"(STRING, "")
//        })
//    }
    "/Characters/:ID/Inventory" {
        summary = "Returns information about a character's inventory."
        security = setOf(ACCOUNT, CHARACTERS, INVENTORIES)

        pathParameter("ID", STRING, "the character's ID")
        schema(record(description = "Information about a bag in a character's inventory.") {
            "Bags"(
                description = "the character's inventory bags",
                type = array(record(description = "Information about an inventory bag.") {
                    CamelCase("id").."ID"(INTEGER, "the bag's item ID")
                    "Size"(INTEGER, "the bag's size")
                    "Inventory"(
                        description = "the bag's content",
                        type = array(record(description = "Information about an item in a character's inventory.") {
                            CamelCase("id").."ID"(INTEGER, "the item's ID")
                            "Count"(INTEGER, "the amount of items in the stack")
                            optional.."Charges"(INTEGER, "the amount of charges remaining on the item")
                            optional.."Skin"(INTEGER, "the ID of the skin applied to the item")
                            optional.."Upgrades"(array(INTEGER), "an array of item IDs for each rune or signet applied to the item")
                            optional..SerialName("upgrade_slot_indices").."UpgradeSlotIndices"(array(INTEGER), "") // TODO description: figure out what this actually describes
                            optional.."Infusions"(array(INTEGER), "an array of item IDs for each infusion applied to the item")
                            optional.."Stats"(
                                description = "contains information on the stats chosen if the item offers an option for stats/prefix",
                                type = record(description = "Information about an item's stats.") {
                                    CamelCase("id").."ID"(INTEGER, "the itemstat ID")
                                    optional..SerialName("Power").."Power"(INTEGER, "the amount of power given by the item")
                                    optional..SerialName("Precision").."Precision"(INTEGER, "the amount of precision given by the item")
                                    optional..SerialName("Toughness").."Toughness"(INTEGER, "the amount of toughness given by the item")
                                    optional..SerialName("Vitality").."Vitality"(INTEGER, "the amount of vitality given by the item")
                                    optional..SerialName("ConditionDamage").."ConditionDamage"(INTEGER, "the amount of condition damage given by the item")
                                    optional..SerialName("ConditionDuration").."ConditionDuration"(INTEGER, "the amount of condition duration given by the item")
                                    optional..SerialName("Healing").."Healing"(INTEGER, "the amount of healing given by the item")
                                    optional..SerialName("BoonDuration").."BoonDuration"(INTEGER, "the amount of boon duration given by the item")
                                }
                            )
                            optional.."Binding"(STRING, "the binding of the material")
                            optional..SerialName("bound_to").."BoundTo"(STRING, "name of the character the item is bound to")
                        }, nullableItems = true)
                    )
                })
            )
        })
    }
    "/Colors" {
        summary = "Returns information about all dye colors in the game."
        cache = 1.hours
        isLocalized = true

        @APIGenDSL
        fun SchemaRecordBuilder.APPEARANCE() = record(description = "Information about the appearance of the color.") {
            "Brightness"(INTEGER, "the brightness")
            "Contrast"(DECIMAL, "the contrast")
            "Hue"(INTEGER, "the hue in HSL colorspace")
            "Saturation"(DECIMAL, "the saturation in HSL colorspace")
            "Lightness"(DECIMAL, "the lightness in HSL colorspace")
            "RBG"(array(INTEGER), "a list containing precalculated RGB values")
        }

        supportedQueries(BY_ID, BY_IDS, BY_PAGE)
        schema(record(description = "Information about a dye color.") {
            CamelCase("id").."ID"(INTEGER, "the color's ID")
            "Name"(STRING, "the color's name")
            SerialName("base_rgb").."BaseRGB"(array(INTEGER), "the base RGB values")
            "Cloth"(APPEARANCE(), "detailed information on its appearance when applied on cloth armor")
            "Leather"(APPEARANCE(), "detailed information on its appearance when applied on leather armor")
            "Metal"(APPEARANCE(), "detailed information on its appearance when applied on metal armor")
            optional.."Fur"(APPEARANCE(), "detailed information on its appearance when applied on fur armor")
            "Item"(INTEGER, "the ID of the dye item")
            "Categories"(array(STRING), "the categories of the color")
        })
    }
    "/Commerce/Delivery" {
        summary = "Returns information about the items and coins currently available for pickup."
        security = setOf(ACCOUNT, TRADINGPOST)

        schema(record(description = "Information about the items and coins currently available for pickup.") {
            "Coins"(INTEGER, "the amount of coins ready for pickup")
            "Items"(
                description = "the items ready for pickup",
                type = array(record(description = "Information about an item ready for pickup.") {
                    CamelCase("id").."ID"(INTEGER, "the item's ID")
                    "Count"(INTEGER, "the amount of this item ready for pickup")
                })
            )
        })
    }
    "/Commerce/Exchange" {
        summary = "Returns information about the gem exchange."
        cache = Duration.INFINITE // We don't expect this to change. Ever.

        schema(array(STRING, "")) // TODO
    }
    "/Commerce/Exchange/:Type" {
        summary = "Returns information about the gem exchange."
        cache = 5.minutes

        pathParameter("Type", STRING, "the exchange type")
        parameter("Quantity", INTEGER, "the amount to exchange")
        schema(record(description = "Information about an exchange.") {
            "CoinsPerGem"(INTEGER, "the number of coins received/required for a single gem")
            "Quantity"(INTEGER, "the number of coins/gems for received for the specified quantity of gems/coins")
        })
    }
    "/Commerce/Listings" {
        summary = "Returns current buy and sell listings from the trading post."

        @APIGenDSL
        fun SchemaRecordBuilder.LISTING() = record(description = "Information about an item's listing.") {
            "Listings"(INTEGER, "the number of individual listings this object refers to (e.g. two players selling at the same price will end up in the same listing)")
            SerialName("unit_price").."UnitPrice"(INTEGER, "the sell offer or buy order price in coins")
            "Quantity"(INTEGER, "the amount of items being sold/bought in this listing")
        }

        supportedQueries(BY_ID, BY_IDS(all = false), BY_PAGE)
        schema(record(description = "Information about an item listed in the trading post.") {
            CamelCase("id").."ID"(INTEGER, "the item's ID")
            "Buys"(array(LISTING()), "list of all buy listings")
            "Sells"(array(LISTING()), "list of all sell listings")
        })
    }
    "/Commerce/Prices" {
        summary = "Returns current aggregated buy and sell listing information from the trading post."

        supportedQueries(BY_ID, BY_IDS(all = false), BY_PAGE)
        schema(record(description = "Information about an item listed in the trading post.") {
            CamelCase("id").."ID"(INTEGER, "the item's ID")
            "Whitelisted"(BOOLEAN, "indicates whether or not a free to play account can purchase or sell this item on the trading post")
            "Buys"(
                description = "the buy information",
                type = record(description = "Information about an item's buy listing.") {
                    SerialName("unit_price").."UnitPrice"(INTEGER, "the highest buy order price in coins")
                    "Quantity"(INTEGER, "the amount of items being bought")
                }
            )
            "Sells"(
                description = "the sell information",
                type = record(description = "Information about an item's sell listing.") {
                    SerialName("unit_price").."UnitPrice"(INTEGER, "the lowest sell order price in coins")
                    "Quantity"(INTEGER, "the amount of items being sold")
                }
            )
        })
    }
    "/Commerce/Transactions" {
        summary = "Returns information about an account's transactions."
        cache = Duration.INFINITE // We don't expect this to change. Ever.
        security(ACCOUNT, TRADINGPOST)

        schema(array(STRING, "")) // TODO
    }
    "/Commerce/Transactions/:Relevance" {
        summary = "Returns information about an account's transactions."
        cache = Duration.INFINITE // We don't expect this to change. Ever.

        pathParameter("Relevance", STRING, "the temporal relevance")
        schema(array(STRING, "")) // TODO
    }
    "/Commerce/Transactions/:Relevance/:Type" {
        summary = "Returns information about an account's transactions."
        cache = 5.minutes

        pathParameter("Relevance", STRING, "the temporal relevance")
        pathParameter("Type", STRING, "the transaction type")
        supportedQueries(BY_PAGE)
        schema(record(description = "Information about a transaction.") {
            CamelCase("id").."ID"(INTEGER, "the transaction's ID")
            SerialName("item_id").."ItemID"(INTEGER, "the item's ID")
            "Price"(INTEGER, "the price in coins")
            "Quantity"(INTEGER, "the quantity of the item")
            "Created"(STRING, "the ISO-8601 standard timestamp of when the transaction was created")
            optional.."Purchased"(STRING, "the ISO-8601 standard timestamp of when the transaction was completed")
        })
    }
    "/Currencies" {
        summary = "Returns information about currencies contained in the account wallet."
        cache = 1.hours
        isLocalized = true

        supportedQueries(BY_ID, BY_IDS, BY_PAGE)
        schema(record(description = "Information about a currency.") {
            CamelCase("id").."ID"(INTEGER, "the currency's ID")
            "Name"(STRING, "the currency's name")
            "Description"(STRING, "a description of the currency")
            "Icon"(STRING, "the currency's icon")
            "Order"(INTEGER, "a number that can be used to sort the list of currencies")
        })
    }
    "/DailyCrafting" {
        summary = "Returns information about the items that can be crafted once per day."
        cache = 1.hours

        supportedQueries(BY_ID, BY_IDS, BY_PAGE)
        schema(record(description = "Information about an item that can be crafted once per day.") {
            CamelCase("id").."ID"(INTEGER, "the ID of the dailycrafting")
        })
    }
    "/Emotes" {
        summary = "Returns information about unlockable emotes."
        cache = 1.hours

        supportedQueries(BY_ID, BY_IDS, BY_PAGE)
        schema(record(description = "Information about an unlockable emote.") {
            CamelCase("id").."ID"(STRING, "the emote's ID")
            "Commands"(
                description = "the commands that may be used to trigger the emote",
                type = array(INTEGER)
            )
            SerialName("unlock_items").."UnlockItems"(
                description = "the IDs of the items that unlock the emote",
                type = array(INTEGER)
            )
        })
    }
    "/Files" {
        summary = "Returns commonly requested in-game assets."
        cache = 1.hours

        supportedQueries(BY_ID, BY_IDS, BY_PAGE)
        schema(record(description = "Information about an in-game asset.") {
            CamelCase("id").."ID"(STRING, "the file identifier")
            "Icon"(STRING, "the URL to the image")
        })
    }
    "/Items" {
        summary = "Returns information about items in the game."
        cache = 1.hours

        @APIGenDSL
        fun SchemaRecordBuilder.INFIX_UPGRADE() = record(description = "Information about an item's infix upgrade.") {
            CamelCase("id").."ID"(INTEGER, "the itemstat ID")
            "Attributes"(
                description = "the list of attribute bonuses granted by this item",
                type = array(record(description = "Information about an infix upgrade's attribute bonuses.") {
                    "Attribute"(STRING, "the attribute this bonus applies to")
                    "Modifier"(INTEGER, "the modifier value")
                })
            )
            optional.."Buff"(
                description = "object containing an additional effect",
                type = record(description = "Information about an infix upgrade's buffs.") {
                    SerialName("skill_id").."SkillId"(INTEGER, "the skill ID of the effect")
                    optional.."Description"(STRING, "the effect's description")
                }
            )
        }

        @APIGenDSL
        fun SchemaRecordBuilder.INFUSION_SLOTS() = array(record(description = "Information about an items infusion slot.") {
            "Flags"(array(STRING), "infusion slot type of infusion upgrades")
            optional..SerialName("item_id").."ItemId"(INTEGER, "the infusion upgrade in the armor piece")
        })

        supportedQueries(BY_ID, BY_IDS(all = false), BY_PAGE)
        schema(record(description = "Information about an item.") {
            CamelCase("id").."ID"(INTEGER, "the item's ID")
            "Name"(STRING, "the item's name")
            "Type"(STRING, "the item's type")
            SerialName("chat_link").."ChatLink"(STRING, "the chat link")
            optional.."Icon"(STRING, "the URL for the item's icon")
            optional.."Description"(STRING, "the item's description")
            "Rarity"(STRING, "the item's rarity")
            "Level"(INTEGER, "the level required to use the item")
            SerialName("vendor_value").."VendorValue"(INTEGER, "the value in coins when selling the item to a vendor")
            optional..SerialName("default_skin").."DefaultSkin"(INTEGER, "the ID of the item's default skin")
            "Flags"(array(STRING), "flags applying to the item")
            SerialName("game_types").."GameTypes"(array(STRING), "the game types in which the item is usable")
            "Restrictions"(array(STRING), "restrictions applied to the item")
            optional..SerialName("upgrades_into").."UpgradesInto"(
                description = "lists what items this item can be upgraded into, and the method of upgrading",
                type = array(record(description = "Information about an item's upgrade.") {
                    "Upgrade"(STRING, "describes the method of upgrading")
                    SerialName("item_id").."ItemId"(INTEGER, "the item ID that results from performing the upgrade")
                })
            )
            optional..SerialName("upgrades_from").."UpgradesFrom"(
                description = "lists what items this item can be upgraded from, and the method of upgrading",
                type = array(record(description = "Information about an item's precursor.") {
                    "Upgrade"(STRING, "describes the method of upgrading")
                    SerialName("item_id").."ItemId"(INTEGER, "the item ID that results from performing the upgrade")
                })
            )
            optional.."Details"(
                description = "additional information about the item",
                type = conditional(description = "Additional information about an item.", disambiguationBySideProperty = true) {
                    "Armor"(record(description = "Additional information about an armor item.") {
                        "Type"(STRING, "the armor slot type")
                        SerialName("weight_class").."WeightClass"(STRING, "the weight class")
                        "Defense"(INTEGER, "the defense value of the armor piece")
                        SerialName("infusion_slots").."InfusionSlots"(INFUSION_SLOTS(), "infusion slots of the armor piece")
                        optional..SerialName("infix_upgrade").."InfixUpgrade"(INFIX_UPGRADE(), "infix upgrade object")
                        optional..SerialName("suffix_item_id").."SuffixItemId"(INTEGER, "the suffix item ID")
                        optional..SerialName("secondary_suffix_item_id").."SecondarySuffixItemId"(STRING, "the secondary suffix item ID")
                        optional..SerialName("stat_choices").."StatChoices"(array(INTEGER), "a list of selectable stat IDs which are visible in /v2/itemstats")
                        optional..SerialName("attribute_adjustment").."AttributeAdjustment"(DECIMAL, "") // TODO doc
                    })
                    "Back"(record(description = "Additional information about a backpiece.") {
                        SerialName("infusion_slots").."InfusionSlots"(INFUSION_SLOTS(), "infusion slots of the back item")
                        optional..SerialName("infix_upgrade").."InfixUpgrade"(INFIX_UPGRADE(), "infix upgrade object")
                        optional..SerialName("suffix_item_id").."SuffixItemId"(INTEGER, "the suffix item ID")
                        optional..SerialName("secondary_suffix_item_id").."SecondarySuffixItemId"(STRING, "the secondary suffix item ID")
                        optional..SerialName("stat_choices").."StatChoices"(array(INTEGER), "a list of selectable stat IDs which are visible in /v2/itemstats")
                        optional..SerialName("attribute_adjustment").."AttributeAdjustment"(DECIMAL, "") // TODO doc
                    })
                    "Bag"(record(description = "Additional information about a bag.") {
                        "Size"(INTEGER, "the number of bag slots")
                        SerialName("no_sell_or_sort").."NoSellOrSort"(BOOLEAN, "whether the bag is invisible")
                    })
                    "Consumable"(record(description = "Additional information about a consumable item.") {
                        "Type"(STRING, "the consumable type")
                        optional.."Description"(STRING, "effect description for consumables applying an effect")
                        optional..SerialName("duration_ms").."DurationMs"(INTEGER, "effect duration in milliseconds")
                        optional..SerialName("unlock_type").."UnlockType"(STRING, "unlock type for unlock consumables")
                        optional..SerialName("color_id").."ColorId"(INTEGER, "the dye ID for dye unlocks")
                        optional..SerialName("recipe_id").."RecipeId"(INTEGER, "the recipe ID for recipe unlocks")
                        optional..SerialName("extra_recipe_ids").."ExtraRecipeIds"(array(INTEGER), "additional recipe IDs for recipe unlocks")
                        optional..SerialName("guild_upgrade_id").."GuildUpgradeId"(INTEGER, "the guild upgrade ID for the item")
                        optional..SerialName("apply_count").."ApplyCount"(INTEGER, "the number of stacks of the effect applied by this item")
                        optional.."Name"(STRING, "the effect type name of the consumable")
                        optional.."Icon"(STRING, "the icon of the effect")
                        optional.."Skins"(array(INTEGER), "a list of skin ids which this item unlocks")
                    })
                    "Container"(record(description = "Additional information about a container.") {
                        "Type"(STRING, "the container type")
                    })
                    "Gathering"(record(description = "Additional information about a gathering tool.") {
                        "Type"(STRING, "the tool type")
                    })
                    "Gizmo"(record(description = "Additional information about a gizmo.") {
                        "Type"(STRING, "the gizmo type")
                        optional..SerialName("guild_upgrade_id").."GuildUpgradeId"(INTEGER, "the guild upgrade ID for the item")
                        optional..SerialName("vendor_ids").."VendorIds"(array(INTEGER), "the vendor IDs")
                    })
                    "MiniPet"(record(description = "Additional information about a mini unlock item.") {
                        SerialName("minipet_id").."MinipetId"(INTEGER, "the miniature it unlocks")
                    })
                    "Tool"(record(description = "Additional information about a tool.") {
                        "Type"(STRING, "the tool type")
                        "Charges"(INTEGER, "the available charges")
                    })
                    "Trinket"(record(description = "Additional information about a trinket.") {
                        "Type"(STRING, "the trinket type")
                        SerialName("infusion_slots").."InfusionSlots"(INFUSION_SLOTS(), "infusion slots of the trinket")
                        optional..SerialName("infix_upgrade").."InfixUpgrade"(INFIX_UPGRADE(), "infix upgrade object")
                        optional..SerialName("suffix_item_id").."SuffixItemId"(INTEGER, "the suffix item ID")
                        optional..SerialName("secondary_suffix_item_id").."SecondarySuffixItemId"(STRING, "the secondary suffix item ID")
                        optional..SerialName("stat_choices").."StatChoices"(array(INTEGER), "a list of selectable stat IDs which are visible in /v2/itemstats")
                        optional..SerialName("attribute_adjustment").."AttributeAdjustment"(DECIMAL, "") // TODO doc
                    })
                    "UpgradeComponent"(record(description = "Additional information about an upgrade component.") {
                        "Type"(STRING, "the type of the upgrade component")
                        "Flags"(array(STRING), "the items that can be upgraded with the upgrade component")
                        SerialName("infusion_upgrade_flags").."InfusionUpgradeFlags"(array(STRING), "applicable infusion slot for infusion upgrades")
                        "Suffix"(STRING, "the suffix appended to the item name when the component is applied")
                        optional..SerialName("infix_upgrade").."InfixUpgrade"(INFIX_UPGRADE(), "infix upgrade object")
                        optional.."Bonuses"(array(STRING), "the bonuses from runes")
                        optional..SerialName("attribute_adjustment").."AttributeAdjustment"(DECIMAL, "") // TODO doc
                    })
                    "Weapon"(record(description = "Additional information about a weapon.") {
                        "Type"(STRING, "the weapon type")
                        SerialName("min_power").."MinPower"(INTEGER, "minimum weapon strength")
                        SerialName("max_power").."MaxPower"(INTEGER, "maximum weapon strength")
                        SerialName("damage_type").."DamageType"(STRING, "the damage type")
                        "Defense"(INTEGER, "the defense value of the weapon")
                        SerialName("infusion_slots").."InfusionSlots"(INFUSION_SLOTS(), "infusion slots of the weapon")
                        optional..SerialName("infix_upgrade").."InfixUpgrade"(INFIX_UPGRADE(), "infix upgrade object")
                        optional..SerialName("suffix_item_id").."SuffixItemId"(INTEGER, "the suffix item ID")
                        optional..SerialName("secondary_suffix_item_id").."SecondarySuffixItemId"(STRING, "the secondary suffix item ID")
                        optional..SerialName("stat_choices").."StatChoices"(array(INTEGER), "a list of selectable stat IDs which are visible in /v2/itemstats")
                        optional..SerialName("attribute_adjustment").."AttributeAdjustment"(DECIMAL, "") // TODO doc
                    })
                }
            )
        })
    }
    "/ItemStats" {
        summary = "Returns information about itemstats."
        cache = 1.hours

        supportedQueries(BY_ID, BY_IDS, BY_PAGE)
        schema(record(description = "Information about a stat set.") {
            CamelCase("id").."ID"(INTEGER, "the stat set's ID")
            "Name"(STRING, "the name of the stat set")
            "Attributes"(
                description = "the list of attribute bonuses",
                type = array(record(description = "Information about an attribute bonus.") {
                    "Attribute"(STRING, "the name of the attribute")
                    "Multiplier"(DECIMAL, "the multiplier for that attribute")
                    "Value"(INTEGER, "the default value for that attribute")
                })
            )
        })
    }
    "/Legends" {
        summary = "Returns information about the Revenant legends."
        cache = 1.hours

        supportedQueries(BY_ID, BY_IDS, BY_PAGE)
        schema(record(description = "Information about a Revenant legend.") {
            CamelCase("id").."ID"(STRING, "the legend's ID")
            "Swap"(INTEGER, "the ID of the profession (swap Legend) skill")
            "Heal"(INTEGER, "the ID of the heal skill")
            "Elite"(INTEGER, "the ID of the elite skills")
            "Utilities"(
                description = "the IDs of the utility skills",
                type = array(INTEGER)
            )
        })
    }
    "/MapChests" {
        summary = "Returns information about the Hero's Choice Chests that can be acquired once per day."
        cache = 1.hours

        supportedQueries(BY_ID, BY_IDS, BY_PAGE)
        schema(record(description = "Information about a Hero's Choice Chests that can be acquired once per day.") {
            CamelCase("id").."ID"(INTEGER, "the ID of the mapchest")
        })
    }
    "/Outfits" {
        summary = "Returns information about outfits."
        cache = 1.hours
        isLocalized = true

        supportedQueries(BY_ID, BY_IDS, BY_PAGE)
        schema(record(description = "Information about an outfit.") {
            CamelCase("id").."ID"(INTEGER, "the outfit's ID")
            "Name"(STRING, "the outfit's name")
            "Icon"(STRING, "the outfit's icon")
            SerialName("unlock_items").."UnlockItems"(
                description = "the IDs of the items that unlock the outfit",
                type = array(INTEGER)
            )
        })
    }
    "/Professions" {
        summary = "Returns information about the game's playable professions."
        cache = 1.hours
        isLocalized = true

        supportedQueries(BY_ID, BY_IDS, BY_PAGE)
        schema(record(description = "Information about a playable profession.") {
            CamelCase("id").."ID"(STRING, "the profession's ID")
            "Name"(STRING, "the profession's localized name")
            since(V2_SCHEMA_2019_12_19T00_00_00_000Z).."Code"(INTEGER, "the profession's palette code")
            "Icon"(STRING, "a render service URL for the profession's icon")
            SerialName("icon_big").."BigIcon"(STRING, "a render service URL for a big version of the profession's icon")
            "Specializations"(array(INTEGER), "the IDs of the profession's specializations")
            "Weapons"(
                description = "information about the weapons usable by this profession",
                type = map(
                    keys = STRING,
                    values = record(description = "Information about a profession's weapon and it's skills.") {
                        optional.."Specialization"(INTEGER, "the ID of the profession's specializations required for this weapon")
                        "Flags"(array(STRING), "additional flags describing this weapon's properties (e.g. MainHand, OffHand, TwoHand, Aquatic)")
                        "Skills"(
                            description = "the skills for the weapon if wielded by this profession",
                            type = array(record(description = "Information about a weapon's skills.") {
                                CamelCase("id").."ID"(INTEGER, "the skill's ID")
                                "Slot"(STRING, "the skill's slot")
                                optional.."Attunement"(STRING, "the elementalist attunement for this skill")
                                optional.."Offhand"(STRING, "the offhand weapon for this skill")
                            })
                        )
                    }
                )
            )
            "Flags"(array(STRING), "additional flags describing this profession's properties (e.g. NoRacialSkills)")
            "Skills"(
                description = "the profession specific skills",
                type = array(record(description = "Information about a profession skill.") {
                    CamelCase("id").."ID"(INTEGER, "the skill's ID")
                    "Slot"(STRING, "the skill's slot")
                    "Type"(STRING, "the skill's type")
                    optional.."Attunement"(STRING, "the elementalist attunement for this skill")
                    optional.."Source"(STRING, "the profession ID of the source of the stolen skill") // TODO is this correct?
                })
            )
            "Training"(
                description = "array of trainings of this profession",
                type = array(record(description = "Information about training track.") {
                    CamelCase("id").."ID"(INTEGER, "the training's ID")
                    "Category"(STRING, "the training's category")
                    "Name"(STRING, "the training's localized name")
                    "Track"(
                        description = "array of skill/trait in the training track",
                        type = array(record(description = "Information about a skill/trait in a track.") {
                            "Cost"(INTEGER, "the amount of skill points required to unlock this step")
                            "Type"(STRING, "the type of the step (e.g. Skill, Trait)")
                            optional..SerialName("skill_id").."SkillId"(INTEGER, "the ID of the skill unlocked by this step")
                            optional..SerialName("trait_id").."TraitId"(INTEGER, "the ID of the trait unlocked by this step")
                        })
                    )
                })
            )
            since(V2_SCHEMA_2019_12_19T00_00_00_000Z)..SerialName("skills_by_palette").."SkillsByPalette"(
                description = "mappings from palette IDs to skill IDs",
                type = array(array(INTEGER))
            )
        })
    }
    "/Races" {
        summary = "Returns information about the game's playable races."
        cache = 1.hours
        isLocalized = true

        supportedQueries(BY_ID, BY_IDS, BY_PAGE)
        schema(record(description = "Information about a playable race.") {
            CamelCase("id").."ID"(STRING, "the race's ID")
            "Name"(STRING, "the race's localized name")
            "Skills"(array(INTEGER), "an array of racial skill IDs")
        })
    }
    "/Titles" {
        summary = "Returns information about the titles that are in the game."
        cache = 1.hours
        isLocalized = true

        supportedQueries(BY_ID, BY_IDS, BY_PAGE)
        schema(record(description = "Information about a title.") {
            CamelCase("id").."ID"(INTEGER, "the ID of the title")
            "Name"(STRING, "the display name of the title")
            deprecated..optional.."Achievement"(INTEGER, "the ID of the achievement that grants this title")
            optional.."Achievements"(array(INTEGER), "the IDs of the achievements that grant this title")
            optional..SerialName("ap_required")..CamelCase("apRequired").."APRequired"(INTEGER, "the amount of AP required to unlock this title")
        })
    }
    "/Traits" {
        summary = "Returns information about the traits available in the game."
        cache = 1.hours
        isLocalized = true

        @APIGenDSL
        fun SchemaConditionalBuilder.FACTS() {
            "AttributeAdjust"(record(description = "Additional information about an attribute adjustment.") {
                optional.."Value"(INTEGER, "the amount 'target' gets adjusted, based on a level 80 character at base stats")
                optional.."Target"(STRING, "the attribute this fact adjusts")
            })
            "Buff"(record(description = "Additional information about a buff.") {
                "Status"(STRING, "the boon, condition, or effect referred to by the fact")
                optional.."Duration"(INTEGER, "the duration of the effect in seconds")
                optional.."Description"(STRING, "the description of the status effect")
                optional..SerialName("apply_count").."ApplyCount"(INTEGER, "the number of stacks applied")
            })
            "BuffConversion"(record(description = "Additional information about a buff-conversion.") {
                "Source"(STRING, "the attribute that is used to calculate the attribute gain")
                "Percent"(INTEGER, "how much of the source attribute is added to target")
                "Target"(STRING, "the attribute that gets added to")
            })
            "ComboField"(record(description = "Additional information about a combo-field.") {
                SerialName("field_type").."FieldType"(STRING, "the type of the field")
            })
            "ComboFinisher"(record(description = "Additional information about a combo-finisher.") {
                SerialName("finisher_type").."FinisherType"(STRING, "the type of finisher")
                "Percent"(INTEGER, "the percent chance that the finisher will trigger")
            })
            "Damage"(record(description = "Additional information about damage.") {
                SerialName("hit_count").."HitCount"(INTEGER, "the amount of times the damage hits")
                SerialName("dmg_multiplier").."DamageMultiplier"(DECIMAL, "the damage multiplier")
            })
            "Distance"(record(description = "Additional information about range.") {
                "Distance"(INTEGER, "the distance value")
            })
            "NoData"(record(description = "No (special) additional information.") {})
            "Number"(record(description = "An additional number.") {
                "Value"(INTEGER, "the number value as referenced by text")
            })
            "Percent"(record(description = "An additional percentage value.") {
                "Percent"(INTEGER, "the percentage value as referenced by text")
            })
            "PrefixedBuff"(record(description = "Additional information about a prefixed buff.") {
                "Status"(STRING, "the boon, condition, or effect referred to by the fact")
                optional.."Duration"(INTEGER, "the duration of the effect in seconds")
                optional.."Description"(STRING, "the description of the status effect")
                optional..SerialName("apply_count").."ApplyCount"(INTEGER, "the number of stacks applied")
                "Prefix"(
                    description = "", // TODO doc
                    type = record(description = "Information about a buff's prefix.") {
                        "Text"(STRING, "") // TODO
                        "Icon"(STRING, "") // TODO
                        "Status"(STRING, "") // TODO
                        "Description"(STRING, "") // TODO
                    }
                )
            })
            "Radius"(record(description = "Additional information about a radius.") {
                "Distance"(INTEGER, "the radius value")
            })
            "Range"(record(description = "Additional information about range.") {
                "Value"(INTEGER, "the range of the trait/skill")
            })
            "Recharge"(record(description = "Additional information about recharge.") {
                "Value"(INTEGER, "the recharge time in seconds")
            })
            "Time"(record(description = "Additional information about time.") {
                "Duration"(INTEGER, "the time value in seconds")
            })
            "Unblockable"(record(description = "A fact, indicating that a trait/skill is unlockable.") {
                "Value"(BOOLEAN, "always true")
            })
        }

        supportedQueries(BY_ID, BY_IDS, BY_PAGE)
        schema(record(description = "Information about a trait.") {
            CamelCase("id").."ID"(INTEGER, "the trait's ID")
            "Tier"(INTEGER, "the trait's tier")
            "Order"(INTEGER, "the trait's order")
            "Name"(STRING, "the trait's name")
            "Description"(STRING, "the trait's description")
            "Slot"(STRING, "the slot for the trait")
            optional.."Facts"(
                description = "a list of facts",
                type = array(conditional(
                    description = "Information about a trait's fact (i.e. effect/property).",
                    sharedConfigure = {
                        "Type"(STRING, "the type of the fact")
                        optional.."Icon"(STRING, "the URL for the fact's icon")
                        optional.."Text"(STRING, "an arbitrary localized string describing the fact")
                    }
                ) { FACTS() }
            ))
            optional..SerialName("traited_facts").."TraitedFacts"(
                description = "Information about a trait's fact (i.e. effect/property) that is only active if a specific trait is active.",
                type = array(conditional(
                    description = "a list of traited facts",
                    sharedConfigure = {
                        "Type"(STRING, "the type of the fact")
                        optional.."Icon"(STRING, "the URL for the fact's icon")
                        optional.."Text"(STRING, "an arbitrary localized string describing the fact")
                        SerialName("requires_trait").."RequiresTrait"(INTEGER, "specifies which trait has to be selected in order for this fact to take effect")
                        optional.."Overrides"(INTEGER, "the array index of the facts object it will override, if the trait specified in requires_trait is selected")
                    }
                ) { FACTS() })
            )
            optional.."Skills"(
                description = "a list of skills related to this trait",
                type = array(record(description = "Information about a skill related to a trait.") {
                    "ID"(INTEGER, "the skill's ID")
                    "Name"(STRING, "the skill's name")
                    "Description"(STRING, "the skill's description")
                    "Icon"(STRING, "the URL of the skill's icon")
                    optional.."Facts"(
                        description = "a list of facts of the skill",
                        type = array(conditional(
                            description = "Information about a trait's effects.",
                            sharedConfigure = {
                                "Type"(STRING, "the type of the fact")
                                optional.."Icon"(STRING, "the URL for the fact's icon")
                                optional.."Text"(STRING, "an arbitrary localized string describing the fact")
                            }
                        ) { FACTS() }))
                    optional..SerialName("traited_facts").."TraitedFacts"(
                        description = "a list of traited facts of the skill",
                        type = array(conditional(
                            description = "Information about a fact that overrides a trait's effect.",
                            sharedConfigure = {
                                "Type"(STRING, "the type of the fact")
                                optional.."Icon"(STRING, "the URL for the fact's icon")
                                optional.."Text"(STRING, "an arbitrary localized string describing the fact")
                                SerialName("requires_trait").."RequiresTrait"(INTEGER, "specifies which trait has to be selected in order for this fact to take effect")
                                optional.."Overrides"(INTEGER, "the array index of the facts object it will override, if the trait specified in requires_trait is selected")
                            }
                        ) { FACTS() })
                    )
                })
            )
            "Specialization"(INTEGER, "the specialization that this trait is part of")
            "Icon"(STRING, "the URL for the trait's icon")
        })
    }
    "/TokenInfo" {
        summary = "Returns information about the supplied API key."
        security(ACCOUNT)

        schema(
            V2_SCHEMA_CLASSIC to record(description = "Information about an API key.") {
                CamelCase("id").."ID"(STRING, "the API key that was requested")
                "Name"(STRING, "the name given to the API key by the account owner")
                "Permissions"(
                    description = "an array of strings describing which permissions the API key has",
                    type = array(STRING)
                )
            },
            V2_SCHEMA_2019_05_22T00_00_00_000Z to record("Information about an API key.") {
                CamelCase("id").."ID"(STRING, "the API key that was requested")
                "Name"(STRING, "the name given to the API key by the account owner")
                "Permissions"(
                    description = "an array of strings describing which permissions the API key has",
                    type = array(STRING)
                )
                "Type"(STRING, "the type of the access token given")
                optional..SerialName("expires_at").."ExpiresAt"(STRING, "if a subtoken is given, ISO8601 timestamp indicating when the given subtoken expires")
                optional..SerialName("issued_at").."IssuedAt"(STRING, "if a subtoken is given, ISO8601 timestamp indicating when the given subtoken was created")
                optional..CamelCase("urls").."URLs"(array(STRING), "an array of strings describing what endpoints are available to this token (if the given subtoken is restricted to a list of URLs)")
            }
        )
    }
    "/WorldBosses" {
        summary = "Returns information about the worldbosses that reward boss chests that can be opened once a day."
        cache = 1.hours

        supportedQueries(BY_ID, BY_IDS, BY_PAGE)
        schema(record(description = "Information about a worldboss that reward boss chests that can be opened once a day.") {
            CamelCase("id").."ID"(STRING, "the worldboss's ID")
        })
    }
    "/Worlds" {
        summary = "Returns information about the available worlds (or servers)."
        cache = 1.hours
        isLocalized = true

        supportedQueries(BY_ID, BY_IDS, BY_PAGE)
        schema(record(description = "Information about an available world (or server).") {
            CamelCase("id").."ID"(INTEGER, "the ID of the world")
            "Name"(STRING, "the name of the world")
            "Population"(STRING, "the population level of the world")
        })
    }
    "/WvW/Objectives" {
        summary = "Returns information about the objectives in the World versus World game mode."
        cache = 1.hours
        isLocalized = true

        supportedQueries(BY_ID, BY_IDS, BY_PAGE)
        schema(record(description = "Information about an objective in the World versus World game mode.") {
            CamelCase("id").."ID"(STRING, "the ID of the objective")
            "Name"(STRING, "the name of the objective")
            "Type"(STRING, "the type of the objective")
            SerialName("sector_id").."SectorId"(INTEGER, "the map sector the objective can be found in")
            SerialName("map_id").."MapId"(INTEGER, "the ID of the map the objective can be found on")
            SerialName("map_type").."MapType"(STRING, "the type of the map the objective can be found on")
            "Coord"(array(DECIMAL), "an array of three numbers representing the X, Y and Z coordinates of the objectives marker on the map")
            SerialName("label_coord").."LabelCoord"(array(DECIMAL), "an array of two numbers representing the X and Y coordinates of the sector centroid")
            "Marker"(STRING, "the icon link")
            SerialName("chat_link").."ChatLink"(STRING, "the chat code for the objective")
            optional..SerialName("upgrade_id").."UpgradeId"(INTEGER, "the ID of the upgrades available for the objective")
        })
    }
    "/WvW/Ranks" {
        summary = "Returns information about the achievable ranks in the World versus World game mode."
        cache = 1.hours
        isLocalized = true

        supportedQueries(BY_ID, BY_IDS, BY_PAGE)
        schema(record(description = "Information about an achievable rank in the World versus World game mode.") {
            CamelCase("id").."ID"(INTEGER, "the ID of the rank")
            "Title"(STRING, "the title of the rank")
            SerialName("min_level").."MinLevel"(INTEGER, "the WvW level required to unlock this rank")
        })
    }
    "/WvW/Upgrades" {
        summary = "Returns information about available upgrades for objectives in the World versus World game mode."
        cache = 1.hours
        isLocalized = true

        supportedQueries(BY_ID, BY_IDS, BY_PAGE)
        schema(record(description = "Information about an upgrade for objectives in the World versus World game mode.") {
            CamelCase("id").."ID"(INTEGER, "the ID of the upgrade")
            "Tiers"(
                description = "the different tiers of the upgrade",
                type = record(description = "Information about an upgrade tier.") {
                    "Name"(STRING, "the name of the upgrade tier")
                    SerialName("yaks_required").."YaksRequired"(INTEGER, "the amount of dolyaks required to reach this upgrade tier")
                    "Upgrades"(
                        description = "the upgrades available at the tier",
                        type = record(description = "Information about an upgrade.") {
                            "Name"(STRING, "the name of the upgrade")
                            "Description"(STRING, "the description for the upgrade")
                            "Icon"(STRING, "the icon link")
                        }
                    )
                }
            )
        })
    }
}