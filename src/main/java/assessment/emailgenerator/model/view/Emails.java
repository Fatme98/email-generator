package assessment.emailgenerator.models.view;

import java.util.List;
import java.util.Map;

public class Emails {
    private List<GeneratedEmail> data;

    public Emails() {

    }

    public List<GeneratedEmail> getData() {
        return data;
    }

    public void setData(List<GeneratedEmail> data) {
        this.data = data;
    }
}
