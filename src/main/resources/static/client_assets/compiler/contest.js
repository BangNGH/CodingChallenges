const testCasesContainer = document.getElementById('test-cases-container');
const resultSection = document.getElementById('result');
var submit = document.getElementById("submit");
var theme = document.getElementById("theme");
const excuting = document.getElementById('excuting');
const assignment_id = document.getElementById('hidden_input');
const contest_idElement = document.getElementById('contest_id');
const number_of_assignments = document.getElementById('number_of_assignments');
const list_assignment_id = document.getElementById('idAssignments');
const languageMode = document.getElementById('language_mode');
const submission_id_4assignment_list = [];
let submitCheck = false;

var seconds = 0;
var timer;
function startTimer() {
    seconds++;
}

// Hàm để dừng bộ đếm thời gian
function stopTimer() {
    // Xóa đối tượng setInterval
    clearInterval(timer);
}

function getTimerValue() {
    // Trả về một chuỗi thời gian
    return seconds;
}

// Hàm để xử lý sự kiện khi trang được mở
window.onload = function () {
    // Bắt đầu bộ đếm thời gian với khoảng thời gian là 1 giây
    timer = setInterval(startTimer, 1000);
};


$(document).ready(function () {
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
        if (activeTab.id === 'description-tab') {
            editorDiv.style.display = 'none';
            submit.setAttribute("disabled", "");
            assignment_id.value = "";
        } else {
            editorDiv.style.display = 'block';
            submit.removeAttribute("disabled");
            assignment_id.value = activeTab.id;
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

    var assignmentValue = list_assignment_id.value;
    var assignment_id_list = JSON.parse(assignmentValue);

    for (let i = 0; i < number_of_assignments.value; i++) {
        var input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'submission_id_4assignment';
        input.id = 'submission_id_4assignment_' + assignment_id_list[i];
        document.body.appendChild(input);
    }

});

$(document).ready(function () {
    editor.on('change', function () {
        submitCheck = false;
        saveContent(editor.getValue());
    });
    const editorDiv = document.getElementById('editor-div');
    var buttonActive = document.querySelector('button.nav-link.active');
    if (buttonActive.id === "description-tab") {
        editorDiv.style.display = 'none';
        submit.setAttribute("disabled", "");
    } else {
        editorDiv.style.display = 'block';
        submit.removeAttribute("disabled");
    }
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
    stopTimer();
    var timerValue = getTimerValue();
    const submission_id_4assignments = document.querySelectorAll('input[name="submission_id_4assignment"]');
    submission_id_4assignments.forEach(input => {
        let submission_kit_to_list = input.value;
        submission_id_4assignment_list.push(submission_kit_to_list);
    });
    const timer = [];
    let timer_value = timerValue;
    timer.push(timer_value);

    console.log(JSON.stringify(timer));
    const response = await fetch('/api/submissions/submit-contest', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            submissions_id_ofAssignment: (submission_id_4assignment_list),
            submissions_id_ofQuiz: timer,
            assignment_kit_id:contest_idElement.value
        })
    });
    const result = await response.text();
    console.log(result);
    const url = `/contest/details/${contest_idElement.value}`;
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
                                        const button = document.getElementById("description-tab");
                                        const description = document.getElementById("description_container");
                                        const btn_endContest = `<button type="button" id="end_test" style="float: right" class="btn btn-outline-primary">Nộp bài</button>`;
                                        description.innerHTML += btn_endContest;
                                        var btn_id = document.getElementById("end_test");
                                        btn_id.onclick = function () {
                                            submitTest();
                                        }
                                        button.click();

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

