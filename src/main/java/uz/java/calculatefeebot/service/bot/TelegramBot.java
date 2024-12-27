package uz.java.calculatefeebot.service.bot;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.java.calculatefeebot.config.BotConfig;
import uz.java.calculatefeebot.constants.command.MessageCommandType;
import uz.java.calculatefeebot.constants.command.UpdateCommandType;
import uz.java.calculatefeebot.constants.enums.Lang;
import uz.java.calculatefeebot.constants.enums.StepEnum;
import uz.java.calculatefeebot.constants.response.UzRowKeyboardConstants;
import uz.java.calculatefeebot.model.enums.EducationLevel;
import uz.java.calculatefeebot.service.AdminService;
import uz.java.calculatefeebot.service.UserService;
import uz.java.calculatefeebot.constants.response.EngRowKeyboardConstants;
import uz.java.calculatefeebot.constants.response.RusRowKeyboardConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot implements UzRowKeyboardConstants {

    Map<Long, String> nameMap = new HashMap<>();
    Map<Long, String> tgUsernameMap = new HashMap<>();
    Map<Long, Integer> ageMap = new HashMap<>();
    Map<Long, Boolean> genderIsManMap = new HashMap<>();
    Map<Long, String> phoneNumberMap = new HashMap<>();
    Map<Long, Boolean> isBlockedMap = new HashMap<>();
    Map<Long, EducationLevel> educationLevelMap = new HashMap<>();
    Map<Long, Lang> langMap = new HashMap<>();
    Map<Long, StepEnum> stepMap = new HashMap<>();

    @Value("${callbackKey}")
    String key;

    private final BotConfig botConfig;

    private final UserService userService;

    private final AdminService adminService;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        UpdateCommandType type = getCommandType(update);
        try {
            switch (type) {
                case MESSAGE_TYPE -> messageStart(update.getMessage(), update.getMessage().getChatId());
                case CALLBACK_QUERY ->
                        callBackStart(update.getCallbackQuery(), update.getCallbackQuery().getMessage().getChatId());
                default -> System.out.println();
            }
        } catch (Exception e) {
            log.warn("Telegram bot error");
        }
    }

    private UpdateCommandType getCommandType(Update update) {
        if (update.hasMessage()) return UpdateCommandType.MESSAGE_TYPE;
        if (update.hasCallbackQuery()) return UpdateCommandType.CALLBACK_QUERY;
        return UpdateCommandType.DEFAULT_COMMAND;
    }

    private void messageStart(Message message, Long chatId) {

        MessageCommandType type = getMessageType(message);
        switch (type) {
            case TEXT -> textStart(message.getText(), chatId);
            case CONTACT -> contactStart(message.getContact(), message.getChatId());
        }
    }

    @SneakyThrows
    private void textStart(String text, Long chatId) {

        System.out.println("chatId -> "+ chatId);
        System.out.println("text -> "+ text);
        System.out.println("");

        SendMessage sendMessage = new SendMessage();
        if (text.equals("/start")) {
            stepMap.put(chatId, StepEnum.START);
            sendMessage.setReplyMarkup(chooseLang(sendMessage, chatId));
            execute(sendMessage);
        }

    }

    @SneakyThrows
    private void callBackStart(CallbackQuery callbackQuery, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        if (stepMap.get(chatId) == StepEnum.CHOOSE_LANG) {
            stepMap.put(chatId, StepEnum.WORK_CONDITION);
            sendMessage.setReplyMarkup(workCondition(sendMessage, chatId));
            execute(sendMessage);
        }
//        else if (stepMap.get(chatId) == WORK_CONDITION && callbackQuery.getData().equals(WOR)) {
//            stepMap
//        }
    }

    private InlineKeyboardMarkup workCondition(SendMessage sendMessage, Long chatId) {
        List<String> inlineCommands = new ArrayList<>();

        if (langMap.get(chatId) == null || langMap.get(chatId) == Lang.UZB) {
            sendMessage.setText(CHOOSE_WORK_CONDITION_UZ);
            for (String s : WORK_CONDITION_LIST_UZ)
                inlineCommands.add(s);
        } else if (langMap.get(chatId) == Lang.RUS) {
            sendMessage.setText(String.valueOf(RusRowKeyboardConstants.WORK_CONDITION_LIST_RUS));
            for (String s : RusRowKeyboardConstants.WORK_CONDITION_LIST_RUS)
                inlineCommands.add(s);
        } else if (langMap.get(chatId) == Lang.ENG) {
            sendMessage.setText(String.valueOf(EngRowKeyboardConstants.WORK_CONDITION_LIST_ENG));
            for (String s : EngRowKeyboardConstants.WORK_CONDITION_LIST_ENG)
                inlineCommands.add(s);
        }


        return customInlineKeyboardMarkup1(inlineCommands, chatId);
    }

    private InlineKeyboardMarkup chooseLang(SendMessage sendMessage, Long chatId) {

        if (langMap.get(chatId) == null || langMap.get(chatId) == Lang.UZB) {
            sendMessage.setText(CHOOSE_LANG_UZ);
        } else if (langMap.get(chatId) == Lang.RUS) {
            sendMessage.setText(RusRowKeyboardConstants.CHOOSE_LANG_RUS);
        } else if (langMap.get(chatId) == Lang.ENG) {
            sendMessage.setText(EngRowKeyboardConstants.CHOOSE_LANG_ENG);
        }

        List<String> inlineCommands = new ArrayList<>();

        inlineCommands.add(String.valueOf(Lang.UZB));
        inlineCommands.add(String.valueOf(Lang.RUS));
        inlineCommands.add(String.valueOf(Lang.ENG));

        return customInlineKeyboardMarkup1(inlineCommands, chatId);
    }

    private InlineKeyboardMarkup customInlineKeyboardMarkup1(List<String> inlineCommands, Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineBlock = new ArrayList<>();

        for (int i = 0; i < inlineCommands.size(); i++) {
            List<InlineKeyboardButton> inlineRow = new ArrayList<>();

            InlineKeyboardButton inlineButton = new InlineKeyboardButton();
            inlineButton.setText(inlineCommands.get(i));
            inlineButton.setCallbackData(stepMap.get(chatId) + key + i);

            inlineRow.add(inlineButton);
            inlineBlock.add(inlineRow);
        }

        inlineKeyboardMarkup.setKeyboard(inlineBlock);
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup customInlineKeyboardMarkup2(List<String> inlineCommands) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineBlock = new ArrayList<>();

        for (int i = 0; i < inlineCommands.size(); i++) {
            List<InlineKeyboardButton> inlineRow = new ArrayList<>();

            InlineKeyboardButton inlineButton = new InlineKeyboardButton();
            inlineButton.setText(inlineCommands.get(i));
            inlineButton.setCallbackData(inlineCommands.get(i) + key);
            inlineRow.add(inlineButton);

            if (i + 1 < inlineCommands.size()) {
                InlineKeyboardButton inlineButton2 = new InlineKeyboardButton();
                inlineButton2.setText(inlineCommands.get(i));
                inlineButton2.setCallbackData(inlineCommands.get(i) + key);
                inlineRow.add(inlineButton2);
            }

            inlineBlock.add(inlineRow);
        }

        inlineKeyboardMarkup.setKeyboard(inlineBlock);
        return inlineKeyboardMarkup;
    }


    private void contactStart(Contact contact, Long chatId) {

    }




    private MessageCommandType getMessageType(Message message) {
        if (message.hasText()) return MessageCommandType.TEXT;
        if (message.hasPhoto()) return MessageCommandType.PHOTO;
        if (message.hasContact()) return MessageCommandType.CONTACT;
        if (message.hasDocument()) return MessageCommandType.DOCUMENT;
        if (message.hasAnimation()) return MessageCommandType.ANIMATION;
        if (message.hasVoice()) return MessageCommandType.VOICE;
        if (message.hasVideo()) return MessageCommandType.VIDEO;
        if (message.hasSticker()) return MessageCommandType.STICKER;
        if (message.hasLocation()) return MessageCommandType.LOCATION;
        if (message.hasPoll()) return MessageCommandType.POLL;
        if (message.hasAudio()) return MessageCommandType.AUDIO;
        return MessageCommandType.INVALID;
    }

    private void executeMessage(String text, Long chatId, String parseMode) {
        log.info("executeMessage parseMode");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        sendMessage.setParseMode(parseMode);
        executeMessage(sendMessage);
    }

    private void executeMessage(String text, Long chatId, ReplyKeyboard replyMarkup) {
        log.info("executeMessage replyMarkup");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(replyMarkup);
        executeMessage(sendMessage);
    }

    private void executeMessage(String text, Long chatId) {
        log.info("executeMessage");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        executeMessage(sendMessage);
    }

    private void executeMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.warn("Bot is not working " + e);
        }
    }

}

