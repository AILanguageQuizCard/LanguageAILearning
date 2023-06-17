package com.chunxia.chatgpt.activity;

public class ActivityIntentKeys {

    public static String SYSTEM_COMMAND = "chatgpt_chat_system_command";
    public static final String BEFORE_USER_MESSAGE_COMMAND = "chatgpt_chat_before_user_message_command";
    public static String START_WORDS = "chatgpt_chat_start_words";
    public static String CHAT_ACTIVITY_START_MODE = "chatgpt_chat_mode";

    public static final String TOPIC_TRAINING_RESULT_KEY = "ai_lingo_master_topic_training_result_key";
    public static final String TOPIC_TRAINING_QUESTION_RESULT_KEY = "ai_lingo_master_topic_training_questions_result_key";

    public static final String TOPIC_TRAINING_RESULT_NUM_KEY = "ai_lingo_master_topic_training_result_num_key";

    public static final String TOPIC_TRAINING_ACTIVITY_TOPIC_KEY = "ai_lingo_master_topic_training_activity_topic_key";

    public static final String TOPIC_TRAINING_ACTIVITY_LEARNING_MATERIAL_KEY = "ai_lingo_master_topic_training_activity_learning_material_key";

    public static final String ACTIVITY_CHAT_ADD_TO_REVIEW_CARD = "activity_chat_add_to_review_card";

    public static final String ACTIVITY_CHAT_MODE = "activity_chat_mode";
    public static final String ACTIVITY_ADD_REVIEW_SENTENCE_CARD_QUESTION = "activity_add_review_card_sentence_card_question";
    public static final String ACTIVITY_ADD_REVIEW_SENTENCE_CARD_ANSWER = "activity_add_review_card_sentence_card_answer";
    public static final String ACTIVITY_ADD_REVIEW_SENTENCE_CARD_TOPIC = "activity_add_review_card_sentence_card_topic";

    public static final String ACTIVITY_REVIEW_CARD_TOPIC = "activity_review_card_topic";
    public static final String ACTIVITY_REVIEW_CARD_MODE = "activity_review_card_mode";
    public static final String ACTIVITY_REVIEW_CARD_MODE_ALL = "activity_review_card_mode_all";
    public static final String ACTIVITY_REVIEW_CARD_MODE_SINGLE = "activity_review_card_mode_single";
    public static final String ACTIVITY_REVIEW_CARD_EDITED_SENTENCES_LIST = "activity_review_card_edited_sentence_list";
    public static final String ACTIVITY_REVIEW_CARD_EDITED_QUESTION = "activity_review_card_edited_question";
    public static final String ACTIVITY_REVIEW_CARD_EDITED_ANSWER = "activity_review_card_edited_answer";

    public static final String TOPIC_TRAINING_ACTIVITY_MODE_KEY = "ai_lingo_master_topic_training_activity_mode_key";
    public static final String TOPIC_TRAINING_SENTENCE_PATTERN = "sentence_pattern_training";
    public static final String TOPIC_TRAINING_GRAMMAR= "grammar_training";
    public static final String TOPIC_TRAINING_TOPIC = "topic_training";

    public static String getActivityChatModeKey(String s) {
        return ACTIVITY_CHAT_MODE + s;
    }
}
