import java.util.UUID;

public class Note {

    UUID GUID;

    String title;

    String content;

    public Note(UUID GUID, String title, String content) {
        this.GUID = GUID;
        this.title = title;
        this.content = content;
    }

    public UUID getGUID() {
        return GUID;
    }

    public void setGUID(UUID GUID) {
        this.GUID = GUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
