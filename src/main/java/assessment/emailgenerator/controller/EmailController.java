package assessment.emailgenerator.controller;

import assessment.emailgenerator.models.view.Emails;
import assessment.emailgenerator.service.EmailGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    private final EmailGeneratorService emailGenerator;

    @Autowired
    public EmailController(EmailGeneratorService emailGeneratorService) {
        this.emailGenerator = emailGeneratorService;
    }

    @GetMapping("/email-generator")
    ResponseEntity<Emails> generateEmail(@RequestParam(value = "Input1", required = false) String input1, @RequestParam(value = "Input2", required = false) String input2,
                                         @RequestParam(value = "Input3", required = false) String input3, @RequestParam(value = "Input4", required = false) String input4,
                                         @RequestParam(value = "Input5", required = false) String input5, @RequestParam(value = "Input6", required = false) String input6,
                                         @RequestParam(value = "expression", required = false) String expression) {
        Emails emails = this.emailGenerator.generateEmails(input1, input2, input3, input4, input5, input6, expression);
        ResponseEntity<Emails> responseEntity = new ResponseEntity<>(emails, HttpStatus.OK);
        return responseEntity;
    }
}
