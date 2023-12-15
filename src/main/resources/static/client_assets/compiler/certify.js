const testCasesContainer = document.getElementById('test-cases-container');
const resultSection = document.getElementById('result');
var run = document.getElementById("run");
var submit = document.getElementById("submit");
var theme = document.getElementById("theme");
const excuting = document.getElementById('excuting');
const assignment_id = document.getElementById('hidden_input');
const current_quiz_id = document.getElementById('current_quiz');
const assignment_kit_idElement = document.getElementById('assignment_kit_id');
const number_of_assignments = document.getElementById('number_of_assignments');
const number_of_quiz = document.getElementById('number_of_quiz');
const durationElement = document.getElementById('duration');
const list_assignment_id = document.getElementById('idAssignments');
const list_quiz_id = document.getElementById('idQuizs');
const languageMode = document.getElementById('language_mode');
const submission_id_4assignment_list = [];
const submission_id_4quiz_list = [];
let submitCheck = false;

function submitQuiz() {
    const quizId = current_quiz_id.value.replace("quizBtn_", "");
    const answer = document.querySelector('input[name="answer_radio_'+quizId+'"]:checked');
    if (answer === null) {
        alert("Hãy chọn đáp án bạn cho rằng đúng được liệt kê bên dưới.");
    } else {
        if (confirm('Bạn có chắc muốn nộp đáp án, bạn sẽ không thể chỉnh sửa sau khi nộp?')) {
            $.ajax({
                type: 'POST',
                url: '/api/submissions/submit-quiz',
                data: {
                    answer: answer.value,
                    quiz_id: quizId
                },
                success: function (response) {
                    console.log(response);
                    var buttonActive = document.querySelector('button.nav-link.active');
                    var quizInputsHasValue = document.querySelectorAll('[name="submission_id_4quiz"][value]');
                    submitCheck = true;
                    if (quizInputsHasValue.length != number_of_assignments.value) {
                        const id = 'submission_id_4quiz_' + buttonActive.id.replace("quizBtn_", "");

                        const submission_input_found = document.getElementById(id);
                        submission_input_found.value = response;
                        buttonActive.setAttribute("disabled", "");
                        buttonActive.style.background = "lightgreen";
                        var inputs = document.querySelectorAll('[name="submission_id_4quiz"]:not([value])');
                        var assignmentInputsHasValue = document.querySelectorAll('[name="submission_id_4assignment"][value]');
                        //đã làm hết quiz
                        if (inputs.length == 0) {
                            //next assignment tab or finish
                            if (assignmentInputsHasValue.length != number_of_assignments.value) {//chưa làm hết assignment
                                var inputs = document.querySelectorAll('[name="submission_id_4assignment"]:not([value])');
                                var id_temp = inputs[0].id;
                                var idWithoutPrefix = id_temp.replace("submission_id_4assignment_", "");
                                const button = document.getElementById(idWithoutPrefix);
                                button.click();
                            } else { //đã làm hết quiz và assignment
                                const button = document.getElementById("description-tab");
                                const description = document.getElementById("description_container");
                                const btn_endContest =`<button type="button" id="end_test" style="float: right" class="btn btn-outline-primary">Nộp bài</button>`;
                                description.innerHTML+=btn_endContest;
                                var btn_id = document.getElementById("end_test");
                                btn_id.onclick = function() {
                                    submitTest();
                                };
                                button.click();
                            }
                        } else {     //chưa làm hết quiz
                            var id_temp = inputs[0].id;
                            var idWithoutPrefix = id_temp.replace("submission_id_4quiz_", "quizBtn_");
                            console.log(idWithoutPrefix);
                            const button = document.getElementById(idWithoutPrefix);
                            button.click();
                        }
                    }
                },
                error: function (error) {
                    console.error('Error when submit quiz:', error);
                }
            });

        }
    }
}
$(document).ready(function () {

    var buttonActive = document.querySelector('button.nav-link.active');
    const editorDiv = document.getElementById('editor-div');
    if (buttonActive.name == "quiztab") {
        editorDiv.style.display = 'none';
    } else {
        editorDiv.style.display = 'block';
    }

    document.getElementById('courseTab').addEventListener('show.bs.tab', function (event) {
        const activeTab = event.target; // Tab đang active
        const previousTab = event.relatedTarget; // Tab trước đó
        window.location.hash = activeTab.id;
        const cppValue = "#include<iostream>\n\nint main() {\n  std::cout << \"Hello world!\";\n\n  return 0;\n}";
        const pythonValue = "print('Hello world!')";
        const javaValue = 'import java.util.*;\nimport java.io.*;\n\npublic class Main {\n    public static void main(String[] args) {\n      System.out.println(\"Hello world!\");\n    }\n}';
        const csharpValue = "using System;\n\nclass Program {\n   static void Main() {\n     Console.WriteLine(\"Hello world!\");\n   }\n}";

        if (previousTab.name !== "quiztab") {
            if (editor.getValue() == cppValue || editor.getValue() == csharpValue || editor.getValue() == javaValue || editor.getValue() == pythonValue) {
                return;
            } else {
                if (submitCheck === false) {
                    if (!confirm('Next tab without saving?')) {
                        event.preventDefault();
                    } else {
                        $.ajax({
                            type: 'POST',
                            url: '/api/submissions/clear-session',
                            success: function (response) {
                                if (languageMode === null) {
                                    var dSelectedText = document.querySelector('label.dd-selected-text');
                                    dSelectedText.innerHTML = "C++";
                                    var selectedInput = document.querySelector('a.dd-option-selected');
                                    selectedInput.classList.remove('dd-option-selected');

                                    const current_input_value = 'input.dd-option-value[value="text/x-c++src"]';
                                    var current_input = document.querySelector(current_input_value);
                                    if (current_input) {
                                        var parentA = current_input.closest('a');
                                        if (parentA) {
                                            parentA.classList.add('dd-option-selected');
                                        } else {
                                            console.log('Không tìm thấy thẻ a chứa input selec language option.');
                                        }
                                    } else {
                                        console.log('Không tìm thấy thẻ input có giá trị là "input_value".');
                                    }
                                    editor.setOption("mode", "text/x-c++src")
                                    editor.setValue("#include<iostream>\n\nint main() {\n  std::cout << \"Hello world!\";\n\n  return 0;\n}")
                                    option = 54;
                                    langague_name = 'text/x-c++src';
                                } else {
                                    const modeValue = languageMode.value;
                                    var dSelectedText = document.querySelector('label.dd-selected-text');
                                    var selectedInput = document.querySelector('a.dd-option-selected');
                                    selectedInput.classList.remove('dd-option-selected');

                                    const current_input_value = 'input.dd-option-value[value="' + modeValue + '"]';
                                    var current_input = document.querySelector(current_input_value);
                                    if (current_input) {
                                        var parentA = current_input.closest('a');
                                        if (parentA) {
                                            parentA.classList.add('dd-option-selected');
                                        } else {
                                            console.log('Không tìm thấy thẻ a chứa input selec language option.');
                                        }
                                    } else {
                                        console.log('Không tìm thấy thẻ input có giá trị là "input_value".');
                                    }
                                    editor.setOption("mode", modeValue)
                                    if (modeValue === "text/x-c++src") {
                                        dSelectedText.innerHTML = "C++";
                                        editor.setValue(cppValue);
                                        option = 54;

                                    } else if (modeValue === "text/x-java") {
                                        dSelectedText.innerHTML = "Java";
                                        editor.setValue(javaValue);
                                        option = 91;

                                    } else if (modeValue === "text/x-csharp") {
                                        dSelectedText.innerHTML = "C#";
                                        editor.setValue(csharpValue);
                                        option = 51;

                                    } else {
                                        dSelectedText.innerHTML = "Python";
                                        editor.setValue(pythonValue);
                                        option = 71;

                                    }

                                    langague_name = modeValue;
                                }

                            },
                            error: function (error) {
                                console.error('Error clear session:', error);
                            }
                        });
                    }
                } else {
                    $.ajax({
                        type: 'POST',
                        url: '/api/submissions/clear-session',
                        success: function (response) {
                            if (languageMode === null) {
                                var dSelectedText = document.querySelector('label.dd-selected-text');
                                dSelectedText.innerHTML = "C++";
                                var selectedInput = document.querySelector('a.dd-option-selected');
                                selectedInput.classList.remove('dd-option-selected');

                                const current_input_value = 'input.dd-option-value[value="text/x-c++src"]';
                                var current_input = document.querySelector(current_input_value);
                                if (current_input) {
                                    var parentA = current_input.closest('a');
                                    if (parentA) {
                                        parentA.classList.add('dd-option-selected');
                                    } else {
                                        console.log('Không tìm thấy thẻ a chứa input selec language option.');
                                    }
                                } else {
                                    console.log('Không tìm thấy thẻ input có giá trị là "input_value".');
                                }
                                editor.setOption("mode", "text/x-c++src")
                                editor.setValue("#include<iostream>\n\nint main() {\n  std::cout << \"Hello world!\";\n\n  return 0;\n}")
                                option = 54;
                                langague_name = 'text/x-c++src';
                            } else {
                                const modeValue = languageMode.value;
                                var dSelectedText = document.querySelector('label.dd-selected-text');
                                var selectedInput = document.querySelector('a.dd-option-selected');
                                selectedInput.classList.remove('dd-option-selected');

                                const current_input_value = 'input.dd-option-value[value="' + modeValue + '"]';
                                var current_input = document.querySelector(current_input_value);
                                if (current_input) {
                                    var parentA = current_input.closest('a');
                                    if (parentA) {
                                        parentA.classList.add('dd-option-selected');
                                    } else {
                                        console.log('Không tìm thấy thẻ a chứa input selec language option.');
                                    }
                                } else {
                                    console.log('Không tìm thấy thẻ input có giá trị là "input_value".');
                                }
                                editor.setOption("mode", modeValue)
                                if (modeValue === "text/x-c++src") {
                                    dSelectedText.innerHTML = "C++";
                                    editor.setValue(cppValue);
                                    option = 54;

                                } else if (modeValue === "text/x-java") {
                                    dSelectedText.innerHTML = "Java";
                                    editor.setValue(javaValue);
                                    option = 91;

                                } else if (modeValue === "text/x-csharp") {
                                    dSelectedText.innerHTML = "C#";
                                    editor.setValue(csharpValue);
                                    option = 51;

                                } else {
                                    dSelectedText.innerHTML = "Python";
                                    editor.setValue(pythonValue);
                                    option = 71;

                                }

                                langague_name = modeValue;
                            }

                        },
                        error: function (error) {
                            console.error('Error clear session:', error);
                        }
                    });
                }
            }
        }

    });
    document.getElementById('courseTab').addEventListener('shown.bs.tab', function (event) {
        const activeTab = event.target; // Tab đang active
        const previousTab = event.relatedTarget; // Tab trước đó
        const editorDiv = document.getElementById('editor-div');
        // Kiểm tra tab và enable/disable các nút tương ứng
        if (activeTab.id === 'description-tab' || activeTab.name === 'quiztab') {
            editorDiv.style.display = 'none';
            run.setAttribute("disabled", "");
            submit.setAttribute("disabled", "");
            current_quiz_id.value = activeTab.id;
            assignment_id.value = "";
        } else {
            editorDiv.style.display = 'block';
            run.removeAttribute("disabled");
            submit.removeAttribute("disabled");
            assignment_id.value = activeTab.id;
            current_quiz_id.value = "";
        }
    });


});
document.addEventListener("DOMContentLoaded", (event) => {
    var hash = window.location.hash;
    if (hash !== '') {
        const tabToSelect = document.getElementById(window.location.hash.substr(1));
        if (tabToSelect) {
            const tab = new bootstrap.Tab(tabToSelect);
            tab.show();
        }
    }
    var quizValue = list_quiz_id.value;
    console.log(quizValue);
    var quiz_id_list = JSON.parse(quizValue);
    var assignmentValue = list_assignment_id.value;
    var assignment_id_list = JSON.parse(assignmentValue);

    for (let i = 0; i < number_of_quiz.value; i++) {
        var input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'submission_id_4quiz';
        input.id = 'submission_id_4quiz_' + quiz_id_list[i];
        document.body.appendChild(input);
        var quiz_submit = document.getElementById("quiz_submitBtn_"+ quiz_id_list[i]);
        quiz_submit.addEventListener ("click", submitQuiz);
    }
    for (let i = 0; i < number_of_assignments.value; i++) {
        var input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'submission_id_4assignment';
        input.id = 'submission_id_4assignment_' + assignment_id_list[i];
        document.body.appendChild(input);
    }


    const testDuration = durationElement.value;
    updateTimer(testDuration);

});

