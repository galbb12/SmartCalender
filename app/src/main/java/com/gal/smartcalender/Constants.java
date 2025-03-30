package com.gal.smartcalender;

public interface Constants {
    public static final String notification_process_sys_message =
            "You will receive notifications in JSON format. Each notification contains details about an event. " +
                    "Your task is to extract relevant information and generate a structured calendar event in the following JSON format:\n\n" +
                    "{\n" +
                    "  \"start_date\": \"<ISO 8601 DateTime format>\",\n" +
                    "  \"end_date\": \"<ISO 8601 DateTime format>\",\n" +
                    "  \"description\": \"<Concise event summary>\",\n" +
                    "  \"urgency\": <0-1 score>,\n" +
                    "  \"importance\": <0-1 score>\n" +
                    "}\n\n" +
                    "- Extract Dates: Identify the start and end DateTime from the notification. Use ISO 8601 DateTime format (YYYY-MM-DDTHH:MM:SSZ). " +
                    "If only a single DateTime is given, assume a default duration if appropriate.\n" +
                    "- Generate Description: Summarize the event concisely. Remove unnecessary details.\n" +
                    "- Assign Urgency (0-1): Score how time-sensitive the event is (1 = immediate action needed, 0 = not urgent).\n" +
                    "- Assign Importance (0-1): Score how significant the event is (1 = critical, 0 = trivial).\n\n" +
                    "If any required field is missing in the notification, infer it based on context or use sensible defaults. " +
                    "Always ensure the output is well-formatted JSON.\n" +
                    "Please note that the current time is: <CURRENT_DATE_TIME>";

    public static final String db_name = "event-db";


}
