package uz.mehrojbek.appmagazinresiduebot.component;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotButton {
    public ReplyKeyboardMarkup start() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList();
        markup.setResizeKeyboard(true);
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add("Kategoriyalar");
        keyboardRow1.add("Brendlar");
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add("Skladlar");
        keyboardRows.add(keyboardRow1);
        keyboardRows.add(keyboardRow2);
        markup.setKeyboard(keyboardRows);
        return markup;
    }


    public ReplyKeyboardMarkup wareHouseButton() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList();
        markup.setResizeKeyboard(true);
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add("Sklad");
        keyboardRow1.add("Do`kon");
        keyboardRows.add(keyboardRow1);
        markup.setKeyboard(keyboardRows);
        return markup;
    }

    public InlineKeyboardMarkup brand(List<String> brands) {

        InlineKeyboardMarkup brandButton = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowList = new ArrayList();
        for (String brand : brands) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText("    " + brand + "    ");
            button.setCallbackData("brand:" + brand);
            List<InlineKeyboardButton> row = new ArrayList();
            row.add(button);
            rowList.add(row);
        }
        brandButton.setKeyboard(rowList);
        return brandButton;
    }

    public InlineKeyboardMarkup keyingi(Integer id, String brandName){
        InlineKeyboardMarkup keyingiButton = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> row = new ArrayList();
        List<List<InlineKeyboardButton>> rowList = new ArrayList();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("keyingi");
        button.setCallbackData("keyingi:"+id+":brandName:"+brandName);
        row.add(button);
        rowList.add(row);
        keyingiButton.setKeyboard(rowList);
        return keyingiButton;
    }

    public InlineKeyboardMarkup keyingi(Integer id){
        InlineKeyboardMarkup keyingiButton = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> row = new ArrayList();
        List<List<InlineKeyboardButton>> rowList = new ArrayList();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("keyingi");
        button.setCallbackData("all:"+id);
        row.add(button);
        rowList.add(row);
        keyingiButton.setKeyboard(rowList);
        return keyingiButton;
    }
}