$(document).ready(function () {
    editor.on('change', function () {
        submitCheck = false;
        saveContent(editor.getValue());
    });
    var buttonActive = document.querySelector('button.nav-link.active');
    if (buttonActive.name !== "quiztab") {
        console.log("GET CONTENT");
        $.ajax({
            type: 'GET',
            url: '/api/submissions/get-content',
            success: function (response) {
                if (response != null) {
                    const dataJson = JSON.parse(response);
                    const mode_receive = dataJson.mode;
                    const current_tab_id = dataJson.current_tab_id;
                    const content_receive = dataJson.content;
                    const language_name_receive = dataJson.language_name;


                    const nextTabButton = document.getElementById(current_tab_id);
                    nextTabButton.click();


                    const current_input_value = 'input.dd-option-value[value="' + mode_receive + '"]';
                    var input_to_change = document.querySelector(current_input_value);
                    var selectedInput = document.querySelector('a.dd-option-selected');
                    var dSelectedValue = document.querySelector('input.dd-selected-value');
                    var dSelectedText = document.querySelector('label.dd-selected-text');

                    if (mode_receive === "text/x-c++src") {
                        dSelectedText.innerHTML = "C++";
                    }
                    if (mode_receive === "text/x-java") {
                        dSelectedText.innerHTML = "Java";
                    }
                    if (mode_receive === "text/x-csharp") {
                        dSelectedText.innerHTML = "C#";
                    }
                    if (mode_receive === "text/x-python") {
                        dSelectedText.innerHTML = "Python";
                    }

                    if (input_to_change) {
                        var parentA = input_to_change.closest('a');
                        // Kiểm tra xem có thẻ a được tìm thấy không
                        if (parentA) {
                            dSelectedValue.value = mode_receive;
                            selectedInput.classList.remove('dd-option-selected');
                            // Xoá class "dd-option-selected" khỏi thẻ a
                            parentA.classList.add('dd-option-selected');
                        } else {
                            console.log('Không tìm thấy thẻ a chứa input.');
                        }
                    } else {
                        console.log('Không tìm thấy thẻ input có giá trị là "input_value".');
                    }

                    editor.setOption("mode", mode_receive);
                    editor.setValue(content_receive);
                    if (mode_receive === "text/x-c++src") {
                        option = 54;
                    }
                    if (mode_receive === "text/x-java") {
                        option = 91;

                    }
                    if (mode_receive === "text/x-csharp") {
                        option = 51;
                    }
                    if (mode_receive === "text/x-python") {
                        option = 71;
                    }
                    langague_name = language_name_receive;
                }
            },
            error: function (error) {
                console.error('Error getting content:', error);
            }
        });

    }

});
async function submitTest() {
    const submission_id_4assignments = document.querySelectorAll('input[name="submission_id_4assignment"]');

    submission_id_4assignments.forEach(input => {
        let submission_kit_to_list = input.value;
        submission_id_4assignment_list.push(submission_kit_to_list);
    });
    const submission_id_4quizs = document.querySelectorAll('input[name="submission_id_4quiz"]');
    submission_id_4quizs.forEach(input => {
        let submission_kit_to_list = input.value;
        submission_id_4quiz_list.push(submission_kit_to_list);
    });
    console.log(JSON.stringify(submission_id_4assignment_list));
    console.log(JSON.stringify(submission_id_4quiz_list));
    const response = await fetch('/api/submissions/end-test', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            submissions_id_ofAssignment: (submission_id_4assignment_list),
            submissions_id_ofQuiz: (submission_id_4quiz_list),
            assignment_kit_id: assignment_kit_idElement.value
        })
    });
    const result = await response.text();
    console.log(result);
    const url = `/skills-verification/details/${assignment_kit_idElement.value}`;
    window.location.href = url;
}
function saveContent(content) {
    // Lấy thẻ input đầu tiên có class "dd-option-selected" trong thẻ a
    var selectedInput = document.querySelector('a.dd-option-selected input.dd-option-value');
    var current_tab_id = document.querySelector('button.nav-link.active');
    $.ajax({
        type: 'POST',
        url: '/api/submissions/save-content',
        data: {
            content: content,
            mode: selectedInput.value,
            langague_name: langague_name,
            current_tab_id: current_tab_id.id
        },
        success: function (response) {
            console.log('Content saved successfully!');
        },
        error: function (error) {
            console.error('Error saving content:', error);
        }
    });
}

