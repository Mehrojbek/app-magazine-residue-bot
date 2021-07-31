package uz.mehrojbek.appmagazinresiduebot.bot;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import uz.mehrojbek.appmagazinresiduebot.repository.UserRepository;
import uz.mehrojbek.appmagazinresiduebot.service.BotFileService;
import uz.mehrojbek.appmagazinresiduebot.service.BotInlineModeService;
import uz.mehrojbek.appmagazinresiduebot.service.BotTextService;
import uz.mehrojbek.appmagazinresiduebot.utils.SystemUtils;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class BotController extends TelegramLongPollingBot {
    private final BotTextService botTextService;
    private final UserRepository userRepository;
    private final BotInlineModeService botInlineModeService;
    private final BotFileService botFileService;

    private final static String downloads = "downloads";

    private static Set<Long> systemUsers = new HashSet<>();
    private static Set<Long> updaterUser = new HashSet<>();
    private static Set<Long> tempUser = new HashSet<>();
    @Value("${bot.token}")
    private String token;
    @Value("${bot.username}")
    private String username;


    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (systemUsers.isEmpty()) {
            systemUsers.addAll(userRepository.getAllByUserChatId());
        }

        if (update.hasMessage()) {

            //Filter authorization
            if (!systemUsers.contains(update.getMessage().getChatId())) {
                if (update.getMessage().hasText()) {
                    if (update.getMessage().getText().equals(SystemUtils.enterCode)) {
                        User user = update.getMessage().getFrom();
                        String firstName = user.getFirstName();
                        Long chatId = update.getMessage().getChatId();
                        uz.mehrojbek.appmagazinresiduebot.entity.User newUser = new uz.mehrojbek.appmagazinresiduebot.entity.User(firstName, chatId);
                        userRepository.save(newUser);
                        systemUsers.add(chatId);
                        execute(botTextService.anyMessage(update.getMessage(), "Muvaffaqiyatli ro'yxatdan o'tdingiz"));
                        execute(botTextService.start(update.getMessage()));
                        return;
                    } else {
                        execute(botTextService.anyMessage(update.getMessage(), "Siz avtorizatsiyadan o'tmagansiz, iltimos parolni kiriting : "));
                        return;
                    }
                }
            }


            Message message = update.getMessage();

            //Text service
            if (message.hasText()) {
                String text = message.getText();
                switch (text) {
                    case "/start":
                        execute(botTextService.start(message));
                        return;
                    case "/excel":
                        tempUser.add(message.getChatId());
                        execute(botTextService.enterCode(message));
                        return;
                    case SystemUtils.excelCode:
                        if (tempUser.contains(message.getChatId())) {
                            updaterUser.add(message.getChatId());
                            try { execute(botTextService.deleteMessage(message)); } catch (Exception e) {e.printStackTrace();}
                            execute(botTextService.anyMessage(message, "faylni yuboring : "));
                        }
                        return;
                    case SystemUtils.brand:
                        execute(botTextService.brands(message));
                        return;
                    case SystemUtils.category:
                        execute(botTextService.products(message,0));
                        return;
                    case SystemUtils.warehouse:
                        return;
                    default:
                        if (text.startsWith("mahsulot id:")) {
                            String[] split = text.split(":");
                            Integer id = Integer.parseInt(split[1]);
                            execute(botTextService.getOneProduct(message,id));
                            return;
                        }
                        execute(botTextService.anyMessage(message,"Noto'g'ri komanda berildi"));
                        return;
                }
            }

            //Document service
            if (message.hasDocument()) {
                String res="";
                try {
                    if (!updaterUser.contains(message.getChatId())) {
                        execute(botTextService.anyMessage(message, "Siz avtorizatsiyadan o'tmagansiz, parolni kiriting!"));
                        return;
                    }

                    Document document = message.getDocument();
                    java.io.File myObj = new java.io.File("downloads/"+document.getFileName());
                    if (myObj.delete()) {
                        res = "Uchirildi";
                    } else {
                        res = "uchirilmadi";
                    }
                    GetFile getFile = new GetFile(document.getFileId());
                    File file = execute(getFile);
                    java.io.File originalFile = downloadFile(file);
                    java.io.File downloadFileToSystem = new java.io.File(downloads + "/" + document.getFileName());
                    FileCopyUtils.copy(originalFile, downloadFileToSystem);
                    SendMessage sendMessage = botFileService.saveProducts(message, document.getFileName());
                    execute(sendMessage);
                    execute(botTextService.anyMessage(message, "File saqlandi"));
                    tempUser.clear();
                    updaterUser.clear();
                    return;
                }catch (Exception e){
                    execute(new SendMessage(String.valueOf(message.getChatId()),"downloadda xatolik  "+e.getMessage() + "\n"+res));
                }
            }
        }


        //Inline mode serviceif
        if(update.hasInlineQuery()) {
            InlineQuery inlineQuery = update.getInlineQuery();
            if (inlineQuery.getQuery().equals(""))
                return;
            AnswerInlineQuery answerInlineQuery = botInlineModeService.answerInlineQuery(inlineQuery);
            try {
                execute(answerInlineQuery);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //CallbackQuery service
        if (update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Message message = callbackQuery.getMessage();
            String brand = callbackQuery.getData();
            if (brand.startsWith("brand:")){
                brand = brand.substring(6);
                execute(botTextService.brandValues(message,brand,0));
                return;
            }
            if (brand.startsWith("keyingi:")){
                String[] split = brand.split(":");
                Integer id = Integer.parseInt(split[1]);
                String brandName = split[3];
                execute(botTextService.brandValues(message,brandName,id));
                return;
            }
            if (brand.startsWith("all:")){
                String[] split = brand.split(":");
                Integer id = Integer.parseInt(split[1]);
                execute(botTextService.products(message,id));
            }
        }
    }
}
