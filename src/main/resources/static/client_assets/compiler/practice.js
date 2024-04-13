import {MY_CHATGPT_API_KEY} from "./config.js"
const testCasesContainer = document.getElementById('test-cases-container');
const resultSection = document.getElementById('result');
var run = document.getElementById("run")
var submit = document.getElementById("submit")
var theme = document.getElementById("theme")
const excuting = document.getElementById('excuting');

const testCasesElement = document.getElementById('testCases');
const testCasesJSON = testCasesElement.getAttribute('data-test-cases');

const assignment_idElement = document.getElementById('assignment_id');
const assignment_id = assignment_idElement.getAttribute('data-assignment_id');

const memory_limit = document.getElementById('assignment_memory_limit');
const time_limit = document.getElementById('assignment_time_limit');

const allTestCase = document.getElementById('allTestCase');
const allTestCaseJSON = allTestCase.getAttribute('data-test-cases');
const testCases = JSON.parse(testCasesJSON);
document.addEventListener('DOMContentLoaded', function () {
    var hash = window.location.hash;
    if (hash !== '') {
        const tabToSelect = document.getElementById(window.location.hash.substr(1));
        if (tabToSelect) {
            const tab = new bootstrap.Tab(tabToSelect);
            tab.show();
        }
    }


});
$(document).ready(function () {

    document.getElementById('courseTab').addEventListener('show.bs.tab', function (event) {
        const activeTab = event.target; // Tab đang active

        if (activeTab.id === 'submission-tab') {
            window.location.hash = 'submission-tab';
            window.location.reload();
        } else {
            window.location.hash = activeTab.id;
        }
        // Kiểm tra tab và enable/disable các nút tương ứng
        if (activeTab.value === 'locked') {
            if (!confirm("If you unlock the solution, your score will not be counted toward your progress.")) {
                event.preventDefault();
                console.log("Cancelled unlock");
            } else {
                const assignmentIDElement = document.getElementById("assignment_id");
                $.ajax({
                    type: 'POST',
                    url: '/api/submissions/unlock-solution',
                    data: {
                        assignment_id: assignmentIDElement.value,
                    },
                    success: function () {

                    },
                    error: function (error) {
                        console.error('Error getting content:', error);
                    }
                });
            }
        }
    });
    editor.on('change', function () {
        saveContent(editor.getValue().trim());
    });
    $.ajax({
        type: 'GET',
        url: '/api/submissions/get-content',
        success: function (response) {
            if (response!=null) {
                const dataJson = JSON.parse(response);
                const mode_receive = dataJson.mode;
                const content_receive = dataJson.content;
                const language_name_receive = dataJson.language_name;
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
                        console.log('Đã add class "dd-option-selected" từ thẻ a.');
                    } else {
                        console.log('Không tìm thấy thẻ a chứa input.');
                    }
                } else {
                    console.log('Không tìm thấy thẻ input có giá trị là "input_value".');
                }

                editor.setOption("mode", mode_receive);
                console.log('content_receive');
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
});

function saveContent(content) {
    // Lấy thẻ input đầu tiên có class "dd-option-selected" trong thẻ a
    var selectedInput = document.querySelector('a.dd-option-selected input.dd-option-value');
    $.ajax({
        type: 'POST',
        url: '/api/submissions/save-content',
        data: {
            content: content,
            mode: selectedInput.value,
            langague_name: langague_name,
            current_tab_id: "-1"
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
async function generateAssessment() {
    const prompt_text = "Hãy đưa ra nhận xét về đoạn code sau đây:"
    const source = editor.getValue().trim();
    const prompt_send = prompt_text+source;
    const url = 'https://simple-chatgpt-api.p.rapidapi.com/ask';
    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-RapidAPI-Key': MY_CHATGPT_API_KEY,
            'X-RapidAPI-Host': 'simple-chatgpt-api.p.rapidapi.com'
        },
        body: JSON.stringify({
            question: prompt_send
        })
    };

    try {
        const response = await fetch(url, options);
        const result = await response.json();
        const outputElement = document.getElementById("chatgpt_response");
        const assessmentBtn = document.getElementById("assessmentBtn");
        const chatgpt_response_container = document.getElementById("chatgpt_response_container");
        assessmentBtn.remove();
        chatgpt_response_container.style.display = "block";
        const chatgpt_loading = document.getElementById(`chatgpt_loading`);
        chatgpt_loading.remove();
        outputElement.innerHTML = result.answer;

    } catch (error) {
        console.error(error);
    }
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
import {MY_API_KEY} from "./config.js";


//batch submission
run.addEventListener("click", async function () {
    testCasesContainer.innerHTML = '';
    excuting.style.display = 'block';
    const bottomElement = document.getElementById('excuting');
    bottomElement.scrollIntoView({behavior: 'smooth'});
    let count = 0;
    let submissions;
    if (time_limit.value !== "0" && memory_limit.value !== "0") {
        submissions = testCases.map(testCase => {
            // Tạo đối tượng submission cho mỗi test case
            return {
                language_id: option,
                source_code: editor.getValue().trim(),
                stdin: testCase.input,
                expected_output: testCase.expectedOutput,
                cpu_time_limit: time_limit.value,
                memory_limit: memory_limit.value
            };
        });
    } else if (time_limit.value !== "0" && memory_limit.value === "0") {
        submissions = testCases.map(testCase => {
            // Tạo đối tượng submission cho mỗi test case
            return {
                language_id: option,
                source_code: editor.getValue().trim(),
                stdin: testCase.input,
                expected_output: testCase.expectedOutput,
                cpu_time_limit: time_limit.value
            };
        });
    } else if (time_limit.value === "0" && memory_limit.value !== "0") {
        submissions = testCases.map(testCase => {
            // Tạo đối tượng submission cho mỗi test case
            return {
                language_id: option,
                source_code: editor.getValue().trim(),
                stdin: testCase.input,
                expected_output: testCase.expectedOutput,
                memory_limit: memory_limit.value
            };
        });
    } else {
        submissions = testCases.map(testCase => {
            // Tạo đối tượng submission cho mỗi test case
            return {
                language_id: option,
                source_code: editor.getValue().trim(),
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
                            <span>Success</span>
                        </div>
                        <h3 class="events__title">
                            <a class="price__features">
                                <i class="far fa-check" style="background: rgb(48 168 32 / 9%);
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
        </div>
        <div id="event-details-${count}" style="display: none; padding: 10px 60px 60px 60px; border-radius: 7px; background-color: white">
            <div class="contact__info white-bg p-relative z-index-1">
                <div class="contact__info-inner white-bg">
                    <ul>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Trạng Thái</h4>
                                    <pre class="pre-tags" style="font-weight: initial; text-align: center">Thành công</pre>
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
                            excuting.style.display = 'none';
                            Prism.highlightAll();
                            resultSection.style.display = 'block';

                        } else if (submission.status && submission.status.id === 4) {
                            //Wrong Answer
                            var stdin = decode(submission.stdin).trim();
                            var stdout = decode(submission.stdout).trim();
                            var expected_output = decode(submission.expected_output).trim();

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
                                    <h4 class="blue-grey">Trạng Thái</h4>
                                    <pre class="pre-tags" style="font-weight: initial;text-align: center">Kết quả không trùng khớp</pre>
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
                                    <h4 class="blue-grey">Trạng Thái</h4>
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
                                    <h4 class="blue-grey">Trạng Thái</h4>
                              
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
});

submit.addEventListener("click", async function () {
    testCasesContainer.innerHTML = '';
    var html = "<div style='display: none' id='assessment'><p id='chatgpt_loading' style='float: left;display: none;animation: slide 1.5s infinite;'><span style='color: #ff9800;' class='yellow-bg yellow-bg-big'>Đang phân tích...</span> </p><a id='assessmentBtn' class='link-btn' style='cursor: pointer;float: right;color: #316314;margin-bottom: 20px;'>Xem đánh giá<i class='far fa-arrow-right'></i><i class='far fa-question-circle'></i></a><pre id='chatgpt_response_container' class='line-numbers language-markup' style='white-space: break-spaces;display: none;margin-bottom: 15px;'><code id='chatgpt_response' class='language-markup'></code></pre></div>";
    testCasesContainer.innerHTML += html;
    let submissions_to_send = [];
    const allTestCases = JSON.parse(allTestCaseJSON);
    const languageInput = document.querySelector('input.dd-selected-value');
    const langagueName=languageInput.value;
    const source_code = editor.getValue().trim();
    excuting.style.display = 'block';
    const bottomElement = document.getElementById('excuting');
    bottomElement.scrollIntoView({behavior: 'smooth'});
    let count = 0;
    let submissions;
    if (time_limit.value !== "0" && memory_limit.value !== "0") {
        submissions = allTestCases.map(testCase => {
            // Tạo đối tượng submission cho mỗi test case
            return {
                language_id: option,
                source_code: editor.getValue().trim(),
                stdin: testCase.input,
                expected_output: testCase.expectedOutput,
                cpu_time_limit: time_limit.value,
                memory_limit: memory_limit.value
            };
        });
    } else if (time_limit.value !== "0" && memory_limit.value === "0") {
        submissions = allTestCases.map(testCase => {
            // Tạo đối tượng submission cho mỗi test case
            return {
                language_id: option,
                source_code: editor.getValue().trim(),
                stdin: testCase.input,
                expected_output: testCase.expectedOutput,
                cpu_time_limit: time_limit.value
            };
        });
    } else if (time_limit.value === "0" && memory_limit.value !== "0") {
        submissions = allTestCases.map(testCase => {
            // Tạo đối tượng submission cho mỗi test case
            return {
                language_id: option,
                source_code: editor.getValue().trim(),
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
                source_code: editor.getValue().trim(),
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

                    const newContainer = document.createElement('div');
                    newContainer.classList.add('col-xxl-10', 'offset-xxl-1', 'col-xl-10', 'offset-xl-1', 'col-lg-10', 'offset-lg-1');
                    if (submission.status && submission.status.id === 3) {
                        //Accepted
                        var stdin = decode(submission.stdin);
                        var stdout = decode(submission.stdout);
                        var expected_output = decode(submission.expected_output);

                        let submission_add_to_list = {
                            executionTime: submission.time,
                            memory: submission.memory,
                            my_output: decode(submission.stdout),
                            expected_output: decode(submission.expected_output),
                            stdin: decode(submission.stdin),
                            ispassed: true
                        };
                        submissions_to_send.push(submission_add_to_list);


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
                        <i class="far fa-check" style="background: rgb(48 168 32 / 9%);
                            border-radius: 50%;
                            font-size: 25px;"></i> Test case ${count}
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
        </div>
        <div id="event-details-${count}" style="display: none; padding: 10px 60px 60px 60px; border-radius: 7px; background-color: white">
            <div class="contact__info white-bg p-relative z-index-1">
                <div class="contact__info-inner white-bg">
                    <ul>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Trạng Thái</h4>
                                    <pre class="pre-tags" style="font-weight: initial; text-align: center">Thành công</pre>
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
                        const assessment_div = document.getElementById("assessment");
                        assessment_div.style.display ="block";

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
                        excuting.style.display = 'none';
                        resultSection.style.display = 'block';
                        Prism.highlightAll();


                    } else if (submission.status && submission.status.id === 4) {
                        //Wrong Answer
                        var stdin = decode(submission.stdin).trim();
                        var stdout = decode(submission.stdout).trim();
                        var expected_output = decode(submission.expected_output).trim();

                        let submission_add_to_list = {
                            executionTime: submission.time,
                            memory: submission.memory,
                            my_output: decode(submission.stdout),
                            expected_output: decode(submission.expected_output),
                            stdin: decode(submission.stdin).trim(),
                            ispassed: false
                        };
                        submissions_to_send.push(submission_add_to_list);

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
                                    <h4 class="blue-grey">Trạng Thái</h4>
                                    <pre class="pre-tags" style="font-weight: initial; text-align: center">Kết quả không trùng khớp</pre>
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
                                    <h4 class="blue-grey">Trạng Thái</h4>
                                
                                        <pre style="font-weight: initial" class="pre-tags line-numbers language-markup">
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
                                    <h4 class="blue-grey">Trạng Thái</h4>
                                 
                                     <pre style="font-weight: initial" class="pre-tags line-numbers language-markup">
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
                            assignment_id: assignment_id,
                            sourceCode: encode(source_code),
                            language: langagueName,
                            testCaseListDTOS: submissions_to_send
                        })
                    });

                    const result = await response.json();
                    console.log("Submission return: " + JSON.stringify(result));

                } catch (error) {
                    console.error("error when call /api/submit: " + error);
                }
            }
        };

        // Bắt đầu kiểm tra trạng thái
        await checkSubmitStatus();
    } catch (error) {
        console.error(error);
    }

    const assessment_btn = document.getElementById(`assessmentBtn`);
    assessment_btn.addEventListener('click', () => {
        const chatgpt_loading = document.getElementById(`chatgpt_loading`);
        chatgpt_loading.style.display = 'block';
        generateAssessment();
    });
});

