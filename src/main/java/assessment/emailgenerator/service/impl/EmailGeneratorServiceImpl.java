package assessment.emailgenerator.service.impl;

import assessment.emailgenerator.common.EmailEvaluator;
import assessment.emailgenerator.models.view.Emails;
import assessment.emailgenerator.models.view.GeneratedEmail;
import assessment.emailgenerator.service.EmailGeneratorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailGeneratorServiceImpl implements EmailGeneratorService {
    @Override
    public Emails generateEmails(String input1, String input2, String input3, String input4, String input5, String input6, String expression) {
        Emails emails = new Emails();
        List<GeneratedEmail> generatedEmails = new ArrayList<>();
        generatedEmails.add(emailGenerator(input1, input2, input3, input4, input5, input6, expression));
        emails.setData(generatedEmails);
        return emails;
    }

    private GeneratedEmail emailGenerator(String input1, String input2, String input3, String input4, String input5, String input6, String expression) {
       String result = EmailEvaluator.evaluateExpression(expression, input1,input2,input3,input4,input5,input6);
        return new GeneratedEmail(result,result);
    }
}
