import Quiz.Models.*;
import Quiz.Models.enums.questionType;
import QuizPage.Models.Result;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

// tests for result class


public class QuestionTest {
    private Question question1 = new QuestionResponseQuestion();
    private Question question5 = new QuestionResponseQuestion(2);
    private Question question6 = new QuestionResponseQuestion(2, "as", 3, 4);
    private Question question2 = new MultipleChoiceQuestion();
    private Question question7 = new MultipleChoiceQuestion(2, "as", 3, 4);
    private Question question3 = new FillInTheBlankQuestion();
    private Question question8 = new FillInTheBlankQuestion(2, "as", 3, 4);
    private Question question4 = new PictureResponseQuestion();
    private Question question9 = new PictureResponseQuestion(2, "as", 3, 4);

    // basic tests
    @Test
    public void testConstructors() {
        assertNotNull(question1);
        assertNotNull(question2);
        assertNotNull(question3);
        assertNotNull(question4);
        assertNotNull(question5);
        assertNotNull(question6);
        assertNotNull(question7);
        assertNotNull(question8);
        assertNotNull(question9);
    }

    @Test
    public void testGetMethods1() {
        assertEquals(question5.getType(), questionType.Question_Response);
        assertEquals(question6.getTypeId(), 4);
        assertEquals(question6.getQuizId(), 3);
        assertEquals(question6.getId(), 2);
        assertEquals(question6.getContent(), "as");

    }


    @Test
    public void testGetMethods2() {
        assertEquals(question7.getType(), questionType.Multiple_Choice);
        assertEquals(question7.getTypeId(), 4);
        assertEquals(question7.getQuizId(), 3);
        assertEquals(question7.getId(), 2);
        assertEquals(question7.getContent(), "as");
    }

    @Test
    public void testGetMethods3() {
        assertEquals(question8.getType(), questionType.Fill_In_The_Blank);
        assertEquals(question8.getTypeId(), 4);
        assertEquals(question8.getQuizId(), 3);
        assertEquals(question8.getId(), 2);
        assertEquals(question8.getContent(), "as");
    }

    @Test
    public void testGetMethods4() {
        assertEquals(question9.getType(), questionType.Picture_Response_Question);
        assertEquals(question9.getTypeId(), 4);
        assertEquals(question9.getQuizId(), 3);
        assertEquals(question9.getId(), 2);
        assertEquals(question9.getContent(), "as");
    }

    @Test
    public void testToString(){
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<h1 class=\"option-heading\">Input Strings</h1>");
        htmlBuilder.append("<label class=\"option-label\">Enter Answers:</label>");
        htmlBuilder.append("<div id=\"stringContainer\">");
        htmlBuilder.append("<input type=\"text\" name=\"strings[]\" placeholder=\"Enter An Answer\"><br>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<button type=\"button\" id=\"addString\">Add Answer</button><br>");
        htmlBuilder.append("<script>");
        htmlBuilder.append("document.getElementById(\"addString\").addEventListener(\"click\", function() {");
        htmlBuilder.append("var stringContainer = document.getElementById(\"stringContainer\");");
        htmlBuilder.append("var input = document.createElement(\"input\");");
        htmlBuilder.append("input.type = \"text\";");
        htmlBuilder.append("input.name = \"strings[]\";");
        htmlBuilder.append("input.placeholder = \"Enter a string\";");
        htmlBuilder.append("stringContainer.appendChild(input);");
        htmlBuilder.append("stringContainer.appendChild(document.createElement(\"br\"));");
        htmlBuilder.append("});");
        htmlBuilder.append("</script>");
        String a =  htmlBuilder.toString();
        assertEquals(question1.getCreatingAnswersHtml(), a);
    }

    @Test
    public void testHtml(){
        assertEquals(question2.getCreatingAnswersHtml(),
                "<div class=\"container\">\n" +
                        "    <h2 class=\"option-heading\">Select an Option:</h2>\n" +
                        "    <input type=\"text\" id=\"newOption\" class=\"option-input\" placeholder=\"Enter an option\">\n" +
                        "    <button type=\"button\" id=\"addOption\" class=\"add-option-button\">Add Option</button>\n" +
                        "    <div id=\"optionsContainer\" class=\"option-container\">\n" +
                        "        <!-- Existing options will be displayed here -->\n" +
                        "    </div>\n" +
                        "    <input type=\"hidden\" id=\"optionsArray\" name=\"optionsArray\" value=\"\">\n" +
                        "    <input type=\"hidden\" id=\"chosenOption\" name=\"chosenOption\" value=\"\">\n" +
                        "</div>\n" +
                        "<script>\n" +
                        "  document.addEventListener(\"DOMContentLoaded\", function() {\n" +
                        "  var optionsArray = []; // Array to store options\n" +
                        "  var chosenOption = \"\"; // Variable to store the chosen option\n" +
                        "\n" +
                        "  document.getElementById(\"addOption\").addEventListener(\"click\", function() {\n" +
                        "    var newOption = document.getElementById(\"newOption\").value;\n" +
                        "\n" +
                        "    if (newOption.trim() !== \"\") {\n" +
                        "      optionsArray.push(newOption); // Add the option to the array\n" +
                        "      if(optionsArray.length === 1){\n" +
                        "        document.getElementById(\"chosenOption\").value = newOption;\n" +
                        "      }\n" +
                        "      var input = document.createElement(\"input\");\n" +
                        "      input.type = \"radio\";\n" +
                        "      input.name = \"option\"; // Keep the name \"option\" for radio buttons\n" +
                        "      input.value = newOption;\n" +
                        "      input.checked = true; // Make the newly added option checked\n" +
                        "\n" +
                        "      var textNode = document.createTextNode(newOption);\n" +
                        "      var br = document.createElement(\"br\");\n" +
                        "\n" +
                        "      var optionsContainer = document.getElementById(\"optionsContainer\");\n" +
                        "      optionsContainer.appendChild(input);\n" +
                        "      optionsContainer.appendChild(textNode);\n" +
                        "      optionsContainer.appendChild(br);\n" +
                        "\n" +
                        "      // Clear the input field\n" +
                        "      document.getElementById(\"newOption\").value = \"\";\n" +
                        "\n" +
                        "      // Update the hidden input field for options array\n" +
                        "      document.getElementById(\"optionsArray\").value = optionsArray.join(\",\");\n" +
                        "\n" +
                        "      // Update chosen option event listeners\n" +
                        "      updateChosenOptionListeners();\n" +
                        "    }\n" +
                        "  });\n" +
                        "\n" +
                        "  // Function to update chosen option event listeners\n" +
                        "  function updateChosenOptionListeners() {\n" +
                        "    var radios = document.getElementsByName(\"option\");\n" +
                        "    for (var i = 0; i < radios.length; i++) {\n" +
                        "      radios[i].addEventListener(\"click\", function() {\n" +
                        "        chosenOption = this.value;\n" +
                        "        document.getElementById(\"chosenOption\").value = chosenOption;\n" +
                        "      });\n" +
                        "    }\n" +
                        "  }\n" +
                        "\n" +
                        "  // Initial call to update event listeners\n" +
                        "  updateChosenOptionListeners();\n" +
                        "  });\n" +
                        "</script>\n");
    }
}
