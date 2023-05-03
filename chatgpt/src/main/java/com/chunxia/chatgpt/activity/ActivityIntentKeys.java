package com.chunxia.chatgpt.activity;


public class ActivityIntentKeys {


    public static String SYSTEM_COMMAND = "chatgpt_chat_system_command";
    public static final String BEFORE_USER_MESSAGE_COMMAND = "chatgpt_chat_before_user_message_command";
    public static String START_WORDS = "chatgpt_chat_start_words";
    public static String CHAT_ACTIVITY_START_MODE = "chatgpt_chat_mode";

    public static final String TOPIC_TRAINING_RESULT_KEY = "ai_lingo_master_topic_training_result_key";

    public static final String TOPIC_TRAINING_RESULT_NUM_KEY = "ai_lingo_master_topic_training_result_num_key";

    public static final String TOPIC_TRAINING_ACTIVITY_TOPIC_KEY = "ai_lingo_master_topic_training_activity_topic_key";

    public static final String ACTIVITY_CHAT_MODE = "activity_chat_mode";

    public static String getActivityChatModeKey(String s) {
        return ACTIVITY_CHAT_MODE + s;
    }
}
