package assessment.emailgenerator.service;

import assessment.emailgenerator.models.view.Emails;

public interface EmailGeneratorService {
    Emails generateEmails(String input1, String input2, String input3, String input4, String input5, String input6, String expression);
}