function updateTimer(endTime) {
    var timeLimitInMinutes = endTime;
    var timeLimitInSeconds = timeLimitInMinutes * 60;
    var timerElement = document.getElementById('timer');

    function startTimer() {
        timeLimitInSeconds--;
        var minutes = Math.floor(timeLimitInSeconds / 60);
        var seconds = timeLimitInSeconds % 60;

        if (minutes == 5 && seconds == 0) {
            alert('5mins left. The test will close and you will fail if timeup, so Please submit the test before the deadline.');
        }
        if (minutes == 1 && seconds == 0) {
            alert('The test will close in 2mins and you will fail if timeup, Please submit the test now!');
        }
        if (minutes <= 4) {
            timerElement.className = 'text-danger';
        }
        if (timeLimitInSeconds < 0) {
            timerElement.textContent = '00:00';
            timerElement.className = 'text-danger';
            clearInterval(timerInterval);
            $.ajax({
                type: 'POST',
                url: '/api/submissions/time-expired',
                data: {
                    assignment_kit_id: assignment_kit_idElement.value
                },
                success: function (response) {
                    alert('Thank you for taking the test. You have used up the allotted time for this test. You can retry this test after 30 days');
                    const url = `/skills-verification/details/${assignment_kit_idElement.value}`;
                    window.location.href = url;
                },
                error: function (error) {
                    console.error('Error saving content:', error);
                }
            });
            return;
        }

        if (minutes < 10) {
            minutes = '0' + minutes;
        }
        if (seconds < 10) {
            seconds = '0' + seconds;
        }

        timerElement.textContent = minutes + ':' + seconds;
    }

    var timerInterval = setInterval(startTimer, 1000);
}

function encode(str) {
    return btoa(unescape(encodeURIComponent(str || "")));
}

function decode(bytes) {
    var escaped = escape(atob(bytes || ""));
    return decodeURIComponent(escaped);

}

theme.addEventListener("change", function () {
    if (theme.value === "Dark") {
        editor.setOption("theme", "dracula")

    }
    if (theme.value === "Light") {
        editor.setOption("theme", "3024-day")
    }
    if (theme.value === "Night") {
        editor.setOption("theme", "night")
    }
    if (theme.value === "Idea") {
        editor.setOption("theme", "idea")
    }
    if (theme.value === "Ocenaic") {
        editor.setOption("theme", "oceanic-next")
    }
})
import {MY_API_KEY} from "./config.js"


function addEventForContainer(newContainer, count) {
    // Thêm container vào container chứa test cases
    testCasesContainer.appendChild(newContainer);
    Prism.highlightAll();
    const viewMoreButton = document.getElementById(`view-more-button-${count}`);
    const testcaseDetails = document.getElementById(`event-details-${count}`);
    viewMoreButton.addEventListener('click', () => {
        if (testcaseDetails.style.display === 'block') {
            testcaseDetails.style.display = 'none';
        } else {
            testcaseDetails.style.display = 'block';
        }
    });
    excuting.style.display = 'none';
    resultSection.style.display = 'block';
}

