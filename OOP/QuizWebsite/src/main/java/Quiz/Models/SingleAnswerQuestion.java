package Quiz.Models;

public abstract class SingleAnswerQuestion extends Question{
    Answer answer;

    public SingleAnswerQuestion() {
    }

    public SingleAnswerQuestion(int id, String content, int quizId, int typeId) {
        super(id, content, quizId, typeId);
    }

    @Override
    public String getCreatingAnswersHtml() {
        String jspCode =
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
                        "</script>\n";

        return jspCode;
    }

    @Override
    public boolean isAnswerCorrect(String answer) {
        return this.answer.getContent().equals(answer);
    }

    @Override
    public void AddAnswer(Answer answer) {
        this.answer = answer;
    }

    public Answer getAnswer(){
        return answer;
    }
}
