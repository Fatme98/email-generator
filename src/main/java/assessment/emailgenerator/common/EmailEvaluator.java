package assessment.emailgenerator.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmailEvaluator {
    public static int iterator = 0;

    public static String evaluateExpression(String expression, String input1, String input2, String input3, String input4, String input5, String input6) {
        String[] array = expression.split(" ");
        List<String> list = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (array[i].startsWith("(input")) {
                for (int j = i; j < array.length; j++) {
                    String element = array[j];
                    if (element.endsWith(" )") || element.equals(")")) {
                        stringBuilder.append(element);
                        i = j;
                        break;
                    }
                    if (element.startsWith("(input") || element.equals(">") || element.equals("<") ||
                            element.startsWith("input") || element.equals("?") || element.equals(":") || element.matches(".*\\d+.*")) {
                        stringBuilder.append(array[j]);
                    } else {
                        stringBuilder.append(";");
                    }

                }
                list.add(stringBuilder.toString());
                stringBuilder = new StringBuilder();
            } else {
                list.add(array[i]);
            }
        }
        list = list.stream().filter(element -> element.startsWith("input") || element.startsWith("(input") || (element.startsWith("'") && element.endsWith("'"))).collect(Collectors.toList());
        String email = generateEmail(list, input1, input2, input3, input4, input5);
        return email;
    }

    private static String generateEmail(List<String>expressionList, String...inputs){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <expressionList.size() ; i++) {
            String expression = expressionList.get(i);
            if(expression.contains(".") && expression.toLowerCase().contains("input")){
                String result = evaluateExpression(expression,inputs);
                stringBuilder.append(result);
            }else{
                if(expression.startsWith("'")&&expression.endsWith("'")){
                    stringBuilder.
                            append(expression.substring(expression.indexOf("'")+1,expression.lastIndexOf("'")));
                }else{
                    stringBuilder.append(takeTheParameterValue(expression,inputs));
                }
            }
        }
        return stringBuilder.toString();
    }

    private static String[] separateToParameterAndMethod(String expression){
        int indexOfDot = expression.indexOf(".", iterator);
        String parameter = expression.substring(iterator,indexOfDot);
        int indexOfBracket = expression.indexOf(")",iterator);
        String method = expression.substring(indexOfDot+1, indexOfBracket+1);
        iterator = indexOfBracket+1;
        String[] array = new String[2];
        array[0]=parameter;
        array[1]=method;
        return array;
    }

    private static String takeTheParameterValue(String parameter, String... inputs){
        String parameterValue = "";
        for (int j = 0, k =1;  j <inputs.length ; j++, k++) {
            if(parameter.toLowerCase().equals("input"+k)){
                parameterValue=inputs[j];
            }
        }
        return parameterValue;
    }

    private static String evaluateMethodMiddleValue(String expression, String element, String value) {
        String subExpression = expression.substring(iterator, iterator + 1);
        int number = Integer.parseInt(subExpression);
        boolean result = true;
        if (element.equals(">")) {
            result = Integer.parseInt(value) > number;
        } else if (element.equals("<")) {
            result = Integer.parseInt(value) < number;
        } else if (element.equals("=")) {
            result = Integer.parseInt(value) == number;
        } else if (element.equals("!")) {
            result = Integer.parseInt(value) != number;
        }

        String middleResult = "false";
        if (result) {
            middleResult = "true";
        }
        iterator = iterator + 1;
        return middleResult;
    }

    private static String evaluateMethodCall(String input, String methodCall) {
        String methodName = methodCall.substring(0, methodCall.indexOf('('));
        String[] arguments = methodCall.substring(methodCall.indexOf('(') + 1, methodCall.indexOf(')')).split(",");
        switch (methodName.toLowerCase()) {
            case "eachwordfirstchars":
                return eachWordFirstChars(input, Integer.parseInt(arguments[0]));
            case "wordscount":
                return String.valueOf(wordsCount(input));
            case "lastwords":
                return lastWords(input, Integer.parseInt(arguments[0]));
            default:
                return "";
        }
    }

    private static String eachWordFirstChars(String input, int countOfChars) {
        String regex = "[\\s\\-\\_\\W+]";
        String[] words = splitAnInputToWords(input, regex);
        StringBuilder stringBuffer = new StringBuilder();
        for (String word : words) {
            for (int i=0;i<countOfChars;i++){
                stringBuffer.append(word.toLowerCase().charAt(i));
            }
        }
        return stringBuffer.toString();
    }

    private static String[] splitAnInputToWords(String input, String regex){
        return input.split(regex);
    }

    private static int wordsCount(String input) {
        String regex = "[\\s\\-\\_\\W+]";
        return splitAnInputToWords(input,regex ).length;
    }

    private static String lastWords(String input, int n) {
        String[] words = splitAnInputToWords(input, " ");
        StringBuilder stringBuilder = new StringBuilder();
        if(n>words.length){
            n=words.length;
        }
        if(n<0){
            n=Math.abs(n);
            if(n>words.length){
                n=words.length;
            }
            for (int i = 0; i <n ; i++) {
                stringBuilder.append(words[i].toLowerCase());
            }
        }
        for (int i = words.length-1; i >= words.length - n; i--) {
            stringBuilder.append(words[i].toLowerCase());
        }
        return stringBuilder.toString();
    }
    private static String filterTheExpression(String expression){
        String filteredExpression = expression;
        if(expression.startsWith("(")){
            filteredExpression=expression.substring(1, expression.length()-1);
        }
        return filteredExpression;
    }
    private static String evaluateExpression(String expression, String ...inputs){
        StringBuilder result = new StringBuilder();
        String middleBooleanResult="true";
        String middleResultFromMethods = "";
        expression = filterTheExpression(expression);
        String[] separatedValues = separateToParameterAndMethod(expression);
        String inputValueFirstly = takeTheParameterValue(separatedValues[0],inputs);
        String methodToCallFirstly = separatedValues[1];
        middleResultFromMethods = evaluateMethodCall(inputValueFirstly,methodToCallFirstly);
        try{
            Integer.parseInt(middleResultFromMethods);
        }catch(NumberFormatException ex){
            result.append(middleResultFromMethods);
        }
        if(!expression.endsWith(separatedValues[1])) {
            while(true){
                String operator = expression.substring(iterator, iterator+1);
                if(operator.equals(";")){
                    iterator=iterator+1;
                    separatedValues = separateToParameterAndMethod(expression);
                    String inputValue = takeTheParameterValue(separatedValues[0],inputs);
                    String methodToCall = separatedValues[1];
                    middleResultFromMethods = evaluateMethodCall(inputValue,methodToCall);
                    result.append(middleResultFromMethods);
                } else if(operator.equals("<") || operator.equals(">") || operator.equals("!")|| operator.equals("=")){
                    iterator=iterator+1;
                    middleBooleanResult = evaluateMethodMiddleValue(expression, operator, middleResultFromMethods);
                } else if(operator.equals("?")){
                    if(middleBooleanResult.equals("true")){
                        iterator=iterator+1;
                        separatedValues=separateToParameterAndMethod(expression);
                        String inputValue = takeTheParameterValue(separatedValues[0],inputs);
                        String methodToCall = separatedValues[1];
                        middleResultFromMethods = evaluateMethodCall(inputValue,methodToCall);
                        result.append(middleResultFromMethods);
                    }else{
                        iterator = expression.indexOf(":",iterator);
                    }
                } else if(operator.equals(".")){
                    iterator=iterator+1;
                    String method = expression.substring(iterator, expression.indexOf(")", iterator)+1);
                    String resultFromMethod = evaluateMethodCall(middleResultFromMethods, method);
                    result.replace(result.indexOf(middleResultFromMethods),middleResultFromMethods.length(), resultFromMethod);
                    middleResultFromMethods=resultFromMethod;
                    iterator=expression.indexOf(")", iterator)+1;
                }else if(operator.equals(":")){
                    if(middleBooleanResult.equals("false")){
                        int index = expression.indexOf(":",iterator);
                        iterator = index+1;
                        String subExpression = expression.substring(iterator);
                        if(subExpression.contains(".")){
                            separatedValues=separateToParameterAndMethod(expression);
                            String inputValue = takeTheParameterValue(separatedValues[0],inputs);
                            String methodToCall = separatedValues[1];
                            middleResultFromMethods=evaluateMethodCall(inputValue,methodToCall);
                            result.append(middleResultFromMethods);
                        }else{
                            String inputValue = takeTheParameterValue(subExpression,inputs);
                            result.append(inputValue);
                        }
                    }
                    break;
                }
            }
        }
        iterator = 0;
        return result.toString();
    }

}

