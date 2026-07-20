package mv.mossuh.momomentum.CONFIGS;

import mv.mossuh.momomentum.MoMomentum;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Messages {

    private static final List<String> messagesConfigList = new ArrayList<>(Arrays.asList("BOOSTERS", "NO_PERMISSION"));
    private static final Map<String, String> messagesMap = new ConcurrentHashMap<>();
    private static final Map<String, List<String>> messagesListMap = new ConcurrentHashMap<>();

    private String messagePosition;
    public Messages(String messagePosition) {
        this.messagePosition = messagePosition;
    }
    private String get() {
        return messagesMap.get(messagePosition);
    }
    public List<String> getStringList() {
        return messagesListMap.get(messagePosition);
    }

    public static List<String> BOOSTERS;
    public static String NO_PERMISSION;


    public static void load(MoMomentum instance) {
        FileConfiguration messagesConfig = instance.getConfigs().getMessages().getConfig();

        for (String messageConfig : messagesConfigList) {
            if (messagesConfig.isString(messageConfig)) {
                String message = messagesConfig.getString(messageConfig);
                if (message != null) {
                    messagesMap.put(messageConfig, message);
                    continue;
                }
                messagesMap.put(messageConfig, "");
            } else if (messagesConfig.isList(messageConfig)) {
                List<String> message = messagesConfig.getStringList(messageConfig);
                if (!message.isEmpty()) {
                    messagesListMap.put(messageConfig, message);
                    continue;
                }
                messagesListMap.put(messageConfig, new ArrayList<>());
            }
        }

        loadMessages();
    }

    private static void loadMessages() {
        BOOSTERS = new Messages("BOOSTERS").getStringList();
        NO_PERMISSION = new Messages("NO_PERMISSION").get();
    }
}
