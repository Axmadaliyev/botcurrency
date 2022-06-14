package com.example.telegrambot.bot;

import com.example.telegrambot.admin.Admin;
import com.example.telegrambot.admin.AdminServicee;
import com.example.telegrambot.model.Story;
import com.example.telegrambot.model.UserSend;
import com.example.telegrambot.service.BotService;
import com.example.telegrambot.twilio.TwilioSms;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;


public class TestBot extends TelegramLongPollingBot {
    private final BotService servisBot = new BotService();
    private final Admin adminClass = new Admin();
    private final UserSend userSend = new UserSend();
    private final AdminServicee adminServicee = new AdminServicee();
    private final Story story = new Story();


    @Override
    public String getBotUsername() {
        return "java_G2_becend_bot";
    }

    @Override
    public String getBotToken() {
        return "5282499655:AAHIGGimUQTfMLWhX8ZC9Jtrxik0HMgD7_U";
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            SendMessage sendMessage = new SendMessage();
            String chatId = update.getMessage().getChatId().toString();
            if (update.getMessage().hasText()) {
                Message message = update.getMessage();
                String text = message.getText();
                log(update.getMessage().getChatId().toString(), update.getMessage().getFrom().getUserName(), text);
                switch (text) {
                    case "/start":
                        execute(adminServicee.start(update, chatId));
                        break;
                    case "/converter":
                        execute(servisBot.handleMessage(message));
                        break;
                    case "/admin":
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Sizga sms kodi boradi\nKod ni kiriting!");
                        execute(sendMessage);
                        TwilioSms.smsCode();
                        break;
                    case "history\uD83D\uDCC3":
                        AdminServicee.ExcelStoryWriter(update);
                        execute(AdminServicee.SendDocumentStory(update,chatId));
                        break;
                    case "users\uD83D\uDCC3":
                        AdminServicee.ExcelWriter(update, chatId);
                        execute(adminServicee.SendDocument(update, chatId));
                        break;
                    case "currencies\uD83D\uDCC3":
                        execute(adminServicee.SendDocumentPDFList(update, chatId));
                        break;
                    case "Reclama":
                        adminServicee.sendReklama();
                        break;
                    case "News":
                        adminServicee.news(update, chatId);
                        break;
                    case "2005":
                        execute(adminServicee.buttons(update, chatId));
                        break;
                    case "/help":
                        execute(adminServicee.help(chatId, update));
                    default:
                        execute(servisBot.natija(message, update));
                }


            } else if (update.getMessage().hasContact()) {
                execute(servisBot.Contact(update, chatId));
                execute(servisBot.daleteKeyboard(update, chatId));

            }


        } else if (update.hasCallbackQuery()) {
            execute(servisBot.handleCallback(update.getCallbackQuery()));
        }


    }

    public void log(String chatId, String userName, String text) {
        System.out.println(chatId + "--" + userName + " : " + text);

    }


}