//batch submission
run.addEventListener("click", async function () {
    testCasesContainer.innerHTML = '';
    excuting.style.display = 'block';
    const bottomElement = document.getElementById('excuting');
    bottomElement.scrollIntoView({behavior: 'smooth'});
    let count = 0;
    let submissions;
    $.ajax({
        url: '/api/submissions/get-assignment-info',
        type: 'GET',
        data: {assignment_id: assignment_id.value},
        success: async (data) => {
            const dataJson = JSON.parse(data);
            const testCases = dataJson.sampleTestCases;
            const time_limit = dataJson.time_limit;
            const memory_limit = dataJson.memory_limit;
            if (time_limit != 0 && memory_limit != 0) {
                submissions = testCases.map(testCase => {
                    // Tạo đối tượng submission cho mỗi test case
                    return {
                        language_id: option,
                        source_code: editor.getValue(),
                        stdin: testCase.input,
                        expected_output: testCase.expectedOutput,
                        cpu_time_limit: time_limit,
                        memory_limit: memory_limit
                    };
                });
            } else if (time_limit != 0 && memory_limit == 0) {
                submissions = testCases.map(testCase => {
                    // Tạo đối tượng submission cho mỗi test case
                    return {
                        language_id: option,
                        source_code: editor.getValue(),
                        stdin: testCase.input,
                        expected_output: testCase.expectedOutput,
                        cpu_time_limit: time_limit
                    };
                });
            } else if (time_limit == 0 && memory_limit != 0) {
                submissions = testCases.map(testCase => {
                    // Tạo đối tượng submission cho mỗi test case
                    return {
                        language_id: option,
                        source_code: editor.getValue(),
                        stdin: testCase.input,
                        expected_output: testCase.expectedOutput,
                        memory_limit: memory_limit
                    };
                });
            } else {
                submissions = testCases.map(testCase => {
                    // Tạo đối tượng submission cho mỗi test case
                    return {
                        language_id: option,
                        source_code: editor.getValue(),
                        stdin: testCase.input,
                        expected_output: testCase.expectedOutput
                    };
                });
            }
            console.log(submissions);

            const postURL = 'https://judge0-ce.p.rapidapi.com/submissions/batch?base64_encoded=false';
            const options = {
                method: 'POST',
                headers: {
                    'content-type': 'application/json',
                    'Content-Type': 'application/json',
                    'X-RapidAPI-Key': MY_API_KEY,
                    'X-RapidAPI-Host': 'judge0-ce.p.rapidapi.com'
                },
                body: JSON.stringify({
                    submissions: submissions
                })
            };

            try {
                const response = await fetch(postURL, options);
                const postResult = await response.json();
                const tokens = postResult.map(item => item.token);
                const tokensString = tokens.join(',');
                console.log("Tokens: " + tokensString);
                const checkStatus = async () => {
                    const getURL = `https://ce.judge0.com/submissions/batch?tokens=${tokensString}&base64_encoded=true&fields=source_code,stdout,stdin,memory,time,token,compile_output,message,status,expected_output`;
                    const getOptions = {
                        method: 'GET',
                        headers: {
                            'X-RapidAPI-Key': MY_API_KEY,
                            'X-RapidAPI-Host': 'judge0-ce.p.rapidapi.com'
                        }
                    };
                    try {
                        const getResult = await fetch(getURL, getOptions);
                        const result = await getResult.json();
                        console.log(result);
                        //"Processing" - "In Queue"
                        const processingSubmissions = result.submissions.filter(submission => submission.status && submission.status.id <= 2);
                        if (processingSubmissions.length >= 1) {
                            setTimeout(() => {
                                checkStatus();
                            }, 4000);
                        } else {
                            result.submissions.every((submission, index) => {
                                const newContainer = document.createElement('div');
                                newContainer.classList.add('col-xxl-10', 'offset-xxl-1', 'col-xl-10', 'offset-xl-1', 'col-lg-10', 'offset-lg-1');
                                if (submission.status && submission.status.id === 3) {
                                    //Accepted
                                    var stdin = decode(submission.stdin);
                                    var stdout = decode(submission.stdout);
                                    var expected_output = decode(submission.expected_output);

                                    count = count + 1;
                                    let isSampleTestCase;
                                    if (count <= testCases.length) {
                                        isSampleTestCase = true;
                                    } else isSampleTestCase = false;

                                    newContainer.innerHTML = `
        <div class="events__item mb-10 hover__active">
              <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
            <div class="events__content">
                <div class="events__meta">
                   <span>${submission.time}s</span>
                                                   <span>${submission.memory}KB</span>
                                                   <span>Sucess</span>
                </div>
                <h3 class="events__title">
                    <a class="price__features">
                        <i class="far fa-check" style="background: rgb(48 168 32 / 9%);;
                            border-radius: 50%;
                            font-size: 25px;"></i> Sample Test case 0${count}
                    </a>
                </h3>
            </div>
             <div class="events__more">
                ${isSampleTestCase ? `<a class="link-btn" id="view-more-button-${count}" style="cursor: pointer">
                            Xem chi tiết
                            <i class="far fa-arrow-right"></i>
                            <i class="far fa-arrow-right"></i>
                          </a>` : `<a class="link-btn" id="view-more-button-${count}" style="cursor: pointer">
                                      Hidden TestCase
                                      <i style="color: #69b35c;" class="far fa-unlock"></i>
                                      <i style="color: #e65972;" class="far fa-lock"></i>
                                   </a>`}
             </div>
        </div>
        <div id="event-details-${count}" style="display: none; padding: 10px 60px 60px 60px; border-radius: 7px; background-color: white">
            <div class="contact__info white-bg p-relative z-index-1">
                <div class="contact__info-inner white-bg">
                    <ul>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Compiler Message</h4>
                                    <pre class="pre-tags" style="font-weight: initial; text-align: center" id="message">Thành công</pre>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Input</h4>
                                   <pre class="pre-tags line-numbers language-markup">
                                       <code class="language-markup">
                                         ${stdin}
                                       </code>
                                    </pre>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Output</h4>
                                    <pre class="pre-tags line-numbers language-markup">
                                       <code class="language-markup">
                                         ${stdout}
                                       </code>
                                    </pre>
                                </div>
                            </div>
                        </li>
                     
                    </ul>
                </div>
            </div>
        </div>
 
    `;
                                    addEventForContainer(newContainer, count);
                                } else if (submission.status && submission.status.id === 4) {
                                    //Wrong Answer
                                    var stdin = decode(submission.stdin);
                                    var stdout = decode(submission.stdout);
                                    var expected_output = decode(submission.expected_output);
                                    count = count + 1;
                                    newContainer.innerHTML = `
        <div class="events__item mb-10 hover__active">
              <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
            <div class="events__content">
                <div class="events__meta">
                   <span>${submission.time}s</span>
                                                   <span>${submission.memory} KB</span>
                                                   <span>Sai kết quả</span>
                </div>
                <h3 class="events__title" style="color: #d12a47;">
                    <a class="price__features">
                        <i class="far fa-warning" style="background: rgb(48 168 32 / 9%);
                            border-radius: 50%;
                            font-size: 25px;"></i>  Sample Test case 0${count}
                    </a>
                </h3>
            </div>
            <div class="events__more">
                <a class="link-btn" id="view-more-button-${count}" style="color: #e65972; cursor: pointer">
                    Xem chi tiết
                    <i class="far fa-arrow-right"></i>
                    <i class="far fa-arrow-right"></i>
                </a>
            </div>
            </div>
        </div>
        <div id="event-details-${count}" style="display: none; padding: 10px 60px 60px 60px; border-radius: 7px; background-color: white">
            <div class="contact__info white-bg p-relative z-index-1">
                <div class="contact__info-inner white-bg">
                    <ul>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Compiler Message</h4>
                                    <pre class="pre-tags" style="font-weight: initial" id="message">Kết quả không trùng khớp</pre>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Input</h4>
                                      <pre class="pre-tags line-numbers language-markup">
                                      <code class="language-markup"> 
                                        ${stdin}
                                       </code>
                                    
                                    </pre>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Your Output</h4>
                                  <pre class="pre-tags line-numbers language-markup">
                                       <code class="language-markup">
                                         ${stdout}
                                       </code>
                                    </pre>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Expected Output</h4>
                                     <pre class="pre-tags line-numbers language-markup">
                                       <code class="language-markup">
                                         ${expected_output}
                                       </code>
                                    </pre>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
 
    `;
                                    addEventForContainer(newContainer, count);
                                    return false;
                                } else if (submission.status && submission.status.id === 11) {
                                    var message = decode(submission.message);
                                    var description = (submission.status.description);
                                    var output = [message, description].join("\n").trim();

                                    count = count + 1;
                                    newContainer.innerHTML = `
        <div class="events__item mb-10 hover__active">
              <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
            <div class="events__content">
                <div class="events__meta">
                   <span>${submission.time}s</span>
                                                   <span>${submission.memory} KB</span>
                                                   <span>Error Status 11</span>
                </div>
                <h3 class="events__title">
                    <a class="price__features" style="color: #d12a47;">
                        <i class="far fa-warning" style="background: rgb(48 168 32 / 9%);
                            border-radius: 50%;
                            font-size: 25px;"></i>Test case 0${count}
                    </a>
                </h3>
            </div>
            <div class="events__more">
                <a class="link-btn" id="view-more-button-${count}" style="color: #e65972; cursor: pointer">
                    Xem chi tiết
                    <i class="far fa-arrow-right"></i>
                    <i class="far fa-arrow-right"></i>
                </a>
            </div>
            </div>
        </div>
        <div id="event-details-${count}" style="display: none; padding: 10px 60px 60px 60px; border-radius: 7px; background-color: white">
            <div class="contact__info white-bg p-relative z-index-1">
                <div class="contact__info-inner white-bg">
                    <ul>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Compiler Message</h4>
                                   <pre style="font-weight: initial class="pre-tags line-numbers language-markup">
                                       <code class="language-markup">
                                         ${output}
                                       </code>
                                    </pre>
                                </div>
                            </div>
                        </li>
                   
                    </ul>
                </div>
            </div>
        </div>
 
    `;
                                    // Thêm container vào container chứa test cases
                                    addEventForContainer(newContainer, count);
                                    return false;
                                } else {

                                    var stdout = decode(submission.stdout);
                                    var compile_output = decode(submission.compile_output);
                                    var output = [compile_output, stdout].join("\n").trim();
                                    console.log("status ID: " + submission.status.id);
                                    count = count + 1;
                                    newContainer.innerHTML = `
        <div class="events__item mb-10 hover__active">
              <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
            <div class="events__content">
                <div class="events__meta">
                   <span>${submission.time}s</span>
                                                   <span>${submission.memory} KB</span>
                                                  <span>Error Status ${submission.status.id}</span>
                </div>
                <h3 class="events__title">
                    <a class="price__features" style="color: #d12a47;">
                        <i class="far fa-warning" style="background: rgb(48 168 32 / 9%);
                            border-radius: 50%;
                            font-size: 25px;"></i>  Sample Test case 0${count}
                    </a>
                </h3>
            </div>
            <div class="events__more">
                <a class="link-btn" id="view-more-button-${count}" style="color: #e65972; cursor: pointer">
                    Xem chi tiết
                    <i class="far fa-arrow-right"></i>
                    <i class="far fa-arrow-right"></i>
                </a>
            </div>
            </div>
        </div>
        <div id="event-details-${count}" style="display: none; padding: 10px 60px 60px 60px; border-radius: 7px; background-color: white">
            <div class="contact__info white-bg p-relative z-index-1">
                <div class="contact__info-inner white-bg">
                    <ul>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Compiler Message</h4>
                                      <pre class="pre-tags line-numbers language-markup">
                                       <code class="language-markup">
                                         ${output}
                                       </code>
                                    </pre>
                                </div>
                            </div>
                        </li>
                   
                    </ul>
                </div>
            </div>
        </div>
 
    `;
                                    addEventForContainer(newContainer, count);
                                    return false;
                                }
                                return true;
                            });
                        }


                    } catch (error) {
                        console.log(error);
                    }
                };
                await checkStatus();
            } catch (error) {
                console.error(error);
            }

        },
        error: (error) => {
            console.log(error);
            console.log("Error with id: " + assignment_id.value);
        }
    });

});

