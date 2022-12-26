package com.lairon.libs.xmessageutils;

import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Map;

public final class MessageUtils {

    private static DecimalFormat format;
    private static StrSubstitutor sub = new StrSubstitutor(Map.of("", ""), "{", "}");


    static {
        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormatSymbols.setGroupingSeparator(',');
        format = new DecimalFormat("#,##0.00", decimalFormatSymbols);
    }

    public static void sendMessage(@NotNull CommandSender commandSender, @NotNull String message) {
        sendMessage(commandSender, message, null);
    }

    public static void sendMessage(@NotNull CommandSender commandSender, @NotNull List<String> messages) {
        sendMessage(commandSender, messages, null);
    }

    public static void sendMessage(@NotNull CommandSender commandSender, @NotNull String message, Map<String, String> placeholders) {
        if (placeholders == null) {
            commandSender.sendMessage(applyColors(message));
            return;
        }
        commandSender.sendMessage(applyColors(applyPlaceholders(message, placeholders)));
    }

    public static void sendMessage(@NotNull CommandSender commandSender, @NotNull List<String> messages, Map<String, String> placeholders) {
        if (placeholders == null) {
            applyColors(messages).forEach(commandSender::sendMessage);
            return;
        }
        applyColors(applyPlaceholders(messages, placeholders)).forEach(commandSender::sendMessage);
    }

    public static ItemStack applyPlaceholders(@NotNull ItemStack itemStack, @NotNull Map<String, String> placeholders) {
        itemStack = itemStack.clone();
        if (!itemStack.hasItemMeta()) return itemStack;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasDisplayName())
            itemMeta.setDisplayName(applyPlaceholders(itemMeta.getDisplayName(), placeholders));
        if (itemMeta.hasLore()) itemMeta.setLore(applyPlaceholders(itemMeta.getLore(), placeholders));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static List<String> applyPlaceholders(@NotNull List<String> messages, @NotNull Map<String, String> placeholders) {
        for (int i = 0; i < messages.size(); i++) {
            messages.set(i, applyPlaceholders(messages.get(i), placeholders));
        }
        return messages;
    }

    public static String applyColors(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> applyColors(List<String> messages) {
        for (int i = 0; i < messages.size(); i++) {
            messages.set(i, applyColors(messages.get(i)));
        }
        return messages;
    }

    public static String applyPlaceholders(@NotNull String message, Map<String, @NotNull String> placeholders) {
        sub.setVariableResolver(StrLookup.mapLookup(placeholders));
        return sub.replace(message);
    }

    public static String toDecimal(@NotNull double d) {
        return format.format(d);
    }

    public static String toDecimal(@NotNull float d) {
        return format.format(d);
    }

}
