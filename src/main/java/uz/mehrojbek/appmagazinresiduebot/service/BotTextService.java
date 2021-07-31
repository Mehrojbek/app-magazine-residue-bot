package uz.mehrojbek.appmagazinresiduebot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotTextService {
    SendMessage enterCode(Message message);
    SendMessage start(Message message);
    SendMessage anyMessage(Message message, String text);
    SendMessage wareHouse(Message message);
    DeleteMessage deleteMessage(Message message);
    SendMessage brands(Message message);
    SendMessage brandValues(Message message, String brandName, Integer id);
    SendMessage products(Message message, Integer id);
    SendMessage products(Message message, boolean wareHouse);
    SendMessage getOneProduct(Message message, Integer id);
    SendMessage deleteAllProduct(Message message);
}