submit.addEventListener("click", async function () {

    if (confirm('Bạn có chắc muốn nộp đáp án này, bạn sẽ không thể chỉnh sửa sau khi nộp?')) {
        testCasesContainer.innerHTML = '';
        let submissions_to_send = [];
        const languageInput = document.querySelector('input.dd-selected-value');
        const langagueName = languageInput.value;
        const source_code = editor.getValue();
        excuting.style.display = 'block';
        const bottomElement = document.getElementById('excuting');
        bottomElement.scrollIntoView({behavior: 'smooth'});
        let count = 0;
        let submissions;
        $.ajax({
            url: '/api/submissions/get-assignment-info',
            type: 'GET',
            data: {assignment_id: assignment_id.value},
            success: async (data) => {
                const dataJson = JSON.parse(data);
                const allTestCases = dataJson.allTestCases;
                const time_limit = dataJson.time_limit;
                const memory_limit = dataJson.memory_limit;
                const testCases = dataJson.sampleTestCases;
                if (time_limit != 0 && memory_limit != 0) {
                    submissions = allTestCases.map(testCase => {
                        // Tạo đối tượng submission cho mỗi test case
                        return {
                            language_id: option,
                            source_code: editor.getValue(),
                            stdin: testCase.input,
                            expected_output: testCase.expectedOutput,
                            cpu_time_limit: time_limit.value,
                            memory_limit: memory_limit.value
                        };
                    });
                } else if (time_limit != 0 && memory_limit == 0) {
                    submissions = allTestCases.map(testCase => {
                        // Tạo đối tượng submission cho mỗi test case
                        return {
                            language_id: option,
                            source_code: editor.getValue(),
                            stdin: testCase.input,
                            expected_output: testCase.expectedOutput,
                            cpu_time_limit: time_limit.value
                        };
                    });
                } else if (time_limit == 0 && memory_limit != 0) {
                    submissions = allTestCases.map(testCase => {
                        // Tạo đối tượng submission cho mỗi test case
                        return {
                            language_id: option,
                            source_code: editor.getValue(),
                            stdin: testCase.input,
                            expected_output: testCase.expectedOutput,
                            memory_limit: memory_limit.value
                        };
                    });
                } else {
                    submissions = allTestCases.map(testCase => {
                        // Tạo đối tượng submission cho mỗi test case
                        return {
                            language_id: option,
                            source_code: editor.getValue(),
                            stdin: testCase.input,
                            expected_output: testCase.expectedOutput
                        };
                    });
                }
                console.log(submissions);
                const postURL = 'https://judge0-ce.p.rapidapi.com/submissions/batch?base64_encoded=false';
                const options = {
                    method: 'POST',
                    headers: {
                        'content-type': 'application/json',
                        'Content-Type': 'application/json',
                        'X-RapidAPI-Key': MY_API_KEY,
                        'X-RapidAPI-Host': 'judge0-ce.p.rapidapi.com'
                    },
                    body: JSON.stringify({
                        submissions: submissions
                    })
                };

                try {
                    const response = await fetch(postURL, options);
                    const postResult = await response.json();
                    const tokens = postResult.map(item => item.token);
                    const tokensString = tokens.join(',');
                    console.log("Tokens: " + tokensString);
                    const checkSubmitStatus = async () => {
                        const getURL = `https://ce.judge0.com/submissions/batch?tokens=${tokensString}&base64_encoded=true&fields=source_code,stdout,stdin,memory,time,token,compile_output,message,status,expected_output`;
                        const getOptions = {
                            method: 'GET',
                            headers: {
                                'X-RapidAPI-Key': MY_API_KEY,
                                'X-RapidAPI-Host': 'judge0-ce.p.rapidapi.com'
                            }
                        };

                        const getResult = await fetch(getURL, getOptions);
                        const result = await getResult.json();
                        console.log(result);
                        const processingSubmissions = result.submissions.filter(submission => submission.status && submission.status.id <= 2);
                        if (processingSubmissions.length >= 1) {
                            setTimeout(() => {
                                checkSubmitStatus();
                            }, 4000);
                        } else {
                            result.submissions.every((submission, index) => {

                                /*   const newContainer = document.createElement('div');
                                   newContainer.classList.add('col-xxl-10', 'offset-xxl-1', 'col-xl-10', 'offset-xl-1', 'col-lg-10', 'offset-lg-1');*/
                                if (submission.status && submission.status.id === 3) {
                                    //Accepted
                                    var stdin = decode(submission.stdin);
                                    var stdout = decode(submission.stdout);
                                    var expected_output = decode(submission.expected_output);

                                    let submission_add_to_list = {
                                        executionTime: submission.time,
                                        memory: submission.memory,
                                        my_output: stdout,
                                        expected_output: expected_output,
                                        stdin: stdin,
                                        ispassed: true
                                    };
                                    submissions_to_send.push(submission_add_to_list);


                                    count = count + 1;
                                    /*
                                                                        newContainer.innerHTML = `
                                            <div class="events__item mb-10 hover__active">
                                                  <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
                                                <div class="events__content">
                                                    <div class="events__meta">
                                                       <span>${submission.time}s</span>
                                                                                       <span>${submission.memory}KB</span>
                                                                                       <span>Sucess</span>
                                                    </div>
                                                    <h3 class="events__title">
                                                        <a class="price__features">
                                                            <i class="far fa-check" style="background: rgb(48 168 32 / 9%);;
                                                                border-radius: 50%;
                                                                font-size: 25px;"></i> Test case 0${count}
                                                        </a>
                                                    </h3>
                                                </div>

                                                    <div class="events__more" style="display: ${isSampleTestCase ? 'block' : 'none'}">
                                                        <a class="link-btn" id="view-more-button-${count}" style="cursor: pointer">
                                                            Xem chi tiết
                                                            <i class="far fa-arrow-right"></i>
                                                            <i class="far fa-arrow-right"></i>
                                                        </a>
                                                    </div>
                                                    <div class="events__more" style="display: ${isSampleTestCase ? 'none' : 'block'}">
                                                        <a class="link-btn" id="view-more-button-${count}" style="cursor: pointer">
                                                            Hidden TestCase
                                                            <i style="color: #69b35c;" class="far fa-unlock"></i>
                                                            <i style="color: #e65972;" class="far fa-lock"></i>
                                                        </a>
                                                    </div>
                                                </div>
                                            </div>
                                            <div id="event-details-${count}" style="display: none; padding: 10px 60px 60px 60px; border-radius: 7px; background-color: white">
                                                <div class="contact__info white-bg p-relative z-index-1">
                                                    <div class="contact__info-inner white-bg">
                                                        <ul>
                                                            <li>
                                                                <div class="contact__info-item d-flex align-items-start mb-20">
                                                                    <div class="contact__info-text" style="width: 100%">
                                                                        <h4 class="blue-grey">Compiler Message</h4>
                                                                        <pre class="pre-tags" style="font-weight: initial; text-align: center"" id="message">Thành công</pre>
                                                                    </div>
                                                                </div>
                                                            </li>
                                                            <li>
                                                                <div class="contact__info-item d-flex align-items-start mb-20">
                                                                    <div class="contact__info-text" style="width: 100%">
                                                                        <h4 class="blue-grey">Input</h4>
                                                                        <pre class="pre-tags" id="input">${stdin}</pre>
                                                                    </div>
                                                                </div>
                                                            </li>
                                                            <li>
                                                                <div class="contact__info-item d-flex align-items-start mb-20">
                                                                    <div class="contact__info-text" style="width: 100%">
                                                                        <h4 class="blue-grey">Output</h4>
                                                                        <pre class="pre-tags" id="my_output">${stdout}</pre>
                                                                    </div>
                                                                </div>
                                                            </li>

                                                        </ul>
                                                    </div>
                                                </div>
                                            </div>

                                        `;
                                                                        // Thêm container vào container chứa test cases
                                                                        testCasesContainer.appendChild(newContainer);
                                                                        const viewMoreButton = document.getElementById(`view-more-button-${count}`);
                                                                        const testcaseDetails = document.getElementById(`event-details-${count}`);
                                                                        viewMoreButton.addEventListener('click', () => {
                                                                            if (testcaseDetails.style.display === 'block') {
                                                                                testcaseDetails.style.display = 'none';
                                                                            } else {
                                                                                testcaseDetails.style.display = 'block';
                                                                            }
                                                                        });

                                                                        resultSection.style.display = 'block';*/
                                    excuting.style.display = 'none';
                                } else if (submission.status && submission.status.id === 4) {
                                    //Wrong Answer
                                    var stdin = decode(submission.stdin);
                                    var stdout = decode(submission.stdout);
                                    var expected_output = decode(submission.expected_output);

                                    let submission_add_to_list = {
                                        executionTime: submission.time,
                                        memory: submission.memory,
                                        my_output: stdout,
                                        expected_output: expected_output,
                                        stdin: stdin,
                                        ispassed: false
                                    };
                                    submissions_to_send.push(submission_add_to_list);
                                    count = count + 1;
                                    /*
                                             newContainer.innerHTML = `
                 <div class="events__item mb-10 hover__active">
                       <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
                     <div class="events__content">
                         <div class="events__meta">
                            <span>${submission.time}s</span>
                                                            <span>${submission.memory} KB</span>
                                                            <span>Sai kết quả</span>
                         </div>
                         <h3 class="events__title" style="color: #d12a47;">
                             <a class="price__features">
                                 <i class="far fa-warning" style="background: rgb(48 168 32 / 9%);
                                     border-radius: 50%;
                                     font-size: 25px;"></i>  Sample Test case 0${count}
                             </a>
                         </h3>
                     </div>
                     <div class="events__more">
                         <a class="link-btn" id="view-more-button-${count}" style="color: #e65972; cursor: pointer">
                             Xem chi tiết
                             <i class="far fa-arrow-right"></i>
                             <i class="far fa-arrow-right"></i>
                         </a>
                     </div>
                     </div>
                 </div>
                 <div id="event-details-${count}" style="display: none; padding: 10px 60px 60px 60px; border-radius: 7px; background-color: white">
                     <div class="contact__info white-bg p-relative z-index-1">
                         <div class="contact__info-inner white-bg">
                             <ul>
                                 <li>
                                     <div class="contact__info-item d-flex align-items-start mb-20">
                                         <div class="contact__info-text" style="width: 100%">
                                             <h4 class="blue-grey">Compiler Message</h4>
                                             <pre class="pre-tags" style="font-weight: initial" id="message">Kết quả không trùng khớp</pre>
                                         </div>
                                     </div>
                                 </li>
                                 <li>
                                     <div class="contact__info-item d-flex align-items-start mb-20">
                                         <div class="contact__info-text" style="width: 100%">
                                             <h4 class="blue-grey">Input</h4>
                                             <pre class="pre-tags" id="input">${stdin}</pre>
                                         </div>
                                     </div>
                                 </li>
                                 <li>
                                     <div class="contact__info-item d-flex align-items-start mb-20">
                                         <div class="contact__info-text" style="width: 100%">
                                             <h4 class="blue-grey">Your Output</h4>
                                             <pre class="pre-tags" id="my_output">${stdout}</pre>
                                         </div>
                                     </div>
                                 </li>
                                 <li>
                                     <div class="contact__info-item d-flex align-items-start mb-20">
                                         <div class="contact__info-text" style="width: 100%">
                                             <h4 class="blue-grey">Expected Output</h4>
                                             <pre class="pre-tags" id="expected_output">${expected_output}</pre>
                                         </div>
                                     </div>
                                 </li>
                             </ul>
                         </div>
                     </div>
                 </div>

             `;
                                             // Thêm container vào container chứa test cases
                                             testCasesContainer.appendChild(newContainer);
                                             const viewMoreButton = document.getElementById(`view-more-button-${count}`);
                                             const testcaseDetails = document.getElementById(`event-details-${count}`);
                                             viewMoreButton.addEventListener('click', () => {
                                                 if (testcaseDetails.style.display === 'block') {
                                                     testcaseDetails.style.display = 'none';
                                                 } else {
                                                     testcaseDetails.style.display = 'block';
                                                 }
                                             });

                                             resultSection.style.display = 'block';*/
                                    excuting.style.display = 'none';
                                    return false;
                                } else if (submission.status && submission.status.id === 11) {

                                    var message = decode(submission.message);
                                    var description = (submission.status.description);
                                    var output = [message, description].join("\n").trim();

                                    let submission_add_to_list = {
                                        executionTime: submission.time,
                                        memory: submission.memory,
                                        my_output: output,
                                        expected_output: decode(submission.expected_output),
                                        stdin: decode(submission.stdin),
                                        ispassed: false
                                    };
                                    submissions_to_send.push(submission_add_to_list);
                                    count = count + 1;
                                    /*  newContainer.innerHTML = `
          <div class="events__item mb-10 hover__active">
                <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
              <div class="events__content">
                  <div class="events__meta">
                     <span>${submission.time}s</span>
                                                     <span>${submission.memory} KB</span>
                                                     <span>Error Status 11</span>
                  </div>
                  <h3 class="events__title">
                      <a class="price__features" style="color: #d12a47;">
                          <i class="far fa-warning" style="background: rgb(48 168 32 / 9%);
                              border-radius: 50%;
                              font-size: 25px;"></i>Test case 0${count}
                      </a>
                  </h3>
              </div>
              <div class="events__more">
                  <a class="link-btn" id="view-more-button-${count}" style="color: #e65972; cursor: pointer">
                      Xem chi tiết
                      <i class="far fa-arrow-right"></i>
                      <i class="far fa-arrow-right"></i>
                  </a>
              </div>
              </div>
          </div>
          <div id="event-details-${count}" style="display: none; padding: 10px 60px 60px 60px; border-radius: 7px; background-color: white">
              <div class="contact__info white-bg p-relative z-index-1">
                  <div class="contact__info-inner white-bg">
                      <ul>
                          <li>
                              <div class="contact__info-item d-flex align-items-start mb-20">
                                  <div class="contact__info-text" style="width: 100%">
                                      <h4 class="blue-grey">Compiler Message</h4>
                                      <pre class="pre-tags" style="font-weight: initial" id="message">${output}</pre>

                                  </div>
                              </div>
                          </li>

                      </ul>
                  </div>
              </div>
          </div>

      `;
                                      // Thêm container vào container chứa test cases
                                      testCasesContainer.appendChild(newContainer);
                                      const viewMoreButton = document.getElementById(`view-more-button-${count}`);
                                      const testcaseDetails = document.getElementById(`event-details-${count}`);
                                      viewMoreButton.addEventListener('click', () => {
                                          if (testcaseDetails.style.display === 'block') {
                                              testcaseDetails.style.display = 'none';
                                          } else {
                                              testcaseDetails.style.display = 'block';
                                          }
                                      });

                                      resultSection.style.display = 'block';*/
                                    excuting.style.display = 'none';
                                    return false;
                                } else {

                                    var stdout = decode(submission.stdout);
                                    var compile_output = decode(submission.compile_output);
                                    var output = [compile_output, stdout].join("\n").trim();

                                    let submission_add_to_list = {
                                        executionTime: submission.time,
                                        memory: submission.memory,
                                        my_output: output,
                                        expected_output: decode(submission.expected_output),
                                        stdin: decode(submission.stdin),
                                        ispassed: false
                                    };
                                    submissions_to_send.push(submission_add_to_list);
                                    count = count + 1;
                                    /*   newContainer.innerHTML = `
           <div class="events__item mb-10 hover__active">
                 <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
               <div class="events__content">
                   <div class="events__meta">
                      <span>${submission.time}s</span>
                                                      <span>${submission.memory} KB</span>
                                                      <span>Error Status ${submission.status.id}</span>
                   </div>
                   <h3 class="events__title">
                       <a class="price__features" style="color: #d12a47;">
                           <i class="far fa-warning" style="background: rgb(48 168 32 / 9%);
                               border-radius: 50%;
                               font-size: 25px;"></i>Test case 0${count}
                       </a>
                   </h3>
               </div>
               <div class="events__more">
                   <a class="link-btn" id="view-more-button-${count}" style="color: #e65972; cursor: pointer">
                       Xem chi tiết
                       <i class="far fa-arrow-right"></i>
                       <i class="far fa-arrow-right"></i>
                   </a>
               </div>
               </div>
           </div>
           <div id="event-details-${count}" style="display: none; padding: 10px 60px 60px 60px; border-radius: 7px; background-color: white">
               <div class="contact__info white-bg p-relative z-index-1">
                   <div class="contact__info-inner white-bg">
                       <ul>
                           <li>
                               <div class="contact__info-item d-flex align-items-start mb-20">
                                   <div class="contact__info-text" style="width: 100%">
                                       <h4 class="blue-grey">Compiler Message</h4>
                                       <pre class="pre-tags" style="font-weight: initial" id="message">${output}</pre>

                                   </div>
                               </div>
                           </li>

                       </ul>
                   </div>
               </div>
           </div>

       `;
                                       // Thêm container vào container chứa test cases
                                       testCasesContainer.appendChild(newContainer);
                                       const viewMoreButton = document.getElementById(`view-more-button-${count}`);
                                       const testcaseDetails = document.getElementById(`event-details-${count}`);
                                       viewMoreButton.addEventListener('click', () => {
                                           if (testcaseDetails.style.display === 'block') {
                                               testcaseDetails.style.display = 'none';
                                           } else {
                                               testcaseDetails.style.display = 'block';
                                           }
                                       });

                                       resultSection.style.display = 'block';*/
                                    excuting.style.display = 'none';
                                    return false;
                                }
                                return true;
                            });
                            try {
                                const response = await fetch('/api/submissions/add', {
                                    method: 'POST',
                                    headers: {
                                        'Content-Type': 'application/json'
                                    },
                                    body: JSON.stringify({
                                        assignment_id: assignment_id.value,
                                        sourceCode: encode(source_code),
                                        language: langagueName,
                                        testCaseListDTOS: submissions_to_send
                                    })
                                });

                                const result = await response.json();
                                var buttonActive = document.querySelector('button.nav-link.active');
                                var inputsHasValue = document.querySelectorAll('[name="submission_id_4assignment"][value]');
                                console.log("Submission ID return: " + JSON.stringify(result));
                                submitCheck = true;
                                if (inputsHasValue.length != number_of_assignments.value) {
                                    const id = 'submission_id_4assignment_' + buttonActive.id;
                                    const submission_input_found = document.getElementById(id);
                                    submission_input_found.value = JSON.stringify(result);
                                    buttonActive.setAttribute("disabled", "");
                                    buttonActive.style.background = "lightgreen";
                                    var inputs = document.querySelectorAll('[name="submission_id_4assignment"]:not([value])');
                                    //đã làm hết assignment
                                    if (inputs.length == 0) {
                                        //next quiz tab or finish
                                        var quizInputsHasValue = document.querySelectorAll('[name="submission_id_4quiz"][value]');
                                        if (quizInputsHasValue.length != number_of_quiz.value) {//chưa làm hết quiz
                                            var inputs = document.querySelectorAll('[name="submission_id_4quiz"]:not([value])');
                                            var id_temp = inputs[0].id;
                                            var idWithoutPrefix = id_temp.replace("submission_id_4quiz_", "quizBtn_");
                                            console.log(idWithoutPrefix);
                                            const button = document.getElementById(idWithoutPrefix);
                                            button.click();
                                        } else { //đã làm hết quiz và assignment
                                            const button = document.getElementById("description-tab");
                                            const description = document.getElementById("description_container");
                                            const btn_endContest =`<button type="button" id="end_test" style="float: right" class="btn btn-outline-primary">Nộp bài</button>`;
                                            description.innerHTML+=btn_endContest;
                                            var btn_id = document.getElementById("end_test");
                                            btn_id.onclick = function() {
                                                submitTest();
                                            };
                                            button.click();
                                        }
                                    } else {  //chưa làm hết assignment
                                        var id_temp = inputs[0].id;
                                        var idWithoutPrefix = id_temp.replace("submission_id_4assignment_", "");
                                        var idNumber = parseInt(idWithoutPrefix);
                                        const button = document.getElementById(idNumber);
                                        button.click();
                                    }
                                }
                            } catch (error) {
                                console.error("error when call /api/submissions/add: " + error);
                            }
                        }
                    };
                    await checkSubmitStatus();
                } catch (error) {
                    console.error(error);
                }
            },
            error: (error) => {
                console.log(error);
                console.log("Error with id: " + assignment_id.value);
            }
        });
    }

});

