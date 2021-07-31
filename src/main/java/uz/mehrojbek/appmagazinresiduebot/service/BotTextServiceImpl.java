package uz.mehrojbek.appmagazinresiduebot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import uz.mehrojbek.appmagazinresiduebot.component.BotButton;
import uz.mehrojbek.appmagazinresiduebot.entity.Product;
import uz.mehrojbek.appmagazinresiduebot.entity.enums.BranchTypeEnum;
import uz.mehrojbek.appmagazinresiduebot.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BotTextServiceImpl implements BotTextService{
    private final BotButton botButton;
    private final ProductRepository productRepository;

    public SendMessage enterCode(Message message) {
        return new SendMessage(String.valueOf(message.getChatId()), "Iltimos parolni kiriting");
    }

    public SendMessage start(Message message) {
        ReplyKeyboardMarkup startButton = botButton.start();
        SendMessage sendMessage = new SendMessage(String.valueOf(message.getChatId()), "Main Menu");
        sendMessage.setReplyMarkup(startButton);
        return sendMessage;
    }

    public SendMessage anyMessage(Message message, String text) {
        return new SendMessage(String.valueOf(message.getChatId()), text);
    }

    public SendMessage wareHouse(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Skladlar");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setReplyMarkup(botButton.wareHouseButton());
        return sendMessage;
    }

    public DeleteMessage deleteMessage(Message message) {
        return new DeleteMessage(String.valueOf(message.getChatId()), message.getMessageId());
    }

    public SendMessage brands(Message message) {
        List<String> brands = productRepository.getAllBrands();
        InlineKeyboardMarkup markup = botButton.brand(brands);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Brendlar:");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public SendMessage brandValues(Message message, String brandName, Integer id) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.enableHtml(true);
        List<Product> products = productRepository.findAllByBrandAndIdGreaterThan(brandName, id);
        String result = "<b>Шахобча   <i>Тури</i>   Бренд   <i>Махсулот</i>   Тариф   <i>Хажми</i>   Колдик   " +
                "<i>валюта</i>   Нархи   <i>Сумма $</i></b>\n\n";
        for (Product product : products) {
            double price = 0;
            if (product.getCurrency().equals("$")) {
                price = product.getPriceUsd();
            } else {
                price = product.getPriceSom();
            }

            if (product.getBranchType().equals(BranchTypeEnum.MAGAZINE)) {
                result = result + "<b> Do`k.   </b>";
            } else {
                result = result + "<b>Skla.   </b>";
            }

            result = result + "<i>" + product.getType() + "</i>   " + product.getBrand() + "   <b>" + product.getName() +
                    "</b>   <i>" + product.getTariff() + "</i>   <b>" + product.getSize() + "</b>   " + product.getResidue() +
                    "   <i>" + product.getCurrency() + "</i>   <b>" + price + "</b>   " + product.getSumPrice() + "\n\n";
            if (result.length() > 3700) {
                InlineKeyboardMarkup keyingi = botButton.keyingi(product.getId(), brandName);
                sendMessage.setText(result);
                sendMessage.setReplyMarkup(keyingi);
                return sendMessage;
            }
        }
        sendMessage.setText(result);
        return sendMessage;
    }

    public SendMessage products(Message message, Integer id) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.enableHtml(true);
        List<Product> products = productRepository.findAllByIdGreaterThan(id);
        String result = "<b>Шахобча   <i>Тури</i>   Бренд   <i>Махсулот</i>   Тариф   <i>Хажми</i>   Колдик   " +
                "<i>валюта</i>   Нархи   <i>Сумма $</i></b>\n\n";
        for (Product product : products) {
            double price = 0;
            if (product.getCurrency().equals("$")) {
                price = product.getPriceUsd();
            } else {
                price = product.getPriceSom();
            }

            if (product.getBranchType().equals(BranchTypeEnum.MAGAZINE)) {
                result = result + "<b> Do`k.   </b>";
            } else {
                result = result + "<b>Skla.   </b>";
            }

            String size = "";
            if (product.getSize() == null) {
                size = "yo'q";
            }else {
                size = String.valueOf(product.getSize());
            }
            result = result + "<i>" + product.getType() + "</i>   " + product.getBrand() + "   <b>" + product.getName() +
                    "</b>   <i>" + product.getTariff() + "</i>   <b>" + size + "</b>   " + product.getResidue() +
                    "   <i>" + product.getCurrency() + "</i>   <b>" + price + "</b>   " + product.getSumPrice() + "\n\n";
            if (result.length() > 3700) {
                InlineKeyboardMarkup keyingi = botButton.keyingi(product.getId());
                sendMessage.setText(result);
                sendMessage.setReplyMarkup(keyingi);
                return sendMessage;
            }
        }
        sendMessage.setText(result);
        return sendMessage;
    }

    public SendMessage products(Message message, boolean warehosue) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.enableHtml(true);
        List<Product> products;
        if (warehosue) {
            products = productRepository.findAllByBranchType(BranchTypeEnum.WAREHOUSE);
        } else {
            products = productRepository.findAllByBranchType(BranchTypeEnum.MAGAZINE);
        }
        String result = "<b>Шахобча   <i>Тури</i>   Бренд   <i>Махсулот</i>   Тариф   <i>Хажми</i>   Колдик   " +
                "<i>валюта</i>   Нархи   <i>Сумма $</i></b>\n\n";
        for (Product product : products) {
            double price = 0;
            if (product.getCurrency().equals("$")) {
                price = product.getPriceUsd();
            } else {
                price = product.getPriceSom();
            }

            if (product.getBranchType().equals(BranchTypeEnum.MAGAZINE)) {
                result = result + "<b> Do`k.   </b>";
            } else {
                result = result + "<b>Skla.   </b>";
            }

            result = result + "<i>" + product.getType() + "</i>   " + product.getBrand() + "   <b>" + product.getName() +
                    "</b>   <i>" + product.getTariff() + "</i>   <b>" + product.getSize() + "</b>   " + product.getResidue() +
                    "   <i>" + product.getCurrency() + "</i>   <b>" + price + "</b>   " + product.getSumPrice() + "\n\n";
            if (result.length() > 3700) {
                InlineKeyboardMarkup keyingi = botButton.keyingi(product.getId());
                sendMessage.setText(result);
                sendMessage.setReplyMarkup(keyingi);
                return sendMessage;
            }
        }
        sendMessage.setText(result);
        return sendMessage;
    }

    public SendMessage getOneProduct(Message message, Integer id) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            String result = "<b>Шахобча   <i>Тури</i>   Бренд   <i>Махсулот</i>   Тариф   <i>Хажми</i>   Колдик   " +
                    "<i>валюта</i>   Нархи   <i>Сумма $</i></b>\n\n";
            double price = 0;
            if (product.getCurrency().equals("$")) {
                price = product.getPriceUsd();
            } else {
                price = product.getPriceSom();
            }

            if (product.getBranchType().equals(BranchTypeEnum.MAGAZINE)) {
                result = result + "<b> Do`k.   </b>";
            } else {
                result = result + "<b>Skla.   </b>";
            }

            result = result + "<i>" + product.getType() + "</i>   " + product.getBrand() + "   <b>" + product.getName() +
                    "</b>   " + product.getTariff() + "   <b>" + product.getSize() + "</b>   " + product.getResidue() +
                    "   <i>" + product.getCurrency() + "</i>   <b>" + price + "</b>   " + product.getSumPrice() + "\n\n";
            sendMessage.setText(result);
            return sendMessage;
        }
        sendMessage.setText("Mahsulot topilmadi");
        return sendMessage;
    }

    public SendMessage deleteAllProduct(Message message) {
        try {
            productRepository.deleteAll();
            return new SendMessage(String.valueOf(message.getChatId()),"Muvaffaqiyatli tozalandi");
        } catch (Exception e) {
            return new SendMessage(String.valueOf(message.getChatId()),"Tozalashda xatolik yuz berdi");
        }
    }
}
