package com.gal.smartcalender;

public interface Constants {
    public static final String notification_process_sys_message =
            "You will receive notifications in JSON format. Each notification contains details about an event. " +
                    "Your task is to extract relevant information and generate a structured calendar event in the following JSON format:\n\n" +
                    "{\n" +
                    "  \"start_date\": \"<ISO 8601 DateTime format>\", // Make sure you provide the right timezone in the ISO 8601 format, look at the given time\n" +
                    "  \"end_date\": \"<ISO 8601 DateTime format>\", // Make sure you provide the right timezone in the ISO 8601 format, look at the given time\n" +
                    "  \"description\": \"<Concise event summary>\",\n" +
                    "  \"urgency\": <0-1 score>,\n" +
                    "  \"importance\": <0-1 score>\n" +
                    "}\n\n" +
                    "- Only create events for meaningful, actionable, or scheduled activities. " +
                    "Do NOT create calendar events for trivial or generic notifications, such as \"You received a message\", \"New notification received\", or app/system alerts, " +
                    "unless they clearly describe a time-based activity the user must act on.\n" +
                    "- Extract Dates: Identify the start and end DateTime from the notification. Use ISO 8601 DateTime format (YYYY-MM-DDTHH:MM:SSZ). " +
                    "- Generate Description: Summarize the event concisely. Remove unnecessary details.\n" +
                    "- Assign Urgency (0-1): Score how time-sensitive the event is (1 = immediate action needed, 0 = not urgent).\n" +
                    "- Assign Importance (0-1): Score how significant the event is (1 = critical, 0 = trivial).\n\n" +
                    "If any required field is missing in the notification, infer it based on context or use sensible defaults. " +
                    "Always ensure the output is well-formatted JSON.\n\n" +
                    "The current date and time is: <CURR_DATE_TIME>.\n" +
                    "If the notification does NOT describe a real or actionable event, return the string NO_EVENT. Do not create unnecessary or spammy events!";

    public static final String db_name = "event-db";

    public static final String no_event_ret = "NO_EVENT";

    public static final String SELECTED_APPS_PREFERENCE  = "selected_apps";


}
